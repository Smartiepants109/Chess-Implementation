package chessGame;

import chess.*;

import java.util.Collection;
import java.util.Vector;

public class ChessPieceImp implements chess.ChessPiece {
    public static ChessPiece OUT_OF_BOUNDS_FOR_MOVE_FINDER;
    ChessGame.TeamColor color;
    PieceType pieceType;


    public ChessPieceImp(ChessGame.TeamColor pieceColor, PieceType type) {
        color = pieceColor;
        pieceType = type;

    }

    @Override
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    @Override
    public PieceType getPieceType() {
        return pieceType;
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Vector<ChessMove> allMoves = new Vector<>();
        int row = myPosition.getRow();
        int column = myPosition.getColumn();
        switch (pieceType) {
            case PAWN:
                if (color == ChessGame.TeamColor.WHITE) {
                    //top
                    if (row == 1) {
                        ChessPositionImp dest = new ChessPositionImp(3, column);
                        if (board.getPiece(dest) == null) {
                            allMoves.add(new ChessMoveImp(myPosition, dest, null));
                        }
                    }
                    if (row == 6) {
                        ChessPositionImp dest = new ChessPositionImp(7, column);
                        if (board.getPiece(dest) == null) {
                            allMoves.add(new ChessMoveImp(myPosition, dest, PieceType.QUEEN));
                        }
                    } else {
                        ChessPositionImp dest = new ChessPositionImp(row + 1, column);
                        if (board.getPiece(dest) == null) {
                            allMoves.add(new ChessMoveImp(myPosition, dest, null));
                        }
                    }
                    //attacking a piece
                    ChessPiece temp = board.getPiece(new ChessPositionImp(row + 1, column + 1));
                    if (isNotNullOrOOB(temp)) {
                        PieceType promo = null;
                        if (row == 6) {
                            promo = PieceType.QUEEN;
                        }
                        allMoves.add(new ChessMoveImp(myPosition, new ChessPositionImp(row + 1, column + 1), promo));
                    }
                    temp = board.getPiece(new ChessPositionImp(row + 1, column - 1));
                    if (isNotNullOrOOB(temp)) {
                        PieceType promo = null;
                        if (row == 6) {
                            promo = PieceType.QUEEN;
                        }
                        allMoves.add(new ChessMoveImp(myPosition, new ChessPositionImp(row + 1, column - 1), promo));
                    }

                } else {
                    //top
                    if (row == 6) {
                        ChessPositionImp dest = new ChessPositionImp(4, column);
                        if (board.getPiece(dest) == null) {
                            allMoves.add(new ChessMoveImp(myPosition, dest, null));
                        }
                    }
                    if (row == 1) {
                        ChessPositionImp dest = new ChessPositionImp(0, column);
                        if (board.getPiece(dest) == null) {
                            allMoves.add(new ChessMoveImp(myPosition, dest, PieceType.QUEEN));
                        }
                    } else {
                        ChessPositionImp dest = new ChessPositionImp(row - 1, column);
                        if (board.getPiece(dest) == null) {
                            allMoves.add(new ChessMoveImp(myPosition, dest, null));
                        }
                    }
                    //attacking a piece
                    ChessPiece temp = board.getPiece(new ChessPositionImp(row - 1, column + 1));
                    if (isNotNullOrOOB(temp)) {
                        PieceType promo = null;
                        if (row == 1) {
                            promo = PieceType.QUEEN;
                        }
                        allMoves.add(new ChessMoveImp(myPosition, new ChessPositionImp(row - 1, column + 1), promo));
                    }
                    temp = board.getPiece(new ChessPositionImp(row - 1, column - 1));
                    if (isNotNullOrOOB(temp)) {
                        PieceType promo = null;
                        if (row == 1) {
                            promo = PieceType.QUEEN;
                        }
                        allMoves.add(new ChessMoveImp(myPosition, new ChessPositionImp(row - 1, column - 1), promo));
                    }

                }
                //Included first double, capture moves, and forward march.
                break;
            case ROOK:
                Rook(board, myPosition, allMoves, row, column);
                break;
            case KNIGHT:
                for (int i = 0; i < 4; i++) {
                    ChessPositionImp iterator = new ChessPositionImp(row, column);
                    iterate(i, iterator);
                    iterate(i, iterator);
                    iterate(i + 1, iterator);
                    if (board.getPiece(iterator) != OUT_OF_BOUNDS_FOR_MOVE_FINDER && board.getPiece(iterator).getTeamColor() != color) {
                        allMoves.add(new ChessMoveImp(myPosition, iterator, null));
                    }
                    iterate(i - 1, iterator);
                    iterate(i - 1, iterator);
                    if (board.getPiece(iterator) != OUT_OF_BOUNDS_FOR_MOVE_FINDER && board.getPiece(iterator).getTeamColor() != color) {
                        allMoves.add(new ChessMoveImp(myPosition, iterator, null));
                    }
                }
                break;
            case KING:
                for (int i = 0; i < 4; i++) {
                    ChessPositionImp iterator = new ChessPositionImp(row, column);
                    // 0 is + 1 column, 1 is + 1 row, then -1 col and -1 row
                    iterate(i, iterator);
                    if (board.getPiece(iterator) != OUT_OF_BOUNDS_FOR_MOVE_FINDER && board.getPiece(iterator).getTeamColor() != color) {
                        allMoves.add(new ChessMoveImp(myPosition, iterator, null));
                    }
                }
                break;
            case BISHOP:
                getBishopMove(board, myPosition, allMoves, row, column);
                break;
            case QUEEN:
                getBishopMove(board, myPosition, allMoves, row, column);
                Rook(board, myPosition, allMoves, row, column);
                break;
        }
        return allMoves;
    }

