package fer.project.gui;

import java.io.IOException;

import fer.projekt.classes.CameraController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GuiController {

	private CameraController cameraController;

	private boolean followObjectBoolean = false;

	@FXML
	private Button buttonUp;
	@FXML
	private Button buttonDown;
	@FXML
	private Button buttonLeft;
	@FXML
	private Button buttonRight;

	@FXML
	private RadioButton autofocus;
	@FXML
	private RadioButton followObject;
	@FXML
	private TextField urlTextField;
	@FXML
	private ChoiceBox objectChoiceBox;
	@FXML
	private Slider zoomSlider;
	@FXML
	private ImageView imageView;

	public GuiController() {
		cameraController = new CameraController();
	}

	@FXML
	private void initialize() {
	}

	@FXML
	private void setImage(Image image) throws IOException {
		imageView.setImage(image);
	}

	@FXML
	private void handleZoom(ActionEvent event) throws IOException {
		cameraController.setZoom((int) zoomSlider.getValue());
	}

	@FXML
	private void handleUp(ActionEvent event) throws IOException {
		cameraController.moveUp();
	}

	@FXML
	private void handleDown(ActionEvent event) throws IOException {
		cameraController.moveDown();
	}

	@FXML
	private void handleLeft(ActionEvent event) throws IOException {
		cameraController.moveLeft();
	}

	@FXML
	private void handleRight(ActionEvent event) throws IOException {
		cameraController.moveRight();
	}

	@FXML
	private void toogleFollowObject(ActionEvent event) {
		followObjectBoolean = !followObjectBoolean;
		applyFollowObject(followObjectBoolean);
	}

	private void applyFollowObject(boolean followObjectBoolean) {
		this.buttonUp.setDisable(!followObjectBoolean);
		this.buttonDown.setDisable(!followObjectBoolean);
		this.buttonLeft.setDisable(!followObjectBoolean);
		this.buttonRight.setDisable(!followObjectBoolean);
	}

	@FXML
	private void toogleAutofocus(ActionEvent event) throws IOException {
		cameraController.switchAutoFocus();
	}
}
