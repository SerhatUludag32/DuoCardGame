package Entity;

import Interface.GameMediator;

public class ShuffleHandsCard extends ActionCard {
    public ShuffleHandsCard() {
        super(40, CardColor.NONE);
    }

    @Override
    public void action(GameMediator mediator, Player playedBy) {
        mediator.shuffleHands(playedBy); // real shuffle

        CardColor chosen = playedBy.chooseColor(); // choose most frequent color
        mediator.setColor(chosen);
        this.setColor(chosen);

    }

    @Override
    public boolean canBePlayedOn(Card topCard) {
        return true;
    }

    @Override
    public String toString() {
        return "SHUFFLE HANDS";
    }

    @Override
    public int getCardValueForDealer() {
        return 40;
    }
}