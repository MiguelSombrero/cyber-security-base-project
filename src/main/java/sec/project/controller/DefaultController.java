
package sec.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DefaultController {

    @RequestMapping("*")
    public String defaultMapping() {
        return "index";
    }
    
    @RequestMapping(value = "/done", method = RequestMethod.GET)
    public String done() {
        return "done";
    }

    @RequestMapping(value = "/about", method = RequestMethod.GET)
    public String about() {
        return "about";
    }

    
    
}

