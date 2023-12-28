package controller;

import java.awt.image.BufferedImage;
import model.IImageState;

/**
 * Utility class providing methods for image-related operations.
 */

public class ImageUtil {

  /**
   * Extracts and returns file extension from given file path.
   *
   * @param filePath filePath to load image from
   * @return file extension from given file path.
   * @throws IllegalStateException if file extension is invalid as in there is no extension
   */

  public static String getFileExtension(String filePath) throws IllegalStateException {
    int dotIndex = filePath.lastIndexOf(".");
    if (dotIndex > 0 && dotIndex < filePath.length() - 1) {
      return filePath.substring(dotIndex + 1).toLowerCase();
    } else {
      throw new IllegalStateException("Invalid file type. File extension is missing\n");
    }
  }

  /**
   * Creates and returns buffered image object created from given IImageState object.
   *
   * @param image IImageState to convert to buffered image object
   * @return buffered image object created from given IImageState object.
   * @throws IllegalArgumentException if image is null.
   */

  public static BufferedImage createBufferedImage(IImageState image)
          throws IllegalArgumentException {
    if (image == null) {
      throw new IllegalArgumentException("The selected file is not a valid image.");
    }
    int width = image.getWidth();
    int height = image.getHeight();
    // create new buffered image object with height/width
    BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    // iterate through to assign RGB values to buffered image
    for (int i = 0;i < height;i++) {
      for (int j = 0; j < width; j++) {
        int r = image.getRedChannel(i, j);
        int g = image.getGreenChannel(i, j);
        int b = image.getBlueChannel(i, j);
        int rgbValue = (r << 16) | (g << 8) | b;
        bufferedImage.setRGB(j, i, rgbValue);
      }
    }
    return bufferedImage;
  }
}
