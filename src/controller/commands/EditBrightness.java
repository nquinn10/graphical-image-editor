package controller.commands;

import java.util.Scanner;

import model.IImageState;
import model.IModel;
import model.transformations.BrightenTransformation;
import model.transformations.ITransformation;

/**
 * This class represents an edit brightness command. It implements the
 * ICommand interface and implements the mandated operation. When initialized
 * and ran, it scans input from the controller and calls to the brighten transformation
 * to return a brightened image and then adds the image to the model.
 */

public class EditBrightness  implements ICommand {

  private IModel model;

  /**
   * Construct a brightness command.
   */

  public EditBrightness() {
    // Empty constructor
  }

  @Override
  public void run(Scanner scanner, IModel model) throws IllegalArgumentException {
    if (scanner == null || model == null) {
      throw new IllegalArgumentException("scanner or model cannot be null");
    }

    if (!scanner.hasNextInt()) {
      throw new IllegalStateException("Second argument must be an int.\n");
    }
    // assign brighten value
    int value = scanner.nextInt();

    if (!scanner.hasNext()) {
      throw new IllegalStateException("Third argument must be image id.\n");
    }
    // assign source image id
    String sourceImageID = scanner.next();

    if (!scanner.hasNext()) {
      throw new IllegalStateException("Fourth argument must be destination id.\n");
    }
    //assign destination image id
    String destID = scanner.next();

    // get the source image from the model
    IImageState sourceImage = model.getImage(sourceImageID);

    if (sourceImage == null) {
      throw new IllegalStateException("Image with specified ID does not exist.\n");
    }

    // call to brighten transformation
    ITransformation brightenTransformation = new BrightenTransformation(value);
    IImageState brightenedImage = brightenTransformation.run(sourceImage);

    // now need to add to model - if destination id is same as source, it will overwrite
    model.addImage(destID, brightenedImage);

  }
}
