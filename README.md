# Pathfinder library for Android

This library takes care of finding an optimal path for you using an implementation of the A* pathfinding algorithm.

## When is this library useful for me?

If your game uses a two-dimensional grid keeping track of the every Node in the game board this library is perfect for you.
Let's say your game uses the following principle:

	- The board's size is 250 * 250
	- blocked nodes have the grid value 0
	- free nodes have the grid value 1
	- There are swamps in your game -> travlling is only possible at half the speed. Those Nodes in the grid may have the grid value 2

Your grid array will look something like this:
	
	int[][] grid = new int[250][250];
	
If now the Node at [200, 175] were to be blocked, its grid value would be 0, resulting in:
	
	grid[200][175] = 0;
	

The following picture shows a simplified game board (left) and its representation in the grid (right).
![Visualisation](https://i.imgur.com/myCvWqI.png "Visualization")
	
You can now initialize the library with those data and rules and simply let the library do the rest for you.	

	1. Initialize via Pathfinder#initialize()
	2. Find optimal paths via Pathfinder#findPath()

for a detailed example on how to use this library see [this](Example.java)
