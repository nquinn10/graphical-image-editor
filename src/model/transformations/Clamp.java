package model.transformations;

/**
 * This abstract class represents a clamp method for all concrete ITransformation
 * subclasses that extend this abstract class. This abstract class implements ITransformation.
 */

public abstract class Clamp implements ITransformation {

  /**
   * Default constructor for clamp method.
   * This default constructor is intentionally left empty. It is provided as a default
   * constructor for convenience. Subclasses can choose to override this default
   * constructor and provide their own initialization logic if needed.
   */

  public Clamp() {
    // Empty constructor
  }

  /**
   * Protected abstract helper method to clamp a given value at the min and max
   * values for a pixel channel. Max 255 and min 0.
   *
   * @param value value to clamp
   */

  protected int clamp(int value) {
    if (value < 0) {
      return 0;
    }
    if (value > 255) {
      return 255;
    }
    return value;
  }

}
