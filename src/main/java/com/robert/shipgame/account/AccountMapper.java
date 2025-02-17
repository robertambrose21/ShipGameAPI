package com.robert.shipgame.account;

import com.robert.shipgame.account.api.dto.AccountDTO;
import com.robert.shipgame.account.data.AccountDAO;
import com.robert.shipgame.account.service.Account;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    AccountDAO pojoToDAO(Account account);

    Account daoToPojo(AccountDAO accountDAO);

    AccountDTO pojoToDTO(Account account);

}
