package controller.io;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import model.IImageState;

/**
 * This class represents a non PPM image saver. It implements the
 * IImageSaver interface and implements the mandated operation. When initialized
 * and ran, it saves a given image to a specified file path.
 */

public class OtherImageTypeSaver implements IImageSaver {

  private final IImageState image;
  private final String pathToSave;
  private final ByteArrayOutputStream output;

  /**
   * Construct an image saver that will save a given image to a given
   * file path by writing to the provided ByteArrayOutputStream.
   *
   * @param pathToSave path where image should be saved
   * @param image image object to be saved
   * @param output ByteArrayOutputStream object to write image to
   *
   * @throws IllegalArgumentException if pathToSave, image, or output are null.
   */

  public OtherImageTypeSaver(String pathToSave, IImageState image, ByteArrayOutputStream output)
          throws IllegalArgumentException {
    if (pathToSave == null || image == null || output == null) {
      throw new IllegalArgumentException("path to save or image cannot be null\n");
    }
    this.pathToSave = pathToSave;
    this.image = image;
    this.output = output;
  }

  /**
   * Private helper method to extract and return file extension from given file path.
   *
   * @param pathToSave pathToSave to save image to
   * @return file extension from given file path.
   */

  private String getFileExtension(String pathToSave) {
    Path path = Paths.get(pathToSave);
    String fileName = path.getFileName().toString();
    int dotIndex = fileName.lastIndexOf(".");
    if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
      return fileName.substring(dotIndex + 1).toLowerCase();
    }
    return "";
  }

  /**
   * Private helper method to create and return buffered image object
   *     created from given IImageState object.
   *
   * @param image IImageState to convert to buffered image object
   * @return buffered image object created from given IImageState object.
   */

  private BufferedImage createBufferedImage(IImageState image) {
    int width = this.image.getWidth();
    int height = this.image.getHeight();
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


  @Override
  public void run() {
    // create buffered image from image object to save and write to ByteArrayOutputStream
    try {
      String fileExtension = getFileExtension(this.pathToSave);
      BufferedImage bufferedImage = createBufferedImage(this.image);
      ImageIO.write(bufferedImage, fileExtension, this.output);
    } catch (IOException e) {
      throw new IllegalStateException("Failed to write to file.\n");
    }

    // create file at given file path and write content of output to file
    File outputFile = new File(this.pathToSave);
    try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
      fileOutputStream.write(this.output.toByteArray());
    } catch (IOException e) {
      throw new IllegalStateException("Failed to write to file.\n");
    }
  }
}
