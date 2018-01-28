package fundsControl.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import fundsControl.models.Transactions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class EditTrxController implements Initializable{

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

    private Transactions transaction;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.transaction = AppController.editTransaction;
        this.editTrxDesc.setText(transaction.getDescription());
        this.editTrxAmount.setText(String.valueOf(transaction.getAmount()));
    }

    public void closeEdit(ActionEvent actionEvent) {
        ((Button)actionEvent.getSource()).getScene().getWindow().hide();
    }

    public void editTrx(ActionEvent actionEvent) {
    }
}
