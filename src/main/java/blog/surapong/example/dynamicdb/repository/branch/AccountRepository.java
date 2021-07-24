package blog.surapong.example.dynamicdb.repository.branch;


import blog.surapong.example.dynamicdb.entity.branch.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}
