package model;

/**
 * This class represents an image pixel that is represented by RGB values.
 *     It implements the IPixel class and implements all operations
 *     mandated by the IPixelState and IPixel interfaces.
 */

public class PixelImpl implements IPixel {

  private int r;
  private int g;
  private int b;

  /**
   * Construct a pixel represented by RGB values. 255 is the max value
   *     for any pixel component.
   *
   * @param r r integer value of the pixel
   * @param g g integer value of the pixel
   * @param b b integer value of the pixel
   *
   * @throws IllegalArgumentException if any pixel component is less than 0
   *     or greater than 255.
   */

  public PixelImpl(int r, int g, int b) throws IllegalArgumentException {
    if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
      throw new IllegalArgumentException("Component values cannot be less than 0 or exceed 255");
    }
    this.r = r;
    this.g = g;
    this.b = b;
  }

  @Override
  public int getR() {
    return this.r;
  }

  @Override
  public int getG() {
    return this.g;
  }

  @Override
  public int getB() {
    return this.b;
  }

  @Override
  public double getAlpha() {
    // placeholder for next assignment
    return 1.0;
  }

  @Override
  public void setR(int r) throws IllegalArgumentException {
    if (r < 0 || r > 255) {
      throw new IllegalArgumentException("Invalid channel value");
    }
    this.r = r;
  }

  @Override
  public void setG(int g) throws IllegalArgumentException {
    if (g < 0 || g > 255) {
      throw new IllegalArgumentException("Invalid channel value");
    }
    this.g = g;
  }

  @Override
  public void setB(int b) throws IllegalArgumentException {
    if (b < 0 || b > 255) {
      throw new IllegalArgumentException("Invalid channel value");
    }
    this.b = b;
  }

  @Override
  public String toString() {
    return this.r + " " + this.g + " " + this.b;
  }

}
