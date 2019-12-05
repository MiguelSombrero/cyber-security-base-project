
package sec.project.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Account;
import sec.project.repository.AccountRepository;

@Controller
public class AccountController {
    
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder encoder;
    
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }
    
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register() {
        return "register";
    }
    
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerUser(@RequestParam String name, @RequestParam String username, @RequestParam String password) {
        Account account = new Account();
        account.setName(name);
        account.setUsername(username);
        account.setPassword(encoder.encode(password));
        List<String> authorities = new ArrayList<>();
        authorities.add("USER");
        account.setAuthorities(authorities);
        
        accountRepository.save(account);
        return "redirect:/login";
    }
    
    @RequestMapping(value = "/profile/{username}", method = RequestMethod.GET)
    public String profile(Model model, @PathVariable String username) {
        Account account = accountRepository.findByUsername(username);
        model.addAttribute("user", account);
        return "profile";
    }
}
