package fundsControl.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import fundsControl.models.TransactionsCategories;
import fundsControl.models.User;
import fundsControl.utils.HibernateUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.hibernate.Session;

import java.net.URL;
import java.util.ResourceBundle;

public class AddCategoryController implements Initializable{

    @FXML
    public JFXTextField categoryName;

    @FXML
    public JFXButton addCategoryBtn;

    private User user;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.user = AppController.user;
    }

    public void addNewCategory(ActionEvent actionEvent) {
        Session session = HibernateUtil.openSession();
        session.getTransaction().begin();
        TransactionsCategories transactionsCategory = new TransactionsCategories(this.user, this.categoryName.getText());
        session.save(transactionsCategory);
        session.getTransaction().commit();
        session.close();
        ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).close();
    }
}
