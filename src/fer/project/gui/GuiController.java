package fer.project.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class GuiController {

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

	public GuiController() {
	}

	@FXML
	private void initialize(){
	}

	@FXML
	private void handleUp(ActionEvent event) {

	}

	@FXML
	private void handleDown(ActionEvent event) {

	}

	@FXML
	private void handleLeft(ActionEvent event) {

	}

	@FXML
	private void handleRight(ActionEvent event) {

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
	private void toogleAutofocus(ActionEvent event) {

	}
}
