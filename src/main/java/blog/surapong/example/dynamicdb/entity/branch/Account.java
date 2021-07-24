package blog.surapong.example.dynamicdb.entity.branch;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "account")
public class Account {
    @Id
    private String accountId;
    private String accountName;
}
