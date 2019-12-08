
package sec.project.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sec.project.domain.Account;
import sec.project.repository.AccountRepository;

/**
 *
 * @author miika
 */

@Service
public class AccountService {
    
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder encoder;
    
    public void registerUser(Account account) {
        account.addAuthority("USER");
        account.setPassword(encoder.encode(account.getPassword()));
        
        accountRepository.save(account);
    }
    
    public Account getUser(String username) {
        return accountRepository.findByUsername(username);
    }
}
