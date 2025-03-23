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
        return getColor() == topCard.getColor()
                || (topCard instanceof NumberCard && this.number == ((NumberCard) topCard).getNumber());
    }

    @Override
    public String toString() {
        return getColor() + " " + number;
    }

    @Override
    public int getCardValueForDealer() {
        return this.number;
    }
}