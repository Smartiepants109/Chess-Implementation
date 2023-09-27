package chessGame;

import chess.ChessPiece;
import chess.ChessPosition;

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
}
