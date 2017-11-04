package com.ichbingrumpig.pathfinder;

import android.graphics.Path;
import android.graphics.PathMeasure;

/**
 * Created by Enno on 31.10.2017.
 * Contains helper methods for {@link Path} objects resulting
 * from the algorithm.
 */

public class PathUtils {

    /**
     * Reverses the passed path.
     * @param path The path to be reversed
     * @return The reversed path
     */
    public static Path reversePath (Path path) {
        PathMeasure pathMeasure = new PathMeasure(path, false);
        int length = (int) pathMeasure.getLength();

        float[] pos = new float[2];
        pathMeasure.getPosTan(length, pos, null);

        Path reversedPath = new Path();
        reversedPath.moveTo(pos[0], pos[1]);
        for (int distance = length - 1; distance > 0; distance--) {
            pathMeasure.getPosTan(distance, pos, null);
            reversedPath.lineTo(pos[0], pos[1]);
        }
        return reversedPath;
    }


}
