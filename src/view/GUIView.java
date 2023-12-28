package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import controller.ImageUtil;
import model.GreyscaleComponent;
import model.kernel.IKernel;
import model.kernel.KernelImpl;

/**
 * This class represents an Image Manipulation and Editor (IME) GUI. It extends
 * the JFrame class and implements the IGUIView, and ActionListener interfaces.
 *
 * <p>The IME GUI provides a user interface for loading, editing, and saving images. It offers
 * various features such as image brightening/darkening, greyscale operations, and applying filters.
 * Users can interact with the GUI through buttons and sliders.
 */

public class GUIView extends JFrame implements ActionListener, IGUIView {

  private final JSlider brightnessSlider;
  private final JLabel showText;
  private final Canvas canvas;
  private final List<ViewListener> listenersToNotify;

  /**
   * Constructs a GUI view for the Image Manipulation and Editor (IME) program.
   * This constructor initializes the GUI components, sets up event listeners, and prepares
   * the user interface for interaction.
   */

  public GUIView() {
    setSize(1100, 700);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setTitle("Image Manipulation and Editor Program");
    // initialize listener list
    this.listenersToNotify = new ArrayList<>();

    // Create the outer panel for holding the Canvas and buttonPanel
    JPanel mainPanel = new JPanel(new BorderLayout());

    // add canvas to main panel with scroll bars
    this.canvas = new Canvas();
    JScrollPane scrollPane = new JScrollPane(this.canvas);
    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    mainPanel.add(scrollPane, BorderLayout.CENTER);

    // create a panel for all buttons
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
    buttonPanel.setPreferredSize(new Dimension(350, buttonPanel.getPreferredSize().height));
    mainPanel.add(buttonPanel, BorderLayout.EAST);

    // create a panel for IO buttons
    JPanel ioButtonPanel = new JPanel(new GridLayout(0, 1));
    ioButtonPanel.setBorder(BorderFactory.createTitledBorder("IO"));
    JButton loadImageButton = new JButton("Load Image");
    ioButtonPanel.add(loadImageButton);
    JButton saveImageButton = new JButton("Save Image");
    ioButtonPanel.add(saveImageButton);
    // add to button panel
    buttonPanel.add(ioButtonPanel);

    // create a panel for brighten button
    JPanel brightenButtonPanel = new JPanel();
    brightenButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
    brightenButtonPanel.setBorder(BorderFactory.createTitledBorder("Brighten/Darken"));
    // create slider for brighten/darken values
    brightnessSlider = new JSlider(JSlider.HORIZONTAL, -255, 255, 0);
    brightnessSlider.setMajorTickSpacing(10);
    brightnessSlider.setMinorTickSpacing(1);
    JLabel valueLabel = new JLabel("Value: " + brightnessSlider.getValue());
    // create listener for shifting of slider + update value
    brightnessSlider.addChangeListener(e -> {
      int value = brightnessSlider.getValue();
      valueLabel.setText("Value: " + value);
    });

    JButton brightenImageButton = new JButton("Apply");
    // add to brighten button panel
    brightenButtonPanel.add(brightnessSlider);
    brightenButtonPanel.add(valueLabel);
    brightenButtonPanel.add(brightenImageButton);
    // add to button panel
    buttonPanel.add(brightenButtonPanel);

    // create a panel for greyscale buttons + add to button panel
    JPanel greyscaleButtonPanel = new JPanel(new GridLayout(3, 3));
    greyscaleButtonPanel.setBorder(BorderFactory.createTitledBorder("Greyscale"));
    JButton redComponentImageButton = new JButton("Red-Component");
    greyscaleButtonPanel.add(redComponentImageButton);
    JButton greenComponentImageButton = new JButton("Green-Component");
    greyscaleButtonPanel.add(greenComponentImageButton);
    JButton blueComponentImageButton = new JButton("Blue-Component");
    greyscaleButtonPanel.add(blueComponentImageButton);
    JButton valueComponentImageButton = new JButton("Value");
    greyscaleButtonPanel.add(valueComponentImageButton);
    JButton lumaComponentImageButton = new JButton("Luma");
    greyscaleButtonPanel.add(lumaComponentImageButton);
    JButton intensityComponentImageButton = new JButton("Intensity");
    greyscaleButtonPanel.add(intensityComponentImageButton);
    buttonPanel.add(greyscaleButtonPanel);

    // create a panel for filter buttons + add to button panel
    JPanel filterButtonPanel = new JPanel(new GridLayout(0, 1));
    filterButtonPanel.setBorder(BorderFactory.createTitledBorder("Filter"));
    JButton blurImageButton = new JButton("Blur");
    filterButtonPanel.add(blurImageButton);
    JButton sharpenImageButton = new JButton("Sharpen");
    filterButtonPanel.add(sharpenImageButton);
    buttonPanel.add(filterButtonPanel);

    // create a panel for color buttons + add to button panel
    JPanel colorButtonPanel = new JPanel(new GridLayout(0, 1));
    colorButtonPanel.setBorder(BorderFactory.createTitledBorder("Color Transformation"));
    JButton sepiaImageButton = new JButton("Sepia");
    colorButtonPanel.add(sepiaImageButton);
    JButton greyscaleImageButton = new JButton("Greyscale");
    colorButtonPanel.add(greyscaleImageButton);
    buttonPanel.add(colorButtonPanel, BorderLayout.NORTH);

    // create message panel to display user feedback
    JPanel messagePanel = new JPanel();
    messagePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    this.showText = new JLabel("");
    messagePanel.add(this.showText);
    buttonPanel.add(messagePanel, BorderLayout.CENTER);

    // set button action commands
    loadImageButton.setActionCommand("load");
    saveImageButton.setActionCommand("save");
    brightenImageButton.setActionCommand("brighten");
    redComponentImageButton.setActionCommand("red");
    greenComponentImageButton.setActionCommand("green");
    blueComponentImageButton.setActionCommand("blue");
    valueComponentImageButton.setActionCommand("value");
    lumaComponentImageButton.setActionCommand("luma");
    intensityComponentImageButton.setActionCommand("intensity");
    blurImageButton.setActionCommand("blur");
    sharpenImageButton.setActionCommand("sharpen");
    sepiaImageButton.setActionCommand("sepia");
    greyscaleImageButton.setActionCommand("greyscale");

    // set button action listeners
    loadImageButton.addActionListener(this);
    saveImageButton.addActionListener(this);
    brightenImageButton.addActionListener(this);
    redComponentImageButton.addActionListener(this);
    greenComponentImageButton.addActionListener(this);
    blueComponentImageButton.addActionListener(this);
    valueComponentImageButton.addActionListener(this);
    lumaComponentImageButton.addActionListener(this);
    intensityComponentImageButton.addActionListener(this);
    blurImageButton.addActionListener(this);
    sharpenImageButton.addActionListener(this);
    sepiaImageButton.addActionListener(this);
    greyscaleImageButton.addActionListener(this);

    setContentPane(mainPanel);

    this.setFocusable(true);
  }

