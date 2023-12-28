package model;

/**
 * This interface represents an image and performs operations to access
 *     components of the image.
 */

public interface IImageState {

  /**
   * Get the width value of the image.
   *
   * @return the width value of the image.
   */

  int getWidth();

  /**
   * Get the height value of the image.
   *
   * @return the height value of the image.
   */

  int getHeight();

  /**
   * Get the max color value of image.
   *
   * @return the max color value of image.
   */

  int getMaxValue();

  /**
   * Get the r channel value of the pixel at the given
   *     row/col position.
   *
   * @param row row position of pixel
   * @param col col position of pixel
   *
   * @return the r channel value of the pixel at the given
   *     row/col position.
   * @throws IllegalArgumentException if row or col is less than 0
   *     or if row >= height or col is >= width.
   */

  int getRedChannel(int row, int col) throws IllegalArgumentException;

  /**
   * Get the g channel value of the pixel at the given
   *     row/col position.
   *
   * @param row row position of pixel
   * @param col col position of pixel
   *
   * @return the g channel value of the pixel at the given
   *     row/col position.
   * @throws IllegalArgumentException if row or col is less than 0
   *     or if row >= height or col is >= width.
   */

  int getGreenChannel(int row, int col) throws IllegalArgumentException;

  /**
   * Get the b channel value of the pixel at the given
   *     row/col position.
   *
   * @param row row position of pixel
   * @param col col position of pixel
   *
   * @return the b channel value of the pixel at the given
   *     row/col position.
   * @throws IllegalArgumentException if row or col is less than 0
   *     or if row >= height or col is >= width.
   */

  int getBlueChannel(int row, int col) throws IllegalArgumentException;


  /**
   * Get the pixel object at a given row/col in the 2d array.
   *
   * @param row row position of pixel
   * @param col col position of pixel
   *
   * @return the pixel object at a given row/col in the 2d array.
   * @throws IllegalArgumentException if row or col is less than 0
   *     or if row >= height or col is >= width.
   */

  IPixel getPixel(int row, int col) throws IllegalArgumentException;

}
