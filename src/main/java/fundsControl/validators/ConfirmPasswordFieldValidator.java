package fundsControl.validators;

import com.jfoenix.validation.base.ValidatorBase;
import fundsControl.controllers.RegisterController;
import javafx.beans.DefaultProperty;
import javafx.scene.control.TextInputControl;

@DefaultProperty("icon")
public class ConfirmPasswordFieldValidator extends ValidatorBase{

    public void setPasswordText(String passwordText) {
        this.passwordText = passwordText;
    }

    private String passwordText;

    public ConfirmPasswordFieldValidator(String passwordText) {
        this.passwordText = passwordText;
    }


    @Override
    protected void eval() {
        this.evalTextInputField();
    }

    private void evalTextInputField() {
        TextInputControl textField = (TextInputControl)this.srcControl.get();
        if (textField.getText().equals(this.passwordText)) {
            this.hasErrors.set(false);
            RegisterController.isConfirmPasswordFieldValid = true;
        } else {
            this.hasErrors.set(true);
            RegisterController.isConfirmPasswordFieldValid = false;
        }
    }


}
