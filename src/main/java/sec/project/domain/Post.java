
package sec.project.domain;

import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 *
 * @author miika
 */

@Data @Entity @NoArgsConstructor @AllArgsConstructor
public class Post extends AbstractPersistable<Long> {

    private LocalDateTime created;
    
    @NotNull
    @Size(min = 1, max = 50)
    private String title;
    
    @NotNull
    @Size(min = 1, max = 5000)
    private String content;

    @ManyToOne
    private Account author;
}
