package fundsControl.validators;

import com.jfoenix.validation.base.ValidatorBase;
import fundsControl.controllers.LoginController;
import fundsControl.controllers.RegisterController;
import javafx.beans.DefaultProperty;
import javafx.scene.control.TextInputControl;

@DefaultProperty("icon")
public class LoginEmailFieldValidator extends ValidatorBase{
    @Override
    protected void eval() {
        this.evalTextInputField();
    }

    private void evalTextInputField() {
        TextInputControl textField = (TextInputControl)this.srcControl.get();
        if (InputsValidator.validateEmail(textField.getText())) {
            this.hasErrors.set(false);
            LoginController.isEmailValid = true;
        } else {
            this.hasErrors.set(true);
            LoginController.isEmailValid = false;
        }
    }
}
