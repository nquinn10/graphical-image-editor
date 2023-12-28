package model.transformations;

import model.IImage;
import model.IImageState;
import model.IPixel;
import model.ImageImpl;
import model.kernel.IKernelState;

/**
 * This class represents an image filtering transformation. It extends the
 * clamp abstract class and implements the ITransformation interface and implements
 * the mandated operation. Given a kernel object, it iterates across the given image,
 * overlaying the kernel 2d array center position on each image pixel. For all
 * overlapping positions, the value in the kernel filter array is multiplied
 * by each individual pixel channel and summed together to return the new RGB values for that pixel
 * before moving on to the next pixel.
 */

public class FilterTransformation extends Clamp implements ITransformation {
  private final IKernelState kernel;

  /**
   * Construct an image filtering transformation that calculates new RGB values for
   *     each pixel in the image based on the overlapping 2d kernel filtering array.
   *
   * @param kernel kernel object that represents a 2D array
   */

  public FilterTransformation(IKernelState kernel) {
    this.kernel = kernel;
  }

  @Override
  public IImageState run(IImageState sourceImage) {
    IImage newImage = new ImageImpl(sourceImage.getWidth(), sourceImage.getHeight());
    // identify center of kernel array
    int centerSlot = (this.kernel.getSize() - 1) / 2;

    // iterate through image array and update pixel channels and set to new image
    for (int row = 0; row < sourceImage.getHeight(); row++) {
      for (int col = 0; col < sourceImage.getWidth(); col++) {
        double redSum = 0;
        double greenSum = 0;
        double blueSum = 0;
        // iterate through 2d kernel array
        for (int kernelRow = 0; kernelRow < this.kernel.getSize(); kernelRow++) {
          for (int kernelCol = 0; kernelCol < this.kernel.getSize(); kernelCol++) {
            // Calculate the relative position of the kernel's center on the image
            int imageRow = row + kernelRow - centerSlot;
            int imageCol = col + kernelCol - centerSlot;

            // Check if the current pixel is within the bounds of the image
            if (imageRow >= 0 && imageRow < sourceImage.getHeight()
                    && imageCol >= 0 && imageCol < sourceImage.getWidth()) {
              // get the image pixel in that position
              IPixel pixel = sourceImage.getPixel(imageRow, imageCol);
              // get the kernel value in that position
              double kernelValue = kernel.getValue(kernelRow, kernelCol);

              // Accumulate the filtered values for each channel
              redSum += pixel.getR() * kernelValue;
              greenSum += pixel.getG() * kernelValue;
              blueSum += pixel.getB() * kernelValue;
            }
          }
        }
        // round, convert to int, and clamp
        int r = clamp((int) Math.round(redSum));
        int g = clamp((int) Math.round(greenSum));
        int b = clamp((int) Math.round(blueSum));
        newImage.setPixel(row, col, r, g, b);
      }
    }
    return newImage;
  }

}
