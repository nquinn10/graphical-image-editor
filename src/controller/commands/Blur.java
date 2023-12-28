package controller.commands;

import java.util.Scanner;

import model.IImageState;
import model.IModel;
import model.kernel.IKernel;
import model.kernel.KernelImpl;
import model.transformations.FilterTransformation;
import model.transformations.ITransformation;

/**
 * This class represents a blur command. It implements the
 * ICommand interface and implements the mandated operation. When initialized
 * and ran, it scans input from the controller and calls to the blur transformation.
 * When calling, it provides a Gaussian blur kernel object. The transformation
 * returns a blurred image and then adds the image to the model.
 */

public class Blur implements ICommand {

  /**
   * Construct a blur command.
   */

  public Blur() {
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

    // create Gaussian blur filter
    double[][] blurFilter = {
            {1.0 / 16, 1.0 / 8, 1.0 / 16},
            {1.0 / 8, 1.0 / 4, 1.0 / 8},
            {1.0 / 16, 1.0 / 8, 1.0 / 16}
    };
    // create kernel object and pass to transformation call
    IKernel blurKernel = new KernelImpl(blurFilter);
    ITransformation blurTransformation = new FilterTransformation(blurKernel);
    IImageState blurImage = blurTransformation.run(sourceImage);

    // now need to add to model - if destination id is same as source, it will overwrite
    model.addImage(destID, blurImage);

  }
}
