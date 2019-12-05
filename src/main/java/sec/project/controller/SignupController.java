package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Account;
import sec.project.domain.Signup;
import sec.project.repository.AccountRepository;
import sec.project.repository.SignupRepository;

@Controller
public class SignupController {

    @Autowired
    private SignupRepository signupRepository;

    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadForm() {
        return "form";
    }
    
    @RequestMapping(value = "/done", method = RequestMethod.GET)
    public String done() {
        return "done";
    }

    @RequestMapping(value = "/signs", method = RequestMethod.GET)
    public String loadSignees(Model model) {
        model.addAttribute("participants", signupRepository.findAll());
        return "signs";
    }
    
    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String submitForm(Authentication authentication, @RequestParam String name, @RequestParam String address) {
        Account account = accountRepository.findByUsername(authentication.getName());
        if (account == null) {
            return "redirect:/login";
        }
        signupRepository.save(new Signup(name, address, account));
        return "redirect:/done";
    }
}
