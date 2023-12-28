package model;

/**
 * This class represents an image and performs operations to access
 *     and set components of the image. It implements the IImage class
 *     and implements all operations mandated by the IImageState and IImage interfaces.
 */

public class ImageImpl implements IImage {

  private final IPixel[][] pixelArray;
  private final int width;
  private final int height;
  private final int maxValue;

  /**
   * Construct an image given the image height, and width
   *     and represent image as 2D IPixel array.
   *
   * @param width width value of the image
   * @param height height value of the image.
   *
   * @throws IllegalArgumentException if height or width are negative
   */

  public ImageImpl(int width, int height) throws IllegalArgumentException {
    if (width < 0 || height < 0) {
      throw new IllegalArgumentException("Cannot have negative height/width");
    }
    this.pixelArray = new IPixel[height][width];
    this.width = width;
    this.height = height;
    this.maxValue = 255; // max pixel value is 255
  }

  @Override
  public int getWidth() {
    return this.width;
  }

  @Override
  public int getHeight() {
    return this.height;
  }

  @Override
  public int getMaxValue() {
    return this.maxValue;
  }


  @Override
  public int getRedChannel(int row, int col) throws IllegalArgumentException {
    if (row < 0 || row >= this.height || col < 0 || col >= this.width) {
      throw new IllegalArgumentException("row or col out of bounds");
    }
    return this.pixelArray[row][col].getR();
  }

  @Override
  public int getGreenChannel(int row, int col) throws IllegalArgumentException {
    if (row < 0 || row >= this.height || col < 0 || col >= this.width) {
      throw new IllegalArgumentException("row or col out of bounds");
    }
    return this.pixelArray[row][col].getG();
  }

  @Override
  public int getBlueChannel(int row, int col)  throws IllegalArgumentException {
    if (row < 0 || row >= this.height || col < 0 || col >= this.width) {
      throw new IllegalArgumentException("row or col out of bounds");
    }
    return this.pixelArray[row][col].getB();
  }

  @Override
  public IPixel getPixel(int row, int col) throws IllegalArgumentException {
    if (row < 0 || row >= this.height || col < 0 || col >= this.width) {
      throw new IllegalArgumentException("row or col out of bounds");
    }
    return pixelArray[row][col];
  }

  @Override
  public void setPixel(int row, int col, int r, int g, int b) throws IllegalArgumentException {
    if (row < 0 || row >= this.height || col < 0 || col >= this.width) {
      throw new IllegalArgumentException("row or col out of bounds");
    }
    if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
      throw new IllegalArgumentException("Component values cannot be less than 0 or exceed 255");
    }
    this.pixelArray[row][col] = new PixelImpl(r, g, b);
  }
}
