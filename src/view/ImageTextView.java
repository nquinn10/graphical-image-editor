package view;

import model.IModel;
import model.IImageState;
import model.IPixel;
import java.io.IOException;

/**
 * This class implements the ImageView interface.
 */

public class ImageTextView implements ImageView {

  private final IModel model;
  private final Appendable out;

  /**
   * Constructs an Image text view given the image model.
   *
   * @param model the image database model
   *
   * @throws IllegalArgumentException if model is null.
   */

  public ImageTextView(IModel model) throws IllegalArgumentException {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null");
    }
    this.model = model;
    this.out = System.out;
  }

  /**
   * Constructs an Image text view given the image model and appendable object.
   *
   * @param model the image database model
   * @param appendable appendable object that this view uses as its destination
   *
   * @throws IllegalArgumentException if model or appendable object is null.
   */

  public ImageTextView(IModel model, Appendable appendable)
          throws IllegalArgumentException {
    if (model == null || appendable == null) {
      throw new IllegalArgumentException("Constructor arguments cannot be null");
    }
    this.model = model;
    this.out = appendable;
  }

  @Override
  public void renderMessage(String message) throws IOException {
    if (message == null) {
      throw new IllegalArgumentException("message cannot be null");
    }
    this.out.append(message);
  }

  /**
   * Return a string that represents the elements of a PPM image. The string contains
   * the image height, width, max value, and color of pixel represented by including the row/col
   * and pixel RGB values at the location. Each element above is on a new line with a new line
   * after the last row/col in the array.
   *
   * @return the image elements as a string.
   */

  @Override
  public String toString(String imageID) {
    IImageState image = model.getImage(imageID); // Get the IImageState object from the model
    if (image == null) {
      return "No image loaded.";
    }

    int width = image.getWidth();
    int height = image.getHeight();
    int maxValue = image.getMaxValue();

    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("P3\n");
    stringBuilder.append(width).append(" ").append(height).append("\n");
    stringBuilder.append(maxValue).append("\n");

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        IPixel pixel = image.getPixel(i, j);
        int r = pixel.getR();
        int g = pixel.getG();
        int b = pixel.getB();
        stringBuilder.append(r).append(" ").append(g).append(" ").append(b).append(" ");
      }
      // Remove extra spaces on the right side of the row
      int length = stringBuilder.length();
      while (length > 0 && stringBuilder.charAt(length - 1) == ' ') {
        length--;
      }
      stringBuilder.setLength(length);
      stringBuilder.append("\n");
    }

    return stringBuilder.toString();
  }

}
