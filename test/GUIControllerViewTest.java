import org.junit.Test;

import controller.GUIController;
import model.IModel;
import model.ModelImpl;
import view.GUIView;
import view.IGUIView;
import view.ViewListener;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * A JUnit test class for the GUIController class and the IGUIView interface and
 *     the GUIView class.
 */


public class GUIControllerViewTest {

  /**
   * This is a test for the GUIController constructor.
   */

  @Test
  public void testGUIControllerConstructor() {
    IModel model = new ModelImpl();
    IGUIView view = new GUIView();

    // test IllegalArgumentExceptions
    // null model
    IModel nullModel = null;
    assertThrows(IllegalArgumentException.class, () -> {
      new GUIController(nullModel, view);
    });
    // null view
    IGUIView nullView = null;
    assertThrows(IllegalArgumentException.class, () -> {
      new GUIController(model, nullView);
    });
  }

  /**
   * Test case to ensure image given to the view by the controller is the image that
   * the view received using a mock view. Also tests that text is set to view correctly.
   */

  @Test
  public void testMockViewValidImage() {
    IModel model = new ModelImpl();
    StringBuilder log = new StringBuilder();
    // initialize mock view with log
    MockView mockView = new MockView(log);
    // create controller
    ViewListener controller = new GUIController(model,mockView );
    // pass file path to view listener controller method
    controller.handleLoadEvent("res/fourbyfour.ppm");


    // confirm image loaded is same as image the view got
    // confirm text set to view is as expected
    String expectedString = "255 0 0 0 0 0 255 0 255 0 255 255 "
            + "0 255 0 255 0 0 0 0 0 255 0 255 "
            + "0 0 255 0 255 0 255 0 0 0 0 0 "
            + "255 255 255 0 0 255 0 255 0 255 0 0 "
            + "Image loaded successfully";
    assertEquals(expectedString, log.toString());
  }

  /**
   * Test case to ensure error message is set to view when file path with no
   *     file extension is provided using a mock view.
   */

  @Test
  public void testMockViewNoFileExtension() {
    IModel model = new ModelImpl();
    StringBuilder log = new StringBuilder();
    // initialize mock view with log
    MockView mockView = new MockView(log);
    // create controller
    ViewListener controller = new GUIController(model,mockView );
    // pass file path to view listener controller method
    // filepath has no extension
    controller.handleLoadEvent("res/fourbyfour");

    // Confirm controller catches + sets error message to view
    String expectedString = "Error: Invalid file type. File extension is missing\n";
    assertEquals(expectedString, log.toString());
  }

  /**
   * Test case to ensure error message is set to view when invalid file extension is
   *     provided using mock view.
   */

  @Test
  public void testMockViewInvalidFileExtension() {
    IModel model = new ModelImpl();
    StringBuilder log = new StringBuilder();
    // initialize mock view with log
    MockView mockView = new MockView(log);
    // create controller
    ViewListener controller = new GUIController(model,mockView );
    // pass file path to view listener controller method
    // invalid file extension
    controller.handleLoadEvent("res/fourbyfour.pdf");


    // Confirm controller catches + sets error message to view
    String expectedString = "Error: Failed to read from file.\n";
    assertEquals(expectedString, log.toString());
  }
}
