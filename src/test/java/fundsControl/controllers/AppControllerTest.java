package fundsControl.controllers;

import fundsControl.models.Transactions;
import fundsControl.models.TransactionsCategories;
import fundsControl.models.User;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Test
public class AppControllerTest {

    public void testGetTransactionsToCalculateBalance() throws Exception {
        Set<Transactions> newSet;
        newSet = appController.getTransactionsToCalculateBalance(transaction4, user.getTransactionsSet());
        Assert.assertEquals(newSet.size(), 2);
        Assert.assertTrue(newSet.contains(transaction5));
        Assert.assertTrue(newSet.contains(transaction6));
        Assert.assertFalse(newSet.contains(transaction2));
    }

    private User user;
    private TransactionsCategories transactionsCategories;
    private AppController appController;

    Transactions transaction1;
    Transactions transaction2;
    Transactions transaction3;
    Transactions transaction4;
    Transactions transaction5;
    Transactions transaction6;

    @BeforeMethod
    public void setUp() throws Exception {
        this.user = new User("mariusz@test.pl", "password123", "Mariusz", new BigDecimal("100.00"));
        this.transactionsCategories = new TransactionsCategories(this.user, "Test Category");
        this.appController = new AppController();


        transaction1 = new Transactions(this.user, this.transactionsCategories, new BigDecimal("100.00"), new BigDecimal("200.00"), new BigDecimal("100.00"), false, "Test Description1", new Date());
        transaction1.setId(1);
        transaction2 = new Transactions(this.user, this.transactionsCategories, new BigDecimal("10.00"), new BigDecimal("100.00"), new BigDecimal("90.00"), false, "Test Description2", new Date());
        transaction2.setId(2);
        transaction3 = new Transactions(this.user, this.transactionsCategories, new BigDecimal("10.00"), new BigDecimal("90.00"), new BigDecimal("80.00"), false, "Test Description3", new Date());
        transaction3.setId(3);
        transaction4 = new Transactions(this.user, this.transactionsCategories, new BigDecimal("5.00"), new BigDecimal("80.00"), new BigDecimal("75.00"), false, "Test Description4", new Date());
        transaction4.setId(4);
        transaction5 = new Transactions(this.user, this.transactionsCategories, new BigDecimal("15.00"), new BigDecimal("75.00"), new BigDecimal("60.00"), false, "Test Description5", new Date());
        transaction5.setId(5);
        transaction6 = new Transactions(this.user, this.transactionsCategories, new BigDecimal("15.00"), new BigDecimal("60.00"), new BigDecimal("45.00"), false, "Test Description6", new Date());
        transaction6.setId(6);

        Set<Transactions> transactionsSet = new HashSet<>();
        transactionsSet.add(transaction1);
        transactionsSet.add(transaction2);
        transactionsSet.add(transaction3);
        transactionsSet.add(transaction4);
        transactionsSet.add(transaction5);
        transactionsSet.add(transaction6);
        user.setTransactionsSet(transactionsSet);
    }

    @AfterMethod
    public void tearDown() throws Exception {
    }

    @Test(description = "When deleteTrx given outgoing transaction and transactionsSet then calculate new balances")
    public void testDeleteTrx() throws Exception {
        appController.deleteTrx(transaction4, appController.getTransactionsToCalculateBalance(transaction4, user.getTransactionsSet()));
//        Assert.assertNull(transaction1);
        Assert.assertEquals(transaction3.getBalanceBefore(), new BigDecimal("90.00"));

        Assert.assertEquals(transaction5.getBalanceBefore(), new BigDecimal("80.00"));
        Assert.assertEquals(transaction5.getBalanceDiff(), new BigDecimal("65.00"));

        Assert.assertEquals(transaction6.getBalanceBefore(), new BigDecimal("65.00"));
        Assert.assertEquals(transaction6.getBalanceDiff(), new BigDecimal("50.00"));
    }

