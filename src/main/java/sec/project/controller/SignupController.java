package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Account;
import sec.project.service.AccountService;
import sec.project.service.SignupService;

@Controller
public class SignupController {

    @Autowired
    private SignupService signupService;
    
    @Autowired
    private AccountService accountService;
    
    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadSignForm() {
        return "form";
    }
    
    @RequestMapping(value = "/signs", method = RequestMethod.GET)
    public String loadParticipants(Model model) {
        model.addAttribute("participants", signupService.getParticipants());
        return "signs";
    }
    
    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String submitSignForm(Authentication authentication, @RequestParam String name, @RequestParam String address) {
        Account account = accountService.getUser(authentication.getName());
        if (account == null) {
            return "redirect:/login";
        }
        signupService.signupToEvent(name, address, account);
        return "redirect:/done";
    }
}
