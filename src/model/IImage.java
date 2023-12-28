package model;

/**
 * This interface represents an image and performs operations to set
 *     components of the image.
 */

public interface IImage extends IImageState {

  /**
   * Set the rgb channel values of the pixel at a given row/col in the 2d array.
   *
   * @param row row position of pixel to set
   * @param col col position of pixel to set
   * @param r r value to set pixel component to
   * @param g g value to set pixel component to
   * @param b b value to set pixel component to
   *
   * @throws IllegalArgumentException if rgb values are less than 0
   *     or greater than 255 or if row or col is less than 0
   *     or if row >= height or col is >= width.
   */

  void setPixel(int row, int col, int r, int g, int b) throws IllegalArgumentException;
}
