package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {

    AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    public Account registerAccount(Account account) {
        if (account.getUsername() == "" || account.getPassword().length() < 4) {
            return null;
        } else {
            return accountDAO.createAccount(account);
        }

    }

    public Account loginAccount(Account account) throws Exception {

        Account loginUser = accountDAO.getAccountByUsernameAndPassword(account);
        return loginUser;
    }
}