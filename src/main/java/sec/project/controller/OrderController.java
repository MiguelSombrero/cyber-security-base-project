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
import sec.project.service.OrderService;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private AccountService accountService;
    
    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public String loadOrderForm() {
        return "order";
    }
    
    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public String submitOrder(Authentication authentication, @RequestParam String name, @RequestParam String address) {
        Account account = accountService.getUser(authentication.getName());
        if (account == null) {
            return "redirect:/login";
        }
        orderService.placeOrder(name, address, account);
        return "redirect:/done";
    }
    
    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public String loadOrders(Model model) {
        model.addAttribute("orders", orderService.getOrders());
        return "orders";
    }
    
}
