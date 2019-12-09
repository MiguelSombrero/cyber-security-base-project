
package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sec.project.domain.Account;
import sec.project.domain.Post;
import sec.project.service.AccountService;
import sec.project.service.PostService;

/**
 *
 * @author miika
 */

@Controller
public class PostController {
    
    @Autowired
    private PostService postService;
    
    @Autowired
    private AccountService accountService;
    
    @RequestMapping(value = "/post", method = RequestMethod.GET)
    public String loadPostForm(@ModelAttribute Post post) {
        return "post";
    }
    
    @RequestMapping(value = "/posts", method = RequestMethod.GET)
    public String loadPosts(Model model) {
        model.addAttribute("posts", postService.getPosts());
        return "index";
    }
    
    @RequestMapping(value = "/posts", method = RequestMethod.POST)
    public String submitPost(Authentication authentication, @ModelAttribute Post post) {
        Account account = accountService.getUser(authentication.getName());
        if (account == null) {
            return "redirect:/login";
        }
        postService.post(post, account);
        return "redirect:/posts";
    }
    
    @RequestMapping(value = "/posts/{id}", method = RequestMethod.DELETE)
    public String deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return "redirect:/posts";
    }
}
