package chessGame;

public class ChessPositionImp implements chess.ChessPosition {
    int row, column;

    public ChessPositionImp(int row, int column) {
        this.row = row - 1;
        this.column = column - 1;
    }

    @Override
    public int getRow() {
        return row + 1;
    }

    @Override
    public int getColumn() {
        return column + 1;
    }

    protected void addToColumn(int i) {
        column = column + i;
    }

    protected void addToRow(int i) {
        row = row + i;
    }
}
