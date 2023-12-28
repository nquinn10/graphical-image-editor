package controller.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import model.IImage;
import model.IImageState;
import model.ImageImpl;

/**
 * This class represents a PPM image loader. It implements the
 * IImageLoader interface and implements the mandated operation. When initialized
 * and ran, it loads an image to specified file path.
 */

public class PPMImageLoader implements IImageLoader {
  private final String filePath;

  /**
   * Construct an image loader that will load a PPM image at a
   * given file path.
   *
   * @param filePath filepath of image to load
   *
   * @throws IllegalArgumentException if pathToSave or image are null.
   */

  public PPMImageLoader(String filePath) throws IllegalArgumentException {
    if (filePath == null) {
      throw new IllegalArgumentException("file path cannot be null\n");
    }
    this.filePath = filePath;
  }

  @Override
  public IImageState run() {
    Scanner sc;

    try {
      sc = new Scanner(new FileInputStream(this.filePath));
    }
    catch (FileNotFoundException e) {
      return null;
    }

    StringBuilder builder = new StringBuilder();
    //read the file line by line, and populate a string.
    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      if (s.charAt(0) != '#') {
        builder.append(s + System.lineSeparator());
      }
    }

    //now set up the scanner to read from the string we just built
    sc = new Scanner(builder.toString());

    String token;

    token = sc.next();
    if (!token.equals("P3")) {
      throw new IllegalArgumentException("Invalid PPM file: plain RAW file should begin with P3");
    }
    int width = sc.nextInt();
    int height = sc.nextInt();
    int maxValue = sc.nextInt();

    // create new image object
    IImage loadedImage = new ImageImpl(width, height);

    for (int i = 0;i < height;i++) {
      for (int j = 0;j < width;j++) {
        int r = sc.nextInt();
        int g = sc.nextInt();
        int b = sc.nextInt();
        // load pixels
        loadedImage.setPixel(i, j, r, g, b);
      }
    }
    return loadedImage;
  }
}
