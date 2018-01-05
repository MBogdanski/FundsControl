package fundsControl.controllers;

import com.jfoenix.controls.JFXTextField;
import fundsControl.models.Transactions;
import fundsControl.models.TransactionsCategories;
import fundsControl.models.User;
import fundsControl.utils.HibernateUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MainController {

    @FXML
    public JFXTextField emailTextField;

    @FXML
    public JFXTextField password;

    public void login(ActionEvent actionEvent) {
        String emailApp = this.emailTextField.getText();
        String password = this.password.getText();
        Session session = HibernateUtil.openSession();
        session.getTransaction().begin();

        Query query = session.createQuery("from User where email = '" + emailApp + "'");
        User user = (User)query.uniqueResult();
        /*
        <----- Testing models ----->
         */

//
//        Query query1 = session.createQuery("from TransactionsCategories where name = 'Rachunki'");
//        TransactionsCategories transactionsCategories = (TransactionsCategories)query1.uniqueResult();
//
//
//        Transactions transactions = new Transactions(user, transactionsCategories, new BigDecimal("275.81"), false, "Za gaz - grudzień", new Date());
//        Transactions transactions1 = new Transactions(user, transactionsCategories, new BigDecimal("1333.21"), true, "wypłata - grudzień", new Date());
//        Set<Transactions> transactionsSet = new HashSet<Transactions>();
//        transactionsSet.add(transactions);
//        transactionsSet.add(transactions1);
//
//        user.setTransactionsSet(transactionsSet);
//
//        System.out.println(transactions.getDescription());
//
//        session.save(user);
//        session.save(transactions);
//        session.save(transactions1);

        session.getTransaction().commit();

        session.close();
        if(user!=null && user.getEmail().equals(emailApp) && user.getPassword().equals(password)){
            // TO-DO Open new window with Main App
        }


    }

//    public List<User> read() {
//        Session session = HibernateUtil.openSession();
//        List<User> data = session.createCriteria(User.class).list();
//        session.close();
//        return data;
//    }

}
