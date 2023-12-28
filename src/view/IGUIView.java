package view;

import java.awt.image.BufferedImage;

/**
 * This interface represents a GUI. It contains methods that all establish
 * a GUI and allow for the setting of elements to it.
 */

public interface IGUIView {

  /**
   * Sets the visibility of the GUI view. If true, the GUI is visible. If false
   * the GUI is not visible.
   *
   * @param visible boolean value to indicate whether the view should be set as visible
   */

  void setVisible(boolean visible);

  /**
   * Adds listeners to view events to list of listeners given a ViewListener object.
   *
   * @param listener listener to view events
   */

  void addViewListener(ViewListener listener);

  /**
   * Used to request focus for the GUI view's frame.
   */

  void requestFrameFocus();

  /**
   * Sets given text content to GUI view.
   *
   * @param text string to set to GUI view
   */

  void setViewText(String text);

  /**
   * Sets given buffered image to GUI view.
   *
   * @param image image to set to GUI view
   */

  void setViewImage(BufferedImage image);

}
