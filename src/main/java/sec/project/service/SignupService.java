
package sec.project.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sec.project.domain.Account;
import sec.project.domain.Signup;
import sec.project.repository.AccountRepository;
import sec.project.repository.SignupRepository;

/**
 *
 * @author miika
 */

@Service
public class SignupService {
    
    @Autowired
    private SignupRepository signupRepository;

    @Autowired
    private AccountRepository accountRepository;

    public List<Signup> getParticipants() {
        return signupRepository.findAll();
    }
    
    public void signupToEvent(String name, String address, Account account) {
        signupRepository.save(new Signup(name, address, account));
    }
}
