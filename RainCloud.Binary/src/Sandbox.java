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
        private final String name;
        private final int width, height;
        private final String filePath;

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

            RCObject levelObject = levelDatabase.getObject("Level 1");
            int width = ((IntField) levelObject.getField("width")).getValue();
            int height = ((IntField) levelObject.getField("height")).getValue();
            String levelPath = levelObject.getString("filePath").getValue();
            String name = levelObject.getString("name").getValue();

            Level lvl = new Level(name, width, height, levelPath);

            RCObject[] entitiesObj = new RCObject[2];
            entitiesObj[0] = levelDatabase.getObject("Entity: 0");
            entitiesObj[1] = levelDatabase.getObject("Entity: 1");

            for (int i = 0; i < entities.size(); i++) {
                int x = ((IntField) entitiesObj[i].getField("x")).getValue();
                int y = ((IntField) entitiesObj[i].getField("y")).getValue();
                BooleanField removed = ((BooleanField) entitiesObj[i].getField("removed"));
                boolean isRemoved = removed.getValue();
                String entityName = levelObject.getString("name").getValue();
                lvl.addEntity(new Entity(lvl, entityName, x, y, isRemoved));
            }

            return lvl;
        }

    }

    class Player extends Entity {
        private String avatarPath;

        public Player(String name, int x, int y, Level level) {
            super(level, name, x, y, false);

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
        public final String name;
        protected final int x, y;
        private boolean removed;
        private final Level level;

        public Entity(Level level, String name) {
            this.x = 0;
            this.y = 0;
            this.level = level;
            this.name = name;
        }

        public Entity(Level level, String name, int x, int y, boolean removed) {
            this.level = level;
            this.name = name;
            this.x = x;
            this.y = y;
            this.removed = removed;
        }

        public void serialize(RCObject entity) {
            entity.addField(new IntField("x", x));
            entity.addField(new IntField("y", y));
            entity.addField(new BooleanField("removed", removed));
        }
    }

    public void play() {
        Level level = new Level("/res/leveldata.rcd");
        Entity mob = new Entity(level, "mob");
        Player player = new Player("Nivisan", 40, 28, level);

        level.addEntity(mob);
        level.addEntity(player);

        level.save("level.rcdb");

        var lvl = level.load("./level.rcdb");
        System.out.println();
    }
}