  @Override
  public void addViewListener(ViewListener listener) {
    this.listenersToNotify.add(listener);
  }

  @Override
  public void requestFrameFocus() {
    this.requestFocus();
  }

  @Override
  public void setViewText(String text) {
    this.showText.setText(text);
  }

  @Override
  public void setViewImage(BufferedImage image) {
    this.canvas.setImage(image);
  }

  /**
   * Opens a file dialog for selecting an image file type (ppm, jpeg, png) and
   * returns the selected file path.
   *
   * @return The file path of the selected image file, or null if no file was selected.
   */

  private String showFileDialog() {
    JFileChooser fileChooser = new JFileChooser();

    // create a file filter for allowed image file types
    FileNameExtensionFilter imageFilter = new FileNameExtensionFilter(
            "Image Files", "ppm", "jpeg", "jpg", "png");

    fileChooser.setFileFilter(imageFilter);

    int returnValue = fileChooser.showOpenDialog(this);

    if (returnValue == JFileChooser.APPROVE_OPTION) {
      File selectedFile = fileChooser.getSelectedFile();
      // get path to load file from and return
      String filePath = selectedFile.getAbsolutePath();
      return filePath;
    } else {
      // if no load location/file path selected, return null
      return null;
    }
  }

  /**
   * Opens a file dialog for selecting a location to save an image file
   * and returns the selected file path.
   *
   * @return The file path where the image should be saved, or null if no location was selected.
   */

