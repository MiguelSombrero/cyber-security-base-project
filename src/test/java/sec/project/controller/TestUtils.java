package sec.project.controller;


import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import sec.project.domain.Account;
import sec.project.domain.Post;
import sec.project.repository.AccountRepository;
import sec.project.repository.PostRepository;


/**
 *
 * @author miika
 */

@Component("test-utils")
public class TestUtils {
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private PostRepository postRepository;
    
    public String createStringOfLength(int length) {
        StringBuilder string = new StringBuilder();
        
        for (int i = 0; i < length; i++) {
            string.append("a");
        }
        
        return string.toString();
    }
    
    public List<Account> getUsers() {
        return accountRepository.findAll();
    }
    
    public List<Post> getPosts() {
        return postRepository.findAll();
    }
    
    public Account saveUser(String name, String username, String password) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        account.setName(name);
        return accountRepository.save(account);
    }
    
    public void savePost(String title, String content, Account account) {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setAuthor(account);
        post.setCreated(LocalDateTime.now());
        postRepository.save(post);
    }
}