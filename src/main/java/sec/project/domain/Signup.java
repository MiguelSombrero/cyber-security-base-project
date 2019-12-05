package sec.project.domain;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Data @Entity @AllArgsConstructor @NoArgsConstructor
public class Signup extends AbstractPersistable<Long> {

    private String name;
    private String address;

    @OneToOne
    private Account account;
    
}
