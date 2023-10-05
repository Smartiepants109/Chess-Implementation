package chessGame;

import chess.*;

import java.util.*;

public class ChessGameImp implements chess.ChessGame {

    TeamColor currentTurn;
    ChessBoard board;
    Map<ChessBoard, Integer> pastMoves;

    public ChessGameImp() {
        currentTurn = TeamColor.WHITE;
        board = new ChessBoardImp();
        pastMoves = new HashMap<ChessBoard, Integer>();
    }

    @Override
    public TeamColor getTeamTurn() {
        return currentTurn;
    }

    @Override
    public void setTeamTurn(TeamColor team) {
        currentTurn = team;
    }

    @Override
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        return board.getPiece(startPosition).pieceMoves(board, startPosition);
    }

    /**
     * ONLY USE FOR 2 TEAM GAMES. If 3 or 4 player games are ever implemented, delete this function
     * and replace any uses with setTeamTurn.
     */
    public void toggleTeamTurn() {
        if (currentTurn == TeamColor.WHITE) {
            currentTurn = TeamColor.BLACK;
        } else {
            currentTurn = TeamColor.WHITE;
        }
    }


    @Override
    public void makeMove(ChessMove move) throws InvalidMoveException {
        int count = 0;
        if (pastMoves.containsKey(board)) {
            count = pastMoves.get(board);
            pastMoves.replace(board, count, count + 1);
        } else {
            pastMoves.put(board, 1);
        }
    }

    @Override
    public boolean isInCheck(TeamColor teamColor) {
        return false;
    }

    @Override
    public boolean isInCheckmate(TeamColor teamColor) {
        return false;
    }

    @Override
    public boolean isInStalemate(TeamColor teamColor) {
        for (Map.Entry<ChessBoard, Integer> entry : pastMoves.entrySet()) {
            if (entry.getValue() >= 3) {
                return true;
            }
        } //Threefold repetition implementation.
        return false;
    }

    @Override
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    @Override
    public ChessBoard getBoard() {
        return board;
    }
}
