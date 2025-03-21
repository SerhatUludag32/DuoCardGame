package Entity;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private List<Player> players;
    private List<Round> rounds;

    public Game(List<Player> players) {
        this.players = players;
        this.rounds = new ArrayList<>();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public void addRound(Round round) {
        rounds.add(round);
    }

    public boolean isGameOver() {
        return players.stream().anyMatch(Player::hasWon);
    }
}