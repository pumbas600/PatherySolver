package net.pumbas.patherysolver.utils;

import java.lang.reflect.Array;

public class MatrixUtils {

  /**
   * Creates a new matrix that is the transpose of the given matrix.
   *
   * @param matrix The matrix to transpose
   * @param <T>    The type of the elements in the matrix
   * @return A new matrix that is the transpose of the given matrix
   * @throws IllegalArgumentException if either of the dimensions of the matrix are 0
   */
  @SuppressWarnings("unchecked")
  public static <T> T[][] transpose(T[][] matrix) {
    int width = matrix.length;
    int height = width == 0 ? 0 : matrix[0].length;

    if (width == 0 || height == 0) {
      throw new IllegalArgumentException("Cannot transpose a matrix with no elements");
    }

    T[][] transposedMatrix = (T[][]) Array.newInstance(matrix[0][0].getClass(), height, width);

    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        transposedMatrix[y][x] = matrix[x][y];
      }
    }

    return transposedMatrix;
  }

}
