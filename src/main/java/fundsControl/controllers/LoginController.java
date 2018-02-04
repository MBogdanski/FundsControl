package fundsControl.controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import fundsControl.models.User;
import fundsControl.utils.HibernateUtil;
import fundsControl.validators.EmailFieldValidator;
import fundsControl.validators.EmptyPasswordValidator;
import fundsControl.validators.LoginEmailFieldValidator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.commons.lang.StringUtils;
import org.controlsfx.control.Notifications;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.mindrot.jbcrypt.BCrypt;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class LoginController implements Initializable {

    public static User user;

    @FXML
    public JFXTextField emailTextField;

    @FXML
    public JFXPasswordField password;

    @FXML
    public Button register;

    @FXML
    public Button login;

    public static boolean isEmailValid;
    public static boolean isPasswordValid;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.login.setDisable(true);
        setLoginInputsValidators(emailTextField, password);
    }

    public void login(ActionEvent actionEvent) {
        if (authenticateUser()) {
            showMainAppScene();
        }
    }

    private boolean authenticateUser() {
            String emailApp = this.emailTextField.getText();
            String passwordApp = this.password.getText();

            Session session = HibernateUtil.openSession();
            session.getTransaction().begin();

            Query query = session.createQuery("from User where email = '" + emailApp + "'");
            User user = (User) query.uniqueResult();
            session.close();
            if (user.getEmail().equals(emailApp) && BCrypt.checkpw(passwordApp, user.getPassword())) {
                LoginController.user = user;
                showLoginSuccessNotification();
                return true;
            } else {
                showLoginFailNotification();
                return false;
            }
    }

    public void register(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxmlFiles/register.fxml"));
        try {
            Parent root = fxmlLoader.load();
            Stage mainStage = new Stage();
            mainStage.setScene(new Scene(root));
            mainStage.setTitle("FundsControl - Sign Up");
            mainStage.show();
            Stage stage = (Stage) this.login.getScene().getWindow();
            stage.hide();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorNotification("Loading register window ended with error, try again later");
        }
    }



    private void showMainAppScene() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxmlFiles/mainApp.fxml"));
        try {
            Parent root = fxmlLoader.load();
            Stage mainStage = new Stage();
            mainStage.setScene(new Scene(root));
            mainStage.setTitle("FundsControl");
            mainStage.show();
            Stage stage = (Stage) this.login.getScene().getWindow();
            stage.close();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorNotification("Loading application main window ended with error, try again later");
        }
    }

    private void showErrorNotification(String text) {
        Notifications notificationsBuilder = Notifications.create()
                .title("Error occured")
                .text(text)
                .graphic(getErrorIcon())
                .hideAfter(Duration.seconds(5))
                .position(Pos.CENTER);
        notificationsBuilder.showError();
    }

    private void showLoginFailNotification() {
        Notifications notificationsBuilder = Notifications.create()
                .title("Wrong credentials")
                .text("Provided email or password are invalid, try again")
                .graphic(getErrorIcon())
                .hideAfter(Duration.seconds(5))
                .position(Pos.CENTER);
        notificationsBuilder.showError();
    }

    private void showLoginSuccessNotification() {
        Notifications notificationsBuilder = Notifications.create()
                .title("Login success")
                .text("Loading appliaction main window")
                .graphic(getSuccessIcon())
                .hideAfter(Duration.seconds(5))
                .position(Pos.BOTTOM_RIGHT);
        notificationsBuilder.show();
    }

    private FontAwesomeIconView getErrorIcon() {
        FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.CLOSE);
        icon.setStyle("-fx-background-color: red");
        return icon;
    }

    private FontAwesomeIconView getSuccessIcon() {
        FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.CHECK);
        icon.setStyle("-fx-background-color: green");
        return icon;
    }

    private void setLoginInputsValidators(JFXTextField emailTextField, JFXPasswordField password) {
        setEmailFieldValidator(emailTextField);
        setPasswordFieldValidator(password);
    }

    private void setEmailFieldValidator(JFXTextField textField) {
        LoginEmailFieldValidator loginEmailFieldValidator = new LoginEmailFieldValidator();
        loginEmailFieldValidator.setMessage("Wrong email format");

        loginEmailFieldValidator.setIcon(getErrorIcon());
        textField.getValidators().add(loginEmailFieldValidator);

        textField.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue) {
                textField.validate();
                enableSignInButton();
            }
        });
        textField.setOnKeyTyped(keyEvent -> textField.validate());
        textField.setOnKeyTyped(keyEvent -> enableSignInButton());
    }

    private void setPasswordFieldValidator(JFXPasswordField passwordField) {
        EmptyPasswordValidator emptyPasswordValidator = new EmptyPasswordValidator();
        emptyPasswordValidator.setMessage("Field can't be empty");

        emptyPasswordValidator.setIcon(getErrorIcon());
        passwordField.getValidators().add(emptyPasswordValidator);

        passwordField.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue) {
                passwordField.validate();
                enableSignInButton();
            }
        });
        passwordField.textProperty().addListener((observableValue, oldValue, newValue) -> passwordField.validate());
        passwordField.textProperty().addListener((observableValue, oldValue, newValue) -> enableSignInButton());
    }

    private void enableSignInButton(){
        if (isEmailValid && isPasswordValid) {
            this.login.setDisable(false);
        } else {
            this.login.setDisable(true);
        }
    }
}
