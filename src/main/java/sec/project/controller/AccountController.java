
package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Account;
import sec.project.service.AccountService;

@Controller
public class AccountController {
    
    @Autowired
    private AccountService accountService;
    
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }
    
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register() {
        return "register";
    }
    
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerUser(@ModelAttribute Account account) {
        accountService.registerUser(account);
        return "redirect:/login";
    }
    
    @RequestMapping(value = "/profile/{username}", method = RequestMethod.GET)
    public String getUserProfile(Model model, @PathVariable String username) {
        model.addAttribute("user", accountService.getUser(username));
        return "profile";
    }
}
