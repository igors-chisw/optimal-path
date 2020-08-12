package com.chisw.pathfind;

public class CellScorer implements Scorer<Cell> {
    @Override
    public double computeCost(Cell from, Cell to) {
        return Math.sqrt(Math.pow(from.getColumn() - to.getColumn(), 2)
                + Math.pow(from.getRow() - to.getRow(), 2));
    }
}
