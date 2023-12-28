package controller;

/**
 * This interface represents a controller for the Image Manipulation and Editor (IME) program.
 */

public interface IController {

  /**
   * Runs the image Manipulation and Editor (IME) program by taking in commands
   * and calling to requested commands and the model. Also
   * writing or rendering to the view.
   *
   * @throws IllegalStateException only if the controller is unable
   *     to successfully read input or transmit output.
   */

  void run();
}
