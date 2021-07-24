package blog.surapong.example.dynamicdb.service;

import blog.surapong.example.dynamicdb.entity.branch.Account;
import blog.surapong.example.dynamicdb.repository.branch.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Transactional(transactionManager = "branchTransactionManager")
    public Account testTransactional(Account account) {
        account = accountRepository.save(account);
        if (true) {
            throw new RuntimeException("Test throw error");
        }
        return account;
    }
}
