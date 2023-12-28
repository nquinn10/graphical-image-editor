package model.transformations;

import model.IImage;
import model.IImageState;
import model.ImageImpl;
import model.kernel.IKernelState;

/**
 * This class represents an image color transformation. It extends the
 * clamp abstract class and implements the ITransformation interface and implements
 * the mandated operation. Given a kernel object representing a matrix containing values,
 * it uses those values to perform a linear color transformation in which the final red,
 * green and blue values of a pixel are linear combinations of its initial red,
 * green and blue values.
 */

public class ColorTransformation extends Clamp implements ITransformation {

  private final IKernelState kernel;

  /**
   * Construct an image coloring transformation that calculates new RGB values for
   *     each pixel in the image based on a 2d matrix.
   *
   * @param kernel kernel object that represents a 2D matrix
   */

  public ColorTransformation(IKernelState kernel) {
    this.kernel = kernel;
  }


  @Override
  public IImageState run(IImageState sourceImage) {
    IImage newImage = new ImageImpl(sourceImage.getWidth(), sourceImage.getHeight());

    // iterate through image array and update pixel channels and set to new image
    for (int row = 0; row < sourceImage.getHeight(); row++) {
      for (int col = 0; col < sourceImage.getWidth(); col ++) {
        int redChannel = sourceImage.getRedChannel(row,col);
        int greenChannel = sourceImage.getGreenChannel(row, col);
        int blueChannel = sourceImage.getBlueChannel(row, col);

        // calc linear channel
        int linearRed = (int) Math.round((redChannel * kernel.getValue(0,0)
                + (greenChannel * kernel.getValue(0,1)
                + (blueChannel * kernel.getValue(0,2)))));
        int linearGreen = (int) Math.round((redChannel * kernel.getValue(1,0)
                + (greenChannel * kernel.getValue(1,1)
                + (blueChannel * kernel.getValue(1,2)))));
        int linearBlue = (int) Math.round((redChannel * kernel.getValue(2,0)
                + (greenChannel * kernel.getValue(2,1)
                + (blueChannel * kernel.getValue(2,2)))));

        // clamp calculated values
        linearRed = clamp(linearRed);
        linearGreen = clamp(linearGreen);
        linearBlue = clamp(linearBlue);

        // set new rgb values
        newImage.setPixel(row, col, linearRed, linearGreen, linearBlue);
      }
    }
    return newImage;
  }
}
