package model.transformations;

import model.IImage;
import model.IImageState;
import model.ImageImpl;

/**
 * This class represents an image brightening transformation. It extends the
 * clamp abstract class and implements the ITransformation interface and implements
 * the mandated operation. It performs image brightening/darkening on an image
 * depending on the value provided when called.
 */

public class BrightenTransformation extends Clamp implements ITransformation {
  private final int brightenValue;

  /**
   * Construct a brighten transformation that will brighten/
   *     darken an image by a given value. Value must be positive to
   *     brighten the image and negative to darken the image.
   *
   * @param brightenValue value to brighten the image by
   */

  public BrightenTransformation(int brightenValue) {
    this.brightenValue = brightenValue;
  }


  @Override
  public IImageState run(IImageState sourceImage) {
    IImage newImage = new ImageImpl(sourceImage.getWidth(), sourceImage.getHeight());

    // iterate through image array and update pixel channels and set to new image
    for (int row = 0; row < sourceImage.getHeight(); row++) {
      for (int col = 0; col < sourceImage.getWidth(); col ++) {
        int newR = clamp(sourceImage.getRedChannel(row, col) + brightenValue);
        int newG = clamp(sourceImage.getGreenChannel(row, col) + brightenValue);
        int newB = clamp(sourceImage.getBlueChannel(row, col) + brightenValue);
        // set new rgb values
        newImage.setPixel(row, col, newR, newG, newB);
      }
    }
    return newImage;
  }
}
