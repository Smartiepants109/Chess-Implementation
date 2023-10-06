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
        Vector<ChessMove> moves = new Vector<>();
        Vector<ChessMove> outputMoves = new Vector<>();
        moves.addAll(board.getPiece(startPosition).pieceMoves(board, startPosition));
        for (int k = 0; k < moves.size(); k++) {
            ChessMove move = moves.get(k);
            ChessBoardImp board1 = new ChessBoardImp();
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    ChessPosition pos = new ChessPositionImp(i + 1, j + 1);
                    board1.addPiece(pos, board.getPiece(pos));
                }
            }
            board1.movePiece(move.getStartPosition(), move.getEndPosition());
            if (!checkHelper(board.getPiece(move.getStartPosition()).getTeamColor(), board1)) {
                outputMoves.add(move);
            }

        }

        return outputMoves;
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
        ChessPosition s = move.getStartPosition();
        Vector<ChessMove> valids = new Vector<>();
        valids.addAll(validMoves(s));
        for (ChessMove move1 : valids) {
            if (move1.getEndPosition().equals(move.getEndPosition())) {
                board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
                board.addPiece(move.getStartPosition(), null);
                return;
            }
        }
        throw new InvalidMoveException("Attempted to move somewhere either illegal or otherwise cause a check");
    }

    @Override
    public boolean isInCheck(TeamColor teamColor) {
        return checkHelper(teamColor, board);
    }

    private boolean checkHelper(TeamColor teamColor, ChessBoard boardUsed) {
        Vector<ChessMove> moves = new Vector<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPositionImp pos = new ChessPositionImp(i + 1, j + 1);
                if (boardUsed.getPiece(pos) != null && boardUsed.getPiece(pos).getTeamColor() != teamColor) {
                    moves.addAll(boardUsed.getPiece(pos).pieceMoves(boardUsed, pos));
                }
            }
        }
        for (ChessMove move : moves) {
            ChessPiece endPiece = boardUsed.getPiece(move.getEndPosition());
            if (endPiece != null && endPiece.getTeamColor() == teamColor && endPiece.getPieceType() == ChessPiece.PieceType.KING) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isInCheckmate(TeamColor teamColor) {
        Vector<ChessMove> moves = new Vector<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPositionImp pos = new ChessPositionImp(i + 1, j + 1);
                ChessPiece cp = board.getPiece(pos);
                if (cp != null && cp.getTeamColor() == teamColor) {
                    moves.addAll(validMoves(pos)); // if there is a move that YOU can do that would not keep you in check
                    //then you have been checkmated.
                    if (moves.size() > 0) {
                        return false;
                    }
                }
            }
        }
        if (moves.size() > 0) {
            return false; // redundancy. Just in case. I don't trust this project, lol.
        }
        return true;
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
