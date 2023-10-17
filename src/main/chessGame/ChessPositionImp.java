package chessGame;

import chess.ChessPosition;

import java.util.Objects;

public class ChessPositionImp implements chess.ChessPosition {
    /**
     * the location that white kings start at.
     */
    public static final ChessPosition DEFTWHITEKINGSTART = new ChessPositionImp(1,5);
    /**
     * The location that white rooks start at.
     */
    public static final ChessPosition DEFTWHITEROOKLEFTSTART = new ChessPositionImp(1, 1);
    /**
     * The location that white rooks start at.
     */
    public static final ChessPosition DEFTWHITEROOKRIGHTSTART = new ChessPositionImp(1, 8);

    /**
     * row and column.
     */
    int row, column;

    /**
     * generates new position based on coordinates.
     * @param row the row of the position.
     * @param column the column of the position.
     */
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

    /**
     * increments column by i.
     * @param i adds this number
     */
    protected void addToColumn(int i) {
        column = column + i;
    }

    /**
     * increments row by i.
     * @param i adds this number
     * */
    protected void addToRow(int i) {
        row = row + i;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPositionImp position = (ChessPositionImp) o;
        return Objects.equals(row, position.row) && Objects.equals(column, position.column);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}
