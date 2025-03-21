package Entity;

import Interface.GameMediator;

public class WildCard extends ActionCard {
    public WildCard() {
        super(50, null); // Initially null or NONE — will be set on play
    }

    @Override
    public void action(GameMediator mediator, Player currentPlayer) {
        CardColor chosen = currentPlayer.chooseColor();
        this.setColor(chosen);
        mediator.setColor(chosen);
    }

    @Override
    public boolean canBePlayedOn(Card topCard) {
        return true;
    }

    @Override
    public String toString() {
        return "WILD (" + (color != null ? color : "unset") + ")";
    }
}