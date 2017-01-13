package fer.project.gui;

import java.io.IOException;

import fer.projekt.classes.Startup;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class GuiMain extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Računalni vid");

		initRootLayout();

		//Startup.start();

		showCameraOverview();
	}

	/**
	 * Initializes the root layout.
	 */
	public void initRootLayout() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GuiMain.class.getResource("RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Shows the camera overview inside the root layout.
	 */
	public void showCameraOverview() {
		try {
			// Load camera overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GuiMain.class.getResource("CameraOverview.fxml"));
			AnchorPane cameraOverview = (AnchorPane) loader.load();

			// Set camera overview into the center of root layout.
			rootLayout.setCenter(cameraOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the main stage.
	 *
	 * @return
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

}
