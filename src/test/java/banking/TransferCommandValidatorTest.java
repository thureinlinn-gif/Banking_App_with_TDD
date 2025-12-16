package banking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TransferCommandValidatorTest {

    private TransferCommandValidator validator;
    private Bank bank;

    @BeforeEach
    public void setUp() {
        bank = new Bank();
        validator = new TransferCommandValidator(bank);
    }

    // Valid transfers
    @Test
    public void valid_transfer_from_checking_to_savings() {
        bank.addAccount(new Checking("12345678", 1.0));
        bank.addAccount(new Savings("87654321", 2.0));
        bank.depositById("12345678", 500);
        assertTrue(validator.validate("Transfer 12345678 87654321 100"));
    }

    @Test
    public void valid_transfer_checking_max_400() {
        bank.addAccount(new Checking("12345678", 1.0));
        bank.addAccount(new Savings("87654321", 2.0));
        bank.depositById("12345678", 500);
        assertTrue(validator.validate("Transfer 12345678 87654321 400"));
    }

    @Test
    public void valid_transfer_savings_max_1000() {
        Savings savings = new Savings("12345678", 2.0);
        savings.deposit(1500);
        bank.addAccount(savings);
        bank.addAccount(new Checking("87654321", 1.0));
        assertTrue(validator.validate("Transfer 12345678 87654321 1000"));
    }

    @Test
    public void valid_transfer_from_matured_cd() {
        CD cd = new CD("12345678", 3.0, 5000);
        cd.incrementMonths(12);
        bank.addAccount(cd);
        bank.addAccount(new Checking("87654321", 1.0));
        assertTrue(validator.validate("Transfer 12345678 87654321 5000"));
    }

    // Invalid - accounts don't exist
    @Test
    public void invalid_transfer_nonexistent_from_account() {
        bank.addAccount(new Savings("87654321", 2.0));
        assertFalse(validator.validate("Transfer 12345678 87654321 100"));
    }

    @Test
    public void invalid_transfer_nonexistent_to_account() {
        bank.addAccount(new Checking("12345678", 1.0));
        assertFalse(validator.validate("Transfer 12345678 87654321 100"));
    }

    @Test
    public void invalid_transfer_both_accounts_nonexistent() {
        assertFalse(validator.validate("Transfer 12345678 87654321 100"));
    }

    // Invalid - command format
    @Test
    public void invalid_transfer_wrong_command_word() {
        assertFalse(validator.validate("Move 12345678 87654321 100"));
    }

    @Test
    public void invalid_transfer_case_sensitive() {
        assertFalse(validator.validate("TRANSFER 12345678 87654321 100"));
    }

    @Test
    public void invalid_transfer_missing_amount() {
        bank.addAccount(new Checking("12345678", 1.0));
        bank.addAccount(new Savings("87654321", 2.0));
        assertFalse(validator.validate("Transfer 12345678 87654321"));
    }

    @Test
    public void invalid_transfer_extra_parameter() {
        bank.addAccount(new Checking("12345678", 1.0));
        bank.addAccount(new Savings("87654321", 2.0));
        assertFalse(validator.validate("Transfer 12345678 87654321 100 extra"));
    }

    @Test
    public void invalid_transfer_empty_command() {
        assertFalse(validator.validate(""));
    }

    @Test
    public void invalid_transfer_null_command() {
        assertFalse(validator.validate(null));
    }

    // Invalid - IDs
    @Test
    public void invalid_transfer_from_id_too_short() {
        assertFalse(validator.validate("Transfer 1234567 87654321 100"));
    }

    @Test
    public void invalid_transfer_from_id_too_long() {
        assertFalse(validator.validate("Transfer 123456789 87654321 100"));
    }

    @Test
    public void invalid_transfer_to_id_too_short() {
        assertFalse(validator.validate("Transfer 12345678 8765432 100"));
    }

    @Test
    public void invalid_transfer_to_id_too_long() {
        assertFalse(validator.validate("Transfer 12345678 876543219 100"));
    }

    @Test
    public void invalid_transfer_from_id_with_letters() {
        assertFalse(validator.validate("Transfer 1234567a 87654321 100"));
    }

    @Test
    public void invalid_transfer_to_id_with_letters() {
        assertFalse(validator.validate("Transfer 12345678 8765432a 100"));
    }

    // Invalid - amounts
    @Test
    public void invalid_transfer_zero_amount() {
        bank.addAccount(new Checking("12345678", 1.0));
        bank.addAccount(new Savings("87654321", 2.0));
        assertFalse(validator.validate("Transfer 12345678 87654321 0"));
    }

    @Test
    public void invalid_transfer_negative_amount() {
        bank.addAccount(new Checking("12345678", 1.0));
        bank.addAccount(new Savings("87654321", 2.0));
        assertFalse(validator.validate("Transfer 12345678 87654321 -100"));
    }

    @Test
    public void invalid_transfer_amount_not_number() {
        bank.addAccount(new Checking("12345678", 1.0));
        bank.addAccount(new Savings("87654321", 2.0));
        assertFalse(validator.validate("Transfer 12345678 87654321 abc"));
    }

    // Invalid - checking limit
    @Test
    public void invalid_transfer_checking_exceeds_400() {
        bank.addAccount(new Checking("12345678", 1.0));
        bank.addAccount(new Savings("87654321", 2.0));
        bank.depositById("12345678", 500);
        assertFalse(validator.validate("Transfer 12345678 87654321 401"));
    }

    // Invalid - savings limit
    @Test
    public void invalid_transfer_savings_exceeds_1000() {
        Savings savings = new Savings("12345678", 2.0);
        savings.deposit(1500);
        bank.addAccount(savings);
        bank.addAccount(new Checking("87654321", 1.0));
        assertFalse(validator.validate("Transfer 12345678 87654321 1001"));
    }

    @Test
    public void invalid_transfer_savings_already_withdrawn() {
        Savings savings = new Savings("12345678", 2.0);
        savings.deposit(1500);
        savings.setWithdrawnThisMonth(true);
        bank.addAccount(savings);
        bank.addAccount(new Checking("87654321", 1.0));
        assertFalse(validator.validate("Transfer 12345678 87654321 500"));
    }

    // Invalid - CD not matured
    @Test
    public void invalid_transfer_from_cd_not_matured() {
        CD cd = new CD("12345678", 3.0, 5000);
        bank.addAccount(cd);
        bank.addAccount(new Checking("87654321", 1.0));
        assertFalse(validator.validate("Transfer 12345678 87654321 5000"));
    }

    @Test
    public void invalid_transfer_cd_partial_amount() {
        CD cd = new CD("12345678", 3.0, 5000);
        cd.incrementMonths(12);
        bank.addAccount(cd);
        bank.addAccount(new Checking("87654321", 1.0));
        assertFalse(validator.validate("Transfer 12345678 87654321 2500"));
    }
}