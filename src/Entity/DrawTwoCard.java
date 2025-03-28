package Entity;

import Interface.GameMediator;

public class DrawTwoCard extends ActionCard {
    public DrawTwoCard(CardColor color) {
        super(20, color);
    }

    @Override
    public void action(GameMediator mediator, Player currentPlayer) {
        Player next = mediator.getNextPlayer(currentPlayer);
        mediator.drawCards(next, 2);
        mediator.skipNextPlayer();
    }

    @Override
    public boolean canBePlayedOn(Card topCard) {
        return getColor() == topCard.getColor() || topCard instanceof DrawTwoCard;
    }

    @Override
    public String toString() {
        return getColor() + " DRAW TWO";
    }

    @Override
    public int getCardValueForDealer() {
        return 20;
    }
}