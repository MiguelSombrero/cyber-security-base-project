
package sec.project.view;

import java.util.concurrent.TimeUnit;
import javax.transaction.Transactional;
import org.fluentlenium.adapter.junit.FluentTest;
import org.fluentlenium.core.annotation.Page;
import org.fluentlenium.core.hook.wait.Wait;
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
public class LoginPageTest extends FluentTest {
    
    @Page
    private LoginPage loginPage;
    
    @Page
    private RegisterPage registerPage;
    
    @Page
    private PostsPage postsPage;
    
    @LocalServerPort
    private Integer port;
    
    @Autowired
    private TestUtils utils;
    
    @Autowired
    private PasswordEncoder encoder;
    
    @Before
    public void setUp() {
        loginPage.go(port);
    }
    
    @Test
    public void loginPageContainsAllInfo() {
        loginPage.isAt();
        
        loginPage.contains("Login to Cyber Security Forum");
        loginPage.contains("Username:");
        loginPage.contains("Password:");
        loginPage.contains("Home");
        loginPage.contains("About");
        loginPage.contains("Login");
        loginPage.contains("Not yet a member?");
        
        loginPage.notContains("Write post");
        loginPage.notContains("Profile");
        loginPage.notContains("Logout");
    }
    
    @Test
    public void canLoginWithValidInput() {
        registerPage.go(port);
        registerPage.fillAndSubmit("Jukka", "jukka", "jukka");
        loginPage.go(port);
        loginPage.fillAndSubmit("jukka", "jukka");
        postsPage.isAt();
    }
    
    @Test
    public void cannotLoginWithInvalidInput() {
        loginPage.fillAndSubmit("nobody", "jukka");
        loginPage.isAt();
        loginPage.contains("Invalid username or password");
    }
}
