
package sec.project.view;

import static org.assertj.core.api.Assertions.assertThat;
import org.fluentlenium.core.FluentPage;
import org.fluentlenium.core.annotation.PageUrl;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author miika
 */

@PageUrl("http://localhost:{port}/register")
public class RegisterPage extends FluentPage {
    
    public void fillAndSubmit(String name, String username, String password) {
        find("#username").fill().with(username);
        find("#password").fill().with(password);
        find("#name").fill().with(name);
        find("#registerButton").click();
    }
    
    public void contains(String text) {
        assertTrue(pageSource().contains(text));
    }
    
    public void notContains(String text) {
        assertFalse(pageSource().contains(text));
    }
    
    @Override
    public void isAt() {
        assertThat(window().title()).contains("Register");
    }
}
