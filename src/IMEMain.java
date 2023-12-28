import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import controller.ControllerImpl;
import controller.GUIController;
import controller.IController;
import model.IModel;
import model.ModelImpl;
import view.GUIView;
import view.IGUIView;
import view.ImageView;
import view.ImageTextView;

/**
 * Run the Image Manipulation and Editor (IME) program interactively via the GUI or by
 *     creating and running command line arguments.
 */

public final class IMEMain {

  /**
   * Constructs a main which parses a given string input
   * and initializes a model, view, and controller based
   * on the command line argument given as a string. - file with .txt file
   * will read the file and run the commands. -text will allow for interactive
   * keyboard input. Running with no command line configurations with initiate the GUI.
   *
   * @param args string input which represents a command line argument
   *             to determine the IME command.
   */

  public static void main(String[] args) {
    IModel model = new ModelImpl();
    ImageView view = new ImageTextView(model);
    IController controller;

    if (args.length >= 2 && args[0].equals("-file")) {
      // Iif the "-file" option is provided, use the script file as input
      String scriptFileName = args[1];
      try (BufferedReader bufferedReader = new BufferedReader(new FileReader(scriptFileName))) {
        StringBuilder scriptContent = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
          scriptContent.append(line).append("\n");
        }
        // use the script content as input for the controller
        Readable readable = new StringReader(scriptContent.toString());
        controller = new ControllerImpl(readable, model, view);
        controller.run();
      } catch (IOException e) {
        throw new IllegalStateException("Unable to read script file");
      }
    } else if (args.length >= 1 && args[0].equals("-text")) {
      // if "-text" option is provided, use the default keyboard input
      controller = new ControllerImpl(new InputStreamReader(System.in), model, view);
      controller.run();
    } else {
      if (args.length > 0) {
        // display an error message for unrecognized command-line arguments
        System.err.println("Invalid command-line arguments. Use -file or -text.");
        return; // quit the program
      }
      // otherwise, default to GUI
      IGUIView guiView = new GUIView();
      IController guiController = new GUIController(model, guiView);
      guiController.run();
    }
  }
}
