package Entity;

import Interface.GameMediator;

import java.awt.*;

public class WildCard extends ActionCard {
    public WildCard(CardColor color) {
        super(50, color); // Initially null or NONE — will be set on play
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
    @Override
    public int getCardValueForDealer() {
        return 50;
    }
}