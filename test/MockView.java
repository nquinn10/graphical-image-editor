import java.awt.*;
import java.awt.image.BufferedImage;

import view.IGUIView;
import view.ViewListener;

/**
 * This class implements the IGUIView interface
 *     and represents a mock view.
 */

public class MockView implements IGUIView {

  public BufferedImage imageToTest;
  private StringBuilder log;

  /**
   * Constructs a mock view of the IGUIView.
   *
   * @param log StringBuilder log to track argument inputs.
   */

  public MockView(StringBuilder log) {
    this.log = log;
  }

  @Override
  public void setVisible(boolean visible) {
    // default impl for mock view
  }

  @Override
  public void addViewListener(ViewListener listener) {
    // default impl for mock view
  }

  @Override
  public void requestFrameFocus() {
    // default impl for mock view
  }

  /**
   * Sets given text to GUI view by appending to mock view log.
   *
   * @param text text to set to view
   */

  @Override
  public void setViewText(String text) {
    this.log.append(text);
  }

  /**
   * Sets given buffered image to GUI view by appending each pixel channel
   *     value to log.
   *
   * @param image image to set to GUI view
   */

  @Override
  public void setViewImage(BufferedImage image) {
    this.imageToTest = image;
    // create string from buffered image in pixels
    for (int row = 0; row < this.imageToTest.getHeight(); row++) {
      for (int col = 0; col < this.imageToTest.getWidth(); col ++) {
        Color color = new Color(this.imageToTest.getRGB(row, col));
        this.log.append(color.getRed() + " " + color.getGreen() + " " + color.getBlue() + " ");
      }
    }
  }
}

