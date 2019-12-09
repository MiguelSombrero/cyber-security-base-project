
package sec.project.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sec.project.domain.Account;
import sec.project.service.AccountService;

@Controller
public class AccountController {
    
    @Autowired
    private AccountService accountService;
    
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@ModelAttribute Account account) {
        return "login";
    }
    
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(@ModelAttribute Account account) {
        return "register";
    }
    
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerUser(@Valid @ModelAttribute Account account, BindingResult result) {
        if (result.hasErrors()) {
            return "register";
        }
        accountService.registerUser(account);
        return "redirect:/login";
    }
    
    @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
    public String deleteUser(@PathVariable Long id) {
        accountService.deleteUser(id);
        return "redirect:/login";
    }
    
    @RequestMapping(value = "/users/{username}", method = RequestMethod.GET)
    public String getUserProfile(Model model, @PathVariable String username) {
        model.addAttribute("user", accountService.getUser(username));
        return "profile";
    }
}
