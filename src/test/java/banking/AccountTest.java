package banking;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccountTest {
    @Test
    public void store_account_apr(){
        Account account = new Checking("12345678", 1.5);
        assertEquals(1.5,account.getApr());
    }

    @Test
    public void depositing_balance_increasing(){
        Account account = new Checking("12345678",1.0);
        account.deposit(100);
        assertEquals(100,account.getBalance());
    }

    @Test
    public void depositing_twice() {
        Account account = new Checking("12345678", 1.0);
        account.deposit(100);
        account.deposit(100);
        assertEquals(200, account.getBalance());
    }

    @Test
    public void withdrawing_balance_decreasing() {
        Account account = new Checking("12345678", 1.0);
        account.deposit(100);
        account.withdraw(50);
        assertEquals(50, account.getBalance());
    }

    @Test
    public void no_balance_below_zero() {
        Account account = new Checking("12345678", 1.0);
        account.deposit(50);
        account.withdraw(100);
        assertEquals(0, account.getBalance());
    }

    @Test
    public void withdrawing_twice() {
        Account account = new Checking("12345678", 1.0);
        account.deposit(300);
        account.withdraw(100);
        account.withdraw(100);
        assertEquals(100, account.getBalance());
    }

    @Test
    public void deposit_works_decimal_values() {
        Account account = new Savings("12345678", 2.5);
        account.deposit(200.25);
        assertEquals(200.25, account.getBalance());
    }

    @Test
    public void withdraw_works_decimal_values() {
        Account account = new Savings("12345678", 2.5);
        account.deposit(300.50);
        account.withdraw(100.50);
        assertEquals(200.00, account.getBalance());
    }

    @Test
    public void withdrawing_exact_balance_leaves_zero() {
        Account account = new Checking("12345678", 1.0);
        account.deposit(100);
        account.withdraw(100);
        assertEquals(0, account.getBalance());
    }

//    @Test
//    public void withdrawing_exact_balance_with_decimals() {
//        Account account = new Savings("12345678", 2.5);
//        account.deposit(150.75);
//        account.withdraw(150.75);
//        assertEquals(0, account.getBalance());
//    }

    @Test
    public void multiple_withdrawals_reaching_exactly_zero() {
        Account account = new Checking("12345678", 1.0);
        account.deposit(200);
        account.withdraw(100);
        account.withdraw(100);
        assertEquals(0, account.getBalance());
    }

    // Transaction History Tests
    @Test
    public void account_starts_with_empty_transaction_history() {
        Account account = new Checking("12345678", 1.0);
        assertEquals(0, account.getTransactionHistory().size());
    }

    @Test
    public void deposit_adds_transaction_to_history() {
        Account account = new Checking("12345678", 1.0);
        account.deposit(100);
        assertEquals(1, account.getTransactionHistory().size());
    }

    @Test
    public void withdraw_adds_transaction_to_history() {
        Account account = new Checking("12345678", 1.0);
        account.deposit(200);
        account.withdraw(50);
        assertEquals(2, account.getTransactionHistory().size());
    }

    @Test
    public void multiple_deposits_add_multiple_transactions() {
        Account account = new Checking("12345678", 1.0);
        account.deposit(100);
        account.deposit(200);
        account.deposit(300);
        assertEquals(3, account.getTransactionHistory().size());
    }

    @Test
    public void transaction_history_records_deposit_type() {
        Account account = new Checking("12345678", 1.0);
        account.deposit(100);
        Transaction transaction = account.getTransactionHistory().get(0);
        assertEquals("deposit", transaction.getType());
    }

    @Test
    public void transaction_history_records_withdraw_type() {
        Account account = new Checking("12345678", 1.0);
        account.deposit(200);
        account.withdraw(50);
        Transaction transaction = account.getTransactionHistory().get(1);
        assertEquals("withdraw", transaction.getType());
    }

    @Test
    public void transaction_history_records_correct_deposit_amount() {
        Account account = new Checking("12345678", 1.0);
        account.deposit(123.45);
        Transaction transaction = account.getTransactionHistory().get(0);
        assertEquals(123.45, transaction.getAmount());
    }

    @Test
    public void transaction_history_records_correct_withdraw_amount() {
        Account account = new Checking("12345678", 1.0);
        account.deposit(200);
        account.withdraw(75.50);
        Transaction transaction = account.getTransactionHistory().get(1);
        assertEquals(75.50, transaction.getAmount());
    }

    // Interest Calculation Tests
    @Test
    public void apply_interest_one_month_increases_balance() {
        Account account = new Checking("12345678", 1.2);
        account.deposit(1000);
        account.applyInterest(1);
        assertTrue(account.getBalance() > 1000);
    }

    @Test
    public void apply_interest_with_zero_months() {
        Account account = new Checking("12345678", 1.2);
        account.deposit(1000);
        account.applyInterest(0);
        assertEquals(1000, account.getBalance());
    }

    @Test
    public void apply_interest_calculates_correctly() {
        Account account = new Checking("12345678", 1.2);
        account.deposit(1000);
        account.applyInterest(1);
        // Expected: 1000 + (1000 * 1.2 / 100 / 12) = 1000 + 1 = 1001
        assertEquals(1001, account.getBalance(), 0.01);
    }

    @Test
    public void apply_interest_twelve_months() {
        Account account = new Savings("12345678", 1.2);
        account.deposit(1000);
        account.applyInterest(12);
        // Should be approximately 1012 (compounded monthly)
        assertTrue(account.getBalance() > 1012);
    }

    @Test
    public void apply_interest_compounds_monthly() {
        Account account = new Savings("12345678", 2.4);
        account.deposit(1000);
        account.applyInterest(2);
        double expectedFirst = 1000 + (1000 * 2.4 / 100 / 12);
        double expectedSecond = expectedFirst + (expectedFirst * 2.4 / 100 / 12);
        assertEquals(expectedSecond, account.getBalance(), 0.01);
    }

    @Test
    public void apply_interest_with_high_apr() {
        Account account = new CD("12345678", 5.0, 10000);
        account.applyInterest(12);
        // Should be significantly higher than 10000
        assertTrue(account.getBalance() > 10500);
    }

    @Test
    public void apply_interest_with_zero_apr() {
        Account account = new Checking("12345678", 0);
        account.deposit(1000);
        account.applyInterest(12);
        assertEquals(1000, account.getBalance());
    }

    // Boundary Condition Tests
    @Test
    public void withdrawing_exact_balance_results_in_zero() {
        Account account = new Checking("12345678", 1.0);
        account.deposit(100);
        account.withdraw(100);
        assertEquals(0, account.getBalance());
    }

    @Test
    public void withdrawing_exact_balance_with_decimals() {
        Account account = new Savings("87654321", 2.5);
        account.deposit(250.50);
        account.withdraw(250.50);
        assertEquals(0, account.getBalance());
    }

    @Test
    public void multiple_operations_resulting_in_exact_zero_balance() {
        Account account = new Checking("12345678", 1.0);
        account.deposit(150);
        account.withdraw(75);
        account.withdraw(75);
        assertEquals(0, account.getBalance());
    }

    @Test
    public void balance_stays_at_zero_after_overdraft_attempt() {
        Account account = new Checking("12345678", 1.0);
        account.deposit(50);
        account.withdraw(100);
        account.withdraw(50);
        assertEquals(0, account.getBalance());
    }
}
