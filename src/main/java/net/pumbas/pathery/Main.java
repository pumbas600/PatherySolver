package net.pumbas.pathery;

import java.util.Set;
import net.pumbas.pathery.models.PatheryMap;
import net.pumbas.pathery.models.Position;
import net.pumbas.pathery.models.TileType;
import net.pumbas.pathery.pathfinding.DijkstraPathFinder;
import net.pumbas.pathery.pathfinding.PathFinder;

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
    Set<Position> walls = Set.of(
//        new Position(1, 2), new Position(2, 3), new Position(2, 4),
//        new Position(3, 4), new Position(4, 4), new Position(5, 4)
    );

    PathFinder pathFinder = new DijkstraPathFinder(map);

    System.out.println(
        pathFinder.findPath(
            walls, map.getStartTiles(), map.getCheckpoints()));
  }


}
