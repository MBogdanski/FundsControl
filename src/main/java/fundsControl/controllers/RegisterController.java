package fundsControl.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import fundsControl.models.User;
import fundsControl.utils.HibernateUtil;
import fundsControl.validators.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {
    @FXML
    public JFXTextField emailField;
    @FXML
    public JFXPasswordField passwordField;
    @FXML
    public JFXPasswordField confPasswordField;
    @FXML
    public JFXTextField usernameField;
    @FXML
    public JFXButton signUpBtn;
    @FXML
    public JFXButton cancelBtn;

    public static boolean isEmailFieldValid = false;
    public static boolean isPasswordFieldValid = false;
    public static boolean isConfirmPasswordFieldValid = false;
    public static boolean isUsernameFieldValid = false;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        signUpBtn.setDisable(true);
        setEmailFieldValidators(emailField);
        setPasswordFieldValidators(passwordField);
        setConfPasswordFieldValidators(confPasswordField);
        setUsernameFieldValidators(usernameField);
    }

    public void register(ActionEvent actionEvent) {
        String email = emailField.getText();
        String hashedPassword = BCrypt.hashpw(passwordField.getText(),BCrypt.gensalt());
        String name = usernameField.getText();

            if (!checkUserExist(emailField.getText())) {
                User user = new User(email, hashedPassword, name, new BigDecimal("0.00"));
                Session session = HibernateUtil.openSession();
                session.getTransaction().begin();
                session.save(user);
                session.getTransaction().commit();
                session.close();
                openLoginWindow();
            } else {
                showErrorNotification();
            }
    }

    private boolean checkUserExist(String email) {
        Session session = HibernateUtil.openSession();
        session.getTransaction().begin();
        Query query = session.createQuery("from User where email = '" + email + "'");
        User user = (User) query.uniqueResult();
        session.close();
        return user != null;
    }

    public void cancelRegister(ActionEvent actionEvent) {
        openLoginWindow();
    }

    private void openLoginWindow() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxmlFiles/login.fxml"));
        try {
            Parent root = fxmlLoader.load();
            Stage mainStage = new Stage();
            mainStage.setScene(new Scene(root));
            mainStage.setTitle("FundsControl - Sign In");
            mainStage.show();
            Stage stage = (Stage) this.signUpBtn.getScene().getWindow();
            stage.hide();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setEmailFieldValidators(JFXTextField textField) {
        setRequiredFieldValidation(textField);
        setEmailFieldValidator(textField);
    }

    private void setPasswordFieldValidators(JFXPasswordField passwordField) {
        setRequiredFieldValidation(passwordField);
        setPasswordFieldValidator(passwordField);
    }

    private void setConfPasswordFieldValidators(JFXPasswordField passwordField) {
        setRequiredFieldValidation(passwordField);
        setConfPasswordFieldValidator(passwordField, passwordField.getText());
    }

    private void setUsernameFieldValidators(JFXTextField textField) {
        setUsernameFieldValidator(textField);
    }

    private void setRequiredFieldValidation(JFXPasswordField passwordField) {
        passwordField.getValidators().add(getRequiredFieldValidator());
        passwordField.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue) {
                passwordField.validate();
                enableSignUpButton();
            }
        });
    }

    private void setRequiredFieldValidation(JFXTextField textField) {
        final RequiredFieldValidator[] requiredFieldValidator = {getRequiredFieldValidator()};
        textField.getValidators().add(requiredFieldValidator[0]);
        textField.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue) {
                textField.validate();
                enableSignUpButton();
            }
        });
    }

    private void setEmailFieldValidator(JFXTextField textField) {
        EmailFieldValidator emailFieldValidator = new EmailFieldValidator();
        emailFieldValidator.setMessage("Wrong email format");


        emailFieldValidator.setIcon(getErrorIcon());
        textField.getValidators().add(emailFieldValidator);

        textField.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue) {
                textField.validate();
                enableSignUpButton();
            }
        });
    }

    private void setPasswordFieldValidator(JFXPasswordField passwordField) {
        PasswordFieldValidator passwordFieldValidator = new PasswordFieldValidator();
        passwordFieldValidator.setMessage("Password is too weak");


        passwordFieldValidator.setIcon(getErrorIcon());
        passwordField.getValidators().add(passwordFieldValidator);
        passwordField.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue) {
                passwordField.validate();
                enableSignUpButton();
            }
        });
    }

    private void setConfPasswordFieldValidator(JFXPasswordField confPasswordField, String passwordFieldText) {
        ConfirmPasswordFieldValidator confirmPasswordFieldValidator = new ConfirmPasswordFieldValidator(passwordFieldText);
        confirmPasswordFieldValidator.setMessage("Password must be equals");

        confirmPasswordFieldValidator.setIcon(getErrorIcon());
        confPasswordField.getValidators().add(confirmPasswordFieldValidator);
        confPasswordField.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue) {
                confirmPasswordFieldValidator.setPasswordText(passwordField.getText());
                confPasswordField.validate();
                enableSignUpButton();
            }
        });
    }

    private void setUsernameFieldValidator(JFXTextField textField) {
        UsernameFieldValidator usernameFieldValidator = new UsernameFieldValidator();
        usernameFieldValidator.setMessage("Field can't be empty");

        usernameFieldValidator.setIcon(getErrorIcon());
        textField.getValidators().add(usernameFieldValidator);
        textField.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue) {
                textField.validate();
                enableSignUpButton();
            }
        });
        textField.setOnKeyTyped(keyEvent -> textField.validate());
    }

    private RequiredFieldValidator getRequiredFieldValidator() {
        RequiredFieldValidator requiredFieldValidator = new RequiredFieldValidator();
        requiredFieldValidator.setMessage("Field can't be empty");

        requiredFieldValidator.setIcon(getErrorIcon());
        return requiredFieldValidator;
    }

    private FontAwesomeIconView getErrorIcon() {
        FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.CLOSE);
        icon.setStyle("-fx-background-color: red");
        return icon;
    }

    private void enableSignUpButton() {
        if (isEmailFieldValid && isPasswordFieldValid && isConfirmPasswordFieldValid && isUsernameFieldValid) {
            signUpBtn.setDisable(false);
        } else {
            signUpBtn.setDisable(true);
        }
    }

    private void showErrorNotification() {
        Notifications notificationsBuilder = Notifications.create()
                .title("User already exists")
                .text("User with provided email already exists")
                .graphic(getErrorIcon())
                .hideAfter(Duration.seconds(5))
                .position(Pos.CENTER);
        notificationsBuilder.showError();
    }
}