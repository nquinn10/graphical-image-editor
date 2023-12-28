package controller.commands;

import java.util.Scanner;

import controller.io.IImageLoader;
import controller.io.OtherImageTypeLoader;
import controller.io.PPMImageLoader;
import model.IImageState;
import model.IModel;

/**
 * This class represents an image loader command. It implements the
 * ICommand interface and implements the mandated operation. When initialized
 * and ran, it scans input from the controller and based on the file extension,
 * it calls to the correct load image type strategy and then adds the image to
 * the given model.
 */

public class LoadImageCommand implements ICommand {

  private IModel model;

  /**
   * Construct a load image command.
   */

  public LoadImageCommand() {
    // Empty constructor
  }

  /**
   * Private helper method to extract and return file extension from given file path.
   *
   * @param filePath filePath to load image from
   * @return file extension from given file path.
   * @throws IllegalStateException if file extension is invalid as in there is no extension
   */

  private String getFileExtension(String filePath) throws IllegalStateException {
    int dotIndex = filePath.lastIndexOf(".");
    if (dotIndex > 0 && dotIndex < filePath.length() - 1) {
      return filePath.substring(dotIndex + 1).toLowerCase();
    } else {
      throw new IllegalStateException("Invalid file type. File extension is missing\n");
    }
  }


  @Override
  public void run(Scanner scanner, IModel model) throws IllegalArgumentException {
    if (scanner == null || model == null) {
      throw new IllegalArgumentException("scanner or model cannot be null\n");
    }
    if (!scanner.hasNext()) {
      throw new IllegalStateException("Second argument must be file path.\n");
    }
    // assign file path
    String filePath = scanner.next();

    if (!scanner.hasNext()) {
      throw new IllegalStateException("Third argument must be destination id.\n");
    }
    // assign image ID
    String destID = scanner.next();
    // try to load the image by extracting correct file extension
    // if fails, catch and throw error message
    try {
      String fileExtension = getFileExtension(filePath);
      // if file extension is not a PPM
      if (!fileExtension.equals("ppm")) {
        // call to load strategy
        IImageLoader imageLoader = new OtherImageTypeLoader(filePath);
        IImageState loadImage = imageLoader.run();
        if (loadImage == null) {
          throw new IllegalStateException("Image with specified ID does not exist.\n");
        }
        // now need to add to model - if destination id is same as source, it will overwrite
        model.addImage(destID, loadImage);
      } else {
        // otherwise is PPM
        // call to load strategy
        IImageLoader imageLoader = new PPMImageLoader(filePath);
        IImageState loadImage = imageLoader.run();
        if (loadImage == null) {
          throw new IllegalStateException("Image with specified ID does not exist.\n");
        }
        // now need to add to model - if destination id is same as source, it will overwrite
        model.addImage(destID, loadImage);
      }
    } catch (IllegalStateException e) {
      throw new IllegalStateException(e.getMessage());
    }
  }
}
