package Server.DataAccess;

import Server.Models.AuthToken;

import java.util.HashSet;
import java.util.Set;

public class AuthDAO {
    Set<AuthToken> tokens = new HashSet<>();

    public boolean insertToken(AuthToken token) {
        return tokens.add(token);
    }

    public AuthToken findToken(String token, String password) {
        return null;
    }
}
