package blog.surapong.example.dynamicdb.entity.center;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
@ToString
@Entity
public class DatabaseInfo {
    @Id
    private String databaseInfoId;
    private String host;
    private String name;
    private Integer port;
    private String username;
    private String password;
}
