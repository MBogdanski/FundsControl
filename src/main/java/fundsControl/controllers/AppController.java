package fundsControl.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import fundsControl.models.Transactions;
import fundsControl.models.User;
import fundsControl.utils.HibernateUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.hibernate.Session;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.util.Set;

public class AppController implements Initializable {

    @FXML
    public ScrollPane scrollPane;

    @FXML
    public AnchorPane mainAnchor;

    private User user;

    @FXML
    public Button loadTrxBtn;

    public void loadTrx(ActionEvent actionEvent) {

        Session session = HibernateUtil.openSession();

        Set<Transactions> transactionsSet = this.user.getTransactionsSet();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        Label trxDescription = new Label("Description");
        Label trxCategoryName = new Label("Category");
        Label trxAmount = new Label("Amount");
        Label trxBalance = new Label("Balance before transactions");
        Label trxDate = new Label("Transaction date");

        trxDescription.setFont(new Font("Arial", 15));
        trxCategoryName.setFont(new Font("Arial", 15));
        trxAmount.setFont(new Font("Arial", 15));
        trxBalance.setFont(new Font("Arial", 15));
        trxDate.setFont(new Font("Arial", 15));
        gridPane.add(trxDescription, 0, 0);
        gridPane.add(trxCategoryName, 1, 0);
        gridPane.add(trxAmount, 2, 0);
        gridPane.add(trxBalance, 3, 0);
        gridPane.add(trxDate, 4, 0);

        Separator separator = new Separator();
        separator.setOrientation(Orientation.HORIZONTAL);

        gridPane.add(separator, 0,1,7,1);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DecimalFormat df = new DecimalFormat("#,###.00");
        DecimalFormat df2 = new DecimalFormat("");
        int i = 2;
        for (Transactions transaction : transactionsSet) {
            Label description = new Label(transaction.getDescription());
            Label categoryName = new Label(transaction.getTransactionsCategories().getName());
            Label amount = new Label
                                (String.valueOf(
                                    df.format(
                                        transaction.getAmount())));
            Label balanceDiff = new Label(
                                        String.valueOf(
                                            df2.format(
                                                transaction.getBalanceDiff())));
            Label date = new Label(
                                String.valueOf(
                                    simpleDateFormat.format(
                                        transaction.getTransactionDate())));
            gridPane.add(description, 0, i, 1, 1);
            gridPane.add(categoryName, 1, i, 1, 1);
            gridPane.add(amount, 2, i, 1, 1);
            gridPane.add(balanceDiff, 3, i, 1, 1);
            gridPane.add(date, 4, i, 1, 1);
            ++i;
        }
        vbox.getChildren().addAll(gridPane);

        scrollPane.setContent(vbox);
        session.close();
    }


    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setUser(User user) {
        this.user = user;
    }

    public void openAddNewTrxWindow(ActionEvent actionEvent) {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("addNewTrx.fxml"));
        try {
            Parent root = fxmlLoader.load();
            AddNewTrxController addNewTrxController = fxmlLoader.getController();
            addNewTrxController.setUser(this.user);

            Stage newTrx = new Stage();
            newTrx.setScene(new Scene(root));
            newTrx.setTitle("Add new transaction");
            newTrx.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
