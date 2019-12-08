package sec.project.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Table(name = "PRODUCT_ORDER")
@Data @Entity @AllArgsConstructor @NoArgsConstructor
public class Order extends AbstractPersistable<Long> {

    private String name;
    private String address;

    @ManyToOne
    private Account account;
    
}
