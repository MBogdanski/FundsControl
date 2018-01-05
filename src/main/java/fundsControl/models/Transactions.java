package fundsControl.models;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Outgoings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Column(name = "outgoing_amount")
    private BigDecimal amount;

    @Column(name = "description")
    private String description;

    @Column(name = "date")
    private Date outgoingDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "outgoing_category_id", nullable = false)
    private OutgoingsCategories outgoingsCategories;

    public Outgoings(User u, OutgoingsCategories oc, BigDecimal amount, String description, Date outgoingDate) {
        this.user = u;
        this.outgoingsCategories = oc;
        this.amount = amount;
        this.description = description;
        this.outgoingDate = outgoingDate;
    }

    public Outgoings() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getOutgoingDate() {
        return outgoingDate;
    }

    public void setOutgoingDate(Date outgoingDate) {
        this.outgoingDate = outgoingDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public OutgoingsCategories getOutgoingsCategories() {
        return outgoingsCategories;
    }

    public void setOutgoingsCategories(OutgoingsCategories outgoingsCategories) {
        this.outgoingsCategories = outgoingsCategories;
    }
}
