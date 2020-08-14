package com.chisw;

import com.chisw.pathfind.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GridButtonPanel {

    private Graph graph = new Graph();
    List<Cell> route = new ArrayList();

    private static final int N = 100;
    private final List<JButton> list = new ArrayList<>();
    private final JRadioButton start = new JRadioButton("start");
    private final JRadioButton obstacle = new JRadioButton("obstacle");
    private final JRadioButton target = new JRadioButton("target");
    private final JSlider size = new JSlider(JSlider.HORIZONTAL, 1, 10,1);

    private JButton getGridButton(int r, int c) {
        int index = r * N + c;
        return list.get(index);
    }

    private List<JButton> getGridButtons(Color color) {
        return list.stream().filter(button -> button.getBackground().equals(color)).collect(Collectors.toList());
    }

    private JButton createGridButton(final int row, final int col) {
        final JButton b = new JButton();
        b.setMaximumSize(new Dimension(5, 5));
        b.setBackground(Color.CYAN);
        b.addActionListener(e -> {
            JButton gb = GridButtonPanel.this.getGridButton(row, col);
            route.stream().filter(cell -> cell.getRow() == row && cell.getColumn() == col).findFirst().ifPresent(cell -> route.remove(cell));
            if(start.isSelected()) {
                GridButtonPanel.this.getGridButtons(Color.YELLOW).forEach(button -> button.setBackground(Color.CYAN));
                graph.setStart(row, col, size.getValue());
                SizeCellsUtil.getAllObjectCells(new Cell(row, col, false), size.getValue()).forEach(cell -> {
                    GridButtonPanel.this.getGridButton(cell.getRow(), cell.getColumn()).setBackground(Color.YELLOW);
                });
                System.out.println("r" + row + ",c" + col
                        + " with size " + size.getValue() + " new start point");
            } else if(obstacle.isSelected()) {
                boolean isObstacle = graph.setObstacle(row, col);
                if(isObstacle) {
                    gb.setBackground(Color.BLACK);
                } else gb.setBackground(Color.CYAN);
                String message = "r" + row + ",c" + col
                        + " is now" + (isObstacle ? " an obstacle" : " not an obstacle");
                System.out.println(message);
            } else if(target.isSelected()) {
                Cell oldTarget = graph.setTarget(row, col);
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
        p.add(new JLabel("Object size"));
        size.setMinimum(1);
        size.setMaximum(10);
        size.setMajorTickSpacing(1);
        size.setPaintTicks(true);
        size.setPaintLabels(true);
        p.add(size);
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
            GridButtonPanel.this.getGridButtons(Color.GREEN).forEach(gb -> gb.setBackground(Color.CYAN));
            RouteFinder routeFinder = new RouteFinder(graph, new CellScorer());
            route = routeFinder.findRoute(graph.getStart(), graph.getTarget());
            route.forEach(cell -> {
                SizeCellsUtil.getAllObjectCells(cell, graph.getStart().getSize()).forEach(objectCell -> {
                    JButton gb = GridButtonPanel.this.getGridButton(objectCell.getRow(), objectCell.getColumn());
                    if(gb.getBackground().equals(Color.CYAN)) gb.setBackground(Color.GREEN);
                });
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
