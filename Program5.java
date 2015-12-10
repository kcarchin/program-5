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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.Group;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

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
		Scene scene = new Scene(root, 1200, 700);

		buttonhelp.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Group roothelp = new Group();
				Scene help = new Scene(roothelp, scene.getWidth(), scene.getHeight());
				help.setFill(Color.BLACK);
				HBox xbox = new HBox();
				Text text = new Text(scene.getWidth() * 0.20, 120, "Welcome to the Unlicensed Light Web Browser 1.1");
				text.setFill(Color.rgb(127, 244, 16));
				text.setFont(Font.font("Verdana", 30));
				roothelp.getChildren().add(text);
				Text text2 = new Text(scene.getWidth() * 0.20, 200,
						"-ULWB is a lightweight, java-based web browser. \n-In order to visit a website, type the url into the address bar. \n-Unfortunately, the current distribution lacks video support, expect it in version 2.0. \n-Enjoy!");
				text2.setFill(Color.rgb(127, 244, 16));
				text2.setFont(Font.font("Verdana", 20));
				roothelp.getChildren().add(text2);
				xbox.getChildren().add(close);
				roothelp.getChildren().add(xbox);
				stage.setScene(help);
				stage.show();
			}
		});
		buttonback.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				final WebHistory history = webEngine.getHistory();
				Platform.runLater(new Runnable() {
					public void run() {
						history.go(-1);
						addressbar.setText("");
						addressbar.setText(webEngine.getLocation());
						stage.setTitle(webEngine.getLocation());
					}
				});
			}
		});
		buttonforward.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				final WebHistory history = webEngine.getHistory();
				Platform.runLater(new Runnable() {
					public void run() {
						history.go(1);
						addressbar.setText("");
						addressbar.setText(webEngine.getLocation());
						stage.setTitle(webEngine.getLocation());
					}
				});

			}
		});
		close.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				stage.setScene(scene);
				stage.show();
			}
		});
		scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
				addressbar.setText("");
				addressbar.setText(webEngine.getLocation());
				stage.setTitle(webEngine.getLocation());
			}
		});
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					if (addressbar.getText().contains("https://")) {
						webEngine.load(addressbar.getText());
					} else {
						webEngine.load(("https://" + addressbar.getText()));
					}
					new java.util.Timer().schedule( 
					        new java.util.TimerTask() {
					            @Override
					            public void run() {
					            	addressbar.setText("");
									addressbar.setText(webEngine.getLocation());
									stage.setTitle(webEngine.getLocation());
					            }
					        }, 
					       1000
					);
					
				}
			}
		});
		BorderPane border = new BorderPane();
		HBox thbox = new HBox();
		thbox.setPrefWidth(scene.getWidth() - 20);
		HBox.setHgrow(thbox, Priority.ALWAYS);
		addressbar.setMaxWidth(
				scene.getWidth() - buttonback.getWidth() - buttonforward.getWidth() - buttonhelp.getWidth());
		thbox.getChildren().addAll(buttonback, buttonforward, addressbar, buttonhelp);
		HBox.setHgrow(addressbar, Priority.ALWAYS);
		border.setTop(thbox);
		border.setBottom(makeStatusBar());
		border.setCenter(makeHtmlBrowser());
		root.getChildren().add(border);
		stage.setWidth(scene.getWidth());
		stage.setHeight(scene.getHeight());
		addressbar.setText("");// is this the right text
		stage.setTitle(webEngine.getLocation());

		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}