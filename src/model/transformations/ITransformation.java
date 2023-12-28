package model.transformations;

import model.IImageState;

/**
 * This interface represents an image transformation strategy.
 */

public interface ITransformation {

  /**
   * Returns a new image object that has been transformed by the
   *     transformation strategy.
   *
   * @param sourceImage image object to be transformed
   *
   * @return a new image object that has been transformed by the
   *     transformation strategy
   */

  IImageState run(IImageState sourceImage);
}
