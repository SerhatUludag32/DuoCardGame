package Entity;

public abstract class Card {
    private int value;
    private CardColor color;

    public Card(int value, CardColor color) {
        this.value = value;
        this.color = color;
    }

    public void setColor(CardColor color) {
        this.color = color;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public CardColor getColor() {
        return color;
    }

    public abstract boolean canBePlayedOn(Card topCard);

    @Override
    public String toString() {
        return color + " Card";
    }


    public int getCardValueForDealer() {
        return 0;
    }
}