package model.kernel;

/**
 * This interface represents an image filtering kernel and performs operations to set
 *     the values of the kernel.
 */

public interface IKernel extends IKernelState {

  /**
   * Set the value of the kernel at a given row/col in the 2d array.
   *
   * @param row row position of value to set
   * @param col col position of value to set
   *
   * @throws IllegalArgumentException if row or col is less than 0
   *     or if row >= size or col is >= size.
   */

  void setValue(int row, int col, double value) throws IllegalArgumentException;
}
