package application;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class AddressFormController {

    @FXML
    private Button proceedToPayButton;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private TextField addressLine1Field;
    @FXML
    private TextField addressLine2Field;
    @FXML
    private TextField cityField;
    @FXML
    private TextField postalCodeField;
    @FXML
    private RadioButton creditCardRadioButton;
    @FXML
    private TextField cardNumberField;
    @FXML
    private TextField expirationDateField;
    @FXML
    private TextField cvvField;

    @FXML
    private Label addressFilledLabel;

    @FXML
    private Label paymentFilledLabel;

    @FXML
    private void initialize() {
        // Bind the progress bar's progress to the completion percentage of the form
        progressBar.progressProperty().bind(calculateProgress());
    }

    @FXML
    private void handleProceedToPay(ActionEvent event) throws IOException {
        // Check if all required fields are filled
        boolean addressFormFilled = !addressLine1Field.getText().isEmpty() &&
                                    !addressLine2Field.getText().isEmpty() &&
                                    !cityField.getText().isEmpty() &&
                                    !postalCodeField.getText().isEmpty();

        boolean paymentFormFilled = !creditCardRadioButton.isSelected() ||
                                    (!cardNumberField.getText().isEmpty() &&
                                     !expirationDateField.getText().isEmpty() &&
                                     !cvvField.getText().isEmpty());

        if (addressFormFilled && paymentFormFilled) {
            // Load the Thank You page
            Parent thankyouPageParent = FXMLLoader.load(getClass().getResource("ThankYou.fxml"));
            Scene thankyouPageScene = new Scene(thankyouPageParent);

            // Get the stage information
            Stage window = (Stage) proceedToPayButton.getScene().getWindow();

            // Set the new scene onto the stage
            window.setScene(thankyouPageScene);
            window.show();
        } else {
            // Display error message within the form
            addressFilledLabel.setText(addressFormFilled ? "" : "Please fill out all address fields.");
            paymentFilledLabel.setText(paymentFormFilled ? "" : "Please fill out all payment fields.");
        }
    }

    @FXML
    private void goToHomePage(ActionEvent event) throws IOException {
        // Load the main page
        Parent homePageParent = FXMLLoader.load(getClass().getResource("Main.fxml"));
        Scene homePageScene = new Scene(homePageParent);

        // Get the stage information
        Stage window = (Stage) ((Button) event.getSource()).getScene().getWindow();

        // Set the new scene onto the stage
        window.setScene(homePageScene);
        window.show();
    }

    private DoubleBinding calculateProgress() {
        // Example: Assuming all address fields and payment fields are required
        int totalFields = 7; // 4 address fields + 3 payment fields

        // Calculate the completion percentage based on filled fields
        return Bindings.createDoubleBinding(() -> {
            int filledFields = 0;

            if (!addressLine1Field.getText().isEmpty()) filledFields++;
            if (!addressLine2Field.getText().isEmpty()) filledFields++;
            if (!cityField.getText().isEmpty()) filledFields++;
            if (!postalCodeField.getText().isEmpty()) filledFields++;
            if (creditCardRadioButton.isSelected()) {
                if (!cardNumberField.getText().isEmpty()) filledFields++;
                if (!expirationDateField.getText().isEmpty()) filledFields++;
                if (!cvvField.getText().isEmpty()) filledFields++;
            }

            return (double) filledFields / totalFields;
        }, addressLine1Field.textProperty(), addressLine2Field.textProperty(), cityField.textProperty(),
                postalCodeField.textProperty(), creditCardRadioButton.selectedProperty(), cardNumberField.textProperty(),
                expirationDateField.textProperty(), cvvField.textProperty());
    }
}
