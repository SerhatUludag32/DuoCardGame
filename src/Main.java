import Entity.Player;
import Business.GamePlay;
import Business.DeckGenerator;
import Data.CSVLogger;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Player> players = List.of(
                new Player("Can"),
                new Player("Yusuf"),
                new Player("Alp")
        );

        GamePlay gamePlay = new GamePlay(players);

        // ✅ IMPORTANT: Load the deck BEFORE starting the game
        gamePlay.initializeDeck(DeckGenerator.generateStandardDuoDeck());

        // ✅ Create logger
        CSVLogger logger = new CSVLogger("game_log.csv");

        // ✅ Start game with 7 cards per player
        gamePlay.startGame(7, logger);

    }
}