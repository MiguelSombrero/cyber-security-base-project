
package sec.project.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Data @Entity @AllArgsConstructor @NoArgsConstructor
public class Account extends AbstractPersistable<Long> {
    private String name;
    private String username;
    private String password;
    
    @OneToMany(mappedBy = "account")
    private List<Order> orders = new ArrayList<>();
    
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> authorities = new ArrayList<>();
    
    public void addOrder(Order order) {
        this.orders.add(order);
    }
}
