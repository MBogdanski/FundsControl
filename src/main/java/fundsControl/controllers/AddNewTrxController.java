package fundsControl.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import fundsControl.models.Transactions;
import fundsControl.models.TransactionsCategories;
import fundsControl.models.User;
import fundsControl.utils.HibernateUtil;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import org.hibernate.Session;

import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class AddNewTrxController implements Initializable {

    @FXML
    public JFXComboBox addTrxCategory2;

    @FXML
    public JFXComboBox addTrxType2;

    private User user;

    @FXML
    public JFXDatePicker addTrxDate;

    @FXML
    public ChoiceBox addTrxType;

    @FXML
    public JFXTextField addTrxDesc;

    @FXML
    public ChoiceBox addTrxCategory;

    @FXML
    public JFXTextField addTrxAmount;

    private List<TransactionsCategories> transactionsCategories;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        addTrxType.setItems(FXCollections.observableArrayList("Income","Outgoing"));
        addTrxType2.setItems(FXCollections.observableArrayList("Income","Outgoing"));
        addTrxCategory.setItems(FXCollections.observableArrayList(getCategoriesNames()));
        addTrxCategory2.setItems(FXCollections.observableArrayList(getCategoriesNames()));


    }

    public void setUser(User user) {
        this.user = user;
    }

    public void addNewTrx(ActionEvent actionEvent) {

        if(validateInputs()){
            LocalDate localDate = addTrxDate.getValue();
            BigDecimal newBalance = calculateNewBalance(getLastTransactionBalance(), new BigDecimal(addTrxAmount.getText()),isCredit());
            System.out.println(newBalance + "----------------------------------------------------------------");
            Transactions transaction = new Transactions(
                        this.user,
                        getTrxCategoryId(addTrxCategory.getSelectionModel().getSelectedItem().toString()),
                        new BigDecimal(addTrxAmount.getText()),
                        getLastTransactionBalance(),
                        newBalance,
                        isCredit(),
                        addTrxDesc.getText(),
                        Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
                        );

            Set<Transactions> transactionsSet = new HashSet<>();

            transactionsSet.add(transaction);

            this.user.setTransactionsSet(transactionsSet);
            this.user.setBalance(newBalance);

            Session session = HibernateUtil.openSession();
            session.getTransaction().begin();

            session.saveOrUpdate(this.user);
            session.save(transaction);

            session.getTransaction().commit();
            session.close();
            ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).close();
        }
    }

    private boolean validateInputs() {
        return true;
    }


    private List<TransactionsCategories> getTransactionsCategories() {
        Session session = HibernateUtil.openSession();
        TypedQuery<TransactionsCategories> typedQuery = session.createQuery("SELECT tc from TransactionsCategories tc", TransactionsCategories.class);
        List<TransactionsCategories> trxCategories = typedQuery.getResultList();
        session.close();
        return trxCategories;
    }

    private List<String> getCategoriesNames() {
        this.transactionsCategories = getTransactionsCategories();
        List<String> categoriesNames = new ArrayList<>();
        for (TransactionsCategories trxCat : this.transactionsCategories) {
            categoriesNames.add(trxCat.getName());
        }
        return categoriesNames;
    }

    private TransactionsCategories getTrxCategoryId(String categoryName) {
        for (TransactionsCategories trxCat : this.transactionsCategories) {
            if (trxCat.getName().equals(categoryName)){
                return trxCat;
            }
        }
        return null;
    }

    private boolean isCredit() {
        return addTrxType.getSelectionModel().getSelectedIndex() == 0;
    }

    private BigDecimal getLastTransactionBalance() {
        Set<Transactions> transactionsSet = this.user.getTransactionsSet();
        Transactions transaction = null;
        for (Transactions trx : transactionsSet) {
            transaction = trx;
        }
        if (transaction != null) {
            return transaction.getBalanceDiff();
        } else {
            throw new NullPointerException();
        }
    }

    private BigDecimal calculateNewBalance(BigDecimal oldBalance, BigDecimal amount, boolean isCredit) {
        BigDecimal newBalance;
        if (isCredit) {
            newBalance = oldBalance.add(amount);
        } else {
            newBalance = oldBalance.subtract(amount);
        }
        return newBalance;
    }
}