    private void getBishopMove(ChessBoard board, ChessPosition myPosition, Vector<ChessMove> vectorToAdd, int row, int column) {
        for (int i = 0; i < 4; i++) {
            ChessPositionImp iterator = new ChessPositionImp(row, column);
            // 0 is + 1 column, 1 is + 1 row, then -1 col and -1 row
            iterate(i, iterator);
            iterate(i + 1, iterator);
            while (board.getPiece(iterator) == null) {
                vectorToAdd.add(new ChessMoveImp(myPosition, iterator, null));
                iterate(i, iterator);
                iterate(i + 1, iterator);
            }
            //now have either hit something or perished
            if (isNotNullOrOOB(board.getPiece(iterator)) && board.getPiece(iterator).getTeamColor() != color) {
                vectorToAdd.add(new ChessMoveImp(myPosition, iterator, null));
            }
        }
    }

    private void Rook(ChessBoard board, ChessPosition myPosition, Vector<ChessMove> collectionToAdd, int row, int column) {
        for (int i = 0; i < 4; i++) {
            ChessPositionImp iterator = new ChessPositionImp(row, column);
            // 0 is + 1 column, 1 is + 1 row, then -1 col and -1 row
            iterate(i, iterator);
            while (board.getPiece(iterator) == null) {
                collectionToAdd.add(new ChessMoveImp(myPosition, iterator, null));
                iterate(i, iterator);
            }
            //now have either hit something or perished
            if (isNotNullOrOOB(board.getPiece(iterator)) && board.getPiece(iterator).getTeamColor() != color) {
                collectionToAdd.add(new ChessMoveImp(myPosition, iterator, null));
            }
        }
    }

    /**
     * @param i        0 is + 1 column, 1 is + 1 row, then -1 col and -1 row
     * @param iterator pos to iterate
     */
    private void iterate(int i, ChessPositionImp iterator) {
        switch (i % 4) {
            case 0:
                iterator.addToColumn(1);
                break;
            case 1:
                iterator.addToRow(1);
                break;
            case 2:
                iterator.addToColumn(-1);
                break;
            default:
                iterator.addToRow(-1);
                break;
        }
    }

    private static boolean isNotNullOrOOB(ChessPiece chessPiece) {
        return chessPiece != null && chessPiece != OUT_OF_BOUNDS_FOR_MOVE_FINDER;
    }
}
