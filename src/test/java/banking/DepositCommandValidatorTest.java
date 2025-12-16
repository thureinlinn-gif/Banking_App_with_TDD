package banking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

//Test class to validate create validation
public class DepositCommandValidatorTest {

    private DepositCommandValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new DepositCommandValidator();
    }

    //Valid Deposit Test

    //1
    @Test
    public void valid_deposit_with_integer_amount() {
        boolean result = validator.validate("Deposit 12345678 500");
        assertTrue(result);
    }

    //2
    @Test
    public void valid_deposit_with_decimal_amount() {
        boolean result = validator.validate("Deposit 87654321 250.50");
        assertTrue(result);
    }

    //3
    @Test
    public void valid_deposit_with_small_amount() {
        boolean result = validator.validate("Deposit 11111111 0.01");
        assertTrue(result);
    }

    //4
    @Test
    public void valid_deposit_with_large_amount() {
        boolean result = validator.validate("Deposit 99999999 1000000");
        assertTrue(result);
    }

    //Invalid Command
    //5
//    @Test
//    public void lowercase_deposit() {
//        boolean result = validator.validate("deposit 12345678 500");
//        assertFalse(result);
//    }

    //6
//    @Test
//    public void uppercase_deposit() {
//        boolean result = validator.validate("DEPOSIT 12345678 500");
//        assertFalse(result);
//    }

    //7
    @Test
    public void misspelled_deposit() {
        boolean result = validator.validate("Depositt 12345678 500");
        assertFalse(result);
    }

    //8
    @Test
    public void wrong_command_word_add() {
        boolean result = validator.validate("Add 12345678 500");
        assertFalse(result);
    }

    //9
    @Test
    public void empty_command() {
        boolean result = validator.validate("12345678 500");
        assertFalse(result);
    }

    //Invalid banking.Account ID
    //10
    @Test
    public void deposit_with_id_too_short() {
        boolean result = validator.validate("Deposit 1234567 100");
        assertFalse(result);
    }

    //11
    @Test
    public void deposit_with_id_too_long() {
        boolean result = validator.validate("Deposit 123456789 100");
        assertFalse(result);
    }

    //12
    @Test
    public void deposit_with_id_containing_letters() {
        boolean result = validator.validate("Deposit 1234567a 100");
        assertFalse(result);
    }

    //13
    @Test
    public void deposit_with_id_all_letters() {
        boolean result = validator.validate("Deposit abcdefgh 100");
        assertFalse(result);
    }

    //14
    @Test
    public void deposit_with_id_special_characters() {
        boolean result = validator.validate("Deposit 1234-678 100");
        assertFalse(result);
    }

    //15
    @Test
    public void deposit_with_decimal_id() {
        boolean result = validator.validate("Deposit 12345678.5 100");
        assertFalse(result);
    }

    //16
    @Test
    public void deposit_with_id_symbols() {
        boolean result = validator.validate("Deposit 12345@78 100");
        assertFalse(result);
    }

    //Invalid amount
    //17
    @Test
    public void deposit_with_negative_amount() {
        boolean result = validator.validate("Deposit 12345678 -100");
        assertFalse(result);
    }

    //18
    @Test
    public void deposit_with_zero_amount() {
        boolean result = validator.validate("Deposit 12345678 0");
        assertFalse(result);
    }

    //19
    @Test
    public void deposit_with_negative_decimal_amount() {
        boolean result = validator.validate("Deposit 12345678 -50.50");
        assertFalse(result);
    }

    //20
    @Test
    public void deposit_with_amount_as_text() {
        boolean result = validator.validate("Deposit 12345678 abc");
        assertFalse(result);
    }

    //21
    @Test
    public void deposit_with_amount_with_text() {
        boolean result = validator.validate("Deposit 12345678 100dollars");
        assertFalse(result);
    }

    //22
    @Test
    public void deposit_with_amount_with_special_characters() {
        boolean result = validator.validate("Deposit 12345678 $100");
        assertFalse(result);
    }

    //Missing parameters
    //23
    @Test
    public void deposit_with_missing_amount() {
        boolean result = validator.validate("Deposit 12345678");
        assertFalse(result);
    }

    //24
    @Test
    public void deposit_with_missing_id_and_amount() {
        boolean result = validator.validate("Deposit");
        assertFalse(result);
    }

    //25
    @Test
    public void deposit_with_missing_id() {
        boolean result = validator.validate("Deposit 500");
        assertFalse(result);
    }

    //Extra Parameters
    //26
    @Test
    public void deposit_with_extra_parameter() {
        boolean result = validator.validate("Deposit 12345678 100 extra");
        assertFalse(result);
    }

    //27
    @Test
    public void deposit_with_multiple_extra_parameters() {
        boolean result = validator.validate("Deposit 12345678 100 extra more");
        assertFalse(result);
    }

    // Add these tests to DepositCommandValidatorTest.java

    @Test
    public void valid_deposit_to_checking_at_limit() {
        Bank bank = new Bank();
        bank.addAccount(new Checking("12345678", 1.0));
        DepositCommandValidator validator = new DepositCommandValidator(bank);
        assertTrue(validator.validate("Deposit 12345678 1000"));
    }

    @Test
    public void invalid_deposit_to_checking_over_limit() {
        Bank bank = new Bank();
        bank.addAccount(new Checking("12345678", 1.0));
        DepositCommandValidator validator = new DepositCommandValidator(bank);
        assertFalse(validator.validate("Deposit 12345678 1001"));
    }

    @Test
    public void valid_deposit_to_savings_at_limit() {
        Bank bank = new Bank();
        bank.addAccount(new Savings("12345678", 2.0));
        DepositCommandValidator validator = new DepositCommandValidator(bank);
        assertTrue(validator.validate("Deposit 12345678 2500"));
    }

    @Test
    public void invalid_deposit_to_savings_over_limit() {
        Bank bank = new Bank();
        bank.addAccount(new Savings("12345678", 2.0));
        DepositCommandValidator validator = new DepositCommandValidator(bank);
        assertFalse(validator.validate("Deposit 12345678 2501"));
    }

    @Test
    public void invalid_deposit_to_cd() {
        Bank bank = new Bank();
        bank.addAccount(new CD("12345678", 3.0, 1000));
        DepositCommandValidator validator = new DepositCommandValidator(bank);
        assertFalse(validator.validate("Deposit 12345678 500"));
    }

    @Test
    public void valid_deposit_to_nonexistent_account() {
        Bank bank = new Bank();
        DepositCommandValidator validator = new DepositCommandValidator(bank);
        // Account doesn't exist, but command format is valid
        assertTrue(validator.validate("Deposit 12345678 500"));
    }

}