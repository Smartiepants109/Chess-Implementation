package Server.DataAccess;

import Server.Models.Game;

import java.util.HashSet;
import java.util.Set;

public class GameDAO {
    Set<Game> games = new HashSet<>();

    public boolean insertGame(Game game) {
        return games.add(game);
    }

    public Game findGame(int gameID) {
        return null;
    }
}
