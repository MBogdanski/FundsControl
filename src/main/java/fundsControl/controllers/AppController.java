package fundsControl.controllers;

import com.jfoenix.controls.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import fundsControl.models.Transactions;
import fundsControl.models.TransactionsCategories;
import fundsControl.models.User;
import fundsControl.utils.HibernateUtil;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.hibernate.Session;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class AppController implements Initializable {

    public static User user;
    public static Transactions editTransaction;
    @FXML
    public ScrollPane scrollPane;
    @FXML
    public AnchorPane mainAnchor;
    @FXML
    public JFXButton addTrx;
    @FXML
    public JFXButton addCategoryBtn;
    @FXML
    public Button loadTrxBtn;
    @FXML
    public Label userName;
    @FXML
    public Label userBalance;
    @FXML
    public JFXComboBox categoriesFilterComboBox;
    @FXML
    public JFXToggleButton applyAmountFilter;
    @FXML
    public JFXTextField filterFromAmountField;
    @FXML
    public JFXDatePicker filterDate;
    @FXML
    public JFXToggleButton applyCategoryFilter;
    @FXML
    public JFXToggleButton applyDateFilter;
    @FXML
    public JFXTextField filterToAmountField;

    private Set<TransactionsCategories> transactionsCategoriesSet;

    public void loadTrx() {

        Session session = HibernateUtil.openSession();
        session.refresh(user);
        Set<Transactions> transactionsSet = user.getTransactionsSet();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        Label trxDescription = new Label("Description");
        trxDescription.setMinWidth(100);
        trxDescription.setTextAlignment(TextAlignment.CENTER);
        Label trxCategoryName = new Label("Category");
        trxCategoryName.setMinWidth(100);
        trxCategoryName.setTextAlignment(TextAlignment.CENTER);
        Label trxAmount = new Label("Amount");
        trxAmount.setMinWidth(100);
        trxAmount.setTextAlignment(TextAlignment.CENTER);
        Label trxBalance = new Label("Balance after transactions");
        trxBalance.setMinWidth(100);
        trxBalance.setTextAlignment(TextAlignment.CENTER);
        Label trxDate = new Label("Transaction date");
        trxDate.setMinWidth(100);
        trxDate.setTextAlignment(TextAlignment.CENTER);


//        trxDescription.setFont(new Font("Arial", 15));
//        trxCategoryName.setFont(new Font("Arial", 15));
//        trxAmount.setFont(new Font("Arial", 15));
//        trxBalance.setFont(new Font("Arial", 15));
//        trxDate.setFont(new Font("Arial", 15));
        gridPane.add(trxDescription, 0, 0);
        gridPane.add(trxCategoryName, 1, 0);
        gridPane.add(trxAmount, 2, 0);
        gridPane.add(trxBalance, 3, 0);
        gridPane.add(trxDate, 4, 0);

        Separator separator = new Separator();
        separator.setOrientation(Orientation.HORIZONTAL);

        gridPane.add(separator, 0, 1, 7, 1);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DecimalFormat df = new DecimalFormat("#,###.00");
        DecimalFormat df2 = new DecimalFormat("");
        int i = 2;
        for (Transactions transaction : transactionsSet) {
            Label description = new Label(transaction.getDescription());
            description.setTextAlignment(TextAlignment.CENTER);
            Label categoryName = new Label(transaction.getTransactionsCategories().getName());
            categoryName.setTextAlignment(TextAlignment.CENTER);
            Label amount = new Label
                    (String.valueOf(
                            df.format(
                                    transaction.getAmount())));
            if (transaction.isCredit()){
                amount.setStyle("-fx-text-fill: green");
            } else {
                amount.setStyle("-fx-text-fill: red");
            }
            amount.setTextAlignment(TextAlignment.CENTER);
            Label balanceDiff = new Label(
                    String.valueOf(
                            df2.format(
                                    transaction.getBalanceDiff())));
            balanceDiff.setTextAlignment(TextAlignment.CENTER);
            Label date = new Label(
                    String.valueOf(
                            simpleDateFormat.format(
                                    transaction.getTransactionDate())));
            date.setTextAlignment(TextAlignment.CENTER);
            Button deleteBtn = new Button("Delete");
            deleteBtn.setMinWidth(100);
            deleteBtn.setOnAction(actionEvent -> deleteTrx(transaction, getTransactionsToCalculateBalance(transaction, user.getTransactionsSet())));
            Button editBtn = new Button("Edit");
            editBtn.setMinWidth(100);
            editBtn.setOnAction(actionEvent -> openEditTrxWindow(transaction));
            gridPane.add(description, 0, i, 1, 1);
            gridPane.add(categoryName, 1, i, 1, 1);
            gridPane.add(amount, 2, i, 1, 1);
            gridPane.add(balanceDiff, 3, i, 1, 1);
            gridPane.add(date, 4, i, 1, 1);
            gridPane.add(editBtn, 5, i, 1, 1);
            gridPane.add(deleteBtn, 6, i, 1, 1);
            ++i;
        }
        gridPane.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(gridPane);

        scrollPane.setContent(vbox);
        session.close();
    }

    private void openEditTrxWindow(Transactions transaction) {
        editTransaction = transaction;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxmlFiles/editTrx.fxml"));
        try {
            Parent root = fxmlLoader.load();
            Stage newTrx = new Stage();
            newTrx.setScene(new Scene(root));
            newTrx.setTitle("Edit transaction");
            newTrx.setOnHiding(windowEvent -> refreshData());
            newTrx.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void initialize(URL url, ResourceBundle resourceBundle) {
        user = LoginController.user;
        loadTrx();
        setUserData();
        categoriesFilterComboBox.setItems(FXCollections.observableArrayList(getCategoriesNames()));
    }

    public void setUserData() {
        this.userName.setText("Hello " + user.getName() + "!");
        DecimalFormat df = new DecimalFormat("0,000.00");
        this.userBalance.setText(String.valueOf(df.format(user.getBalance())));
        if (user.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            userBalance.setStyle("-fx-text-fill: red");
        } else {
            userBalance.setStyle("-fx-text-fill: green");
        }
    }

    public void openAddNewTrxWindow(ActionEvent actionEvent) {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxmlFiles/addNewTrx.fxml"));
        try {
            Parent root = fxmlLoader.load();
            Stage newTrx = new Stage();
            newTrx.setScene(new Scene(root));
            newTrx.setTitle("Add new transaction");
            newTrx.setOnHiding(windowEvent -> refreshData());
            newTrx.show();

        } catch (IOException e) {
            e.printStackTrace();
            showErrorNotification("Loading edit transaction window end with error, try again later");
        }
    }

    public void openAddNewCategoryWindow(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxmlFiles/addNewCategory.fxml"));
        try {
            Parent root = fxmlLoader.load();
            Stage newTrx = new Stage();
            newTrx.setScene(new Scene(root));
            newTrx.setTitle("Add new category");
            newTrx.setResizable(false);
            newTrx.show();

        } catch (IOException e) {
            e.printStackTrace();
            showErrorNotification("Loading adding category window end with error, try again later");
        }
    }

     Set<Transactions> getTransactionsToCalculateBalance(Transactions transaction, Set<Transactions> transactionsSet) {
        Set<Transactions> newlyCalculatedTrxSet = new HashSet<>();
        for (Transactions trx : transactionsSet) {
            if (trx.getId() > transaction.getId()) {
                newlyCalculatedTrxSet.add(trx);
            }
        }
        return newlyCalculatedTrxSet;
    }

     private List<Transactions> sortTrxList(Set<Transactions> transactionsSet) {
        List<Transactions> trxList = new ArrayList<>(transactionsSet);
        trxList.sort(Comparator.comparing(Transactions::getId));
        return trxList;
    }

     void deleteTrx(Transactions transaction, Set<Transactions> newlyCalculatedTrxSet) {
        Session session = HibernateUtil.openSession();
        session.getTransaction().begin();
        List<Transactions> trxList;
        trxList = sortTrxList(newlyCalculatedTrxSet);
        BigDecimal balanceBefore = transaction.getBalanceBefore();
        BigDecimal userBalance = null;
        for (Transactions trx : trxList) {
            trx.setBalanceBefore(balanceBefore);
            if (trx.isCredit()) {
                trx.setBalanceDiff(trx.getBalanceBefore().add(trx.getAmount()));
            } else {
                trx.setBalanceDiff(trx.getBalanceBefore().subtract(trx.getAmount()));
            }
            balanceBefore = trx.getBalanceDiff();
            userBalance = trx.getBalanceDiff();
            session.update(trx);
        }
        user.setBalance(userBalance);
        session.update(user);
        session.delete(transaction);
        session.getTransaction().commit();
        session.refresh(user);
        session.close();
        refreshData();
    }

     private void refreshData() {
        setUserData();
        loadTrx();
    }

    private List<String> getCategoriesNames() {
        this.transactionsCategoriesSet = user.getTransactionsCategoriesSet();
        List<String> categoriesNames = new ArrayList<>();
        for (TransactionsCategories trxCat : this.transactionsCategoriesSet) {
            categoriesNames.add(trxCat.getName());
        }
        return categoriesNames;
    }

    public void applyFilters() {

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
    private FontAwesomeIconView getErrorIcon() {
        FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.CLOSE);
        icon.setStyle("-fx-background-color: red");
        return icon;
    }
}
