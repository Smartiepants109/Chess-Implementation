package chessGame;

import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Objects;

public class ChessMoveImp implements chess.ChessMove {
    ChessPositionImp start;
    ChessPositionImp end;
    ChessPiece.PieceType promoPiece;

    public ChessMoveImp(ChessPosition startPosition, ChessPosition endPosition, ChessPiece.PieceType promotionPiece) {
        start = new ChessPositionImp(startPosition.getRow(), startPosition.getColumn());
        end = new ChessPositionImp(endPosition.getRow(), endPosition.getColumn());
        promoPiece = promotionPiece;
    }

    @Override
    public ChessPosition getStartPosition() {
        return start;
    }

    @Override
    public ChessPosition getEndPosition() {
        return end;
    }

    @Override
    public ChessPiece.PieceType getPromotionPiece() {
        return promoPiece;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessMoveImp move = (ChessMoveImp) o;
        return Objects.equals(start, move.start) && Objects.equals(end, move.end) && promoPiece == move.promoPiece;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, promoPiece);
    }
}
