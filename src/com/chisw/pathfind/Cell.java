package com.chisw.pathfind;

public class Cell implements GraphNode {

    public Cell() {
        super();
    }

    public Cell(String id, int row, int column, boolean obstacle) {
        this.id = id;
        this.row = row;
        this.column = column;
        this.obstacle = obstacle;
    }

    private String id;
    private int row;
    private int column;
    private boolean obstacle;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public boolean isObstacle() {
        return obstacle;
    }

    public void setObstacle(boolean obstacle) {
        this.obstacle = obstacle;
    }
}
