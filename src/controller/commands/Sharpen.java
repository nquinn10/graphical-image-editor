package controller.commands;

import java.util.Scanner;

import model.IImageState;
import model.IModel;
import model.kernel.IKernel;
import model.kernel.KernelImpl;
import model.transformations.FilterTransformation;
import model.transformations.ITransformation;

/**
 * This class represents a sharpen command. It implements the
 * ICommand interface and implements the mandated operation. When initialized
 * and ran, it scans input from the controller and calls to the sharpen transformation.
 * When calling, it provides a sharpen kernel object. The transformation
 * returns a sharpened image and then adds the image to the model.
 */

public class Sharpen implements ICommand {

  /**
   * Construct a sharpen command.
   */

  public Sharpen() {
    // Empty constructor
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

    // create sharpen filter
    double[][] sharpenFilter = {
            {-1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8},
            {-1.0 / 8, 1.0 / 4, 1.0 / 4, 1.0 / 4, -1.0 / 8},
            {-1.0 / 8, 1.0 / 4, 1.0, 1.0 / 4, -1.0 / 8},
            {-1.0 / 8, 1.0 / 4, 1.0 / 4, 1.0 / 4, -1.0 / 8},
            {-1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8}
    };

    // create kernel object and pass to transformation call
    IKernel sharpenKernel = new KernelImpl(sharpenFilter);
    ITransformation sharpenTransformation = new FilterTransformation(sharpenKernel);
    IImageState sharpenImage = sharpenTransformation.run(sourceImage);

    // now need to add to model - if destination id is same as source, it will overwrite
    model.addImage(destID, sharpenImage);
  }
}
