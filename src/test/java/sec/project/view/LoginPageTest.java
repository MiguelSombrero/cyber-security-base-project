/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.project.view;

import java.util.concurrent.TimeUnit;
import javax.transaction.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import org.fluentlenium.core.hook.wait.Wait;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
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
import sec.project.domain.Account;
import sec.project.repository.AccountRepository;

/**
 *
 * @author miika
 */

@Wait
@Transactional
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginPageTest extends org.fluentlenium.adapter.junit.FluentTest {
    
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
        Account account = new Account();
        account.setUsername("miika");
        account.setPassword(encoder.encode("miika"));
        account.setName("Miika Somero");
        accountRepository.save(account);
        
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
        find("#username").fill().with("miika");
        find("#password").fill().with("miika");
        find("#loginButton").click();
        
        assertTrue(pageSource().contains("Welcome to the Cyber Security forum!"));
        assertTrue(pageSource().contains("Write post"));
        assertTrue(pageSource().contains("Profile"));
        assertTrue(pageSource().contains("Logout"));
        assertTrue(pageSource().contains("Christmas chocolate - appropriate amount?"));
        assertTrue(pageSource().contains("Cats empathy"));
        assertTrue(pageSource().contains("© Miika Somero 2019"));
        assertTrue(pageSource().contains("NEWS"));
        assertTrue(pageSource().contains("POSTS"));
        
        assertThat(window().title()).contains("Posts");
    }
    
    @Test
    public void canLogoutAfterLogin() {
        find("#username").fill().with("miika");
        find("#password").fill().with("miika");
        find("#loginButton").click();
        
        await().atMost(2, TimeUnit.SECONDS);
        
        find("#logoutButton").click();
        
        assertTrue(pageSource().contains("You have been logged out ..."));
        assertThat(window().title()).contains("Login");
    }
    
    @Test
    public void cannotLoginWithInvalidUsername() {
        find("#username").fill().with("username");
        find("#password").fill().with("password");
        find("#loginButton").click();
        
        assertTrue(pageSource().contains("Invalid username or password"));
    }
}
