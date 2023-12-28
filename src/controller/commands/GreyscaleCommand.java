package controller.commands;

import java.util.Scanner;

import model.GreyscaleComponent;
import model.IImageState;
import model.IModel;
import model.transformations.ITransformation;
import model.transformations.IntensityTransformation;
import model.transformations.LumaTransformation;
import model.transformations.ValueComponentTransformation;
import model.transformations.ValueTransformation;

/**
 * This class represents a greyscale component command. It implements the
 * ICommand interface and implements the mandated operation. When initialized
 * and ran, it scans input from the controller and calls to given greyscale component
 * transformation strategy to return a new image and then adds the image to the model.
 */

public class GreyscaleCommand implements ICommand {
  private IModel model;
  private final GreyscaleComponent greyscaleComponent;

  /**
   * Construct a greyscale component command given the greyscale component enum value.
   *
   * @param greyscaleComponent enum value for greyscale component
   *
   * @throws IllegalArgumentException if greyscaleComponent is null.
   */

  public GreyscaleCommand(GreyscaleComponent greyscaleComponent) {
    if (greyscaleComponent == null) {
      throw new IllegalArgumentException("greyscale component cannot be null");
    }
    this.greyscaleComponent = greyscaleComponent;
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
    // call to correct transformation based on greyscaleComponent enum
    ITransformation greyscaleComponentTransformation = null;
    if (this.greyscaleComponent == GreyscaleComponent.RED
            || this.greyscaleComponent == GreyscaleComponent.GREEN
            || this.greyscaleComponent == GreyscaleComponent.BLUE) {
      greyscaleComponentTransformation = new ValueComponentTransformation(this.greyscaleComponent);
    } else if (this.greyscaleComponent == GreyscaleComponent.VALUE) {
      greyscaleComponentTransformation = new ValueTransformation();
    } else if (this.greyscaleComponent == GreyscaleComponent.INTENSITY) {
      greyscaleComponentTransformation = new IntensityTransformation();
    } else if (this.greyscaleComponent == GreyscaleComponent.LUMA) {
      greyscaleComponentTransformation = new LumaTransformation();
    }
    // run the transformation and store image
    IImageState greyscaleComponentImage = greyscaleComponentTransformation.run(sourceImage);

    // now need to add to model - if destination id is same as source, it will overwrite
    model.addImage(destID, greyscaleComponentImage);
  }
}