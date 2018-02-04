package fundsControl.validators;

import com.jfoenix.validation.base.ValidatorBase;
import fundsControl.controllers.LoginController;
import fundsControl.controllers.RegisterController;
import javafx.scene.control.TextInputControl;

public class EmptyPasswordValidator extends ValidatorBase{
    @Override
    protected void eval() {
        this.evalTextInputField();
    }

    private void evalTextInputField() {
        TextInputControl textField = (TextInputControl)this.srcControl.get();
        if (!textField.getText().isEmpty()) {
            this.hasErrors.set(false);
            LoginController.isPasswordValid = true;
        } else {
            this.hasErrors.set(true);
            LoginController.isPasswordValid = false;
        }
    }
}
