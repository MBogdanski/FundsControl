package fundsControl.validators;

import com.jfoenix.validation.base.ValidatorBase;
import fundsControl.controllers.RegisterController;
import javafx.beans.DefaultProperty;
import javafx.scene.control.TextInputControl;

@DefaultProperty("icon")
public class PasswordFieldValidator extends ValidatorBase{
    @Override
    protected void eval() {
        this.evalTextInputField();
    }

    private void evalTextInputField() {
        TextInputControl textField = (TextInputControl)this.srcControl.get();
        if (InputsValidator.validatePassword(textField.getText())) {
            this.hasErrors.set(false);
            RegisterController.isPasswordFieldValid = true;
        } else {
            this.hasErrors.set(true);
            RegisterController.isPasswordFieldValid = false;
        }
    }
}
