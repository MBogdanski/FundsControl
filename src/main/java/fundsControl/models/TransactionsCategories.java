package fundsControl.models;

import javax.persistence.*;
import java.util.Set;

@Entity
public class OutgoingsCategories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "outgoingsCategories")
    private Set<Transactions> outgoings;

    public OutgoingsCategories(String description) {
        this.description = description;
    }

    public OutgoingsCategories() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
