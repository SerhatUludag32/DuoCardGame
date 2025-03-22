package Entity;

public class NumberCard extends Card {
    private int number;

    public NumberCard(int number, CardColor color) {
        super(number, color);  // value = number for scoring
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public boolean canBePlayedOn(Card topCard) {
        return this.color == topCard.getColor()
                || (topCard instanceof NumberCard && this.number == ((NumberCard) topCard).getNumber());
    }

    @Override
    public String toString() {
        return color + " " + number;
    }

    @Override
    public int getCardValueForDealer() {
        return this.number;
    }
}