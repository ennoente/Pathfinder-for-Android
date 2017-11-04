package com.ichbingrumpig.pathfinder;

import android.graphics.Path;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseArray;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * The main class of this library
 * To use this library initialize the algorithm by calling {@link Pathfinder#initialize(Settings)}.
 * @see Settings
 *
 */
public class Pathfinder implements Runnable {
    /** The log tag for this class */
    private static final String TAG = "Pathfinder";

    /** The cost for travelling vertically or horizontally */
    private static final int V_H_COST = 10;

    /** The cost of travelling diagonally */
    private static final int DIAGONAL_COST = 14;

    /** The board dimensions */
    private static int boardWidth, boardHeight;

    private static SparseArray<Float> travellingCostRules;

    /** The settings for the algorithm */
    private static Settings settings;

    /** Stores the starting time. For debugging purposes */
    private long startStart;

    /** The starting coordinates */
    private int startX, startY;

    /** The destination coordinates */
    private int destX, destY;

    /** Stored the rounds the while-loop has made. For debugging purposes. */
    private int rounds = 0;

    /** The closed set */
    private boolean[][] closed;

    /** The open set */
    private PriorityQueue<Node> open;

    /**
     * The Callback interface
     * Once the algorithm has finished, {@link OnPathFoundListener#onPathFound(Path)} is called,
     * either with the resulting path object or null if no path is available.
     */
    private OnPathFoundListener onPathFoundListener;

    /**
     * Initializes the algorithm.
     * The settings must return three variables:
     *
     * @param settings The settings for this implementation.
     */
    public static void initialize (@NonNull Settings settings) {
        Pathfinder.settings = settings;
        boardWidth = settings.getGrid().length;
        boardHeight = settings.getGrid()[0].length;
        travellingCostRules = settings.setTravellingCostRules();
        Log.d(TAG, "Width and height of pathfinding grid: " + boardWidth + "|" + boardHeight);
        Log.d(TAG, "Total of " + (boardWidth * boardHeight) + " pixels.");
        Log.d(TAG, "Pathfinder initialized.");
    }

    /**
     * Implementation of the A* pathfinding algorithm.
     * Findes the optimal path between two points based on a grid of Nodes.
     *
     * @param startX The x-coordinate of the starting point
     * @param startY The y-coordinate of the starting point
     * @param destX The x-coordinate of the destination point
     * @param destY The y-coordinate of the destination point
     * @param onPathFoundListener The callback interface, see {@link OnPathFoundListener#onPathFound(Path)}
     */
    public static void findPath (int startX, int startY, int destX, int destY, @NonNull OnPathFoundListener onPathFoundListener) {
        if (settings == null) throw new IllegalArgumentException("Pathfinder not initialized! Pathfinder#initialize must be called before finding an algorithm.");
        Log.d(TAG, "Starting new thread.");
        new Thread(new Pathfinder(startX, startY, destX, destY, onPathFoundListener)).start();
    }

    /**
     * Constructor for a new algorithm object.
     *
     * @param startX The x-coordinate of the starting point
     * @param startY The y-coordinate of the starting point
     * @param destX The x-coordinate of the destination point
     * @param destY The y-coordinate of the destination point
     * @param onPathFoundListener The callback interface, see {@link OnPathFoundListener#onPathFound(Path)}
     */
    private Pathfinder(int startX, int startY, int destX, int destY, OnPathFoundListener onPathFoundListener) {
        startStart = System.currentTimeMillis();
        this.startX = startX;
        this.startY = startY;
        this.destX = destX;
        this.destY = destY;
        this.onPathFoundListener = onPathFoundListener;
    }

    @Override
    public void run() {
        long startGrid = System.currentTimeMillis();

        // Set up the node grid
        Node[][] nodes = new Node[boardWidth][boardHeight];
        for (int j = 0; j < boardHeight; j++) {
            for (int i = 0; i < boardWidth; i++) {
                if (settings.isNodeBlocked(i, j)) continue;
                nodes[i][j] = new Node(i, j);
                nodes[i][j].heuristicCost = (Math.abs(i - destX) + Math.abs(j - destY)) * 10;
                nodes[i][j].travellingFactor = travellingCostRules.get(settings.getGrid()[i][j], 1f);
            }
        }
        Log.d(TAG, "Setting up the grid took " + (System.currentTimeMillis() - startGrid) + " Milliseconds");

        nodes[destX][destY].heuristicCost = 0;

        // Initialize comparator for PriorityQueue (open set)
        Comparator<Node> comparator = new Comparator<Node>() {
            @Override
            public int compare(Node n1, Node n2) {
                return n1.finalCost < n2.finalCost ? -1 :
                        n1.finalCost > n2.finalCost ? 1 :
                        0;
            }
        };

        // Initialize the closed and open set
        closed = new boolean[boardWidth][boardHeight];
        open = new PriorityQueue<>(1, comparator);

        // Add the starting node to the open set
        open.add(nodes[startX][startY]);

        Log.d(TAG, "Beginning while loop");
        final long startWhile = System.currentTimeMillis();

        while (true) {
            rounds++;

            // Grab node from PriorityQueue with lowest f-cost
            Node current = open.poll();

            // No path possible -> return null
            if (current == null) {
                Log.d(TAG, "No Path possible!");
                onPathFoundListener.onPathFound(null);
                break;
            }

            // Path was found -> return path via Interface
            if (current.x == destX && current.y == destY) {
                Log.d(TAG, "Path found! Building and returning...");
                Log.d(TAG, "Complete algorithm took " + (System.currentTimeMillis() - startStart) + " Miliseconds");
                Log.d(TAG, "While-Loop alone took " + (System.currentTimeMillis() - startWhile) + " Miliseconds and made " + rounds + " Rounds");

                Path path = new Path();
                path.moveTo(current.x, current.y);
                while (current.parent != null) {
                    path.lineTo(current.parent.x, current.parent.y);
                    current = current.parent;
                }
                onPathFoundListener.onPathFound(path);
                break;
            }

            // Add the current node to the closed set
            closed[current.x][current.y] = true;

            // Neighboring node to @current
            Node n;

            // Neighbor to the left
            if (current.x - 1 > 0) {
                n = nodes[current.x - 1][current.y];
                checkAndUpdateCost(current, n, V_H_COST);

                // Top-left
                if (current.y - 1 >= 0) {
                    n = nodes[current.x - 1][current.y - 1];
                    checkAndUpdateCost(current, n, DIAGONAL_COST);
                }

                // Bottom-left
                if (current.y + 1 < boardHeight) {
                    n = nodes[current.x - 1][current.y + 1];
                    checkAndUpdateCost(current, n, DIAGONAL_COST);
                }
            }

            // Neighbor to the top
            if (current.y - 1 >= 0) {
                n = nodes[current.x][current.y - 1];
                checkAndUpdateCost(current, n, V_H_COST);
            }

            // Neighbor to the bottom
            if (current.y + 1 < boardHeight) {
                n = nodes[current.x][current.y + 1];
                checkAndUpdateCost(current, n, V_H_COST);
            }

            // Neighbor to the right
            if (current.x + 1 < boardWidth) {
                n = nodes[current.x + 1][current.y];
                checkAndUpdateCost(current, n, V_H_COST);

                // Top right
                if (current.y - 1 >= 0) {
                    n = nodes[current.x + 1][current.y - 1];
                    checkAndUpdateCost(current, n, DIAGONAL_COST);
                }

                // Bottom right
                if (current.y + 1 < boardHeight) {
                    n = nodes[current.x + 1][current.y + 1];
                    checkAndUpdateCost(current, n, DIAGONAL_COST);
                }
            }
        }
        Log.i(TAG, "Returning. Closing thread");
    }

    /**
     * Evaluates the cost of travelling to the new Node n.
     * @param current The current node @n is the neighbor of
     * @param n The new node -> @current's "neighbor"
     * @param cost The cost of travelling
     */
    private void checkAndUpdateCost(Node current, Node n, int cost) {
        if (n == null || closed[n.x][n.y] || settings.isNodeBlocked(n.x, n.y)) return;

        // The new node's final cost
        //                       cost of travelling to @current      +  n's heuristic cost   +        n's travelling cost
        int nFinalCost = (current.finalCost - current.heuristicCost) +    n.heuristicCost    +      (int) (cost * n.travellingFactor); // * travellingFactor;

        boolean nIsInOpen = open.contains(n);
        if (!nIsInOpen || nFinalCost < n.finalCost) {
            n.finalCost = nFinalCost;
            n.parent = current;
            if (!nIsInOpen)
                open.add(n);
        }
    }
}
