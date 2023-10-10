package chessGame;

import chess.*;

import java.util.*;

public class ChessPieceImp implements chess.ChessPiece {
    public static ChessPiece OUT_OF_BOUNDS_FOR_MOVE_FINDER = new ChessPieceImp(true);
    ChessGame.TeamColor color;
    PieceType pieceType;
    boolean isError;

    public boolean isVariable() {
        return isError;
    }

    public boolean setVariableYes() {
        if (pieceType == PieceType.QUEEN) {
            return false;
        }
        isError = false;
        return true;
    }


    private ChessPieceImp(boolean isError) {
        this.isError = true;
        color = ChessGame.TeamColor.WHITE;
        pieceType = PieceType.QUEEN;
    }

    public ChessPieceImp(ChessGame.TeamColor pieceColor, PieceType type) {
        color = pieceColor;
        pieceType = type;
        isError = false;
        if (pieceType == PieceType.KING || pieceType == PieceType.ROOK || pieceType == PieceType.PAWN) {
            isError = true; // isError is only an error indicator if the pieceType is a queen.
        }
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
        Set<ChessMove> allMoves = new HashSet<>();
        int row = myPosition.getRow();
        int column = myPosition.getColumn();
        switch (pieceType) {
            case PAWN:
                if (color == ChessGame.TeamColor.WHITE) {
                    //top
                    if (row == 2) {
                        ChessPositionImp dest = new ChessPositionImp(4, column);
                        ChessPositionImp behindDest = new ChessPositionImp(3, column);
                        if (board.getPiece(dest) == null && board.getPiece(behindDest) == null) {
                            allMoves.add(new ChessMoveImp(myPosition, dest, null));
                        }
                    }
                    if (row == 7) {
                        ChessPositionImp dest = new ChessPositionImp(8, column);
                        if (board.getPiece(dest) == null) {
                            allMoves.add(new ChessMoveImp(myPosition, dest, PieceType.QUEEN));
                            allMoves.add(new ChessMoveImp(myPosition, dest, PieceType.ROOK));
                            allMoves.add(new ChessMoveImp(myPosition, dest, PieceType.KNIGHT));
                            allMoves.add(new ChessMoveImp(myPosition, dest, PieceType.BISHOP));
                        }
                    } else {
                        ChessPositionImp dest = new ChessPositionImp(row + 1, column);
                        if (board.getPiece(dest) == null) {
                            allMoves.add(new ChessMoveImp(myPosition, dest, null));
                        }
                    }
                    //attacking a piece
                    ChessPiece temp = board.getPiece(new ChessPositionImp(row + 1, column + 1));
                    if (isNotNullOrOOB(temp) && temp.getTeamColor() != color) {
                        PieceType promo = null;
                        if (row == 7) {
                            allMoves.add(new ChessMoveImp(myPosition, new ChessPositionImp(row + 1, column + 1), PieceType.QUEEN));
                            allMoves.add(new ChessMoveImp(myPosition, new ChessPositionImp(row + 1, column + 1), PieceType.BISHOP));
                            allMoves.add(new ChessMoveImp(myPosition, new ChessPositionImp(row + 1, column + 1), PieceType.ROOK));
                            allMoves.add(new ChessMoveImp(myPosition, new ChessPositionImp(row + 1, column + 1), PieceType.KNIGHT));
                        } else {
                            allMoves.add(new ChessMoveImp(myPosition, new ChessPositionImp(row + 1, column + 1), promo));
                        }
                    }
                    temp = board.getPiece(new ChessPositionImp(row + 1, column - 1));
                    if (isNotNullOrOOB(temp) && temp.getTeamColor() != color) {
                        PieceType promo = null;
                        if (row == 7) {
                            allMoves.add(new ChessMoveImp(myPosition, new ChessPositionImp(row + 1, column - 1), PieceType.BISHOP));
                            allMoves.add(new ChessMoveImp(myPosition, new ChessPositionImp(row + 1, column - 1), PieceType.QUEEN));
                            allMoves.add(new ChessMoveImp(myPosition, new ChessPositionImp(row + 1, column - 1), PieceType.ROOK));
                            allMoves.add(new ChessMoveImp(myPosition, new ChessPositionImp(row + 1, column - 1), PieceType.KNIGHT));
                        } else {
                            allMoves.add(new ChessMoveImp(myPosition, new ChessPositionImp(row + 1, column - 1), promo));
                        }
                    }

                } else {
                    //top
                    if (row == 7) {
                        ChessPositionImp dest = new ChessPositionImp(5, column);
                        ChessPositionImp behindDest = new ChessPositionImp(6, column);
                        if (board.getPiece(dest) == null && board.getPiece(behindDest) == null) {
                            allMoves.add(new ChessMoveImp(myPosition, dest, null));
                        }
                    }
                    if (row == 2) {
                        ChessPositionImp dest = new ChessPositionImp(1, column);
                        if (board.getPiece(dest) == null) {
                            allMoves.add(new ChessMoveImp(myPosition, dest, PieceType.QUEEN));
                            allMoves.add(new ChessMoveImp(myPosition, dest, PieceType.BISHOP));
                            allMoves.add(new ChessMoveImp(myPosition, dest, PieceType.ROOK));
                            allMoves.add(new ChessMoveImp(myPosition, dest, PieceType.KNIGHT));
                        }
                    } else {
                        ChessPositionImp dest = new ChessPositionImp(row - 1, column);
                        if (board.getPiece(dest) == null) {
                            allMoves.add(new ChessMoveImp(myPosition, dest, null));
                        }
                    }
                    //attacking a piece
                    ChessPiece temp = board.getPiece(new ChessPositionImp(row - 1, column + 1));
                    if (isNotNullOrOOB(temp) && temp.getTeamColor() != color) {
                        PieceType promo = null;
                        if (row == 2) {
                            allMoves.add(new ChessMoveImp(myPosition, new ChessPositionImp(row - 1, column + 1), PieceType.ROOK));
                            allMoves.add(new ChessMoveImp(myPosition, new ChessPositionImp(row - 1, column + 1), PieceType.KNIGHT));
                            allMoves.add(new ChessMoveImp(myPosition, new ChessPositionImp(row - 1, column + 1), PieceType.QUEEN));
                            allMoves.add(new ChessMoveImp(myPosition, new ChessPositionImp(row - 1, column + 1), PieceType.BISHOP));
                        } else {
                            allMoves.add(new ChessMoveImp(myPosition, new ChessPositionImp(row - 1, column + 1), promo));
                        }
                    }
                    temp = board.getPiece(new ChessPositionImp(row - 1, column - 1));
                    if (isNotNullOrOOB(temp) && temp.getTeamColor() != color) {
                        PieceType promo = null;
                        if (row == 2) {
                            allMoves.add(new ChessMoveImp(myPosition, new ChessPositionImp(row - 1, column - 1), PieceType.KNIGHT));
                            allMoves.add(new ChessMoveImp(myPosition, new ChessPositionImp(row - 1, column - 1), PieceType.BISHOP));
                            allMoves.add(new ChessMoveImp(myPosition, new ChessPositionImp(row - 1, column - 1), PieceType.ROOK));
                            allMoves.add(new ChessMoveImp(myPosition, new ChessPositionImp(row - 1, column - 1), PieceType.QUEEN));
                        } else {
                            allMoves.add(new ChessMoveImp(myPosition, new ChessPositionImp(row - 1, column - 1), promo));
                        }
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
                    ifNothingOrEnemyThenAddToMoves(board, myPosition, allMoves, iterator);
                    iterate(i - 1, iterator);
                    iterate(i - 1, iterator);
                    ifNothingOrEnemyThenAddToMoves(board, myPosition, allMoves, iterator);
                }
                break;
            case KING:
                //TODO: Check for castling, if possible, enable.
                ChessPieceImp leftWhiteStarter = (ChessPieceImp) board.getPiece(ChessPositionImp.DEFTWHITEROOKLEFTSTART);
                if (leftWhiteStarter != null) {
                    if (isError && myPosition.equals(ChessPositionImp.DEFTWHITEKINGSTART) && leftWhiteStarter.getPieceType() == PieceType.ROOK && leftWhiteStarter.isError) {

                    }
                }
                ChessPieceImp rightWhiteStarter = (ChessPieceImp) board.getPiece(ChessPositionImp.DEFTWHITEROOKRIGHTSTART);
                if (leftWhiteStarter != null) {
                    if (isError && myPosition.equals(ChessPositionImp.DEFTWHITEKINGSTART) && rightWhiteStarter.getPieceType() == PieceType.ROOK && rightWhiteStarter.isError) {

                    }
                }

                ChessPositionImp iterator = new ChessPositionImp(row, column);
                // 0 is + 1 column, 1 is + 1 row, then -1 col and -1 row
                iterate(0, iterator);
                ifNothingOrEnemyThenAddToMoves(board, myPosition, allMoves, iterator);
                iterate(1, iterator);
                ifNothingOrEnemyThenAddToMoves(board, myPosition, allMoves, iterator);
                iterate(2, iterator);
                ifNothingOrEnemyThenAddToMoves(board, myPosition, allMoves, iterator);
                iterate(2, iterator);
                ifNothingOrEnemyThenAddToMoves(board, myPosition, allMoves, iterator);
                iterate(3, iterator);
                ifNothingOrEnemyThenAddToMoves(board, myPosition, allMoves, iterator);
                iterate(3, iterator);
                ifNothingOrEnemyThenAddToMoves(board, myPosition, allMoves, iterator);
                iterate(0, iterator);
                ifNothingOrEnemyThenAddToMoves(board, myPosition, allMoves, iterator);
                iterate(0, iterator);
                ifNothingOrEnemyThenAddToMoves(board, myPosition, allMoves, iterator);

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

    private void ifNothingOrEnemyThenAddToMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> allMoves, ChessPositionImp iterator) {
        if (board.getPiece(iterator) == null) {
            allMoves.add(new ChessMoveImp(myPosition, iterator, null));
        } else {
            if (!isError(board.getPiece(iterator)) && board.getPiece(iterator).getTeamColor() != color) {
                allMoves.add(new ChessMoveImp(myPosition, iterator, null));
            }
        }
    }

    private void getBishopMove(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> vectorToAdd, int row, int column) {
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

    private void Rook(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> collectionToAdd, int row, int column) {
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
        return chessPiece != null && !isError(chessPiece);
    }

    private static boolean isError(ChessPiece chessPiece) {
        return ((ChessPieceImp) chessPiece).isError && ((ChessPieceImp) chessPiece).pieceType == PieceType.QUEEN;
    }

    public String toString() {
        if (isError && pieceType == PieceType.QUEEN) {
            return "ERROR - YOU SHOULD NOT SEE THIS";
        }
        if (color == ChessGame.TeamColor.WHITE) {
            switch (pieceType) {
                case KING -> {
                    return "K";
                }
                case BISHOP -> {
                    return "B";
                }
                case KNIGHT -> {
                    return "N";
                }
                case PAWN -> {
                    return "P";
                }
                case ROOK -> {
                    return "R";
                }
                default -> {
                    return "Q";
                }
            }
        } else {
            switch (pieceType) {
                case KING -> {
                    return "k";
                }
                case BISHOP -> {
                    return "b";
                }
                case KNIGHT -> {
                    return "n";
                }
                case PAWN -> {
                    return "p";
                }
                case ROOK -> {
                    return "r";
                }
                default -> {
                    return "q";
                }
            }

        }
    }
}
