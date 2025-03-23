package Entity;

public class Hand extends Deck {
    public Hand() {
        super();
    }

    public int getTotalPoints() {
        return getCards().stream().mapToInt(Card::getValue).sum();
    }

    public void removeCard(Card card) {
        getCards().remove(card);
    }

    public int size() {
        return getCards().size();
    }

    @Override
    public String toString() {
        if (getCards().isEmpty()) return "(empty hand)";
        StringBuilder sb = new StringBuilder();
        for (Card card : getCards()) {
            sb.append(card).append(", ");
        }
        return sb.substring(0, sb.length() - 2); // remove trailing comma and space
    }
}