  private String showSaveFileDialog() {
    JFileChooser fileChooser = new JFileChooser();

    // Create a file filter for allowed image file types
    FileNameExtensionFilter imageFilter = new FileNameExtensionFilter(
            "Image Files", "jpeg", "jpg", "ppm", "png");

    fileChooser.setFileFilter(imageFilter);

    int returnValue = fileChooser.showSaveDialog(this);

    if (returnValue == JFileChooser.APPROVE_OPTION) {
      File selectedFile = fileChooser.getSelectedFile();
      // get path to save file to and return
      String filePath = selectedFile.getAbsolutePath();
      return filePath;
    } else {
      // if no save location/file path selected, return null
      return null;
    }
  }

  /**
   * Initiates process of loading an image by opening a file dialog to choose an image/file path.
   * Notifies the registered listeners with the selected file path.
   * If no image is selected, a message is set in the view indicating that no file was selected.
   */

  private void emitLoadEvent() {
    // get file path from load file dialog
    String filePath = showFileDialog();
    if (filePath == null) {
      setViewText("No file selected.");
    } else {
      // pass filePath to view listener method
      for ( ViewListener listener : listenersToNotify ) {
        listener.handleLoadEvent(filePath);
      }
    }
  }

  /**
   * Initiates process of saving an image by opening a file dialog to choose a save location.
   * Notifies the registered listeners with the selected file path.
   * If no location is selected, a message is set in the view indicating no filepath was specified.
   */

  private void emitSaveEvent() {
    // get file path from save file dialog
    String filePath = showSaveFileDialog();
    if (filePath == null) {
      setViewText("Must enter filepath to save.");
    } else {
      try {
        String extension = ImageUtil.getFileExtension(filePath);
        if (extension.equals("ppm") || extension.equals("jpeg") || extension.equals("png")) {
          // pass filePath to view listener method
          for (ViewListener listener : listenersToNotify) {
            listener.handleSaveEvent(filePath);
          }
        } else {
          setViewText("Invalid file extension. Must be .jpeg, .ppm, or .png.");
        }
      } catch (IllegalStateException e) {
        setViewText("Error: " + e.getMessage());
      }
    }
  }

  /**
   * Initiates process of brightening an image by retrieving value from slider object.
   * Notifies the registered listeners with the slider value.
   * If value equals 0, a message is set to view to inform user to apply value.
   */

  private void emitBrightnessEvent() {
    // get slider value
    int sliderValue = this.brightnessSlider.getValue();
    if (sliderValue == 0) {
      setViewText("Must apply value from slider.");
    } else {
      // pass sliderValue to view listener method
      for ( ViewListener listener : listenersToNotify ) {
        listener.handleBrightenEvent(sliderValue);
      }
    }
  }

  /**
   * Initiates process of greyscaling an image based on greyscaleComponent given.
   * greyscaleComponent enum value is assigned in switch statement based on button selected.
   * Notifies the registered listeners with the greyscaleComponent enum value.
   */

  private void emitGreyscaleEvent(GreyscaleComponent greyscaleComponent) {
    // pass greyscaleComponent to view listener method
    for ( ViewListener listener : listenersToNotify ) {
      listener.handleGreyscaleEvent(greyscaleComponent);
    }
  }

  /**
   * Initiates process of applying a blur effect to an image based on Gaussian blur filter.
   * Blur filter is created as a kernel object.
   * The kernel object is then passed to the registered listeners.
   */

  private void emitBlurEvent() {
    // create Gaussian blur filter
    double[][] blurFilter = {
            {1.0 / 16, 1.0 / 8, 1.0 / 16},
            {1.0 / 8, 1.0 / 4, 1.0 / 8},
            {1.0 / 16, 1.0 / 8, 1.0 / 16}
    };
    // create kernel object and pass to view listener method
    IKernel blurKernel = new KernelImpl(blurFilter);
    for ( ViewListener listener : listenersToNotify ) {
      listener.handleBlurEvent(blurKernel);
    }
  }

