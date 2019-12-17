/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

@PageUrl("http://localhost:{port}/posts")
public class PostsPage extends FluentPage {
    
    public void contains(String text) {
        assertTrue(pageSource().contains(text));
    }
    
    public void notContains(String text) {
        assertFalse(pageSource().contains(text));
    }
    
    @Override
    public void isAt() {
        assertThat(window().title()).contains("Posts");
    }
}

