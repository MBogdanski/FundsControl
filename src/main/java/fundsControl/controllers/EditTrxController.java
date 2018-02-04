package fundsControl.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import fundsControl.models.Transactions;
import fundsControl.models.User;
import fundsControl.utils.HibernateUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.hibernate.Session;

import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;

public class EditTrxController implements Initializable {

    @FXML
    public JFXTextField editTrxDesc;

    @FXML
    public JFXTextField editTrxAmount;

    @FXML
    public JFXDatePicker editTrxDate;

    @FXML
    public JFXButton editSaveBtn;

    @FXML
    public JFXButton editCancelBtn;

    private User user;

    private Transactions transaction;

    private Set<Transactions> transactionsSet;

    private List<Transactions> sortedTrxList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.user = AppController.user;
        this.transaction = AppController.editTransaction;
        transactionsSet = this.user.getTransactionsSet();
        this.editTrxDesc.setText(transaction.getDescription());
        DecimalFormat df = new DecimalFormat("#,###.00");
        this.editTrxAmount.setText(String.valueOf(df.format(transaction.getAmount())));
        LocalDate localDate = LocalDate.parse(transaction.getTransactionDate().toString());
        this.editTrxDate.setValue(localDate);
        setEditTrxAmountValidators(this.editTrxAmount);
        setEditTrxDescValidators(this.editTrxDesc);
    }

    public void closeEdit(ActionEvent actionEvent) {
        ((Button) actionEvent.getSource()).getScene().getWindow().hide();
    }

    public void editTrx(ActionEvent actionEvent) {
        if (validateInputs()) {
            Session session = HibernateUtil.openSession();
            session.getTransaction().begin();
            BigDecimal newAmount = new BigDecimal(this.editTrxAmount.getText());
            transaction.setAmount(newAmount);
            if (transaction.isCredit()) {
                transaction.setBalanceDiff(transaction.getBalanceBefore().add(newAmount));
            } else {
                transaction.setBalanceDiff(transaction.getBalanceBefore().subtract(newAmount));
            }
            sortedTrxList = sortTrxList(getTransactionsToUpdateBalance(this.transaction, this.transactionsSet));

            BigDecimal newUserBalance = null;
            BigDecimal balanceBefore = transaction.getBalanceDiff();
            for (Transactions trx : sortedTrxList) {
                trx.setBalanceBefore(balanceBefore);
                if (trx.isCredit()) {
                    trx.setBalanceDiff(trx.getBalanceBefore().add(trx.getAmount()));
                } else {
                    trx.setBalanceDiff(trx.getBalanceBefore().subtract(trx.getAmount()));
                }
                balanceBefore = trx.getBalanceDiff();
                newUserBalance = trx.getBalanceDiff();
                session.update(trx);
            }
            user.setBalance(newUserBalance);
            session.update(user);
            session.update(transaction);
            session.getTransaction().commit();
            session.close();
            ((Button) actionEvent.getSource()).getScene().getWindow().hide();
            showSuccessNotification();
        }
    }


    private void setEditTrxDescValidators(JFXTextField textField) {
        setRequiredFieldValidation(textField);
    }

    private void setEditTrxAmountValidators(JFXTextField textField) {
        setRequiredFieldValidation(textField);
    }

    private void setRequiredFieldValidation(JFXTextField textField) {
        final RequiredFieldValidator[] requiredFieldValidator = {getRequiredFieldValidator()};
        textField.getValidators().add(requiredFieldValidator[0]);
        textField.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue) {
                textField.validate();
            }
        });
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

    private boolean validateInputs() {
        return validateDesc() && validateAmount() && validateDate();
    }

    private boolean validateDate() {
        if (this.editTrxDate.getValue() != null) {
            return true;
        } else {
            showErrorNotification("Empty datefield");
            return false;
        }
    }

    private boolean validateAmount() {
        try {
            BigDecimal newAmount = new BigDecimal(this.editTrxAmount.getText());
        } catch (NumberFormatException e) {
            e.getMessage();
            showErrorNotification("Wrong amount format or empty amount field");
            return false;
        }
        return true;
    }

    private boolean validateDesc() {
        if (!this.editTrxDesc.getText().isEmpty()) {

            return true;
        } else {
            showErrorNotification("Empty description field");
            return false;
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

    private void showSuccessNotification(){
        Notifications notificationsBuilder = Notifications.create()
                .title("Edit transaction")
                .text("Transaction edited successfully")
                .graphic(getSuccessIcon())
                .hideAfter(Duration.seconds(5))
                .position(Pos.BOTTOM_RIGHT);
        notificationsBuilder.showError();
    }
    private FontAwesomeIconView getSuccessIcon() {
        FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.CHECK);
        icon.setStyle("-fx-background-color: green");
        return icon;
    }

    private Set<Transactions> getTransactionsToUpdateBalance(Transactions transaction, Set<Transactions> transactionsSet) {
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
}
