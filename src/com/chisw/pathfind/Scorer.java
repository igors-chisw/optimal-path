package com.chisw.pathfind;

public interface Scorer<T extends GraphNode> {
    double computeCost(T from, T to);
}
