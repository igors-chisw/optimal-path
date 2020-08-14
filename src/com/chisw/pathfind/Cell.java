package com.chisw.pathfind;

public class Cell {

    public Cell() {
        super();
    }

    public Cell(int row, int column, boolean obstacle) {
        this.id = row + "_" + column;
        this.row = row;
        this.column = column;
        this.obstacle = obstacle;
    }

    private String id;
    private int row;
    private int column;
    private boolean obstacle;
    private int size;

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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cell cell = (Cell) o;

        return id.equals(cell.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
