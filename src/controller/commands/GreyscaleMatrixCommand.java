package controller.commands;

import java.util.Scanner;

import model.IImageState;
import model.IModel;
import model.kernel.IKernel;
import model.kernel.KernelImpl;
import model.transformations.ColorTransformation;
import model.transformations.ITransformation;

/**
 * This class represents a greyscale matrix command. It implements the
 * ICommand interface and implements the mandated operation. When initialized
 * and ran, it scans input from the controller and calls to the color transformation
 * strategy with the initialized matrix for performing a greyscale linear color
 * transformation and then adds the image to the model.
 */

public class GreyscaleMatrixCommand implements  ICommand {

  /**
   * Construct a greyscale matrix command.
   */

  public GreyscaleMatrixCommand() {
    // Empty Constructor
  }

  @Override
  public void run(Scanner scanner, IModel model) throws IllegalArgumentException {
    if (scanner == null || model == null) {
      throw new IllegalArgumentException("scanner or model cannot be null\n");
    }

    if (!scanner.hasNext()) {
      throw new IllegalStateException("Second argument must be image id.\n");
    }
    // assign source image id
    String sourceImageID = scanner.next();

    if (!scanner.hasNext()) {
      throw new IllegalStateException("Third argument must be destination id.\n");
    }
    // assign destination image id
    String destID = scanner.next();

    // get the source image from the model
    IImageState sourceImage = model.getImage(sourceImageID);
    if (sourceImage == null) {
      throw new IllegalStateException("Image with specified ID does not exist.\n");
    }

    // create greyscale matrix filter
    double[][] greyscaleFilter = {
            {0.2126, 0.7152, 0.0722},
            {0.2126, 0.7152, 0.0722},
            {0.2126, 0.7152, 0.0722}
    };
    // create kernel object and pass to transformation call
    IKernel greyscaleMatrix = new KernelImpl(greyscaleFilter);
    ITransformation greyscaleColorTransformation = new ColorTransformation(greyscaleMatrix);
    IImageState greyscaleImage = greyscaleColorTransformation.run(sourceImage);

    // now need to add to model - if destination id is same as source, it will overwrite
    model.addImage(destID, greyscaleImage);
  }
}
