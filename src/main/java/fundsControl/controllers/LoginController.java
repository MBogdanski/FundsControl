package fundsControl.controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import fundsControl.models.Transactions;
import fundsControl.models.User;
import fundsControl.utils.HibernateUtil;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.IOException;


public class LoginController {

    public User user;

    @FXML
    public JFXTextField emailTextField;

    @FXML
    public JFXPasswordField password;

    @FXML
    public Button register;

    private ObservableList<Transactions> transactionsObservableList;

    public void initialize() {

    }

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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("mainApp.fxml"));
        try {
            Parent root = fxmlLoader.load();
            AppController appController = fxmlLoader.getController();
            appController.setUser(this.user);

            Stage mainStage = new Stage();
            mainStage.setScene(new Scene(root));
            mainStage.setTitle("FundsControl");
            mainStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