    public void testDeleteTrxGivenIncomeTrx() {
        transaction1 = new Transactions(this.user, this.transactionsCategories, new BigDecimal("100.00"), new BigDecimal("200.00"), new BigDecimal("100.00"), false, "Test Description1", new Date());
        transaction1.setId(1);
        transaction2 = new Transactions(this.user, this.transactionsCategories, new BigDecimal("10.00"), new BigDecimal("100.00"), new BigDecimal("90.00"), false, "Test Description2", new Date());
        transaction2.setId(2);
        transaction3 = new Transactions(this.user, this.transactionsCategories, new BigDecimal("10.00"), new BigDecimal("90.00"), new BigDecimal("80.00"), false, "Test Description3", new Date());
        transaction3.setId(3);
        transaction4 = new Transactions(this.user, this.transactionsCategories, new BigDecimal("5.00"), new BigDecimal("80.00"), new BigDecimal("85.00"), true, "Test Description4", new Date());
        transaction4.setId(4);
        transaction5 = new Transactions(this.user, this.transactionsCategories, new BigDecimal("15.00"), new BigDecimal("85.00"), new BigDecimal("70.00"), false, "Test Description5", new Date());
        transaction5.setId(5);
        transaction6 = new Transactions(this.user, this.transactionsCategories, new BigDecimal("15.00"), new BigDecimal("70.00"), new BigDecimal("55.00"), false, "Test Description6", new Date());
        transaction6.setId(6);

        Set<Transactions> transactionsSet = new HashSet<>();
        transactionsSet.add(transaction1);
        transactionsSet.add(transaction2);
        transactionsSet.add(transaction3);
        transactionsSet.add(transaction4);
        transactionsSet.add(transaction5);
        transactionsSet.add(transaction6);
        user.setTransactionsSet(transactionsSet);

        Assert.assertEquals(transaction5.getBalanceBefore(), new BigDecimal("85.00"));
        Assert.assertEquals(transaction5.getBalanceDiff(), new BigDecimal("70.00"));
        appController.deleteTrx(transaction4, appController.getTransactionsToCalculateBalance(transaction4, user.getTransactionsSet()));

        Assert.assertEquals(transaction3.getBalanceBefore(), new BigDecimal("90.00"));

        Assert.assertEquals(transaction5.getBalanceBefore(), new BigDecimal("80.00"));
        Assert.assertEquals(transaction5.getBalanceDiff(), new BigDecimal("65.00"));

        Assert.assertEquals(transaction6.getBalanceBefore(), new BigDecimal("65.00"));
        Assert.assertEquals(transaction6.getBalanceDiff(), new BigDecimal("50.00"));
    }

    public void testDeleteTrxGivenOutgoingTrxAndTransactionsSetWithinIncomeTrx() {
        transaction1 = new Transactions(this.user, this.transactionsCategories, new BigDecimal("100.00"), new BigDecimal("200.00"), new BigDecimal("100.00"), false, "Test Description1", new Date());
        transaction1.setId(1);
        transaction2 = new Transactions(this.user, this.transactionsCategories, new BigDecimal("10.00"), new BigDecimal("100.00"), new BigDecimal("90.00"), false, "Test Description2", new Date());
        transaction2.setId(2);
        transaction3 = new Transactions(this.user, this.transactionsCategories, new BigDecimal("10.00"), new BigDecimal("90.00"), new BigDecimal("80.00"), false, "Test Description3", new Date());
        transaction3.setId(3);
        transaction4 = new Transactions(this.user, this.transactionsCategories, new BigDecimal("5.00"), new BigDecimal("80.00"), new BigDecimal("85.00"), true, "Test Description4", new Date());
        transaction4.setId(4);
        transaction5 = new Transactions(this.user, this.transactionsCategories, new BigDecimal("15.00"), new BigDecimal("85.00"), new BigDecimal("70.00"), false, "Test Description5", new Date());
        transaction5.setId(5);
        transaction6 = new Transactions(this.user, this.transactionsCategories, new BigDecimal("15.00"), new BigDecimal("70.00"), new BigDecimal("55.00"), false, "Test Description6", new Date());
        transaction6.setId(6);

        Set<Transactions> transactionsSet = new HashSet<>();
        transactionsSet.add(transaction1);
        transactionsSet.add(transaction2);
        transactionsSet.add(transaction3);
        transactionsSet.add(transaction4);
        transactionsSet.add(transaction5);
        transactionsSet.add(transaction6);
        user.setTransactionsSet(transactionsSet);

        appController.deleteTrx(transaction3, appController.getTransactionsToCalculateBalance(transaction3, user.getTransactionsSet()));

        Assert.assertEquals(transaction4.getBalanceBefore(), new BigDecimal("90.00"));
        Assert.assertEquals(transaction4.getBalanceDiff(), new BigDecimal("95.00"));

        Assert.assertEquals(transaction5.getBalanceBefore(), new BigDecimal("95.00"));
        Assert.assertEquals(transaction5.getBalanceDiff(), new BigDecimal("80.00"));

        Assert.assertEquals(transaction6.getBalanceBefore(), new BigDecimal("80.00"));
        Assert.assertEquals(transaction6.getBalanceDiff(), new BigDecimal("65.00"));
    }
}