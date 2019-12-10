package sec.project.controller;


import org.springframework.stereotype.Component;


/**
 *
 * @author miika
 */

@Component
public class TestUtils {
    
    public String createStringOfLength(int length) {
        StringBuilder string = new StringBuilder();
        
        for (int i = 0; i < length; i++) {
            string.append("a");
        }
        
        return string.toString();
    }
}