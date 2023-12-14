package passoffTests;

import chess.ChessBoard;
import chessGame.*;

/**
 * Used for testing your code
 * Add in code using your classes for each method for ea
 */
public class TestFactory {

    //Chess Functions
    //------------------------------------------------------------------------------------------------------------------
    public static ChessBoard getNewBoard() {
        return new ChessBoardImp();
    }

    public static chess.ChessGame getNewGame() {
        return new ChessGameImp();
    }

    public static chess.ChessPiece getNewPiece(chess.ChessGame.TeamColor pieceColor, chess.ChessPiece.PieceType type) {
        return new ChessPieceImp(pieceColor, type);
    }

    public static chess.ChessPosition getNewPosition(Integer row, Integer col) {
        return new ChessPositionImp(row, col);
    }

    public static chess.ChessMove getNewMove(chess.ChessPosition startPosition, chess.ChessPosition endPosition, chess.ChessPiece.PieceType promotionPiece) {
        return new ChessMoveImp(startPosition, endPosition, promotionPiece);
    }
    //------------------------------------------------------------------------------------------------------------------


    //Server API's
    //------------------------------------------------------------------------------------------------------------------
    public static String getServerPort() {
        return "8080";
    }
    //------------------------------------------------------------------------------------------------------------------


    //Websocket Tests
    //------------------------------------------------------------------------------------------------------------------
    public static Long getMessageTime() {
        /*
        Changing this will change how long tests will wait for the server to send messages.
        3000 Milliseconds (3 seconds) will be enough for most computers. Feel free to change as you see fit,
        just know increasing it can make tests take longer to run.
        (On the flip side, if you've got a good computer feel free to decrease it)
         */
        return 1000L;
    }
    //------------------------------------------------------------------------------------------------------------------
}
