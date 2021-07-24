package blog.surapong.example.dynamicdb.repository.center;

import blog.surapong.example.dynamicdb.entity.center.DatabaseInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DatabaseInfoRepository extends JpaRepository<DatabaseInfo, String> {
}
