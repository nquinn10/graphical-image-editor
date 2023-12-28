package model.kernel;

/**
 * This interface represents an image filtering kernel and performs operations to access
 *     components of the kernel.
 */

public interface IKernelState {

  /**
   * Get the size of the kernel array.
   *
   * @return the size of the kernel array.
   */

  int getSize();

  /**
   * Get the value at a given row/col in the 2d array.
   *
   * @param row row position of value
   * @param col col position of value
   *
   * @return the value at a given row/col in the 2d array.
   * @throws IllegalArgumentException if row or col is less than 0
   *     or if row >= size or col is >= size.
   */

  double getValue(int row, int col) throws IllegalArgumentException;
}





