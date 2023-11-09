package net.pumbas.patherysolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import net.pumbas.patherysolver.models.PatheryMap;
import net.pumbas.patherysolver.models.Position;
import net.pumbas.patherysolver.models.Solution;
import net.pumbas.patherysolver.models.TileType;
import net.pumbas.patherysolver.pathfinding.DijkstraPathFinder;
import net.pumbas.patherysolver.pathfinding.PathFinder;

public class Main {

  public static void main(String[] args) {
    System.out.println("Hello world!");

    String[] codedMap = new String[]{
        "rororooooooor",
        "rorooroooooof",
        "roooooooocror",
        "sooooooooooor",
        "rooooooooooor",
        "roooooooooror"
    };

    PatheryMap map = new PatheryMap(TileType.decode(codedMap), 6, Set.of(new Position(9, 2)));
    Solution solution = new Solution(new ArrayList<>());

    PathFinder pathFinder = new DijkstraPathFinder(map);

    System.out.println(
        pathFinder.getPathLength(solution, map.getStartTiles(), map.getCheckpoints()));

    System.out.println(Arrays.deepToString(TileType.decode(codedMap)));
  }


}
