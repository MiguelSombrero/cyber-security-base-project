
package sec.project.view;

import javax.transaction.Transactional;
import static org.junit.Assert.assertEquals;
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
    
    private String host;
    
    @Before
    public void setUp() {
        host = "http://localhost:" + port;
    }
    
    @Test
    public void canRegisterWithValidInput() {
        goTo(host + "/register");
        
        find("#username").fill().with("username");
        find("#password").fill().with("password");
        find("#name").fill().with("Miguel");
        find("#registerButton").click();
        
        Account account = accountRepository.findByUsername("username");
        
        assertEquals("username", account.getUsername());
        assertEquals("Miguel", account.getName());
        assertTrue(encoder.matches("password", account.getPassword()));
    }
}
