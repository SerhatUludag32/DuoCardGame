package Entity;

import Interface.GameMediator;

public class SkipCard extends ActionCard {
    public SkipCard(CardColor color) {
        super(20, color);
    }

    @Override
    public void action(GameMediator mediator, Player currentPlayer) {
        mediator.skipNextPlayer();
    }

    @Override
    public boolean canBePlayedOn(Card topCard) {
        return this.color == topCard.getColor() || topCard instanceof SkipCard;
    }

    @Override
    public String toString() {
        return color + " SKIP";
    }

    @Override
    public int getCardValueForDealer() {
        return 20;
    }
}