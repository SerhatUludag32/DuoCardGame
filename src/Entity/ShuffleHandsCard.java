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

        System.out.println("[DEBUG] " + playedBy.getName() + " played SHUFFLE HANDS and chose color: " + chosen);
    }

    @Override
    public boolean canBePlayedOn(Card topCard) {
        return true;
    }

    @Override
    public String toString() {
        return "SHUFFLE HANDS";
    }

    public void setColor(CardColor color) {
        this.color = color;
    }
}