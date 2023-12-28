package model.transformations;

import model.IImage;
import model.IImageState;
import model.ImageImpl;

/**
 * This class represents an image intensity transformation. It extends the
 * clamp abstract class and implements the ITransformation interface
 * and implements the mandated operation. It finds
 * the average of the three components for each pixel and set that for each
 * pixel chanel.
 */

public class IntensityTransformation extends Clamp implements ITransformation {


  /**
   * Construct an intensity transformation that finds
   * the average of the three components for each pixel and set that for each
   * pixel chanel.
   */

  public IntensityTransformation() {
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
        int averageComponent = clamp((redChannel + greenChannel + blueChannel) / 3);
        averageComponent = Math.round(averageComponent);
        // set new rgb values
        newImage.setPixel(row, col, averageComponent, averageComponent, averageComponent);
      }
    }
    return newImage;
  }
}
