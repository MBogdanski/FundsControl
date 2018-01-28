package fundsControl.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import fundsControl.models.User;
import fundsControl.utils.HibernateUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.apache.commons.validator.routines.EmailValidator;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable{
    @FXML
    public JFXTextField emailField;
    @FXML
    public JFXTextField passwordField;
    @FXML
    public JFXTextField confPasswordField;
    @FXML
    public JFXTextField usernameField;
    @FXML
    public JFXButton singUpBtn;
    @FXML
    public JFXButton cancelBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void register(ActionEvent actionEvent) {

        if (checkUserExist(emailField.getText())){

        }
        User user = new User(emailField.getText(), passwordField.getText(), usernameField.getText(),new BigDecimal("0.00"));
        Session session = HibernateUtil.openSession();
        session.getTransaction().begin();
        session.save(user);
        session.getTransaction().commit();
        session.close();
    }

    private boolean checkUserExist(String email) {
        Session session = HibernateUtil.openSession();
        session.getTransaction().begin();
        Query query = session.createQuery("from User where email = '" + email + "'");
        User user = (User) query.uniqueResult();
        session.close();
        return user != null;
    }

    private void validateInputs(String email, String password, String confirmPassword, String name) {
        validateEmail(email);
    }

    private boolean validateEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    private boolean validatePassword() {

    }

    public void cancelRegister(ActionEvent actionEvent) {
        ((Button)actionEvent.getSource()).getScene().getWindow().hide();
    }
}
