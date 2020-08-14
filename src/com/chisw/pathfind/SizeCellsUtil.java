package com.chisw.pathfind;

import java.util.HashSet;
import java.util.Set;

public class SizeCellsUtil {

    public static Set<Cell> getAllObjectCells(Cell center, int size) {
        Set<Cell> cells = new HashSet<>();
        cells.add(center);
        for(int c = center.getColumn() - (size - (size / 2) - 1); c <= center.getColumn() + (size - (size / 2) - (size % 2)); c++) {
            for(int r = center.getRow() - (size - (size / 2) - 1); r <= center.getRow() + (size - (size / 2) - (size % 2)); r++) {
                cells.add(new Cell(r, c, false));
            }
        }
        return cells;
    }
}
