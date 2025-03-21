package Entity;
import Interface.GameMediator;

public class ReverseCard extends ActionCard {
    public ReverseCard(CardColor color) {
        super(20, color);
    }

    @Override
    public void action(GameMediator mediator, Player currentPlayer) {
        mediator.reverseDirection();
    }

    @Override
    public boolean canBePlayedOn(Card topCard) {
        return this.color == topCard.getColor() || topCard instanceof ReverseCard;
    }

    @Override
    public String toString() {
        return color + " REVERSE";
    }
}