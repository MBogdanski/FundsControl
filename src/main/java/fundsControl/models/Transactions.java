package fundsControl.models;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "balance_before")
    private BigDecimal balanceBefore;

    @Column(name = "balance_diff")
    private BigDecimal balanceDiff;

    @Column(name = "isCredit")
    private boolean isCredit;

    @Column(name = "description")
    private String description;

    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date transactionDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private TransactionsCategories transactionsCategories;

    public Transactions(User u, TransactionsCategories transactionsCategories, BigDecimal amount, BigDecimal balanceBefore, BigDecimal balanceDiff, boolean isCredit, String description, Date transactionDate) {
        this.user = u;
        this.transactionsCategories = transactionsCategories;
        this.amount = amount;
        this.balanceBefore = balanceBefore;
        this.balanceDiff = balanceDiff;
        this.isCredit = isCredit;
        this.description = description;
        this.transactionDate = transactionDate;
    }

    public Transactions() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getBalanceBefore() {
        return balanceBefore;
    }

    public void setBalanceBefore(BigDecimal balanceBefore) {
        this.balanceBefore = balanceBefore;
    }

    public BigDecimal getBalanceDiff() {
        return balanceDiff;
    }

    public void setBalanceDiff(BigDecimal balanceDiff) {
        this.balanceDiff = balanceDiff;
    }

    public boolean isCredit() {
        return isCredit;
    }

    public void setCredit(boolean credit) {
        isCredit = credit;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TransactionsCategories getTransactionsCategories() {
        return transactionsCategories;
    }

    public void setTransactionsCategories(TransactionsCategories transactionsCategories) {
        this.transactionsCategories = transactionsCategories;
    }
}
