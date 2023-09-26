package chessGame;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public class myPiece implements chess.ChessPiece{
    @Override
    public ChessGame.TeamColor getTeamColor() {
        return null;
    }

    @Override
    public PieceType getPieceType() {
        return null;
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return null;
    }
}
