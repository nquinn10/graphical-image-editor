package controller.commands;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import controller.io.IImageSaver;
import controller.io.OtherImageTypeSaver;
import controller.io.PPMImageSaver;
import model.IImageState;
import model.IModel;

/**
 * This class represents an image saver command. It implements the
 * ICommand interface and implements the mandated operation. When initialized
 * and ran, it scans input from the controller and based on the file extension,
 * it calls to the correct save image type strategy and then adds the image to
 * save the image.
 */

public class SaveImageCommand implements ICommand {

  //private IModel model;

  /**
   * Construct a save image command.
   */

  public SaveImageCommand() {
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
    // assign save file path
    String saveFilePath = scanner.next();

    if (!scanner.hasNext()) {
      throw new IllegalStateException("Third argument must be destination id.\n");
    }
    // assign image ID to be saved
    String imageName = scanner.next();

    // get image from model
    IImageState saveImage = model.getImage(imageName);
    if (saveImage == null) {
      throw new IllegalStateException("Image with specified ID does not exist.\n");
    }

    try {
      // get and store file extension
      String fileExtension = getFileExtension(saveFilePath);
      // if file extension is not a PPM
      if (!fileExtension.equals("ppm")) {
        // call to other image save strategy
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        IImageSaver otherImageTypeSaver = new OtherImageTypeSaver(saveFilePath, saveImage, output);
        otherImageTypeSaver.run();
      } else {
        // call to ppm save strategy
        // create file at given file path and write to file
        File outputFile = new File(saveFilePath);
        try (FileWriter fileWriter = new FileWriter(outputFile)) {
          PPMImageSaver ppmImageSaver = new PPMImageSaver(saveFilePath, saveImage, fileWriter);
          ppmImageSaver.run();
        } catch (IOException e) {
          throw new IllegalStateException("Failed to write to file.\n");
        }
      }
    } catch (IllegalStateException e) {
      throw new IllegalStateException(e.getMessage());
    }
  }
}
