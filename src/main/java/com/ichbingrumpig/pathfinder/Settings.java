package com.ichbingrumpig.pathfinder;

import android.util.SparseArray;

/**
 * This interface is used to initialize the algorithm.
 * The algorithm needs some data and rules to work properly. Those data and rules can be set
 * via this interface.
 *
 * {@link #getGrid()}:
 * The algorithm needs the game defining grid to create the {@link Node} array.
 * Override {@link #getGrid()} to return this game grid.
 *
 * {@link #setTravellingCostRules()}:
 * Can be used to set rules, if and which values in the game grid
 * have specific travelling cost factors.
 *
 * {@link #isNodeBlocked(int, int)}:
 * Is used to set rules, which values in the game grid mark Nodes to be blocked.
 *
 * @see #getGrid()
 * @see #setTravellingCostRules()
 * @see #isNodeBlocked(int, int)
 */
public interface Settings {

    /**
     * Override this method to return the game grid array
     * This game grid is modulated as a two-dimensional int array:
     *
     * int[][] grid = new int[width][height];
     *
     * with @width and @height being the width and height of the game board.
     * Thus every pixel in the game is represented by one field inside the grid array
     * and is therefore represented by one Node in the pathginding algorithm.
     *
     * @return The game defining grid as a two-dimensional int array
     */
    int[][] getGrid();

    /**
     * This library supports multiple travelling factors for different Nodes.
     * Override this method to set rules for the travelling costs of Nodes by
     * returning a SparseArray with key-value pairs containing the grid value as key
     * and the travelling factor as value.
     *
     *
     * In the following example the grid contains Nodes representing woods in a game.
     * Travelling those Nodes is possible, but only with half the speed, ergo double the costs.
     * Insert like the following:
     *
     *
     * public SparseArray<Float> setTravellingCostRules() {
     *     SparseArray<Float> travellingRules = new SparseArray<Float>();
     *     travellingRules.put (grid_value, cost).
     *     return travellingRules;
     * }
     *
     * All Nodes that have no special travelling cost factor (given by this setter) automatically
     * have a travelling factor of 1f.
     *
     * @return The travelling cost rules of the game.
     */
    SparseArray<Float> setTravellingCostRules();

    /**
     * Set the rules if the Node at [x][y] is blocked. Override to customize the rules of blocked Nodes.
     *
     * In this example Nodes are marked as blocked if the grid value at [x, y]
     * is bigger than or equal to 0, or if the grid's value at [x, y] is -2.
     *
     *
     * public boolean isNodeBlocked (int x, int y) {
     *     return getGrid()[x][y] >= 0 || getGrid()[x][y] == -2;
     * }
     *
     *
     * @param x The x-coordinate of the Node
     * @param y The y-coordinate of the Node
     * @return If the Node at [x, y] is blocked
     *
     */
    boolean isNodeBlocked(int x, int y);
}
