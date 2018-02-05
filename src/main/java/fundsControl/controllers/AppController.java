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
import java.time.LocalDate;
import java.time.ZoneId;
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
    public JFXToggleButton applyAmountFilter;
    @FXML
    public JFXToggleButton applyCategoryFilter;
    @FXML
    public JFXToggleButton applyDateFilter;
    @FXML
    public JFXComboBox categoriesFilterComboBox;
    @FXML
    public JFXTextField filterFromAmountField;
    @FXML
    public JFXTextField filterToAmountField;
    @FXML
    public JFXDatePicker filterToDate;
    @FXML
    public JFXDatePicker filterFromDate;
    @FXML
    public Label outgoingsAmountLabel;
    @FXML
    public Label incomingsAmountLabel;
    @FXML
    public Label outgoinsAmountNumberLabel;
    @FXML
    public Label incomingsAmountNumberLabel;

    private boolean isAmountFilterOn = false;
    private boolean isCategoryFilterOn = false;
    private boolean isDateFilterOn = false;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        user = LoginController.user;
        loadTrx(user.getTransactionsSet());
        this.loadTrxBtn.setOnAction(actionEvent -> loadTrx(user.getTransactionsSet()));
        setUserData();
        categoriesFilterComboBox.setItems(FXCollections.observableArrayList(getCategoriesNames()));
        applyFilters();
    }

    private void loadTrx(Set<Transactions> transactionsSet) {

        Session session = HibernateUtil.openSession();
        session.refresh(user);

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
        DecimalFormat df2 = new DecimalFormat(".00");
        BigDecimal outgoingsAmount = new BigDecimal("0");
        BigDecimal incomingsAmount = new BigDecimal("0");
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
            if (transaction.isCredit()) {
                amount.setStyle("-fx-text-fill: green");
                incomingsAmount = incomingsAmount.add(transaction.getAmount());
            } else {
                amount.setStyle("-fx-text-fill: red");
                outgoingsAmount = outgoingsAmount.add(transaction.getAmount());
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
        showTransactionsAmounts(outgoingsAmount, incomingsAmount);
        scrollPane.setContent(vbox);
        session.close();
    }

    private void showTransactionsAmounts(BigDecimal outgoingsAmount, BigDecimal incomingsAmount) {
        DecimalFormat df = new DecimalFormat(".00");
        if (outgoingsAmount.compareTo(BigDecimal.ZERO) == 0) {
            outgoinsAmountNumberLabel.setText("0.00");
        } else {
            outgoinsAmountNumberLabel.setText(String.valueOf(df.format(outgoingsAmount)));
            outgoinsAmountNumberLabel.setStyle("-fx-text-fill: red");
        }

        if (incomingsAmount.compareTo(BigDecimal.ZERO) == 0) {
            incomingsAmountNumberLabel.setText("0.00");
        } else {
            incomingsAmountNumberLabel.setText(String.valueOf(df.format(incomingsAmount)));
            incomingsAmountNumberLabel.setStyle("-fx-text-fill: green");
        }
    }

    private void openEditTrxWindow(Transactions transaction) {
        if (isAmountFilterOn || isDateFilterOn || isCategoryFilterOn) {
            showErrorNotification("Editing transactions is not available in filtered list");
        } else {
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
    }

    private void setUserData() {
        this.userName.setText("Hello " + user.getName() + "!");
        DecimalFormat df = new DecimalFormat(".00");
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
        if (isAmountFilterOn || isDateFilterOn || isCategoryFilterOn) {
            showErrorNotification("Deleting transactions is not available in filtered list");
        } else {
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
    }

    private void refreshData() {
        setUserData();
        loadTrx(user.getTransactionsSet());
    }

    private List<String> getCategoriesNames() {
        Set<TransactionsCategories> transactionsCategoriesSet = user.getTransactionsCategoriesSet();
        List<String> categoriesNames = new ArrayList<>();
        for (TransactionsCategories trxCat : transactionsCategoriesSet) {
            categoriesNames.add(trxCat.getName());
        }
        return categoriesNames;
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


    private void applyFilters() {
        applyCategoryFilter.selectedProperty().addListener((observableValue, toggle, newToggle) -> {
            isCategoryFilterOn = applyCategoryFilter.isSelected();
            loadTrx(getFilteredTrxSet(user.getTransactionsSet()));
        });

        applyDateFilter.selectedProperty().addListener((observableValue, toggle, newToggle) -> {
            isDateFilterOn = applyDateFilter.isSelected();
            loadTrx(getFilteredTrxSet(user.getTransactionsSet()));
        });

        applyAmountFilter.selectedProperty().addListener((observableValue, toggle, newToggle) -> {
            isAmountFilterOn = applyAmountFilter.isSelected();
            loadTrx(getFilteredTrxSet(user.getTransactionsSet()));
        });
    }

    private Set<Transactions> getFilteredTrxSet(Set<Transactions> transactionsSet){
        Set<Transactions> trxRemovingList = new HashSet<>();
        if (isCategoryFilterOn) {
            for (Transactions trx : transactionsSet) {
                if (!trx.getTransactionsCategories().getName().equals(categoriesFilterComboBox.getSelectionModel().getSelectedItem().toString())){
                    trxRemovingList.add(trx);
                }
            }
            transactionsSet.removeAll(trxRemovingList);
        }

        if (isDateFilterOn) {
            transactionsSet = filterTrxSetByDates(transactionsSet);
        }

        if (isAmountFilterOn) {
            transactionsSet = filterTrxSetByAmounts(transactionsSet);
        }

        return transactionsSet;
    }

    private Set<Transactions> filterTrxSetByAmounts(Set<Transactions> transactionsSet) {
        BigDecimal fromAmount = getAmountFrom();
        BigDecimal toAmount = getToAmount();
        Set<Transactions> trxRemovingList = new HashSet<>();

        if (fromAmount != null && toAmount != null){
            for (Transactions trx : transactionsSet) {
                if (trx.getAmount().compareTo(fromAmount) <= 0 || trx.getAmount().compareTo(toAmount) >= 0) {
                    trxRemovingList.add(trx);
                }
            }
            transactionsSet.removeAll(trxRemovingList);
            return transactionsSet;
        }

        if (fromAmount != null) {
            for (Transactions trx : transactionsSet) {
                if (trx.getAmount().compareTo(fromAmount) < 0) {
                    trxRemovingList.add(trx);
                }
            }
            transactionsSet.removeAll(trxRemovingList);
            return transactionsSet;
        }

        if (toAmount != null) {
            for (Transactions trx : transactionsSet) {
                if (trx.getAmount().compareTo(toAmount) > 0) {
                    trxRemovingList.add(trx);
                }
            }
            transactionsSet.removeAll(trxRemovingList);
            return transactionsSet;
        }

        return transactionsSet;
    }

    private BigDecimal getAmountFrom(){
        if (!this.filterFromAmountField.getText().isEmpty()){
            try {
                return new BigDecimal(this.filterFromAmountField.getText());
            } catch (NumberFormatException e){
                e.getMessage();
                showErrorNotification("Wrong From Amount format");
            }
        }
        return null;
    }

    private BigDecimal getToAmount() {
        if (!this.filterToAmountField.getText().isEmpty()){
            try {
                return new BigDecimal(this.filterToAmountField.getText());
            } catch (NumberFormatException e){
                e.getMessage();
                showErrorNotification("Wrong To Amount format");
            }
        }
        return null;
    }

    private Set<Transactions> filterTrxSetByDates(Set<Transactions> transactionsSet) {
        LocalDate fromDate = filterFromDate.getValue();
        LocalDate toDate = filterToDate.getValue();
        Set<Transactions> trxRemovingList = new HashSet<>();

        if (fromDate != null && toDate != null) {
            for (Transactions trx : transactionsSet) {
                if (!trx.getTransactionDate().after(Date.from(fromDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                        || !trx.getTransactionDate().before(Date.from(toDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))) {
                            trxRemovingList.add(trx);
                        }
            }
            transactionsSet.removeAll(trxRemovingList);
            return transactionsSet;
        }

        if (fromDate != null) {
            for (Transactions trx : transactionsSet) {
                if (trx.getTransactionDate().before(Date.from(fromDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))){
                    trxRemovingList.add(trx);
                }
            }
            transactionsSet.removeAll(trxRemovingList);
            return transactionsSet;
        }

        if (toDate != null) {
            for (Transactions trx : transactionsSet) {
                if (trx.getTransactionDate().after(Date.from(toDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))){
                    trxRemovingList.add(trx);
                }
            }
            transactionsSet.removeAll(trxRemovingList);
            return transactionsSet;
        }
        return transactionsSet;
    }
}
