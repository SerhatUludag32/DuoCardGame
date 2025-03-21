package Entity;

import Interface.GameMediator;

public class ShuffleHandsCard extends ActionCard {
    public ShuffleHandsCard() {
        super(40, CardColor.NONE);
    }

    @Override
    public void action(GameMediator mediator, Player currentPlayer) {
        mediator.shuffleHands();
    }

    @Override
    public boolean canBePlayedOn(Card topCard) {
        return true;
    }

    @Override
    public String toString() {
        return "SHUFFLE HANDS";
    }
}