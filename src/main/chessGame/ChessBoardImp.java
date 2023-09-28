package chessGame;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;

public class ChessBoardImp implements chess.ChessBoard {
    private static final int BOARD_SIZE = 8;

    public ChessBoardImp() {
        resetBoard();
    }

    private ChessPiece[][] board;

    public ChessBoardImp(ChessBoard oldBoard) {
        board = new ChessPiece[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                ChessPositionImp pos = new ChessPositionImp(i, j);
                board[i][j] = oldBoard.getPiece(pos);
            }
        }
    }

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
