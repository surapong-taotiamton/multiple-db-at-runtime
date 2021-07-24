package blog.surapong.example.dynamicdb.api;

import blog.surapong.example.dynamicdb.entity.branch.Account;
import blog.surapong.example.dynamicdb.repository.branch.AccountRepository;
import blog.surapong.example.dynamicdb.service.AccountService;
import blog.surapong.example.dynamicdb.service.BranchDataSourceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/test/insert")
    public String testInsert(
            @RequestParam("db") String dbInfoId
    ) {
        BranchDataSourceContext.setCurrentBranchDataSourceId(dbInfoId);
        String id = UUID.randomUUID().toString();
        Account account = new Account()
                .setAccountId(dbInfoId + id )
                .setAccountName("AccountName-" + id);
        account = accountRepository.save(account);

        return account.getAccountName();
    }

    @GetMapping("/test/error")
    public String testError(
            @RequestParam("db") String dbInfoId
    ) {
        BranchDataSourceContext.setCurrentBranchDataSourceId(dbInfoId);

        String id = UUID.randomUUID().toString();
        Account account = new Account()
                .setAccountId(dbInfoId + id )
                .setAccountName("AccountName-" + id);
        account = accountRepository.save(account);

        return account.getAccountName();
    }
}
