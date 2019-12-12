
package sec.project.view;

import javax.transaction.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import sec.project.controller.TestUtils;
import sec.project.domain.Account;
import sec.project.repository.AccountRepository;

/**
 *
 * @author miika
 */

@Transactional
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegisterViewTest extends org.fluentlenium.adapter.junit.FluentTest {
    
    @LocalServerPort
    private Integer port;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private PasswordEncoder encoder;
    
    @Autowired
    private TestUtils utils;
    
    @Before
    public void setUp() {
        goTo("http://localhost:" + port + "/register");
    }
    
    @Test
    public void registerPageContainsAllInfo() {
        assertTrue(pageSource().contains("Create account to Cyber Security Forum"));
        assertTrue(pageSource().contains("Username:"));
        assertTrue(pageSource().contains("Name:"));
        assertTrue(pageSource().contains("Password:"));
        assertTrue(pageSource().contains("Home"));
        assertTrue(pageSource().contains("About"));
        assertTrue(pageSource().contains("Login"));
        assertTrue(pageSource().contains("Already member?"));
        
        assertFalse(pageSource().contains("Write post"));
        assertFalse(pageSource().contains("Profile"));
        assertFalse(pageSource().contains("Logout"));
        
        assertThat(window().title()).contains("Register");
    }
    @Test
    public void canRegisterWithValidInput() {
        find("#username").fill().with("username");
        find("#password").fill().with("password");
        find("#name").fill().with("Miguel");
        find("#registerButton").click();
        
        Account account = accountRepository.findByUsername("username");
        
        assertEquals("username", account.getUsername());
        assertEquals("Miguel", account.getName());
        assertTrue(encoder.matches("password", account.getPassword()));
        
        assertTrue(pageSource().contains("Login to Cyber Security Forum"));
    }
    
    @Test
    public void cannotRegisterWithTooShortNameame() {
        find("#username").fill().with("username");
        find("#password").fill().with("password");
        find("#name").fill().with("");
        find("#registerButton").click();
        
        assertEquals(0L, accountRepository.findAll().stream().count());
        assertTrue(pageSource().contains("Name should be between 1-20 character"));
    }
    
    @Test
    public void cannotRegisterWithTooLongName() {
        find("#username").fill().with("username");
        find("#password").fill().with("password");
        find("#name").fill().with(utils.createStringOfLength(21));
        find("#registerButton").click();
        
        assertEquals(0L, accountRepository.findAll().stream().count());
        assertTrue(pageSource().contains("Name should be between 1-20 character"));
    }
    
    @Test
    public void cannotRegisterWithTooShortUsername() {
        find("#username").fill().with("user");
        find("#password").fill().with("password");
        find("#name").fill().with("Miguel");
        find("#registerButton").click();
        
        assertEquals(0L, accountRepository.findAll().stream().count());
        assertTrue(pageSource().contains("Username should be between 5-20 character"));
    }
    
    @Test
    public void cannotRegisterWithTooLongUsername() {
        find("#username").fill().with(utils.createStringOfLength(21));
        find("#password").fill().with("password");
        find("#name").fill().with("Miguel");
        find("#registerButton").click();
        
        assertEquals(0L, accountRepository.findAll().stream().count());
        assertTrue(pageSource().contains("Username should be between 5-20 character"));
    }
    
    @Test
    public void cannotRegisterWithUsernameTaken() {
        find("#username").fill().with("username");
        find("#password").fill().with("password");
        find("#name").fill().with("Miguel");
        find("#registerButton").click();
        
        assertEquals(1L, accountRepository.findAll().stream().count());
        
        goTo("http://localhost:" + port + "/register");
        find("#username").fill().with("username");
        find("#password").fill().with("password");
        find("#name").fill().with("Miguel");
        find("#registerButton").click();
        
        assertEquals(1L, accountRepository.findAll().stream().count());
        assertTrue(pageSource().contains("username allready taken - please select another one"));
    }
    
    /*
    @Test
    public void cannotRegisterWithTooShortPassword() {
        // miksi tämä ei mene läpi? Antaa että yksi henkilö olisi tietokannassa
        find("#username").fill().with("username");
        find("#password").fill().with("pass");
        find("#name").fill().with("Miguel");
        find("#registerButton").click();
        
        assertEquals(0L, accountRepository.findAll().stream().count());
        assertTrue(pageSource().contains("Password should be between 5-100 character"));
    }
    */
    
    @Test
    public void cannotRegisterWithTooLongPassword() {
        find("#username").fill().with("username");
        find("#password").fill().with(utils.createStringOfLength(101));
        find("#name").fill().with("Miguel");
        find("#registerButton").click();
        
        assertEquals(0L, accountRepository.findAll().stream().count());
        assertTrue(pageSource().contains("Password should be between 5-100 character"));
    }
}
