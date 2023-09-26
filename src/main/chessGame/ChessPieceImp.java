package chessGame;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public class ChessPieceImp implements chess.ChessPiece {
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
        return null; //FIXME
    }
}
