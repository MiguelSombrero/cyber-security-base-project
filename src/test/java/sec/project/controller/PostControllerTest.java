
package sec.project.controller;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import sec.project.domain.Account;
import sec.project.domain.Post;

/**
 *
 * @author miika
 */

@Transactional
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class PostControllerTest {
    
    @Autowired
    private TestUtils utils;
    
    @Autowired
    private PasswordEncoder encoder;
    
    @Autowired
    private WebApplicationContext webAppContext;
    
    private MockMvc mock;

    @PostConstruct
    public void init() {
        this.mock = MockMvcBuilders.webAppContextSetup(webAppContext)
                .apply(springSecurity())
                .build();
    }
    
    @Before
    public void setUp() {
        Account account = utils.saveUser("Jukka Roinanen", "jukka", encoder.encode("jukka"));
        utils.savePost("Title", "Content", account);
    }

    @Test
    @WithMockUser
    public void getPostFormWithAuthenticatedUser() throws Exception {
        mock.perform(get("/post"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Post to forum")))
                .andExpect(view().name("post"));
    }
    
    @Test
    @WithAnonymousUser
    public void getPostFormWithAnonymousUser() throws Exception {
        mock.perform(get("/post"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }
    
    @Test
    @WithMockUser
    public void getPostsWithAuthenticatedUser() throws Exception {
        MvcResult result = mock.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Welcome to the Cyber Security forum!")))
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("posts"))
                .andReturn();
        
        List<Post> posts = (List) result.getModelAndView().getModel().get("posts");
        assertTrue(posts.size() >= 1);
        
    }
    
    @Test
    @WithAnonymousUser
    public void getPostsWithAnonymousUser() throws Exception {
        mock.perform(get("/posts"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }
    
    @Test
    @WithMockUser("jukka")
    public void canPostWhenAuthenticated() throws Exception {
        long count = utils.getPosts().size();
        
        MvcResult result = mock.perform(post("/posts")
                .param("title", "Story of me life")
                .param("content", "I was born in 1982"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"))
                .andReturn();
        
        assertEquals(count + 1, utils.getPosts().size());
    }
    
    // HERE SHOULD BE THE TESTS, WHICH TEST THAT POSTING FAILS WHEN VALIDATION OF THE POST FAILS
    // FOR NOW, THESE VALIDATION RULES DOES NOT EXISTS
    
    @Test
    @WithAnonymousUser
    public void cannotPostWhenNotAuthenticated() throws Exception {
        long count = utils.getPosts().size();
        
        MvcResult result = mock.perform(post("/posts")
                .param("title", "Story of me life")
                .param("content", "I was born in 1982"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"))
                .andReturn();
        
        assertEquals(count, utils.getPosts().size());
    }
    
    @Test
    @WithMockUser("nobody")
    public void cannotPostWithUnexistingUser() throws Exception {
        long count = utils.getPosts().size();
        
        MvcResult result = mock.perform(post("/posts")
                .param("title", "Story of me life")
                .param("content", "I was born in 1982"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andReturn();
        
        assertEquals(count, utils.getPosts().size());
    }
    
    @Test
    @WithMockUser
    public void canDeletePostsWhenAuthenticated() throws Exception {
        List<Post> posts = utils.getPosts();
        
        MvcResult result = mock.perform(MockMvcRequestBuilders.delete("/posts/" + posts.get(0).getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"))
                .andReturn();
        
        assertEquals(posts.size() - 1, utils.getPosts().size());
    }
    
    // HERE IS THE PLACE TESTS TO TEST THAT USER CANNOT DELETE SOMEONE ELSES POSTS
    // THESE RESTRICTIONS DOES NOT EXIST
    
    /*
    
    THIS TEST IS CAUSING STACKOVERFLOW - WHY?
    @Test
    @WithAnonymousUser
    public void cannotDeletePostsWhenNotAuthenticated() throws Exception {
        List<Post> posts = utils.getPosts();
        
        MvcResult result = mock.perform(MockMvcRequestBuilders.delete("/posts/" + posts.get(0).getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"))
                .andReturn();
        
        assertEquals(posts.size(), utils.getPosts());
    }
    */
}
