package view;

import model.GreyscaleComponent;
import model.kernel.IKernelState;

/**
 * This interface represents a view listener that contains methods that represent actions
 * or events that the view can trigger. The methods in the interface are intended to be called
 * by the view to inform the listener (controller) about different user actions or events that
 * occurred in the view. The controller can then execute these events with the given info.
 */

public interface ViewListener {

  /**
   * Handles the loading of an image from a given file path.
   *
   * @param filePath the file path of the image to be loaded.
   *
   * @throws IllegalStateException if any errors occur when attempted to load an image
   *     from a file path and add it to the model.
   */

  void handleLoadEvent(String filePath);

  /**
   * Handles the saving of an image to a given file path.
   *
   * @param filePath the file path of the image to be saved.
   *
   * @throws IllegalStateException if any errors occur when attempted to save an image
   *     to a file path.
   */

  void handleSaveEvent(String filePath);

  /**
   * Handles performing a greyscale operation on the current image given an enum
   *     value to represent the operation to be performed.
   *
   * @param greyscaleComponent enum value for greyscale component
   */

  void handleGreyscaleEvent(GreyscaleComponent greyscaleComponent);

  /**
   * Handles performing a brighten/darken operation on the current image given an
   *     integer value to brighten/darken the image by.
   *
   * @param brightenValue integer to brighten/darken the image by.
   */

  void handleBrightenEvent(int brightenValue);

  /**
   * Handles performing a blur operation on the current image given a
   *     kernel object to represent the 2D filter array.
   *
   * @param kernel kernel object that represents a 2D array.
   */

  void handleBlurEvent(IKernelState kernel);

  /**
   * Handles performing a sharpen operation on the current image given a
   *     kernel object to represent the 2D filter array.
   *
   * @param kernel kernel object that represents a 2D array.
   */

  void handleSharpenEvent(IKernelState kernel);

  /**
   * Handles performing a greyscale matrix operation on the current image given a
   *     kernel object to represent the 2D filter array/matrix.
   *
   * @param kernel kernel object that represents a 2D array/matrix.
   */

  void handleGreyscaleMatrixEvent(IKernelState kernel);

  /**
   * Handles performing a sepia matrix operation on the current image given a
   *     kernel object to represent the 2D filter array/matrix.
   *
   * @param kernel kernel object that represents a 2D array/matrix.
   */

  void handleSepiaMatrixEvent(IKernelState kernel);

}
