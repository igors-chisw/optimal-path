package com.chisw.pathfind;

import java.util.*;
import java.util.stream.Collectors;

public class RouteFinder {
    private final Graph graph;
    private final CellScorer nextNodeScorer;

    public RouteFinder(Graph graph, CellScorer nextNodeScorer) {
        this.graph = graph;
        this.nextNodeScorer = nextNodeScorer;
    }

    public List<Cell> findRoute(Cell from, Cell to) {
        Map<Cell, RouteNode> allNodes = new HashMap<>();
        Queue<RouteNode> openSet = new PriorityQueue<>();

        RouteNode start = new RouteNode(from, null, 0d, nextNodeScorer.computeCost(from, to));
        allNodes.put(from, start);
        openSet.add(start);

        while (!openSet.isEmpty()) {
            System.out.println("Open Set contains: " + openSet.stream().map(RouteNode::getCurrent).collect(Collectors.toSet()));
            RouteNode next = openSet.poll();
            System.out.println("Looking at node: " + next);
            if (targetReachedForSize(to, next, from.getSize())) {
                System.out.println("Found our destination!");

                List<Cell> route = new ArrayList<>();
                RouteNode current = next;
                do {
                    route.add(0, current.getCurrent());
                    current = allNodes.get(current.getPrevious());
                } while (current != null);

                System.out.println("Route: " + route);
                return route;
            }

            graph.getConnections(next.getCurrent()).forEach(connection -> {
                if (reachableForSize(from.getSize(), connection)) {
                    double newScore = next.getRouteScore() + nextNodeScorer.computeCost(next.getCurrent(), connection);
                    RouteNode nextNode = allNodes.getOrDefault(connection, new RouteNode(connection));
                    allNodes.put(connection, nextNode);

                    if (nextNode.getRouteScore() > newScore) {
                        nextNode.setPrevious(next.getCurrent());
                        nextNode.setRouteScore(newScore);
                        nextNode.setEstimatedScore(newScore + nextNodeScorer.computeCost(connection, to));
                        openSet.add(nextNode);
                        System.out.println("Found a better route to node: " + nextNode);
                    }
                }
            });
        }

        throw new IllegalStateException("No route found");
    }

    private boolean targetReachedForSize(Cell to, RouteNode next, int size) {
        if(size == 1) return to.equals(next.getCurrent());
        return SizeCellsUtil.getAllObjectCells(next.getCurrent(), size).stream().anyMatch(to::equals);
    }

    private boolean reachableForSize(int size, Cell cell) {
        if(size == 1) return true;
        return SizeCellsUtil.getAllObjectCells(cell, size).stream().noneMatch(objectCell -> {
            Cell node;
            try {
                node = graph.getNode(objectCell.getId());
            } catch (IllegalArgumentException e) {
                return true;
            }
            return node.isObstacle();
        });
    }
}
