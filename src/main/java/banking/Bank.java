package banking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Bank {

    private HashMap<String, Account> accounts;

    public Bank(){

        accounts = new HashMap<>();
    }

    public void addAccount(Account account) {

        accounts.put(account.getId(), account);
    }

    public Account getAccountById(String id) {

        return accounts.get(id);
    }

    public int getNumberOfAccounts(){

        return accounts.size();
    }

    public void depositById(String id, double amount) {
        Account account = getAccountById(id);
        if (account != null) {
            account.deposit(amount);
        }
    }

    public void withdrawById(String id, double amount) {
        Account account = getAccountById(id);
        if (account != null) {
            account.withdraw(amount);
        }
    }

    public void passTime(int months) {
        List<String> accountsToRemove = new ArrayList<>();

        for (Account account : accounts.values()) {
            // Apply interest based on account type
            account.applyInterest(months);

            // Reset savings withdrawal limits
            if (account instanceof Savings) {
                Savings savingsAccount = (Savings) account;
                savingsAccount.resetWithdrawalLimit();
            }

            // Increment CD maturity counter
            if (account instanceof CD) {
                CD cdAccount = (CD) account;
                cdAccount.incrementMonths(months);
            }

            // Remove accounts with balance less than $25 after 1 month passes
            if (account.getBalance() < 25) {
                accountsToRemove.add(account.getId());
            }
        }

        // Remove accounts with insufficient balance
        for (String id : accountsToRemove) {
            accounts.remove(id);
        }
    }

    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts.values());
    }
}
