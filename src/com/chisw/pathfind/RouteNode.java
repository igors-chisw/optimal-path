package com.chisw.pathfind;

class RouteNode implements Comparable<RouteNode> {
    private final Cell current;
    private Cell previous;
    private double routeScore;
    private double estimatedScore;

    RouteNode(Cell current) {
        this(current, null, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    RouteNode(Cell current, Cell previous, double routeScore, double estimatedScore) {
        this.current = current;
        this.previous = previous;
        this.routeScore = routeScore;
        this.estimatedScore = estimatedScore;
    }

    @Override
    public int compareTo(RouteNode other) {
        if (this.estimatedScore > other.estimatedScore) {
            return 1;
        } else if (this.estimatedScore < other.estimatedScore) {
            return -1;
        } else {
            return 0;
        }
    }

    public Cell getCurrent() {
        return current;
    }

    public Cell getPrevious() {
        return previous;
    }

    public void setPrevious(Cell previous) {
        this.previous = previous;
    }

    public double getRouteScore() {
        return routeScore;
    }

    public void setRouteScore(double routeScore) {
        this.routeScore = routeScore;
    }

    public double getEstimatedScore() {
        return estimatedScore;
    }

    public void setEstimatedScore(double estimatedScore) {
        this.estimatedScore = estimatedScore;
    }
}
