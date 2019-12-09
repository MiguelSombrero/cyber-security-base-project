
package sec.project.domain;

import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
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
    private String title;
    private String content;

    @ManyToOne (fetch = FetchType.LAZY)
    private Account author;
}
