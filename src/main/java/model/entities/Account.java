package model.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Account {
    private String accountId;
    private String source;
    private List<AccountSourceMappings> accountSourceMappings;
}
