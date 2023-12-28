package controller.io;

import java.io.IOException;

import model.IImageState;
import model.IPixel;

/**
 * This class represents a PPM image saver. It implements the
 * IImageSaver interface and implements the mandated operation. When initialized
 * and ran, it saves a given image to a specified file path.
 */

public class PPMImageSaver implements IImageSaver {
  private final IImageState image;
  private final Appendable output;

  /**
   * Construct a PPM image saver that will save a given image to a given
   * file path by writing to the provided appendable.
   *
   * @param pathToSave path where image should be saved
   * @param image image object to be saved
   * @param appendable appendable object to write image to
   *
   * @throws IllegalArgumentException if pathToSave or image are null.
   */

  public PPMImageSaver(String pathToSave, IImageState image, Appendable appendable)
          throws IllegalArgumentException {
    if (pathToSave == null || image == null) {
      throw new IllegalArgumentException("path to save or image cannot be null\n");
    }
    String pathToSaveFile = pathToSave;
    this.image = image;
    this.output = appendable;
  }

  /**
   * Helper method to write to the appendable.
   *
   * @param message string message to write to appendable
   *
   * @throws IllegalStateException if IOException occurs when writing
   *     to the appendable.
   */

  private void write(String message) throws IllegalStateException {
    try {
      this.output.append(message);
    } catch (IOException e) {
      throw new IllegalStateException("Writing failed.");
    }
  }


  @Override
  public void run() {
    int width = image.getWidth();
    int height = image.getHeight();
    int maxValue = image.getMaxValue();

    // Write the PPM header information to the Appendable
    write("P3\n");
    write(width + " " + height + "\n");
    write(maxValue + "\n");

    // Loop through the 2D array and write RGB values for each pixel
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        IPixel pixel = image.getPixel(i, j);
        write(pixel.getR() + " ");
        write(pixel.getG() + " ");
        write(pixel.getB() + " ");
      }
      write("\n"); // Move to the next line after writing a row
    }
  }
}
