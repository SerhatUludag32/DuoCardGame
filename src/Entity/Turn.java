package Entity;

public class Turn {
    private Card lastPlayedCard;
    private CardColor turnColor;
    private boolean skip;
    private int cardsToDraw;

    public Turn() {
        this.skip = false;
        this.cardsToDraw = 0;
        this.turnColor = null;
    }

    public Card getLastPlayedCard() {
        return lastPlayedCard;
    }

    public void setLastPlayedCard(Card card) {
        this.lastPlayedCard = card;
    }

    public CardColor getTurnColor() {
        return turnColor;
    }

    public void setTurnColor(CardColor color) {
        this.turnColor = color;
    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public int getCardsToDraw() {
        return cardsToDraw;
    }

    public void setCardsToDraw(int count) {
        this.cardsToDraw = count;
    }

    public void resetTurnFlags() {
        this.skip = false;
        this.cardsToDraw = 0;
    }
}