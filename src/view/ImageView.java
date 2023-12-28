package view;

import java.io.IOException;

/**
 * This interface represents operations that should be offered by
 * a text view for an Image.
 */

public interface ImageView {

  /**
   * Return a string that represents the elements of an image. The string contains
   * the image height, width, max value, and color of pixel represented by including the row/col
   * and pixel RGB values at the location. Each element above is ont a new line with a new line
   * after the last row/col in the array.
   *
   * @return the image elements as a string.
   */

  String toString(String imageID);

  /**
   * Render a specific message to the view.
   * @param message the message to be transmitted
   * @throws IOException if transmission of the message to the destination fails
   */

  void renderMessage(String message) throws IOException;

}
