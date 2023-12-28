package controller.io;

import model.IImageState;

/**
 * This interface represents an image loader that will load an image to a
 * specified file path.
 */

public interface IImageLoader {

  /**
   * Returns an image object that has been read from a file path.
   *
   * @return an image object that has been read from a file path.
   */

  IImageState run();
}
