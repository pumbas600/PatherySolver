package net.pumbas.patherysolver.models;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Solution {
  
  private final List<Position> wallPositions;

}
