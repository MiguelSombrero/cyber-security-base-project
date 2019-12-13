
package sec.project.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
   
    @CacheEvict(cacheNames = "posts-cache", allEntries=true)
    public void post(Post post, Account author) {
        post.setCreated(LocalDateTime.now());
        post.setAuthor(author);
        postRepository.save(post);
    }
    
    @Cacheable("posts-cache")
    public List<Post> getPosts() {
        return postRepository.findTop100ByOrderByCreatedDesc();
    }
    
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
