package ch.nivisan.rain.game.input;

public enum MouseButton {
    None(0), Left(1), Middle(2), Right(3), SideFront(6), SideBack(7);
    private final int button;

    MouseButton(int btn) {
        this.button = btn;
    }

    public int getNumValue() {
        return button;
    }
}