package Entity;

import Interface.GameMediator;

public class WildDrawFourCard extends ActionCard {
    public WildDrawFourCard() {
        super(50, null);
    }

    @Override
    public void action(GameMediator mediator, Player currentPlayer) {
        CardColor chosen = currentPlayer.chooseColor();
        this.setColor(chosen);
        mediator.setColor(chosen);

        Player next = mediator.getNextPlayer(currentPlayer);
        mediator.drawCards(next, 4);
        mediator.skipNextPlayer();
    }

    @Override
    public boolean canBePlayedOn(Card topCard) {
        return true;
    }

    @Override
    public String toString() {
        return "WILD DRAW FOUR (" + (color != null ? color : "unset") + ")";
    }
}