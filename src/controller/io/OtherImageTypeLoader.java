package controller.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import model.IImage;
import model.IImageState;
import model.ImageImpl;

/**
 * This class represents a non PPM image loader. It implements the
 * IImageLoader interface and implements the mandated operation. When initialized
 * and ran, it loads an image to specified file path.
 */

public class OtherImageTypeLoader implements  IImageLoader {

  private final String filePath;

  /**
   * Construct an image loader that will load an image at a
   * given file path.
   *
   * @param filePath filepath of image to load
   *
   * @throws IllegalArgumentException if pathToSave or image are null.
   */

  public OtherImageTypeLoader(String filePath) {
    if (filePath == null) {
      throw new IllegalArgumentException("file path cannot be null\n");
    }
    this.filePath = filePath;
  }

  @Override
  public IImageState run() {
    BufferedImage image = null;
    try {
      File imageFile = new File(this.filePath);
      // read from file and store as buffered image
      image = ImageIO.read(imageFile);
      if (image == null) {
        throw new IOException("The selected file is not a valid image.");
      }
    } catch (IOException e) {
      throw new IllegalStateException("Failed to read from file.\n");
    }
    // extract and assign height and width
    int height = image.getHeight();
    int width = image.getWidth();

    // create image object
    IImage loadedImage = new ImageImpl(width, height);
    for (int i = 0;i < height;i++) {
      for (int j = 0;j < width;j++) {
        int pixelRGB = image.getRGB(j, i); // retrieve RGB and assign
        int r = (pixelRGB >> 16) & 0xFF;
        int g = (pixelRGB >> 8) & 0xFF;
        int b = pixelRGB & 0xFF;
        // load pixels
        loadedImage.setPixel(i, j, r, g, b);
      }
    }
    return loadedImage;
  }
}