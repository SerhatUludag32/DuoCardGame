package Entity;

public enum CardColor {
    RED, GREEN, BLUE, YELLOW, NONE; // NONE for shuffle

    public static CardColor getRandomColor() {
        CardColor[] values = {RED, GREEN, BLUE, YELLOW};
        return values[(int) (Math.random() * values.length)];
    }
}