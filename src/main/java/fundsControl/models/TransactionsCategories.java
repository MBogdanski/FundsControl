package fundsControl.models;

import javax.persistence.*;
import java.util.Set;

@Entity
public class TransactionsCategories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "transactionsCategories")
    private Set<Transactions> Transactions;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public TransactionsCategories(User user, String name) {
        this.user = user;
        this.name = name;
    }

    public TransactionsCategories() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String description) {
        this.name = description;
    }

    public Set<Transactions> getTransactions() {
        return Transactions;
    }

    public void setTransactions(Set<Transactions> transactions) {
        Transactions = transactions;
    }
}
