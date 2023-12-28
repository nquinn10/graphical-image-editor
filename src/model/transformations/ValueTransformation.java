package model.transformations;

import model.IImage;
import model.IImageState;
import model.ImageImpl;

/**
 * This class represents an image value transformation. It extends the
 * clamp abstract class and implements the ITransformation interface and
 * implements the mandated operation. It finds the maximum value of the
 * three components for each pixel and set that for each pixel chanel.
 */

public class ValueTransformation extends Clamp implements ITransformation {

  /**
   * Construct a value transformation that find the maximum value of the
   * three components for each pixel and set that for each pixel chanel.
   */

  public ValueTransformation() {
    // Empty constructor
  }


  @Override
  public IImageState run(IImageState sourceImage) {
    IImage newImage = new ImageImpl(sourceImage.getWidth(), sourceImage.getHeight());

    // iterate through image array and update pixel channels and set to new image
    for (int row = 0; row < sourceImage.getHeight(); row++) {
      for (int col = 0; col < sourceImage.getWidth(); col ++) {
        int redChannel = clamp(sourceImage.getRedChannel(row, col));
        int greenChannel = clamp(sourceImage.getGreenChannel(row, col));
        int blueChannel = clamp(sourceImage.getBlueChannel(row, col));
        int maxComponent = Math.max(redChannel, Math.max(greenChannel, blueChannel));
        // set new rgb values
        newImage.setPixel(row, col, maxComponent, maxComponent, maxComponent);
      }
    }
    return newImage;
  }
}
