# Pathfinder-for-Android
Pathfinder library for Android using the A* pathfinding algorithm.

Please feel free to use and play around with this library in your projects.

This library assumes the use of a two-dimensional grid defining the game world. Those data and the rules for Nodes
and their representation in the grid can be passed by initializing the algorithm. Call Pathfinder#initialize(Settings settings)
for that. I tried documenting as much as possible in the Java files, though.

I am sure there are some things to optimize, but I did my best at finding the
right balance between flexibility and performance. If you have any feedback I would love to hear what you have to say
about the library.

This library is based on a Java implementation I found on codebytes:
    http://www.codebytes.in/2015/02/a-shortest-path-finding-algorithm.html
In the article a nice YouTube tutorial is linked. Be sure to check those links out if you want
to learn more about the A* algorithm itself.
