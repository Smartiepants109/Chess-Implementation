package chessGame;

import chess.ChessPiece;
import chess.ChessPosition;

public class myBoard implements chess.ChessBoard {
    public myBoard() {
        resetBoard();
    }

    private ChessPiece[][] board;

    @Override
    public void addPiece(ChessPosition position, ChessPiece piece) {

    }

    @Override
    public ChessPiece getPiece(ChessPosition position) {
        return null;
    }

    @Override
    public void resetBoard() {
        board = new ChessPiece[8][8];
    }
}
