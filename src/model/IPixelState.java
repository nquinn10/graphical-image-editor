package model;

/**
 * This interface represents operations that can be used to get the pixel values for RGB from
 * the image, without changing it.
 */

public interface IPixelState {

  /**
   * Get the r integer value of the pixel.
   *
   * @return the r integer value of the pixel.
   */

  int getR();

  /**
   * Get the g integer value of the pixel.
   *
   * @return the g integer value of the pixel.
   */

  int getG();

  /**
   * Get the b integer value of the pixel.
   *
   * @return the b integer value of the pixel.
   */

  int getB();

  /**
   * Get the alpha double value of the pixel.
   *
   * @return the alpha double value of the pixel.
   */

  double getAlpha();
}
