package model.kernel;

/**
 * This class represents an image filtering kernel and performs operations to access
 *     and set components of the kernel. It implements the IKernel class
 *     and implements all operations mandated by the IKernelState and IKernel interfaces.
 */

public class KernelImpl implements IKernel {
  private final int size;
  private final double[][] kernelData;

  /**
   * Construct an image filtering kernel given 2D array size
   *     and represent the kernel as 2D IPixel array.
   *
   * @param size size of 2D kernel array
   *
   * @throws IllegalArgumentException if kernel size is not odd
   */

  // Constructor 1
  public KernelImpl(int size) throws IllegalArgumentException {
    if ((size % 2) != 1 || size < 3) {
      throw new IllegalArgumentException("Size must be odd or greater than 3");
    }
    this.size = size;
    this.kernelData = new double[size][size];
  }

  /**
   * Construct an image filtering kernel given a 2D array if values.
   *
   * @param values 2D array of values
   *
   * @throws IllegalArgumentException if kernel size is not odd or square
   */

  // Constructor 2
  public KernelImpl(double[][] values) throws IllegalArgumentException {

    int rows = values.length;
    if (rows % 2 != 1 || rows < 3) {
      throw new IllegalArgumentException("Kernel size must be odd or greater than 3");
    }
    this.size = rows;
    for (double[] row : values) {
      if (row.length != size) {
        throw new IllegalArgumentException("Values array must be square");
      }
    }
    this.kernelData = values;
  }


  @Override
  public void setValue(int row, int col, double value) {
    if (row < 0 || row >= this.size || col < 0 || col >= this.size) {
      throw new IllegalArgumentException("row or col out of bounds");
    }
    kernelData[row][col] = value;
  }

  @Override
  public int getSize() {
    return this.size;
  }

  @Override
  public double getValue(int row, int col) throws IllegalArgumentException {
    if (row < 0 || row >= this.size || col < 0 || col >= this.size) {
      throw new IllegalArgumentException("row or col out of bounds");
    }
    return this.kernelData[row][col];
  }
}
