import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.Group;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
public class Program5 extends Application {
	// INSTANCE VARIABLES
	// These variables are included to get you started.

	private WebView browser = null;
	private WebEngine webEngine = null;
	private TextField statusbar = null;
	Button buttonback = new Button("<-");
	Button buttonforward = new Button("->");
	Button buttonhelp = new Button("?");
	TextField addressbar = new TextField();
	Button close = new Button("x");

	// HELPER METHODS
	/**
	 * Retrieves the value of a command line argument specified by the index.
	 * 
	 * @param index
	 *            - position of the argument in the args list.
	 * @return The value of the command line argument.
	 */
	private String getParameter(int index) {
		Parameters params = getParameters();
		List<String> parameters = params.getRaw();
		return !parameters.isEmpty() ? parameters.get(0) : "";
	}

	/**
	 * Creates a WebView which handles mouse and some keyboard events, and
	 * manages scrolling automatically, so there's no need to put it into a
	 * ScrollPane. The associated WebEngine is created automatically at
	 * construction time.
	 * 
	 * @return browser - a WebView container for the WebEngine.
	 */
	private WebView makeHtmlBrowser() {
		browser = new WebView();
		webEngine = browser.getEngine();
		webEngine.load(getParameter(0));
		return browser;
	}

	/**
	 * hbox group Generates the status bar layout and text field.
	 * 
	 * @return statusbarPane - the HBox layout that contains the statusbar.
	 */
	private HBox makeStatusBar() {
		HBox statusbarPane = new HBox();
		statusbarPane.setPadding(new Insets(5, 4, 5, 4));
		statusbarPane.setSpacing(10);
		statusbarPane.setStyle("-fx-background-color: #336699;");
		statusbar = new TextField();
		HBox.setHgrow(statusbar, Priority.ALWAYS);
		statusbarPane.getChildren().addAll(statusbar);
		return statusbarPane;
	}

	// REQUIRED METHODS
	/**
	 * @param primaryStage
	 *            - the primary stage for this application, onto which the
	 *            application scene can be set.
	 */
	@Override
	public void start(Stage stage) {
		Group root = new Group();

		Scene scene = new Scene(root, 500, 500);

		buttonhelp.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Group roothelp = new Group();
				Scene help = new Scene(roothelp, scene.getWidth(), scene.getHeight());
				help.setFill(Color.BLACK);
				HBox xbox = new HBox();
				Text text = new Text(scene.getWidth()*0.25, 120, "Welcome to the Unlicensed Web Browser Mk.1");
				text.setFill(Color.rgb(127, 244, 16));
				text.setFont(Font.font ("Verdana", 20));
				roothelp.getChildren().add(text);
				xbox.getChildren().add(close);
				roothelp.getChildren().add(xbox);
				stage.setScene(help);
				stage.show();

			}
		});
		buttonback.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				final WebHistory history = webEngine.getHistory();
				ObservableList<WebHistory.Entry> entryList = history.getEntries();
				int currentIndex = history.getCurrentIndex();
				Platform.runLater(new Runnable() {
					public void run() {
						history.go(-1);
					}
				});
			}
		});

		buttonforward.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {

			}
		});
		close.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				stage.setScene(scene);
				stage.show();
			}
		});

		BorderPane border = new BorderPane();
		HBox thbox = new HBox();
		thbox.setPrefWidth(scene.getWidth());
		HBox.setHgrow(thbox, Priority.ALWAYS);
		addressbar.setMaxWidth(
				scene.getWidth() - buttonback.getWidth() - buttonforward.getWidth() - buttonhelp.getWidth());
		thbox.getChildren().addAll(buttonback, buttonforward, addressbar, buttonhelp);
		HBox.setHgrow(addressbar, Priority.ALWAYS);
		border.setTop(thbox);
		border.setBottom(

		makeStatusBar());
		border.setCenter(makeHtmlBrowser());
		root.getChildren().add(border);
		// stage.setWidth(scene.getWidth());
		// stage.setHeight(scene.getHeight());
		addressbar.setText(webEngine.getLocation());// is this the right text?
													// &&&needs to be refreshed
													// every click
		stage.setTitle(webEngine.getLocation());// also needs to be refreshed
		stage.setScene(scene);
		stage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}
}