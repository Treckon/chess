package chess;

public class Position implements ChessPosition{

    private int rowNum = 0;
    private int colNum = 0;

    Position(int row, int col){
        rowNum = row;
        colNum = col;
    }

    @Override
    public int getRow() {
        return rowNum;
    }

    @Override
    public int getColumn() {
        return colNum;
    }

    public String getPosition(){
        StringBuilder position = new StringBuilder();
        position.append('(');
        position.append(rowNum);
        position.append(',');
        position.append(colNum);
        position.append(')');

        return position.toString();
    }
}
