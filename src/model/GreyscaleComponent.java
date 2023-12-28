package model;

/**
 * This enum represents a greyscale component.
 */

public enum GreyscaleComponent {
  RED("red"),
  GREEN("green"),
  BLUE("blue"),
  VALUE("value"),
  INTENSITY("intensity"),
  LUMA("luma");

  private final String txt;

  /**
   * Constructs a string of the enum value.
   */

  GreyscaleComponent(String txt) {
    this.txt = txt;
  }

  /**
   * Returns a string of the enum type. Either "red",
   *     "green", "blue", "value", "luma", or "intensity.
   *
   * @return a string of the enum type. Either "red",
   *     "green", "blue", "value", "luma", or "intensity.
   */

  public String toString() {
    return txt;
  }
}
