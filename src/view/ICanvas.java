package view;

import java.awt.image.BufferedImage;

/**
 * This interface represents a canvas that displays images.
 */

public interface ICanvas {

  /**
   * Sets the image to be displayed on the canvas.
   *
   * @param image the BufferedImage to be displayed on the canvas.
   */

  void setImage(BufferedImage image);
}
