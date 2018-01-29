package fundsControl.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import fundsControl.models.User;
import fundsControl.utils.HibernateUtil;
import fundsControl.validators.InputsValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.query.Query;

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

    private InputsValidator inputsValidator;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inputsValidator = new InputsValidator();

        setEmailFieldValidators(emailField);
        setPasswordFieldValidators(passwordField);
        setConfPasswordFieldValidator(confPasswordField);
        setUsernameFieldValidator(usernameField);
    }

    public void register(ActionEvent actionEvent) {
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confPasswordField.getText();
        String name = usernameField.getText();

        if (validateInputs(email, password, confirmPassword, name)) {
            if (checkUserExist(emailField.getText())) {
                User user = new User(email, password, name, new BigDecimal("0.00"));
                Session session = HibernateUtil.openSession();
                session.getTransaction().begin();
                session.save(user);
                session.getTransaction().commit();
                session.close();
                openLoginWindow();
            }
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

    private boolean validateInputs(String email, String password, String confirmPassword, String name) {
        return validateEmail(email) && validatePassword(password) && validatePasswords(password, confirmPassword) && validateName(name);
    }

    private boolean validateEmail(String email) {
        return inputsValidator.validateEmail(email);
    }

    private boolean validatePassword(String password) {
        return inputsValidator.validatePassword(password);
    }

    private boolean validatePasswords(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    private boolean validateName(String name) {
        return !name.equals("");
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
    }

    private void setPasswordFieldValidators(JFXPasswordField passwordField) {
        setRequiredFieldValidation(passwordField);
    }

    private void setConfPasswordFieldValidator(JFXPasswordField passwordField) {
        setRequiredFieldValidation(passwordField);
    }

    private void setUsernameFieldValidator(JFXTextField textField) {
        setRequiredFieldValidation(textField);
    }

    private void setRequiredFieldValidation(JFXPasswordField passwordField) {
        passwordField.getValidators().add(getRequiredFieldValidator());
        passwordField.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue) {
                passwordField.validate();
            }
        });
    }

    private void setRequiredFieldValidation(JFXTextField textField) {
        final RequiredFieldValidator[] requiredFieldValidator = {getRequiredFieldValidator()};
        textField.getValidators().add(requiredFieldValidator[0]);
        textField.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue) {
                if (textField.validate()) {
                    System.out.println("validate work");
                    if (validateEmail(textField.getText())) {
                        System.out.println("work");
                    } else {
                        System.out.println("nie work");
                        requiredFieldValidator[0] = getRequiredFieldValidator();

                        textField.getValidators().add(requiredFieldValidator[0]);
                        textField.focusedProperty().addListener((observableValue2, oldValue2, newValue2) -> {
                            textField.getActiveValidator().setMessage("Wrong email format");
                        });
                    }
                }
            }
        });
    }

    private RequiredFieldValidator getRequiredFieldValidator() {
        RequiredFieldValidator requiredFieldValidator = new RequiredFieldValidator();
        requiredFieldValidator.setMessage("Field can't be empty");
        FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.CLOSE);
        icon.setStyle("-fx-background-color: red");
        requiredFieldValidator.setIcon(icon);
        return requiredFieldValidator;
    }
}