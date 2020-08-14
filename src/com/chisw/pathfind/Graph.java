package com.chisw.pathfind;

import java.util.*;
import java.util.stream.Collectors;

public class Graph {
    private final Set<Cell> cells = new HashSet<>();

    private final Map<String, Set<String>> connections = new HashMap<>();
    private Cell start;
    private Cell target;

    public Graph() {
        for(int i = 0; i < 100; i++) {
            for(int j = 0; j < 100; j++) {
                Cell cell = new Cell(i, j, false);
                cells.add(cell);
                Set<String> connectionsValue = new HashSet<>();
                for(int k = i - 1; k <= i + 1; k++) {
                    for (int v = j - 1; v <= j + 1; v++) {
                        if(k >= 0 && k < 100 && v >= 0 && v < 100 && !(k == i && v == j)) {
                            connectionsValue.add(k + "_" + v);
                        }
                    }
                }
                connections.put(cell.getId(), connectionsValue);
            }
        }
        start = getCellById("00");
        start = getCellById("9999");
    }

    public Cell getNode(String id) {
        return cells.stream()
                .filter(node -> node.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No node found with ID"));
    }

    public Set<Cell> getConnections(Cell node) {
        return connections.get(node.getId()).stream()
                .map(this::getNode)
                .collect(Collectors.toSet());
    }


    public Set<Cell> getCells() {
        return cells;
    }

    public Map<String, Set<String>> getConnections() {
        return connections;
    }

    public Cell getStart() {
        return start;
    }

    public Cell getTarget() {
        return target;
    }

    public boolean setObstacle(int row, int column) {
        Cell cell = getCellById(row + "_" + column);
        if(cell == null) return false;
        if(!cell.isObstacle()) {
            cell.setObstacle(true);
            markObstacle(cell.getId());
            return true;
        } else {
            cell.setObstacle(false);
            unmarkObstacle(cell);
            return false;
        }
    }

    public void setStart(int row, int column, int size) {
        start = getCellById(row + "_" + column);
        start.setSize(size);
    }

    public Cell setTarget(int row, int column) {
        Cell newTarget = getCellById(row + "_" + column);
        if(newTarget == null) return target;
        Cell oldTarget = target;
        target = newTarget;
        return oldTarget;
    }

    public Cell getByRowAndColumn(int row, int column) {
        return getCellById(row + "_" + column);
    }

    private void markObstacle(String cellId) {
        connections.remove(cellId);

        List<Map.Entry<String, Set<String>>> entryList = connections.entrySet().stream().filter(entry -> entry.getValue().contains(cellId))
                .peek(entry -> {
                    Set<String> nodes = entry.getValue();
                    nodes.remove(cellId);
                    entry.setValue(nodes);
                }).collect(Collectors.toList());
        entryList.forEach(entry -> connections.put(entry.getKey(), entry.getValue()));
    }

    private void unmarkObstacle(Cell cell) {
        Set<Cell> nearestCells = new HashSet<>();
        for(int row = cell.getRow() - 1; row <= cell.getRow() + 1; row++) {
            for(int column = cell.getColumn() - 1; column <= cell.getColumn() + 1; column++) {
                Cell nearCell = getCellById(row + "_" + column);
                if(nearCell == null) continue;
                if(nearCell.equals(cell) || nearCell.isObstacle()) continue;
                nearestCells.add(nearCell);
            }
        }
        connections.put(cell.getId(), new HashSet<>(nearestCells.stream().map(Cell::getId).collect(Collectors.toList())));
        nearestCells.forEach(nearCell -> {
            Set<String> ids = connections.get(nearCell.getId());
            ids.add(cell.getId());
            connections.put(nearCell.getId(), ids);
        });
    }

    private Cell getCellById(String id) {
        return cells.stream().filter(cell -> cell.getId().equals(id)).findFirst().orElse(null);
    }
}
