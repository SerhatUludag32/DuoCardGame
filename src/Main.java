import Entity.Player;
import Business.GamePlay;
import Business.DeckGenerator;
import Data.CSVLogger;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Random rand = new Random();
        int numPlayers = rand.nextInt(3) + 2; // Randomly 2 to 4 players

        List<Player> players = new ArrayList<>();
        for (int i = 1; i <= numPlayers; i++) {
            players.add(new Player("Player " + i));
        }

        System.out.println("🎮 Starting game with " + numPlayers + " players:");
        for (Player p : players) {
            System.out.println("🔹 " + p.getName());
        }

        GamePlay gamePlay = new GamePlay(players);

        // Initialize the deck
        gamePlay.initializeDeck(DeckGenerator.generateStandardDuoDeck());

        // Create logger
        CSVLogger logger = new CSVLogger("game_log.csv");

        // Start the game
        gamePlay.startGame(7, logger);
    }
}