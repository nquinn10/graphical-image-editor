package model;

/**
 * This interface represents the setting operations that can be performed to
 * update the values of individual pixel components.
 */

public interface IPixel extends IPixelState {

  /**
   * Set the r integer value of the pixel.
   *
   * @param r r value to set pixel component to
   * @throws IllegalArgumentException if value is less than 0
   *     or greater than 255.
   */

  void setR(int r) throws IllegalArgumentException;

  /**
   * Set the g integer value of the pixel.
   *
   * @param g g value to set pixel component to
   * @throws IllegalArgumentException if value is less than 0
   *     or greater than 255.
   */

  void setG(int g) throws IllegalArgumentException;

  /**
   * Set the b integer value of the pixel.
   *
   * @param b b value to set pixel component to
   * @throws IllegalArgumentException if value is less than 0
   *     or greater than 255.
   */

  void setB(int b) throws IllegalArgumentException;
}
