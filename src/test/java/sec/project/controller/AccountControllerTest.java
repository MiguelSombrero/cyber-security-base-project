
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

/**
 *
 * @author miika
 */

@Transactional
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountControllerTest {
    
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
    }
    
    @Test
    public void getLoginPage() throws Exception {
        mock.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Login to Cyber Security Forum")))
                .andExpect(view().name("login"));
    }
    
    @Test
    public void canLoginWithExistingAccount() throws Exception {
        MvcResult result = mock.perform(post("/login")
                .param("username", "jukka")
                .param("password", "jukka"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"))
                .andReturn();
    }
    
    @Test
    public void cannotLoginWithUnexistingAccount() throws Exception {
        MvcResult result = mock.perform(post("/login")
                .param("username", "nobody")
                .param("password", "miika"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"))
                .andReturn();
    }
    
    @Test
    public void getRegisterPage() throws Exception {
        mock.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Create account to Cyber Security Forum")))
                .andExpect(view().name("register"));
    }
    
    @Test
    public void canRegisterNewAccount() throws Exception {
        long count = utils.getUsers().size();
        
        MvcResult result = mock.perform(post("/register")
                .param("username", "username")
                .param("password", "password")
                .param("name", "Miika Somero"))
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andReturn();
        
        assertEquals(count + 1, utils.getUsers().size());
    }
    
    @Test
    public void cannotRegisterNewAccountWithTooShortUsername() throws Exception {
        long count = utils.getUsers().size();
        
        MvcResult result = mock.perform(post("/register")
                .param("username", "user")
                .param("password", "password")
                .param("name", "Miika Somero"))
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrors("account", "username"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andReturn();
        
        assertEquals(count, utils.getUsers().size());
    }
    
    @Test
    public void cannotRegisterNewAccountWithTooShortName() throws Exception {
        long count = utils.getUsers().size();
        
        MvcResult result = mock.perform(post("/register")
                .param("username", "username")
                .param("password", "password")
                .param("name", ""))
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrors("account", "name"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andReturn();
        
        assertEquals(count, utils.getUsers().size());
    }
    
    @Test
    public void cannotRegisterNewAccountWithTooShortPassword() throws Exception {
        long count = utils.getUsers().size();
        
        MvcResult result = mock.perform(post("/register")
                .param("username", "username")
                .param("password", "pass")
                .param("name", "Miika Somero"))
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrors("account", "password"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andReturn();
        
        assertEquals(count, utils.getUsers().size());
    }
    
    
    @Test
    public void cannotRegisterNewAccountWithTooLongUsername() throws Exception {
        long count = utils.getUsers().size();
        
        MvcResult result = mock.perform(post("/register")
                .param("username", utils.createStringOfLength(21))
                .param("password", "password")
                .param("name", "Miika Somero"))
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrors("account", "username"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andReturn();
        
        assertEquals(count, utils.getUsers().size());
    }
    
    @Test
    public void cannotRegisterNewAccountWithTooLongPassword() throws Exception {
        long count = utils.getUsers().size();
        
        MvcResult result = mock.perform(post("/register")
                .param("username", "username")
                .param("password", utils.createStringOfLength(101))
                .param("name", "Miika Somero"))
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrors("account", "password"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andReturn();
        
        assertEquals(count, utils.getUsers().size());
    }
    
    @Test
    public void cannotRegisterNewAccountWithTooLongName() throws Exception {
        long count = utils.getUsers().size();
        
        MvcResult result = mock.perform(post("/register")
                .param("username", "username")
                .param("password", "password")
                .param("name", utils.createStringOfLength(21)))
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrors("account", "name"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andReturn();
        
        assertEquals(count, utils.getUsers().size());
    }
    
    @Test
    public void cannotRegisterNewAccountWithUsernameAllreadyTaken() throws Exception {
        long count = utils.getUsers().size();
        
        MvcResult result = mock.perform(post("/register")
                .param("username", "miika")
                .param("password", "password")
                .param("name", "Miika Somero"))
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrors("account", "username"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andReturn();
        
        assertEquals(count, utils.getUsers().size());
    }
    
    @Test
    @WithMockUser
    public void canDeleteAccount() throws Exception {
        List<Account> users = utils.getUsers();
        
        MvcResult result = mock.perform(MockMvcRequestBuilders.delete("/users/" + users.get(0).getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andReturn();
        
        assertEquals(users.size() - 1, utils.getUsers().size());
    }
    
    // HERE IS THE PLACE TESTS TO TEST THAT USER CANNOT DELETE SOMEONE ELSE
    // THESE RESTRICTIONS DOES NOT EXIST
    
    @Test
    @WithMockUser("jukka")
    public void getProfilePage() throws Exception {
        MvcResult result = mock.perform(get("/users/jukka"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Jukka Roinanen 's profile")))
                .andExpect(view().name("profile"))
                .andExpect(model().attributeExists("user"))
                .andReturn();
        
         Account user = (Account) result.getModelAndView().getModel().get("user");
         
         assertEquals("Jukka Roinanen", user.getName());
         assertEquals("jukka", user.getUsername());
    }
}
