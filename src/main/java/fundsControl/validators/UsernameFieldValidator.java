package fundsControl.validators;

import com.jfoenix.validation.base.ValidatorBase;
import fundsControl.controllers.RegisterController;
import javafx.beans.DefaultProperty;
import javafx.scene.control.TextInputControl;

@DefaultProperty("icon")
public class UsernameFieldValidator extends ValidatorBase{
    @Override
    protected void eval() {
        this.evalTextInputField();
    }

    private void evalTextInputField() {
        TextInputControl textField = (TextInputControl)this.srcControl.get();
        if (textField.getText() != null && !textField.getText().isEmpty()) {
            this.hasErrors.set(false);
            RegisterController.isUsernameFieldValid = true;
        } else {
            this.hasErrors.set(true);
            RegisterController.isUsernameFieldValid = false;
        }
    }
}
