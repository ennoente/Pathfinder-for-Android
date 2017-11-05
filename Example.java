package com.ichbingrumpig.misc.tests.usingthepathfinderlibrary;

import android.graphics.Path;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;

import com.ichbingrumpig.pathfinder.Pathfinder;
import com.ichbingrumpig.pathfinder.Settings;
import com.ichbingrumpig.pathfinder.OnPathFoundListener;

/**
 * This class is an example how to initialize and use my Pathfinder library.
 * Some things are simplified to show the important principles.
 *
 * I just randomly thought of some rules this example game has.
 * Try to play along for now so you can initialize the library to fit your data and rules.
 */
public class Example extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    /** The game grid storing the grid values for every pixel on the game board */
    int[][] gameGrid;

    /**
     * These values define the structure of the grid.
     * Let's say a town is built. All of the pixels in its area
     * are then marked as 1 in the gameGrid array.
     * In the game you should not be able to pass through towns, so they are blocked.
     *
     * There are also boulders lying around.
     * For obvious (and for explanatory) reasons each boulder has its own grid value, starting at 4 and incrementing for each
     * following boulder object (5, 6, ...).
     * Boulders block Nodes in their area (we will come back to boulders later on).
     *
     * GRID_VALUE_TOWN                    -> blocked
     * GRID_VALUE_SWAMPS                  -> Only half the speed
     * GRID_VALUE_ALIENS                  -> Only a third of the normal speed
     * GRID_VALUE_BOULDERS_STARTING_VALUE -> Starting value of borders. Increments for each following boulder object
     */
    private static final int GRID_VALUE_TOWN = 1;
    private static final int GRID_VALUE_SWAMPS = 2;
    private static final int GRID_VALUE_ALIENS = 3;
    private static final int GRID_VALUE_BOULDERS_STARTING_VALUE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ...
        // Optaining the game board's width and size returned width = 550 and height = 450

        // We initialize the grid with the board's width and height
        gameGrid = new int[550][450];

        // If stuff happens on the board (like e.g. towns are build, aliens land on some Nodes, etc)
        // the gameGrid array will be updated. See the declaration of the GRID_VALUE_ constants above.

        // Now we initialize the library to our rules
        Pathfinder.initialize(new Settings() {
            @Override
            public int[][] getGrid() {
                // If the algorithm asks for the grid, return our game grid array.
                return gameGrid;
            }

            @Override
            public SparseArray<Float> setTravellingCostRules() {
                // This is where we specify our travelling cost rules.
                // As we defined above, if a node contains aliens (-> grid at that location has value of 3)
                // travelling should be only a third as fast as normal.
                // Travelling on swamps should be only half as fast.

                // !Note:
                // Remember that we are initializing the COSTS, not the travelling speed.
                // Rules of thumb: Generally, travelling half as fast means double the costs.

                // !Also note:
                // We don't put in the value of towns since we don't move slower, but not at all (they are blocked, after all).
                SparseArray<Float> travellingCostRules = new SparseArray<>();

                // The key is the grid value
                // while the value is the factor. (For swamps this is 2, because the costs are doubled and for
                // alien Nodes the value is 3, since the costs are tripled).
                travellingCostRules.put(GRID_VALUE_SWAMPS, 2f);
                travellingCostRules.put(GRID_VALUE_ALIENS, 3f);

                // By default Nodes have a travelling factor of 1, if not specified.

                return travellingCostRules;
            }

            @Override
            public boolean isNodeBlocked(int x, int y) {
                // Here we return true if the Node at [x, y] is blocked.
                // In our example every Node in the area of a town is blocked,
                // as well as every Node that is in the area of a boulder.
                // Recap: Boulders each have their own grid value, starting at 4 and incrementing per boulder.
                // Thus we return true if the grid value at [x, y] is 1 (town)
                // OR bigger than or equal to 4 (boulder):
                return gameGrid[x][y] == GRID_VALUE_TOWN || gameGrid[x][y] >= GRID_VALUE_BOULDERS_STARTING_VALUE;
            }
        });

        // Now the library is completely initialized and you can find Paths by calling Pathfinder#findPath().
        // Let's say we want the shortest path from [0, 0] to [150, 150]:

        Pathfinder.findPath(0, 0, 150, 150, new OnPathFoundListener() {
            @Override
            public void onPathFound(Path path) {
                // The algorithm will return here.
                // If a path has been found the resulting android.graphics.Path object is passed into this method.
                // If no path is possible NULL is passed into this method.
                if (path == null) Log.d(TAG, "No Path possible!");
                else {
                    // ...
                    // Awesome path stuff going on here.
                }
            }
        });

        // ...
        // Other awesome game stuff
        // ...
    }
}
