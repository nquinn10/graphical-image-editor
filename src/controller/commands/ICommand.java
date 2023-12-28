package controller.commands;

import java.util.Scanner;

import model.IModel;

/**
 * This interface represents a command that can be executed in the
 * Image Manipulation and Editor (IME) program.
 */

public interface ICommand {

  /**
   * Run a command by scanning input arguments and extracting required
   * contents from the given model to perform it.
   *
   * @param scanner to scan input arguments
   * @param model image storage database
   *
   * @throws IllegalArgumentException if scanner or model are null
   */
  void run(Scanner scanner, IModel model) throws IllegalArgumentException;
}
