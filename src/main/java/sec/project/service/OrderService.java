
package sec.project.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sec.project.domain.Account;
import sec.project.domain.Order;
import sec.project.repository.AccountRepository;
import sec.project.repository.OrderRepository;

/**
 *
 * @author miika
 */

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AccountRepository accountRepository;

    public List<Order> getOrders() {
        return orderRepository.findAll();
    }
    
    public void placeOrder(String name, String address, Account account) {
        orderRepository.save(new Order(name, address, account));
    }
}
