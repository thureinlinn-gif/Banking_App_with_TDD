package banking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class CommandProcessorTest {

    private Bank bank;
    private CommandProcessor processor;

    @BeforeEach
    public void setUp() {
        bank = new Bank();
        processor = new CommandProcessor(bank);
    }

    // Create banking.Checking test

    @Test
    public void process_create_checking_account() {
        processor.processCommand("Create checking 12345678 1.0");
        assertEquals(1, bank.getNumberOfAccounts());
        Account account = bank.getAccountById("12345678");
        assertNotNull(account);
        assertEquals(1.0, account.getApr());
        assertEquals(0, account.getBalance());
    }

    @Test
    public void process_create_checking_with_decimal_apr() {
        processor.processCommand("Create checking 12345678 1.5");
        Account account = bank.getAccountById("12345678");
        assertEquals(1.5, account.getApr());
    }

    @Test
    public void process_create_checking_with_zero_apr() {
        processor.processCommand("Create checking 12345678 0");
        Account account = bank.getAccountById("12345678");
        assertEquals(0, account.getApr());
    }

    @Test
    public void process_create_checking_with_max_apr() {
        processor.processCommand("Create checking 12345678 10");
        Account account = bank.getAccountById("12345678");
        assertEquals(10, account.getApr());
    }

    @Test
    public void process_create_checking_stores_correct_id() {
        processor.processCommand("Create checking 87654321 2.5");
        Account account = bank.getAccountById("87654321");
        assertNotNull(account);
        assertEquals("87654321", account.getId());
    }

    // Create Saving Tests

    @Test
    public void process_create_savings_account() {
        processor.processCommand("Create savings 87654321 2.5");
        assertEquals(1, bank.getNumberOfAccounts());
        Account account = bank.getAccountById("87654321");
        assertNotNull(account);
        assertEquals(2.5, account.getApr());
        assertEquals(0, account.getBalance());
    }

    @Test
    public void process_create_savings_with_integer_apr() {
        processor.processCommand("Create savings 12345678 5");
        Account account = bank.getAccountById("12345678");
        assertEquals(5, account.getApr());
    }

    @Test
    public void process_create_savings_with_zero_apr() {
        processor.processCommand("Create savings 11111111 0");
        Account account = bank.getAccountById("11111111");
        assertEquals(0, account.getApr());
    }

    @Test
    public void process_create_savings_with_max_apr() {
        processor.processCommand("Create savings 22222222 10");
        Account account = bank.getAccountById("22222222");
        assertEquals(10, account.getApr());
    }

    @Test
    public void process_create_savings_stores_correct_id() {
        processor.processCommand("Create savings 99999999 3.5");
        Account account = bank.getAccountById("99999999");
        assertNotNull(account);
        assertEquals("99999999", account.getId());
    }

    // Create banking.CD tests

    @Test
    public void process_create_cd_account() {
        processor.processCommand("Create cd 11111111 3.0 1000");
        assertEquals(1, bank.getNumberOfAccounts());
        Account account = bank.getAccountById("11111111");
        assertNotNull(account);
        assertEquals(3.0, account.getApr());
        assertEquals(1000, account.getBalance());
    }

    @Test
    public void process_create_cd_with_decimal_balance() {
        processor.processCommand("Create cd 11111111 4.0 2005.54");
        Account account = bank.getAccountById("11111111");
        assertEquals(2005.54, account.getBalance());
    }

    @Test
    public void process_create_cd_with_decimal_apr() {
        processor.processCommand("Create cd 87654321 2.5 1500");
        Account account = bank.getAccountById("87654321");
        assertEquals(2.5, account.getApr());
        assertEquals(1500, account.getBalance());
    }

    @Test
    public void process_create_cd_with_zero_apr() {
        processor.processCommand("Create cd 33333333 0 5000");
        Account account = bank.getAccountById("33333333");
        assertEquals(0, account.getApr());
        assertEquals(5000, account.getBalance());
    }

    @Test
    public void process_create_cd_with_max_apr() {
        processor.processCommand("Create cd 44444444 10 10000");
        Account account = bank.getAccountById("44444444");
        assertEquals(10, account.getApr());
        assertEquals(10000, account.getBalance());
    }

    @Test
    public void process_create_cd_with_large_balance() {
        processor.processCommand("Create cd 55555555 3.5 100000");
        Account account = bank.getAccountById("55555555");
        assertEquals(100000, account.getBalance());
    }

    @Test
    public void process_create_cd_stores_correct_id() {
        processor.processCommand("Create cd 12121212 2.0 5000");
        Account account = bank.getAccountById("12121212");
        assertNotNull(account);
        assertEquals("12121212", account.getId());
    }

    // Create multiple account tests

    @Test
    public void process_create_two_different_accounts() {
        processor.processCommand("Create checking 12345678 1.0");
        processor.processCommand("Create savings 87654321 2.0");
        assertEquals(2, bank.getNumberOfAccounts());
    }

    @Test
    public void process_create_three_different_account_types() {
        processor.processCommand("Create checking 11111111 1.0");
        processor.processCommand("Create savings 22222222 2.0");
        processor.processCommand("Create cd 33333333 3.0 1000");
        assertEquals(3, bank.getNumberOfAccounts());
        assertNotNull(bank.getAccountById("11111111"));
        assertNotNull(bank.getAccountById("22222222"));
        assertNotNull(bank.getAccountById("33333333"));
    }

    @Test
    public void process_create_multiple_checking_accounts() {
        processor.processCommand("Create checking 11111111 1.0");
        processor.processCommand("Create checking 22222222 2.0");
        processor.processCommand("Create checking 33333333 3.0");
        assertEquals(3, bank.getNumberOfAccounts());
    }

    // Deposit into new account tests

    @Test
    public void process_deposit_into_new_checking_account() {
        processor.processCommand("Create checking 12345678 1.0");
        processor.processCommand("Deposit 12345678 100");
        Account account = bank.getAccountById("12345678");
        assertEquals(100, account.getBalance());
    }

    @Test
    public void process_deposit_into_new_savings_account() {
        processor.processCommand("Create savings 87654321 2.0");
        processor.processCommand("Deposit 87654321 200");
        Account account = bank.getAccountById("87654321");
        assertEquals(200, account.getBalance());
    }

    @Test
    public void process_deposit_into_cd_account() {
        processor.processCommand("Create cd 11111111 3.0 1000");
        processor.processCommand("Deposit 11111111 500");
        Account account = bank.getAccountById("11111111");
        assertEquals(1500, account.getBalance());
    }

    @Test
    public void process_deposit_with_decimal_amount() {
        processor.processCommand("Create checking 12345678 1.0");
        processor.processCommand("Deposit 12345678 250.50");
        Account account = bank.getAccountById("12345678");
        assertEquals(250.50, account.getBalance());
    }

    @Test
    public void process_deposit_small_amount() {
        processor.processCommand("Create savings 12345678 2.0");
        processor.processCommand("Deposit 12345678 0.01");
        Account account = bank.getAccountById("12345678");
        assertEquals(0.01, account.getBalance());
    }

    @Test
    public void process_deposit_large_amount() {
        processor.processCommand("Create checking 12345678 1.0");
        processor.processCommand("Deposit 12345678 1000000");
        Account account = bank.getAccountById("12345678");
        assertEquals(1000000, account.getBalance());
    }

    // Deposit into existing balance test

    @Test
    public void process_deposit_into_account_with_existing_balance() {
        processor.processCommand("Create checking 12345678 1.0");
        processor.processCommand("Deposit 12345678 100");
        processor.processCommand("Deposit 12345678 50");
        Account account = bank.getAccountById("12345678");
        assertEquals(150, account.getBalance());
    }

    @Test
    public void process_deposit_adds_to_cd_initial_balance() {
        processor.processCommand("Create cd 11111111 3.0 1000");
        processor.processCommand("Deposit 11111111 250");
        Account account = bank.getAccountById("11111111");
        assertEquals(1250, account.getBalance());
    }

    @Test
    public void process_multiple_deposits_same_account() {
        processor.processCommand("Create checking 12345678 1.0");
        processor.processCommand("Deposit 12345678 100");
        processor.processCommand("Deposit 12345678 50");
        processor.processCommand("Deposit 12345678 25");
        Account account = bank.getAccountById("12345678");
        assertEquals(175, account.getBalance());
    }

    @Test
    public void process_deposit_with_decimals_adds_correctly() {
        processor.processCommand("Create savings 12345678 2.0");
        processor.processCommand("Deposit 12345678 100.50");
        processor.processCommand("Deposit 12345678 50.25");
        Account account = bank.getAccountById("12345678");
        assertEquals(150.75, account.getBalance());
    }

    @Test
    public void process_many_deposits_same_account() {
        processor.processCommand("Create checking 12345678 1.0");
        processor.processCommand("Deposit 12345678 10");
        processor.processCommand("Deposit 12345678 20");
        processor.processCommand("Deposit 12345678 30");
        processor.processCommand("Deposit 12345678 40");
        processor.processCommand("Deposit 12345678 50");
        Account account = bank.getAccountById("12345678");
        assertEquals(150, account.getBalance());
    }

    // Deposit into different account tests

    @Test
    public void process_deposits_into_different_accounts() {
        processor.processCommand("Create checking 11111111 1.0");
        processor.processCommand("Create savings 22222222 2.0");
        processor.processCommand("Deposit 11111111 100");
        processor.processCommand("Deposit 22222222 200");
        assertEquals(100, bank.getAccountById("11111111").getBalance());
        assertEquals(200, bank.getAccountById("22222222").getBalance());
    }

    @Test
    public void process_deposit_does_not_affect_other_accounts() {
        processor.processCommand("Create checking 11111111 1.0");
        processor.processCommand("Create savings 22222222 2.0");
        processor.processCommand("Create cd 33333333 3.0 1000");
        processor.processCommand("Deposit 11111111 500");
        assertEquals(500, bank.getAccountById("11111111").getBalance());
        assertEquals(0, bank.getAccountById("22222222").getBalance());
        assertEquals(1000, bank.getAccountById("33333333").getBalance());
    }

    @Test
    public void process_alternating_deposits_to_different_accounts() {
        processor.processCommand("Create checking 11111111 1.0");
        processor.processCommand("Create savings 22222222 2.0");
        processor.processCommand("Deposit 11111111 100");
        processor.processCommand("Deposit 22222222 200");
        processor.processCommand("Deposit 11111111 50");
        processor.processCommand("Deposit 22222222 75");
        assertEquals(150, bank.getAccountById("11111111").getBalance());
        assertEquals(275, bank.getAccountById("22222222").getBalance());
    }

    // Mixed Create and Deposit Test

    @Test
    public void process_create_and_deposit_sequence() {
        processor.processCommand("Create checking 12345678 1.0");
        processor.processCommand("Deposit 12345678 100");
        processor.processCommand("Create savings 87654321 2.0");
        processor.processCommand("Deposit 87654321 200");
        assertEquals(2, bank.getNumberOfAccounts());
        assertEquals(100, bank.getAccountById("12345678").getBalance());
        assertEquals(200, bank.getAccountById("87654321").getBalance());
    }

    @Test
    public void process_multiple_creates_then_multiple_deposits() {
        processor.processCommand("Create checking 11111111 1.0");
        processor.processCommand("Create savings 22222222 2.0");
        processor.processCommand("Create cd 33333333 3.0 1000");
        processor.processCommand("Deposit 11111111 100");
        processor.processCommand("Deposit 22222222 200");
        processor.processCommand("Deposit 33333333 300");
        assertEquals(3, bank.getNumberOfAccounts());
        assertEquals(100, bank.getAccountById("11111111").getBalance());
        assertEquals(200, bank.getAccountById("22222222").getBalance());
        assertEquals(1300, bank.getAccountById("33333333").getBalance());
    }

    @Test
    public void process_complex_sequence_of_commands() {
        processor.processCommand("Create checking 12345678 1.5");
        processor.processCommand("Deposit 12345678 500");
        processor.processCommand("Create savings 87654321 2.5");
        processor.processCommand("Deposit 12345678 250");
        processor.processCommand("Deposit 87654321 1000");
        processor.processCommand("Create cd 11111111 3.5 5000");
        processor.processCommand("Deposit 11111111 2500");
        assertEquals(3, bank.getNumberOfAccounts());
        assertEquals(750, bank.getAccountById("12345678").getBalance());
        assertEquals(1000, bank.getAccountById("87654321").getBalance());
        assertEquals(7500, bank.getAccountById("11111111").getBalance());
    }

    // Add these tests to CommandProcessorTest.java

    @Test
    public void process_withdraw_from_checking() {
        processor.processCommand("Create checking 12345678 1.0");
        processor.processCommand("Deposit 12345678 500");
        processor.processCommand("Withdraw 12345678 100");
        Account account = bank.getAccountById("12345678");
        assertEquals(400, account.getBalance());
    }

    @Test
    public void process_withdraw_from_savings() {
        processor.processCommand("Create savings 12345678 2.0");
        processor.processCommand("Deposit 12345678 1000");
        processor.processCommand("Withdraw 12345678 500");
        Account account = bank.getAccountById("12345678");
        assertEquals(500, account.getBalance());
        Savings savings = (Savings) account;
        assertTrue(savings.hasWithdrawnThisMonth());
    }

    @Test
    public void process_transfer_between_accounts() {
        processor.processCommand("Create checking 12345678 1.0");
        processor.processCommand("Create savings 87654321 2.0");
        processor.processCommand("Deposit 12345678 500");
        processor.processCommand("Transfer 12345678 87654321 300");
        assertEquals(200, bank.getAccountById("12345678").getBalance());
        assertEquals(300, bank.getAccountById("87654321").getBalance());
    }

    @Test
    public void process_transfer_marks_savings_withdrawn() {
        processor.processCommand("Create savings 12345678 2.0");
        processor.processCommand("Create checking 87654321 1.0");
        processor.processCommand("Deposit 12345678 1000");
        processor.processCommand("Transfer 12345678 87654321 500");
        Savings savings = (Savings) bank.getAccountById("12345678");
        assertTrue(savings.hasWithdrawnThisMonth());
    }

    @Test
    public void process_pass_applies_interest() {
        processor.processCommand("Create checking 12345678 1.2");
        processor.processCommand("Deposit 12345678 1000");
        processor.processCommand("Pass 1");
        assertTrue(bank.getAccountById("12345678").getBalance() > 1000);
    }

    @Test
    public void process_pass_removes_low_balance_account() {
        processor.processCommand("Create checking 12345678 0");
        processor.processCommand("Deposit 12345678 20");
        processor.processCommand("Pass 1");
        assertNull(bank.getAccountById("12345678"));
    }

    @Test
    public void process_pass_resets_savings_withdrawal() {
        processor.processCommand("Create savings 12345678 2.0");
        processor.processCommand("Deposit 12345678 1000");
        Savings savings = (Savings) bank.getAccountById("12345678");
        savings.setWithdrawnThisMonth(true);
        processor.processCommand("Pass 1");
        assertFalse(savings.hasWithdrawnThisMonth());
    }

    @Test
    public void process_pass_increments_cd_months() {
        processor.processCommand("Create cd 12345678 3.0 1000");
        processor.processCommand("Pass 6");
        CD cd = (CD) bank.getAccountById("12345678");
        assertEquals(6, cd.getMonthsPassed());
    }
}