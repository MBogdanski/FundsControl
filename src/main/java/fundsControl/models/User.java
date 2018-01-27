package fundsControl.models;

import org.hibernate.annotations.Sort;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;
import java.util.SortedSet;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, unique = true)
    private Integer id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "balance")
    private BigDecimal balance;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", orphanRemoval = true)
    @OrderBy
    private Set<Transactions> transactionsSet;

    public Set<TransactionsCategories> getTransactionsCategoriesSet() {
        return transactionsCategoriesSet;
    }

    public void setTransactionsCategoriesSet(Set<TransactionsCategories> transactionsCategoriesSet) {
        this.transactionsCategoriesSet = transactionsCategoriesSet;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private Set<TransactionsCategories> transactionsCategoriesSet;

    public User(String email, String password, String name, BigDecimal balance) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.balance = balance;
    }

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Set<Transactions> getTransactionsSet() {
        return transactionsSet;
    }

    public void setTransactionsSet(Set<Transactions> transactionsSet) {
        this.transactionsSet = transactionsSet;
    }

}