  /**
   * Initiates process of applying a sharpen effect to an image based on sharpen filter.
   * Sharpen filter is created as a kernel object.
   * The kernel object is then passed to the registered listeners.
   */

  private void emitSharpenEvent() {
    // create sharpen filter
    double[][] sharpenFilter = {
            {-1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8},
            {-1.0 / 8, 1.0 / 4, 1.0 / 4, 1.0 / 4, -1.0 / 8},
            {-1.0 / 8, 1.0 / 4, 1.0, 1.0 / 4, -1.0 / 8},
            {-1.0 / 8, 1.0 / 4, 1.0 / 4, 1.0 / 4, -1.0 / 8},
            {-1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8}
    };
    // create kernel object and pass to view listener method
    IKernel sharpenKernel = new KernelImpl(sharpenFilter);
    for ( ViewListener listener : listenersToNotify ) {
      listener.handleSharpenEvent(sharpenKernel);
    }
  }

  /**
   * Initiates process of applying a greyscale effect using a given filter matrix.
   * Greyscale matrix filter is created as a kernel object.
   * The kernel object is then passed to the registered listeners.
   */

  private void emitGreyscaleMatrixEvent() {
    // create greyscale matrix filter
    double[][] greyscaleFilter = {
            {0.2126, 0.7152, 0.0722},
            {0.2126, 0.7152, 0.0722},
            {0.2126, 0.7152, 0.0722}
    };
    // create kernel object and pass to view listener method
    IKernel greyscaleMatrix = new KernelImpl(greyscaleFilter);
    for ( ViewListener listener : listenersToNotify ) {
      listener.handleGreyscaleMatrixEvent(greyscaleMatrix);
    }
  }

  /**
   * Initiates process of applying a sepia effect using a given filter matrix.
   * Sepia matrix filter is created as a kernel object.
   * The kernel object is then passed to the registered listeners.
   */

  private void emitSepiaMatrixEvent() {
    // create sepia matrix filter
    double[][] sepiaFilter = {
            {0.393, 0.769, 0.189},
            {0.349, 0.686, 0.168},
            {0.272, 0.534, 0.131}
    };
    // create kernel object and pass to view listener method
    IKernel sepiaMatrix = new KernelImpl(sepiaFilter);
    for ( ViewListener listener : listenersToNotify ) {
      listener.handleSepiaMatrixEvent(sepiaMatrix);
    }
  }

  /**
   * Handles the action events triggered by various GUI components, such as buttons and sliders.
   * The method processes the action command associated with the event and delegates to
   * corresponding methods for further processing based on the action command.
   *
   * @param e the ActionEvent object representing the event.
   * @throws IllegalStateException if the action command is unknown or unsupported.
   */

  @Override
  public void actionPerformed(ActionEvent e) {
    switch ( e.getActionCommand() ) {
      case "load":
        emitLoadEvent();
        break;
      case "save":
        emitSaveEvent();
        break;
      case "red":
        emitGreyscaleEvent(GreyscaleComponent.RED);
        break;
      case "green":
        emitGreyscaleEvent(GreyscaleComponent.GREEN);
        break;
      case "blue":
        emitGreyscaleEvent(GreyscaleComponent.BLUE);
        break;
      case "value":
        emitGreyscaleEvent(GreyscaleComponent.VALUE);
        break;
      case "intensity":
        emitGreyscaleEvent(GreyscaleComponent.INTENSITY);
        break;
      case "luma":
        emitGreyscaleEvent(GreyscaleComponent.LUMA);
        break;
      case "brighten":
        emitBrightnessEvent();
        break;
      case "blur":
        emitBlurEvent();
        break;
      case "sharpen":
        emitSharpenEvent();
        break;
      case "sepia":
        emitSepiaMatrixEvent();
        break;
      case "greyscale":
        emitGreyscaleMatrixEvent();
        break;
      default:
        throw new IllegalStateException("Unknown action command.");
    }
  }
}
