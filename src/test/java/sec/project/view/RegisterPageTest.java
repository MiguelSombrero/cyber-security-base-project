
package sec.project.view;

import javax.transaction.Transactional;
import org.fluentlenium.adapter.junit.FluentTest;
import org.fluentlenium.core.annotation.Page;
import static org.junit.Assert.assertEquals;
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

/**
 *
 * @author miika
 */

@Transactional
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegisterPageTest extends FluentTest {
    
    @Page
    private RegisterPage registerPage;
    
    @Page
    private LoginPage loginPage;
            
    @LocalServerPort
    private Integer port;
    
    @Autowired
    private TestUtils utils;
    
    @Autowired
    private PasswordEncoder encoder;
    
    @Before
    public void setUp() {
        registerPage.go(port);
    }
    
    @Test
    public void registerPageContainsAllInfo() {
        registerPage.isAt();
        
        registerPage.contains("Create account to Cyber Security Forum");
        registerPage.contains("Username:");
        registerPage.contains("Password:");
        registerPage.contains("Name:");
        registerPage.contains("Home");
        registerPage.contains("About");
        registerPage.contains("Login");
        registerPage.contains("Already member?");
        
        registerPage.notContains("Write post");
        registerPage.notContains("Profile");
        registerPage.notContains("Logout");
    }
    
    @Test
    public void canRegisterWithValidInput() {
        long count = utils.getUsers().size();
        registerPage.fillAndSubmit("Miguel", "username", "password");
        loginPage.isAt();
        assertEquals(count + 1, utils.getUsers().size());
    }
    
    @Test
    public void cannotRegisterWithTooShortName() {
        long count = utils.getUsers().size();
        registerPage.fillAndSubmit("", "username", "password");
        registerPage.isAt();
        registerPage.contains("Name should be between 1-20 character");
        assertEquals(count, utils.getUsers().size());
    }
    
    @Test
    public void cannotRegisterWithTooLongName() {
        long count = utils.getUsers().size();
        registerPage.fillAndSubmit(utils.createStringOfLength(21), "username", "password");
        registerPage.isAt();
        registerPage.contains("Name should be between 1-20 character");
        assertEquals(count, utils.getUsers().size());
    }
    
    @Test
    public void cannotRegisterWithTooShortUsername() {
        long count = utils.getUsers().size();
        registerPage.fillAndSubmit("Miguel", "user", "password");
        registerPage.isAt();
        registerPage.contains("Username should be between 5-20 character");
        assertEquals(count, utils.getUsers().size());
    }
    
    @Test
    public void cannotRegisterWithTooLongUsername() {
        long count = utils.getUsers().size();
        registerPage.fillAndSubmit("Miguel", utils.createStringOfLength(21), "password");
        registerPage.isAt();
        registerPage.contains("Username should be between 5-20 character");
        assertEquals(count, utils.getUsers().size());
    }
    
    @Test
    public void cannotRegisterWithTooShortPassword() {
        long count = utils.getUsers().size();
        registerPage.fillAndSubmit("Miguel", "username", "pass");
        registerPage.isAt();
        registerPage.contains("Password should be between 5-100 character");
        assertEquals(count, utils.getUsers().size());
    }
    
    @Test
    public void cannotRegisterWithTooLongPassword() {
        long count = utils.getUsers().size();
        registerPage.fillAndSubmit("Miguel", "username", utils.createStringOfLength(101));
        registerPage.isAt();
        registerPage.contains("Password should be between 5-100 character");
        assertEquals(count, utils.getUsers().size());
    }
    
    @Test
    public void cannotRegisterWithUsernameTaken() {
        registerPage.fillAndSubmit("Jukka", "jukka", "jukka");
        long count = utils.getUsers().size();
        registerPage.go(port);
        registerPage.fillAndSubmit("Jukka", "jukka", "jukka");
        registerPage.isAt();
        registerPage.contains("username already taken - please select another one");
        assertEquals(count, utils.getUsers().size());
    }
}
