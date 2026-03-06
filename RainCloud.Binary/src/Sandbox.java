import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import ch.nivisan.raincloud.serialization.DbDeserializer;
import ch.nivisan.raincloud.serialization.FileService;
import ch.nivisan.raincloud.serialization.RCDatabase;
import ch.nivisan.raincloud.serialization.RCObject;
import ch.nivisan.raincloud.serialization.RCString;
import ch.nivisan.raincloud.serialization.fields.BooleanField;
import ch.nivisan.raincloud.serialization.fields.IntField;

public class Sandbox {
    public class Level {
        private List<Entity> entities = new ArrayList<>();
        private String name;
        private int width, height;
        private String filePath;

        public Level(String path) {
            this.name = "Level 1";
            this.filePath = path;
            width = 64;
            height = 128;
        }

        public Level(String name, int width, int height, String filePath) {
            this.name = name;
            this.width = width;
            this.height = height;
            this.filePath = filePath;
        }

        public void setEntities(List<Entity> entities) {
            this.entities = entities;
        }

        public void addEntity(Entity entity) {
            entities.add(entity);
        }

        public void update() {
        }

        public void render() {
        }

        public void serialize(RCObject object) {
            object.addField(new IntField("height", height));
            object.addField(new IntField("width", width));
            object.addString(new RCString("name", name));
            object.addString(new RCString("filePath", filePath));
        }

        public void save(String string) {
            RCDatabase rcdb = new RCDatabase("LevelDB");
            RCObject level = new RCObject(name);
            serialize(level);

            rcdb.addObject(level);

            for (int i = 0; i < entities.size(); i++) {
                var e = entities.get(i);
                RCObject entity = new RCObject("Entity: " + i);
                entity.addField(new IntField("index", i));
                e.serialize(entity);

                rcdb.addObject(entity);
            }

            byte[] data = new byte[rcdb.getSize()];
            rcdb.getBytes(data, 0);

            FileService.saveToFile(string, data);

        }

        public Level load(String path) {
            byte[] leveldata = FileService.getFromFile("./level.rcdb");
            RCDatabase levelDatabase = DbDeserializer.Deserialize(leveldata);
            System.out.println(levelDatabase.getName());

            RCObject levelObject = levelDatabase.getObject("Level 1");
            int width = ((IntField) levelObject.getField("width")).getValue();
            int height = ((IntField) levelObject.getField("height")).getValue();
            RCString rclevelPath = levelObject.getString("filePath");
            var levelPath = rclevelPath.getValue();
            String name = levelObject.getString("name").getValue();

            Level lvl = new Level(name, width, height, levelPath);

            RCObject[] entitiesObj = new RCObject[2];
            entitiesObj[0] = levelDatabase.getObject("Entity: 0");
            entitiesObj[1] = levelDatabase.getObject("Entity: 1");

            List<Entity> entities = new ArrayList<>();
            for (int i = 0; i < entities.size(); i++) {
                entities.add(new Entity(lvl));
            }

            lvl.setEntities(entities);
            return lvl;
        }

    }

    class Player extends Entity {
        private String name;
        private String avatarPath;

        public Player(String name, int x, int y, Level level) {
            super(level);
            this.name = name;
            this.x = x;
            this.y = y;

            this.avatarPath = "/res/avatar.png";
        }

        @Override
        public void serialize(RCObject object) {
            super.serialize(object);
            object.addString(new RCString("name", name));
            object.addString(new RCString("avatarPath", avatarPath));
        }
    }

    class Entity {
        private String name;
        protected int x, y;
        private boolean removed = false;
        private Level level;

        public Entity(Level level) {
            this.x = 0;
            this.y = 0;
            this.level = level;
        }

        public void serialize(RCObject entity) {
            entity.addField(new IntField("x", x));
            entity.addField(new IntField("y", y));
            entity.addField(new BooleanField("removed", removed));
        }
    }

    public void play() {
        Level level = new Level("/res/leveldata.rcd");
        Entity mob = new Entity(level);
        Player player = new Player("Nivisan", 40, 28, level);

        level.addEntity(mob);
        level.addEntity(player);

        level.save("level.rcdb");

        var lvl = level.load("./level.rcdb");
        System.out.println();
    }
}
