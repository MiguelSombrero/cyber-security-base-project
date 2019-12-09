
package sec.project.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import sec.project.domain.Post;

/**
 *
 * @author miika
 */
public interface PostRepository extends JpaRepository<Post, Long> {
     List<Post> findAllByOrderByCreatedDesc();
}
