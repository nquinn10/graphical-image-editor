package model.transformations;

import model.IImage;
import model.IImageState;
import model.ImageImpl;
import model.GreyscaleComponent;

/**
 * This class represents an image value component transformation. It extends the
 * clamp abstract class and implements the ITransformation interface
 * and implements the mandated operation. Given a pixel channel
 * enum value (red, green, or blue), it updates all of the pixel channel values for each pixel
 * in the image to the value of the given pixel channel enum value (red, green, or blue).
 */

public class ValueComponentTransformation extends Clamp implements ITransformation {

  private final GreyscaleComponent greyscaleComponent;

  /**
   * Construct a value component transformation that will update
   *     all of the pixel channel values for each pixel in the image to
   *     the value of the given pixel channel enum value (red, green, or blue).
   *
   * @param greyscaleComponent pixel component enum value (red, green, or blue)
   */

  public ValueComponentTransformation(GreyscaleComponent greyscaleComponent) {
    this.greyscaleComponent = greyscaleComponent;
  }


  @Override
  public IImageState run(IImageState sourceImage) {
    IImage newImage = new ImageImpl(sourceImage.getWidth(), sourceImage.getHeight());

    // iterate through image array and update pixel channels and set to new image
    for (int row = 0; row < sourceImage.getHeight(); row++) {
      for (int col = 0; col < sourceImage.getWidth(); col ++) {
        if (this.greyscaleComponent == GreyscaleComponent.RED) {
          int newR = clamp(sourceImage.getRedChannel(row, col));
          // set new rgb values
          newImage.setPixel(row, col, newR, newR, newR);
        } else if (this.greyscaleComponent == GreyscaleComponent.GREEN) {
          int newG = clamp(sourceImage.getGreenChannel(row, col));
          // set new rgb values
          newImage.setPixel(row, col, newG, newG, newG);
        } else if (this.greyscaleComponent == GreyscaleComponent.BLUE) {
          int newB = clamp(sourceImage.getBlueChannel(row, col));
          // set new rgb values
          newImage.setPixel(row, col, newB, newB, newB);
        }
      }
    }
    return newImage;
  }
}
