
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
public class RegisterViewTest extends org.fluentlenium.adapter.junit.FluentTest {
    
    @LocalServerPort
    private Integer port;
    
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
        long count = utils.getUsers().size();
        
        find("#username").fill().with("username");
        find("#password").fill().with("password");
        find("#name").fill().with("Miguel");
        find("#registerButton").click();
        
        assertEquals(count + 1, utils.getUsers().size());
        assertTrue(pageSource().contains("Login to Cyber Security Forum"));
    }
    
    @Test
    public void cannotRegisterWithTooShortNameame() {
        long count = utils.getUsers().size();
        
        find("#username").fill().with("username");
        find("#password").fill().with("password");
        find("#name").fill().with("");
        find("#registerButton").click();
        
        assertEquals(count, utils.getUsers().size());
        assertTrue(pageSource().contains("Name should be between 1-20 character"));
    }
    
    @Test
    public void cannotRegisterWithTooLongName() {
        long count = utils.getUsers().size();
        
        find("#username").fill().with("username");
        find("#password").fill().with("password");
        find("#name").fill().with(utils.createStringOfLength(21));
        find("#registerButton").click();
        
        assertEquals(count, utils.getUsers().size());
        assertTrue(pageSource().contains("Name should be between 1-20 character"));
    }
    
    @Test
    public void cannotRegisterWithTooShortUsername() {
        long count = utils.getUsers().size();
        
        find("#username").fill().with("user");
        find("#password").fill().with("password");
        find("#name").fill().with("Miguel");
        find("#registerButton").click();
        
        assertEquals(count, utils.getUsers().size());
        assertTrue(pageSource().contains("Username should be between 5-20 character"));
    }
    
    @Test
    public void cannotRegisterWithTooLongUsername() {
        long count = utils.getUsers().size();
        
        find("#username").fill().with(utils.createStringOfLength(21));
        find("#password").fill().with("password");
        find("#name").fill().with("Miguel");
        find("#registerButton").click();
        
        assertEquals(count, utils.getUsers().size());
        assertTrue(pageSource().contains("Username should be between 5-20 character"));
    }
    
    @Test
    public void cannotRegisterWithUsernameTaken() {
        long count = utils.getUsers().size();
        
        find("#username").fill().with("username");
        find("#password").fill().with("password");
        find("#name").fill().with("Miguel");
        find("#registerButton").click();
        
        assertEquals(count + 1, utils.getUsers().size());
        
        goTo("http://localhost:" + port + "/register");
        find("#username").fill().with("username");
        find("#password").fill().with("password");
        find("#name").fill().with("Miguel");
        find("#registerButton").click();
        
        assertEquals(count + 1, utils.getUsers().size());
        assertTrue(pageSource().contains("username allready taken - please select another one"));
    }
    
    @Test
    public void cannotRegisterWithTooShortPassword() {
        long count = utils.getUsers().size();
        
        find("#username").fill().with("username");
        find("#password").fill().with("pass");
        find("#name").fill().with("Miguel");
        find("#registerButton").click();
        
        assertEquals(count, utils.getUsers().size());
        assertTrue(pageSource().contains("Password should be between 5-100 character"));
    }
    
    @Test
    public void cannotRegisterWithTooLongPassword() {
        long count = utils.getUsers().size();
        
        find("#username").fill().with("username");
        find("#password").fill().with(utils.createStringOfLength(101));
        find("#name").fill().with("Miguel");
        find("#registerButton").click();
        
        assertEquals(count, utils.getUsers().size());
        assertTrue(pageSource().contains("Password should be between 5-100 character"));
    }
}
