package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import controller.commands.Blur;
import controller.commands.EditBrightness;
import controller.commands.GreyscaleCommand;
import controller.commands.GreyscaleMatrixCommand;
import controller.commands.ICommand;
import controller.commands.LoadImageCommand;
import controller.commands.SaveImageCommand;
import controller.commands.SepiaMatrixCommand;
import controller.commands.Sharpen;
import model.IModel;
import model.GreyscaleComponent;
import view.ImageView;

/**
 * This class implements the IController interface.
 */

public class ControllerImpl implements  IController {
  private final Readable input;
  private final IModel model;
  private final ImageView view;
  private final Map<String, ICommand> commandMap;

  /**
   * Constructs an Image Manipulation and Editor (IME) program controller given
   *  image database model, view, and readable.
   *
   * @param model the image database model
   * @param view the view to render messages
   * @param input readable input
   *
   * @throws IllegalArgumentException if model, view, or readable are null.
   */

  public ControllerImpl(Readable input, IModel model, ImageView view)
          throws IllegalArgumentException {
    if (model == null || input == null || view == null) {
      throw new IllegalArgumentException("Constructor argument cannot be null.");
    }
    this.input = input;
    this.model = model;
    this.view = view;
    // create hashmap and add commands
    this.commandMap = new HashMap<String, ICommand>();
    this.commandMap.put("save", new SaveImageCommand());
    this.commandMap.put("load", new LoadImageCommand());
    this.commandMap.put("brighten", new EditBrightness());
    this.commandMap.put("luma", new GreyscaleCommand(GreyscaleComponent.LUMA));
    this.commandMap.put("value", new GreyscaleCommand(GreyscaleComponent.VALUE));
    this.commandMap.put("intensity", new GreyscaleCommand(GreyscaleComponent.INTENSITY));
    this.commandMap.put("red-component", new GreyscaleCommand(GreyscaleComponent.RED));
    this.commandMap.put("green-component", new GreyscaleCommand(GreyscaleComponent.GREEN));
    this.commandMap.put("blue-component", new GreyscaleCommand(GreyscaleComponent.BLUE));
    this.commandMap.put("blur", new Blur());
    this.commandMap.put("sharpen", new Sharpen());
    this.commandMap.put("greyscale", new GreyscaleMatrixCommand());
    this.commandMap.put("sepia", new SepiaMatrixCommand());
  }

  /**
   * Helper method to render a message to the view.
   *
   * @param message string message to write to view
   *
   * @throws IllegalStateException if IOException occurs when writing
   *     to the view.
   */

  private void write(String message) throws IllegalStateException {
    try {
      this.view.renderMessage(message);
    } catch (IOException e) {
      throw new IllegalStateException("Fail to write to view.");
    }
  }


  @Override
  public void run() throws IllegalStateException {
    Scanner scanner = new Scanner(this.input);

    while (scanner.hasNext()) {
      String command = scanner.next();

      // get command from map
      ICommand commandToRun = this.commandMap.getOrDefault(command, null);
      if (commandToRun == null) {
        write("Invalid command. Please enter a valid command.\n");
        // skip past rest fo the command and allow the user to re-enter
        scanner.nextLine();
        continue;
      }
      try {
        commandToRun.run(scanner, this.model);
        write("Command completed successfully\n");
      } catch (IllegalStateException e) {
        // write any message from commands
        write(e.getMessage());
      }
    }
  }
}
