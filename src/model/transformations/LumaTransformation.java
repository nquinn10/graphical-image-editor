package model.transformations;

import model.IImage;
import model.IImageState;
import model.ImageImpl;

/**
 * This class represents an image luma transformation. It extends the
 * clamp abstract class and implements the ITransformation interface
 * and implements the mandated operation. It finds
 * the weighted sum (0.2126r + 0.7152g + 0.0722b) and sets that for each
 * pixel chanel.
 */

public class LumaTransformation extends Clamp implements ITransformation {

  /**
   * Construct a luma transformation that finds
   * the weighted sum (0.2126r + 0.7152g + 0.0722b) and sets that for each
   * pixel chanel.
   */

  public LumaTransformation() {
    // Empty constructor
  }


  @Override
  public IImageState run(IImageState sourceImage) {
    IImage newImage = new ImageImpl(sourceImage.getWidth(), sourceImage.getHeight());

    // iterate through image array and update pixel channels and set to new image
    for (int row = 0; row < sourceImage.getHeight(); row++) {
      for (int col = 0; col < sourceImage.getWidth(); col ++) {
        double redChannel = sourceImage.getRedChannel(row, col) * 0.2126;
        double greenChannel = sourceImage.getGreenChannel(row, col) * 0.7152;
        double blueChannel = sourceImage.getBlueChannel(row, col) * 0.0722;
        int weightedSum = clamp((int) Math.round(redChannel + greenChannel + blueChannel));
        // set new rgb values
        newImage.setPixel(row, col, weightedSum, weightedSum, weightedSum);
      }
    }
    return newImage;
  }
}
