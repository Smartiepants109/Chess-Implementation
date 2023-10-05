package chessGame;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;

public class ChessBoardImp implements chess.ChessBoard {
    private static final int BOARD_SIZE = 8;
    private ChessPiece[][] board;

    public ChessBoardImp() {
        resetBoard();
    }


    public ChessBoardImp(ChessBoard oldBoard) {
        board = new ChessPiece[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                ChessPositionImp pos = new ChessPositionImp(i, j);
                board[i][j] = oldBoard.getPiece(pos);
            }
        }
    }

    /**
     * @param position where to add the piece to
     * @param piece    the piece to add. DOES NOT COPY. USE PROPER PIECE
     */
    @Override
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow()][position.getColumn()] = piece;
    }

    public void removePiece(ChessPosition position) {
        board[position.getRow()][position.getColumn()] = null; // RowColumn, or ColumnRow?
    }

    public void movePiece(ChessPosition start, ChessPosition end) {
        //Track dead pieces? Potential TODO here
        board[end.getRow()][end.getColumn()] = board[start.getRow()][start.getColumn()];
        board[start.getRow()][start.getColumn()] = null;
    }

    public void swapPiece(ChessPosition pos1, ChessPosition pos2) {
        ChessPiece temp = board[pos2.getRow()][pos2.getColumn()];
        board[pos2.getRow()][pos2.getColumn()] = board[pos1.getRow()][pos1.getColumn()];
        board[pos1.getRow()][pos1.getColumn()] = temp;
    }

    @Override
    public ChessPiece getPiece(ChessPosition position) {
        int row, column;
        row = position.getRow();
        column  = position.getColumn();
        if(row < 0 || row > 7 || column < 0 || column > 7){
            return ChessPieceImp.OUT_OF_BOUNDS_FOR_MOVE_FINDER;
        }
        return board[position.getRow()][position.getColumn()];
    }

    @Override
    public void resetBoard() {
        board = new ChessPiece[8][8];
    }
}
