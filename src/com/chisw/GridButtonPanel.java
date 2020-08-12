package com.chisw;

import com.chisw.pathfind.Cell;
import com.chisw.pathfind.CellScorer;
import com.chisw.pathfind.Graph;
import com.chisw.pathfind.RouteFinder;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GridButtonPanel {

    private CellGrid cellGrid = new CellGrid();
    List<Cell> route = new ArrayList();

    private static final int N = 100;
    private final List<JButton> list = new ArrayList<>();
    private final JRadioButton start = new JRadioButton("start");
    private final JRadioButton obstacle = new JRadioButton("obstacle");
    private final JRadioButton target = new JRadioButton("target");

    private JButton getGridButton(int r, int c) {
        int index = r * N + c;
        return list.get(index);
    }

    private JButton createGridButton(final int row, final int col) {
        final JButton b = new JButton();
        b.setMaximumSize(new Dimension(5, 5));
        b.setBackground(Color.CYAN);
        b.addActionListener(e -> {
            JButton gb = GridButtonPanel.this.getGridButton(row, col);
            route.stream().filter(cell -> cell.getRow() == row && cell.getColumn() == col).findFirst().ifPresent(cell -> route.remove(cell));
            if(start.isSelected()) {
                Cell oldStart = cellGrid.setStart(row, col);
                if(oldStart != null) {
                    JButton oldStartButton = GridButtonPanel.this.getGridButton(oldStart.getRow(), oldStart.getColumn());
                    oldStartButton.setBackground(Color.CYAN);
                }
                gb.setBackground(Color.YELLOW);
                System.out.println("r" + row + ",c" + col
                        + " new start point");
            } else if(obstacle.isSelected()) {
                boolean isObstacle = cellGrid.setObstacle(row, col);
                if(isObstacle) {
                    gb.setBackground(Color.BLACK);
                } else gb.setBackground(Color.CYAN);
                String message = "r" + row + ",c" + col
                        + " is now" + (isObstacle ? " not an obstacle" : " an obstacle");
                System.out.println(message);
            } else if(target.isSelected()) {
                Cell oldTarget = cellGrid.setTarget(row, col);
                if(oldTarget != null) {
                    JButton oldTargetButton = GridButtonPanel.this.getGridButton(oldTarget.getRow(), oldTarget.getColumn());
                    oldTargetButton.setBackground(Color.CYAN);
                }
                gb.setBackground(Color.RED);
                System.out.println("r" + row + ",c" + col
                        + " new target point");
            }
        });
        return b;
    }

    private JPanel createGridPanel() {
        JPanel p = new JPanel(new GridLayout(N, N));
        for (int i = 0; i < N * N; i++) {
            int row = i / N;
            int col = i % N;
            JButton gb = createGridButton(row, col);
            list.add(gb);
            p.add(gb);
        }
        return p;
    }

    private JPanel createControlPanel() {
        JPanel p = new JPanel(new GridLayout(3, 4));
        p.add(createFindButton());
        p.add(new JLabel("Choose start, target, obstacle and click on the grid"));
        start.setBackground(Color.YELLOW);
        obstacle.setBackground(Color.BLACK);
        target.setBackground(Color.RED);
        ButtonGroup group = new ButtonGroup();
        start.setSelected(true);
        group.add(start);
        group.add(obstacle);
        group.add(target);
        p.add(start);
        p.add(obstacle);
        p.add(target);
        return p;
    }

    private JPanel createPanel() {
        JPanel p = new JPanel(new GridLayout(1, 2));
        p.add(createGridPanel());
        p.add(createControlPanel());
        return p;
    }

    private JButton createFindButton() {
        JButton find = new JButton("Find");
        find.setAlignmentX(1100);
        find.setAlignmentX(300);
        find.addActionListener(e -> {
            if(!route.isEmpty()) route.stream().skip(1).limit(route.size() - 2).forEach(cell -> {
                JButton gb = GridButtonPanel.this.getGridButton(cell.getRow(), cell.getColumn());
                gb.setBackground(Color.CYAN);
            });
            Graph<Cell> graph = new Graph(cellGrid.getCells(), cellGrid.getConnections());
            RouteFinder<Cell> routeFinder = new RouteFinder<>(graph, new CellScorer(), new CellScorer());
            route = routeFinder.findRoute(cellGrid.getStart(), cellGrid.getTarget());
            route.stream().skip(1).limit(route.size() - 2).forEach(cell -> {
                JButton gb = GridButtonPanel.this.getGridButton(cell.getRow(), cell.getColumn());
                gb.setBackground(Color.GREEN);
            });
        });
        return find;
    }

    private void display() {
        JFrame f = new JFrame("GridButton");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(createPanel());
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> new GridButtonPanel().display());
    }
}
