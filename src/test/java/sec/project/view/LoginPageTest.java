
package sec.project.view;

import javax.transaction.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
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
public class LoginPageTest extends org.fluentlenium.adapter.junit.FluentTest {
    
    @LocalServerPort
    private Integer port;
    
    @Autowired
    private PasswordEncoder encoder;
    
    @Autowired
    private TestUtils utils;
    
    @Before
    public void setUp() {
        utils.saveUser("Jukka Roinanen", "jukka", encoder.encode("jukka"));
        goTo("http://localhost:" + port + "/login");
    }
    
    @Test
    public void loginPageContainsAllInfo() {
        assertTrue(pageSource().contains("Login to Cyber Security Forum"));
        assertTrue(pageSource().contains("Username:"));
        assertTrue(pageSource().contains("Password:"));
        assertTrue(pageSource().contains("Home"));
        assertTrue(pageSource().contains("About"));
        assertTrue(pageSource().contains("Login"));
        assertTrue(pageSource().contains("Not yet a member?"));
        
        assertFalse(pageSource().contains("Write post"));
        assertFalse(pageSource().contains("Profile"));
        assertFalse(pageSource().contains("Logout"));
        
        assertThat(window().title()).contains("Login");
    }
    
    @Test
    public void canLoginWithValidInput() {
        find("#username").fill().with("jukka");
        find("#password").fill().with("jukka");
        find("#loginButton").click();
        assertThat(window().title()).contains("Posts");
    }
    
    @Test
    public void cannotLoginWithInvalidUsername() {
        find("#username").fill().with("nobody");
        find("#password").fill().with("password");
        find("#loginButton").click();
        assertThat(window().title()).contains("Login");
    }
}
