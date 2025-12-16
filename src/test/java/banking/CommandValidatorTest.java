package banking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

//The main parent class that delegates the tasks to CreateValidator and DepositValidator
public class CommandValidatorTest {

    private CommandValidator validator;
    Bank bank;
    @BeforeEach
    public void setUp() {
        bank = new Bank();
        validator = new CommandValidator(bank);
    }

    //Test for Create Command Delegation
    @Test
    public void valid_create_command_delegates_correctly() {
        boolean result = validator.validateCommand("Create checking 12345678 1.5");
        assertTrue(result);
    }

    //Test for Deposit Command Delegation
    @Test
    public void valid_deposit_command_delegates_correctly() {
        boolean result = validator.validateCommand("Deposit 12345678 500");
        assertTrue(result);
    }

    //Test for Invalid Command Delegation
    @Test
    public void invalid_command_type_returns_false() {
        boolean result = validator.validateCommand("Withdraw 12345678 100");
        assertFalse(result);
    }

    //Test for Empty Command
    @Test
    public void empty_command_returns_false() {
        boolean result = validator.validateCommand("");
        assertFalse(result);
    }

    @Test
    public void command_with_only_command_word_returns_false() {
        boolean result = validator.validateCommand("Create");
        assertFalse(result);
    }

    @Test
    public void command_with_only_deposit_word_returns_false() {
        boolean result = validator.validateCommand("Deposit");
        assertFalse(result);
    }

    @Test
    public void single_word_invalid_command_returns_false() {
        boolean result = validator.validateCommand("RandomCommand");
        assertFalse(result);
    }

    @Test
    public void valid_deposit_command_through_parent_validator_returns_true() {
        boolean result = validator.validateCommand("Deposit 12345678 500");
        assertTrue(result);
    }

    @Test
    public void invalid_deposit_command_through_parent_validator_returns_false() {
        boolean result = validator.validateCommand("Deposit 12345678 -100");
        assertFalse(result);
    }

    @Test
    public void deposit_with_zero_amount_through_parent_validator_returns_false() {
        boolean result = validator.validateCommand("Deposit 12345678 0");
        assertFalse(result);
    }

    // Add these tests to CommandValidatorTest.java to kill remaining mutants

    @Test
    public void valid_withdraw_command_delegates_correctly() {
        Checking checking = new Checking("12345678", 1.0);
        checking.deposit(500);
        bank.addAccount(checking);
        boolean result = validator.validateCommand("Withdraw 12345678 100");
        assertTrue(result);
    }

    @Test
    public void invalid_withdraw_command_returns_false() {
        boolean result = validator.validateCommand("Withdraw 12345678 -100");
        assertFalse(result);
    }

    @Test
    public void valid_transfer_command_delegates_correctly() {
        Checking checking = new Checking("12345678", 1.0);
        checking.deposit(500);
        bank.addAccount(checking);
        bank.addAccount(new Savings("87654321", 2.0));
        boolean result = validator.validateCommand("Transfer 12345678 87654321 100");
        assertTrue(result);
    }

    @Test
    public void invalid_transfer_command_returns_false() {
        boolean result = validator.validateCommand("Transfer 12345678 87654321 -100");
        assertFalse(result);
    }

    @Test
    public void valid_pass_command_delegates_correctly() {
        boolean result = validator.validateCommand("Pass 1");
        assertTrue(result);
    }

    @Test
    public void invalid_pass_command_returns_false() {
        boolean result = validator.validateCommand("Pass 0");
        assertFalse(result);
    }

    @Test
    public void withdraw_command_with_nonexistent_account_returns_false() {
        boolean result = validator.validateCommand("Withdraw 12345678 100");
        assertFalse(result);
    }

    @Test
    public void transfer_command_with_nonexistent_accounts_returns_false() {
        boolean result = validator.validateCommand("Transfer 12345678 87654321 100");
        assertFalse(result);
    }

    @Test
    public void pass_command_with_invalid_months_returns_false() {
        boolean result = validator.validateCommand("Pass 61");
        assertFalse(result);
    }

}