package fundsControl.controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import fundsControl.models.User;
import fundsControl.utils.HibernateUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class LoginController implements Initializable {

    public User user;

    @FXML
    public JFXTextField emailTextField;

    @FXML
    public JFXPasswordField password;

    @FXML
    public Button register;

    @FXML
    public Button login;


    public void login(ActionEvent actionEvent) {

        if (authenticateUser()) {
            showMainAppScene();
        }
    }

    public boolean authenticateUser() {
        if (verifyCredentials()) {

            String emailApp = this.emailTextField.getText();
            String passwordApp = this.password.getText();

            Session session = HibernateUtil.openSession();
            session.getTransaction().begin();

            Query query = session.createQuery("from User where email = '" + emailApp + "'");
            User user = (User) query.uniqueResult();
            session.close();
            if (user.getEmail().equals(emailApp) && user.getPassword().equals(passwordApp)) {
                this.user = user;
                return true;
            }

        }

        return false;
    }

    public void register(ActionEvent actionEvent) {
    }


    public boolean verifyCredentials() {
        String email = this.emailTextField.getText();
        String password = this.password.getText();
        return !StringUtils.isEmpty(email) && !StringUtils.isEmpty(password);
    }


    public void showMainAppScene() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxmlFiles/mainApp.fxml"));
        try {
            Parent root = fxmlLoader.load();
            AppController appController = fxmlLoader.getController();
            appController.setUser(this.user);

            Stage mainStage = new Stage();
            mainStage.setScene(new Scene(root));
            mainStage.setTitle("FundsControl");
            mainStage.show();
            Stage stage = (Stage) this.login.getScene().getWindow();
            stage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
