package chessGame;

public class ChessPositionImp implements chess.ChessPosition {
    int row, column;

    public ChessPositionImp(int row, int column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getColumn() {
        return column;
    }

    protected void addToColumn(int i) {
        column = column + i;
    }

    protected void addToRow(int i) {
        row = column + i;
    }
}
