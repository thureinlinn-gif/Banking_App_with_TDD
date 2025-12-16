package banking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BankTest {

    private Bank bank;

    @BeforeEach
    public void setUp() {
        bank = new Bank();
    }

    @Test
    public void bank_starts_with_no_accounts() {
        Bank bank = new Bank();
        assertEquals(0, bank.getNumberOfAccounts());
    }

    @Test
    public void adding_one_account_gives_bank_one_account() {
        Bank bank = new Bank();
        Account checking = new Checking("12345678", 1.0);
        bank.addAccount(checking);
        assertEquals(1, bank.getNumberOfAccounts());
    }

    @Test
    public void adding_two_accounts_gives_bank_two_accounts() {
        Bank bank = new Bank();
        Account checking = new Checking("12345678", 1.0);
        Account savings = new Savings("87654321", 2.0);
        bank.addAccount(checking);
        bank.addAccount(savings);
        assertEquals(2, bank.getNumberOfAccounts());
    }

    @Test
    public void retrieving_account_by_id_returns_correct_account() {
        Bank bank = new Bank();
        Account checking = new Checking("12345678", 1.0);
        bank.addAccount(checking);

        Account retrieved = bank.getAccountById("12345678");
        assertEquals(checking, retrieved);
    }

    @Test
    public void depositing_through_bank_updates_correct_account() {
        Bank bank = new Bank();
        Account checking = new Checking("12345678", 1.0);
        bank.addAccount(checking);

        bank.depositById("12345678", 100);
        assertEquals(100, checking.getBalance());
    }

    public void withdrawing_through_bank_updates_correct_account() {
        Bank bank = new Bank();
        Account savings = new Savings("87654321", 2.0);
        bank.addAccount(savings);
        savings.deposit(200);

        bank.withdrawById("87654321", 75);
        assertEquals(125, savings.getBalance());
    }

    @Test
    public void depositing_twice_through_bank_works() {
        Bank bank = new Bank();
        Account checking = new Checking("12345678", 1.0);
        bank.addAccount(checking);

        bank.depositById("12345678", 100);
        bank.depositById("12345678", 50);
        assertEquals(150, checking.getBalance());
    }

    @Test
    public void withdrawing_twice_through_bank_works() {
        Bank bank = new Bank();
        Account savings = new Savings("87654321", 2.0);
        bank.addAccount(savings);
        savings.deposit(300);

        bank.withdrawById("87654321", 100);
        bank.withdrawById("87654321", 50);
        assertEquals(150, savings.getBalance());
    }

    @Test
    public void bank_can_manage_multiple_account_types() {
        Bank bank = new Bank();
        Account checking = new Checking("11111111", 1.0);
        Account savings = new Savings("22222222", 2.0);
        Account cd = new CD("33333333", 3.0, 1000);

        bank.addAccount(checking);
        bank.addAccount(savings);
        bank.addAccount(cd);

        assertEquals(3, bank.getNumberOfAccounts());
    }

    @Test
    public void depositing_to_specific_account_does_not_affect_others() {
        Bank bank = new Bank();
        Account checking = new Checking("11111111", 1.0);
        Account savings = new Savings("22222222", 2.0);

        bank.addAccount(checking);
        bank.addAccount(savings);

        bank.depositById("11111111", 100);

        assertEquals(100, checking.getBalance());
        assertEquals(0, savings.getBalance());
    }

    // Interest Application Tests
    @Test
    public void pass_time_applies_interest_to_checking() {
        Checking checking = new Checking("12345678", 1.2);
        checking.deposit(1000);
        bank.addAccount(checking);

        bank.passTime(1);

        assertTrue(checking.getBalance() > 1000);
    }

    @Test
    public void pass_time_applies_interest_to_savings() {
        Savings savings = new Savings("87654321", 2.4);
        savings.deposit(1000);
        bank.addAccount(savings);

        bank.passTime(1);

        assertTrue(savings.getBalance() > 1000);
    }

    @Test
    public void pass_time_applies_interest_to_cd() {
        CD cd = new CD("11111111", 3.0, 1000);
        bank.addAccount(cd);

        bank.passTime(1);

        assertTrue(cd.getBalance() > 1000);
    }

    @Test
    public void pass_time_applies_interest_to_multiple_accounts() {
        Checking checking = new Checking("12345678", 1.2);
        checking.deposit(1000);
        Savings savings = new Savings("87654321", 2.4);
        savings.deposit(1000);
        bank.addAccount(checking);
        bank.addAccount(savings);

        bank.passTime(1);

        assertTrue(checking.getBalance() > 1000);
        assertTrue(savings.getBalance() > 1000);
    }

    @Test
    public void pass_time_twelve_months_compounds_interest() {
        Checking checking = new Checking("12345678", 1.2);
        checking.deposit(1000);
        bank.addAccount(checking);

        bank.passTime(12);

        assertTrue(checking.getBalance() > 1012);
    }

    // Savings Withdrawal Reset Tests
    @Test
    public void pass_time_resets_savings_withdrawal_limit() {
        Savings savings = new Savings("87654321", 2.0);
        savings.deposit(1000);
        savings.setWithdrawnThisMonth(true);
        bank.addAccount(savings);

        bank.passTime(1);

        assertFalse(savings.hasWithdrawnThisMonth());
    }

    @Test
    public void pass_time_resets_multiple_savings_accounts() {
        Savings savings1 = new Savings("11111111", 2.0);
        savings1.deposit(1000);
        savings1.setWithdrawnThisMonth(true);

        Savings savings2 = new Savings("22222222", 2.5);
        savings2.deposit(1000);
        savings2.setWithdrawnThisMonth(true);

        bank.addAccount(savings1);
        bank.addAccount(savings2);

        bank.passTime(1);

        assertFalse(savings1.hasWithdrawnThisMonth());
        assertFalse(savings2.hasWithdrawnThisMonth());
    }

    @Test
    public void pass_time_resets_savings_after_multiple_months() {
        Savings savings = new Savings("87654321", 2.0);
        savings.deposit(1000);
        savings.setWithdrawnThisMonth(true);
        bank.addAccount(savings);

        bank.passTime(3);

        assertFalse(savings.hasWithdrawnThisMonth());
    }

    // CD Maturity Tests
    @Test
    public void pass_time_increments_cd_months() {
        CD cd = new CD("11111111", 3.0, 1000);
        bank.addAccount(cd);

        bank.passTime(1);

        assertEquals(1, cd.getMonthsPassed());
    }

    @Test
    public void pass_time_increments_cd_to_maturity() {
        CD cd = new CD("11111111", 3.0, 1000);
        bank.addAccount(cd);

        bank.passTime(12);

        assertTrue(cd.hasMatured());
    }

    @Test
    public void pass_time_increments_cd_beyond_maturity() {
        CD cd = new CD("11111111", 3.0, 1000);
        bank.addAccount(cd);

        bank.passTime(15);

        assertTrue(cd.hasMatured());
        assertEquals(15, cd.getMonthsPassed());
    }

    @Test
    public void pass_time_increments_multiple_cds() {
        CD cd1 = new CD("11111111", 3.0, 1000);
        CD cd2 = new CD("22222222", 3.5, 2000);
        bank.addAccount(cd1);
        bank.addAccount(cd2);

        bank.passTime(6);

        assertEquals(6, cd1.getMonthsPassed());
        assertEquals(6, cd2.getMonthsPassed());
    }

    // Account Removal Tests (balance < $25)
    @Test
    public void pass_time_removes_account_with_balance_below_25() {
        Checking checking = new Checking("12345678", 0);
        checking.deposit(20);
        bank.addAccount(checking);

        bank.passTime(1);

        assertNull(bank.getAccountById("12345678"));
        assertEquals(0, bank.getNumberOfAccounts());
    }

    @Test
    public void pass_time_removes_account_with_zero_balance() {
        Checking checking = new Checking("12345678", 0);
        bank.addAccount(checking);

        bank.passTime(1);

        assertNull(bank.getAccountById("12345678"));
    }

    @Test
    public void pass_time_keeps_account_with_balance_exactly_25() {
        Checking checking = new Checking("12345678", 0);
        checking.deposit(25);
        bank.addAccount(checking);

        bank.passTime(1);

        assertEquals(checking, bank.getAccountById("12345678"));
        assertEquals(1, bank.getNumberOfAccounts());
    }

    @Test
    public void pass_time_keeps_account_with_balance_above_25() {
        Checking checking = new Checking("12345678", 1.0);
        checking.deposit(100);
        bank.addAccount(checking);

        bank.passTime(1);

        assertEquals(checking, bank.getAccountById("12345678"));
    }

    @Test
    public void pass_time_removes_multiple_accounts_below_25() {
        Checking checking1 = new Checking("11111111", 0);
        checking1.deposit(10);
        Checking checking2 = new Checking("22222222", 0);
        checking2.deposit(15);
        Checking checking3 = new Checking("33333333", 0);
        checking3.deposit(100);

        bank.addAccount(checking1);
        bank.addAccount(checking2);
        bank.addAccount(checking3);

        bank.passTime(1);

        assertEquals(1, bank.getNumberOfAccounts());
        assertNull(bank.getAccountById("11111111"));
        assertNull(bank.getAccountById("22222222"));
        assertEquals(checking3, bank.getAccountById("33333333"));
    }

    @Test
    public void pass_time_removes_account_with_24_point_99() {
        Checking checking = new Checking("12345678", 0);
        checking.deposit(24.99);
        bank.addAccount(checking);

        bank.passTime(1);

        assertNull(bank.getAccountById("12345678"));
    }

    @Test
    public void pass_time_keeps_account_with_25_point_01() {
        Checking checking = new Checking("12345678", 0);
        checking.deposit(25.01);
        bank.addAccount(checking);

        bank.passTime(1);

        assertEquals(checking, bank.getAccountById("12345678"));
    }

    // Combined Operations Tests
    @Test
    public void pass_time_applies_all_operations_together() {
        // Account with low balance (should be removed)
        Checking checking1 = new Checking("11111111", 0);
        checking1.deposit(10);

        // Account with good balance (should remain with interest)
        Checking checking2 = new Checking("22222222", 1.2);
        checking2.deposit(1000);

        // Savings with withdrawal flag (should reset)
        Savings savings = new Savings("33333333", 2.0);
        savings.deposit(1000);
        savings.setWithdrawnThisMonth(true);

        // CD (should mature and gain interest)
        CD cd = new CD("44444444", 3.0, 1000);

        bank.addAccount(checking1);
        bank.addAccount(checking2);
        bank.addAccount(savings);
        bank.addAccount(cd);

        bank.passTime(12);

        // checking1 should be removed
        assertNull(bank.getAccountById("11111111"));

        // checking2 should have interest
        assertTrue(checking2.getBalance() > 1000);

        // savings should have reset withdrawal and have interest
        assertFalse(savings.hasWithdrawnThisMonth());
        assertTrue(savings.getBalance() > 1000);

        // CD should be matured with interest
        assertTrue(cd.hasMatured());
        assertTrue(cd.getBalance() > 1000);

        assertEquals(3, bank.getNumberOfAccounts());
    }

    @Test
    public void pass_time_zero_months_does_nothing() {
        Checking checking = new Checking("12345678", 1.2);
        checking.deposit(1000);
        bank.addAccount(checking);

        bank.passTime(0);

        assertEquals(1000, checking.getBalance());
    }

    @Test
    public void pass_time_multiple_times() {
        Checking checking = new Checking("12345678", 1.2);
        checking.deposit(1000);
        bank.addAccount(checking);

        bank.passTime(1);
        double balanceAfterOne = checking.getBalance();

        bank.passTime(1);
        double balanceAfterTwo = checking.getBalance();

        assertTrue(balanceAfterTwo > balanceAfterOne);
        assertTrue(balanceAfterOne > 1000);
    }

    @Test
    public void get_all_accounts_returns_correct_list() {
        Checking checking = new Checking("12345678", 1.0);
        Savings savings = new Savings("87654321", 2.0);
        bank.addAccount(checking);
        bank.addAccount(savings);

        assertEquals(2, bank.getAllAccounts().size());
    }

    @Test
    public void get_all_accounts_empty_bank() {
        assertEquals(0, bank.getAllAccounts().size());
    }

    @Test
    public void get_all_accounts_after_pass_time_removal() {
        Checking checking = new Checking("12345678", 0);
        checking.deposit(10);
        bank.addAccount(checking);

        bank.passTime(1);

        assertEquals(0, bank.getAllAccounts().size());
    }
}


