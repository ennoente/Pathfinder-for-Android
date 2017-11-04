package com.ichbingrumpig.pathfinder;

/**
 * Vital part of the A* implementation
 *
 * A Node object represents one cell/node/pixel in the grid
 */

class Node {

    /** The final cost of this Node */
    int finalCost = 0;

    /** The heuristic cost of this Node */
    int heuristicCost = 0;

    /** The x and y coordinates in the grid */
    int x, y;

    /** The travelling factor of this Node. g(n) = cost * travellingFactor */
    float travellingFactor;

    /** This Node's parent.
     * The parent is the Node that led to this Node.
     * The greatest parent is the starting Node.
     */
    Node parent;

    /** Constructor
     *
     * @param x The x-coordinate of the Node object in the grid
     * @param y The y-coordinate of the Node object in the grid
     */
    Node(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
