package com.ichbingrumpig.pathfinder;

import android.graphics.Path;

/**
 * This interface is used as a callback from the pathfinder algorithm
 * Once the algorithm has finished {@link #onPathFound(Path)} is invoked.
 * If the algorithm finished successful having found a path the resulting
 * {@link Path} object is passed into the method.
 * If the algorithm finished unsuccessful null is passed into the method.
 */
public interface OnPathFoundListener {

    /**
     * Invoked either once a path was found or if no path is possible.
     * @param path The resulting @{@link Path} object. Null if no path is possible.
     */
    void onPathFound(Path path);
}
