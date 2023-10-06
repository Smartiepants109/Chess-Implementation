package chessGame;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

public class ChessBoardImp implements chess.ChessBoard {
    private static final int BOARD_SIZE = 8;
    private ChessPiece[][] board;

    public ChessBoardImp() {
        board = new ChessPiece[8][8];

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
        board[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    public void removePiece(ChessPosition position) {
        board[position.getRow() - 1][position.getColumn() - 1] = null; // RowColumn, or ColumnRow?
    }

    public void movePiece(ChessPosition start, ChessPosition end) {
        //Track dead pieces? Potential TODO here
        board[end.getRow() - 1][end.getColumn() - 1] = board[start.getRow() - 1][start.getColumn() - 1];
        board[start.getRow() - 1][start.getColumn() - 1] = null;
    }

    public void swapPiece(ChessPosition pos1, ChessPosition pos2) {
        ChessPiece temp = board[pos2.getRow() - 1][pos2.getColumn() - 1];
        board[pos2.getRow() - 1][pos2.getColumn() - 1] = board[pos1.getRow() - 1][pos1.getColumn() - 1];
        board[pos1.getRow() - 1][pos1.getColumn() - 1] = temp;
    }

    @Override
    public ChessPiece getPiece(ChessPosition position) {
        int row, column;
        row = position.getRow() - 1;
        column = position.getColumn() - 1;
        if (row < 0 || row > 7 || column < 0 || column > 7) {
            return ChessPieceImp.OUT_OF_BOUNDS_FOR_MOVE_FINDER;
        }
        return board[position.getRow() - 1][position.getColumn() - 1];
    }

    @Override
    public void resetBoard() {
        board = new ChessPiece[8][8];
        int bRow = 0;

        int aRow = 7;
        board[bRow][0] = new ChessPieceImp(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        board[bRow][1] = new ChessPieceImp(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        board[bRow][2] = new ChessPieceImp(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        board[bRow][3] = new ChessPieceImp(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        board[bRow][4] = new ChessPieceImp(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        board[bRow][5] = new ChessPieceImp(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        board[bRow][6] = new ChessPieceImp(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        board[bRow][7] = new ChessPieceImp(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        for (int i = 0; i < 8; i++) {
            board[1][i] = new ChessPieceImp(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        }

        board[aRow][7] = new ChessPieceImp(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        board[aRow][6] = new ChessPieceImp(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        board[aRow][5] = new ChessPieceImp(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        board[aRow][3] = new ChessPieceImp(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        board[aRow][4] = new ChessPieceImp(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        board[aRow][2] = new ChessPieceImp(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        board[aRow][1] = new ChessPieceImp(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        board[aRow][0] = new ChessPieceImp(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        for (int i = 0; i < 8; i++) {
            board[6][i] = new ChessPieceImp(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            sb.append("|");
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == null) {
                    sb.append(" ");
                } else {
                    sb.append(board[i][j].toString());
                }
                sb.append("|");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
