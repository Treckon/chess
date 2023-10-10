package chess;

import java.util.Objects;

public class ChessPositionImpl implements ChessPosition{

    public ChessPositionImpl(int row, int col){
        rowNum = row;
        colNum = col;
    }

    private int rowNum;
    private int colNum;
    @Override
    public int getRow() {
        return rowNum;
    }

    @Override
    public int getColumn() {
        return colNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPositionImpl that = (ChessPositionImpl) o;
        return rowNum == that.rowNum && colNum == that.colNum;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowNum, colNum);
    }


}
