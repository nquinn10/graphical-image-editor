package view;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

/**
 * The Canvas class represents a graphical area where images can be displayed. It extends the JPanel
 * class and implements the ICanvas and Scrollable interfaces to provide image setting and scrolling
 * functionality if the image dimensions exceed the canvas dimensions.
 */

public class Canvas extends JPanel implements ICanvas, Scrollable {

  private BufferedImage image;

  /**
   * Constructs a Canvas with default dimensions and a gray background.
   */

  public Canvas() {
    setBackground(Color.GRAY);
    setPreferredSize(new Dimension(1100, 700));
  }

  /**
   * Overrides the paintComponent method in JComponent class to provide custom rendering of the
   *     image. This method is called automatically whenever the component needs to be painted on
   *     the screen. If an image is available, it is drawn on the center of the canvas. If no image
   *     is available, the method falls back to the default behavior.
   *
   * @param g The Graphics object used for rendering.
   */

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    // draw the image if available
    if (image != null) {
      int canvasWidth = getWidth();
      int canvasHeight = getHeight();

      int imageWidth = image.getWidth();
      int imageHeight = image.getHeight();

      // calculate the starting position to center the image
      int x = (canvasWidth - imageWidth) / 2;
      int y = (canvasHeight - imageHeight) / 2;

      // draw image on canvas
      g.drawImage(image, x, y, this);
    } else {
      // fallback to the original behavior
    }
  }

  @Override
  public void setImage(BufferedImage image) {
    this.image = image;
    // trigger repaint to show the new image
    repaint();
  }

  /**
   * Returns the preferred size of the viewport for scrolling purposes. Overidden from Scrollable
   *     class.
   * @return Preferred viewport size.
   */

  @Override
  public Dimension getPreferredScrollableViewportSize() {
    return new Dimension(1100, 700);
  }

  /**
   * Returns the unit increment for scrolling. Overidden from Scrollable class.
   * @param visibleRect Visible area of the viewport.
   * @param orientation Scrolling orientation.
   * @param direction Scrolling direction.
   * @return Unit increment.
   */

  @Override
  public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
    return 10;
  }

  /**
   * Returns the block increment for scrolling. Overidden from Scrollable class.
   * @param visibleRect Visible area of the viewport.
   * @param orientation Scrolling orientation.
   * @param direction Scrolling direction.
   * @return Block increment.
   */

  @Override
  public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
    return 100;
  }

  /**
   * Determines if the component tracks the viewport's width. Overidden from Scrollable class.
   * @return true if tracking width, false otherwise.
   */

  @Override
  public boolean getScrollableTracksViewportWidth() {
    return false;
  }

  /**
   * Determines if the component tracks the viewport's height. Overidden from Scrollable class.
   * @return true if tracking height, false otherwise.
   */

  @Override
  public boolean getScrollableTracksViewportHeight() {
    return false;
  }
}
