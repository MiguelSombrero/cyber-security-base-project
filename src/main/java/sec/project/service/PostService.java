
package sec.project.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sec.project.domain.Account;
import sec.project.domain.Post;
import sec.project.repository.PostRepository;

/**
 *
 * @author miika
 */

@Service
public class PostService {
    
    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private AccountService accountService;
   
    public void post(Post post, Account author) {
        post.setCreated(LocalDateTime.now());
        post.setAuthor(author);
        postRepository.save(post);
    }
    
    public List<Post> getPosts() {
        return postRepository.findAllByOrderByCreatedDesc();
    }
    
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
