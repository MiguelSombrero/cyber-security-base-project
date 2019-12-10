
package sec.project.controller;

import javax.transaction.Transactional;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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
import sec.project.repository.AccountRepository;

/**
 *
 * @author miika
 */

@Transactional
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private TestUtils utils;
    
    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mock;

    @Before
    public void setUp() {
        this.mock = MockMvcBuilders.webAppContextSetup(webAppContext).build();
        
        Account account = new Account();
        account.setUsername("miika");
        account.setPassword("miika");
        account.setName("Miika Somero");
        accountRepository.save(account);
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
                .param("username", "miika")
                .param("password", "miika"))
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"))
                .andReturn();
    }
    
    @Test
    public void cannotLoginWithUnexistingAccount() throws Exception {
        // how to test that login was unsuccesfull?
        
        MvcResult result = mock.perform(post("/login")
                .param("username", "nobody")
                .param("password", "miika"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"))
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
        MvcResult result = mock.perform(post("/register")
                .param("username", "username")
                .param("password", "password")
                .param("name", "Miika Somero"))
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andReturn();
        
        assertEquals(2L, accountRepository.findAll().stream().count());
    }
    
    @Test
    public void cannotRegisterNewAccountWithTooShortUsername() throws Exception {
        MvcResult result = mock.perform(post("/register")
                .param("username", "user")
                .param("password", "password")
                .param("name", "Miika Somero"))
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrors("account", "username"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andReturn();
        
        assertEquals(1L, accountRepository.findAll().stream().count());
    }
    
    @Test
    public void cannotRegisterNewAccountWithTooShortName() throws Exception {
        MvcResult result = mock.perform(post("/register")
                .param("username", "username")
                .param("password", "password")
                .param("name", ""))
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrors("account", "name"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andReturn();
        
        assertEquals(1L, accountRepository.findAll().stream().count());
    }
    
    @Test
    public void cannotRegisterNewAccountWithTooShortPassword() throws Exception {
        MvcResult result = mock.perform(post("/register")
                .param("username", "username")
                .param("password", "pass")
                .param("name", "Miika Somero"))
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrors("account", "password"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andReturn();
        
        assertEquals(1L, accountRepository.findAll().stream().count());
    }
    
    
    @Test
    public void cannotRegisterNewAccountWithTooLongUsername() throws Exception {
        MvcResult result = mock.perform(post("/register")
                .param("username", utils.createStringOfLength(21))
                .param("password", "password")
                .param("name", "Miika Somero"))
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrors("account", "username"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andReturn();
        
        assertEquals(1L, accountRepository.findAll().stream().count());
    }
    
    @Test
    public void cannotRegisterNewAccountWithTooLongPassword() throws Exception {
        MvcResult result = mock.perform(post("/register")
                .param("username", "username")
                .param("password", utils.createStringOfLength(101))
                .param("name", "Miika Somero"))
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrors("account", "password"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andReturn();
        
        assertEquals(1L, accountRepository.findAll().stream().count());
    }
    
    @Test
    public void cannotRegisterNewAccountWithTooLongName() throws Exception {
        MvcResult result = mock.perform(post("/register")
                .param("username", "username")
                .param("password", "password")
                .param("name", utils.createStringOfLength(21)))
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrors("account", "name"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andReturn();
        
        assertEquals(1L, accountRepository.findAll().stream().count());
    }
    
    @Test
    public void cannotRegisterNewAccountWithUsernameAllreadyTaken() throws Exception {
        MvcResult result = mock.perform(post("/register")
                .param("username", "miika")
                .param("password", "password")
                .param("name", "Miika Somero"))
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrors("account", "username"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andReturn();
        
        assertEquals(1L, accountRepository.findAll().stream().count());
    }
    
}
