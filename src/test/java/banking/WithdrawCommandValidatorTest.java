package banking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class WithdrawCommandValidatorTest {

    private WithdrawCommandValidator validator;
    private Bank bank;

    @BeforeEach
    public void setUp() {
        bank = new Bank();
        validator = new WithdrawCommandValidator(bank);
    }

    // Valid Withdraw Tests - Checking Account
    @Test
    public void valid_withdraw_from_checking_with_integer_amount() {
        bank.addAccount(new Checking("12345678", 1.0));
        bank.depositById("12345678", 500);
        boolean result = validator.validate("Withdraw 12345678 100");
        assertTrue(result);
    }

    @Test
    public void valid_withdraw_from_checking_with_decimal_amount() {
        bank.addAccount(new Checking("12345678", 1.0));
        bank.depositById("12345678", 500);
        boolean result = validator.validate("Withdraw 12345678 100.50");
        assertTrue(result);
    }

    @Test
    public void valid_withdraw_from_checking_max_amount_400() {
        bank.addAccount(new Checking("12345678", 1.0));
        bank.depositById("12345678", 500);
        boolean result = validator.validate("Withdraw 12345678 400");
        assertTrue(result);
    }

    @Test
    public void valid_withdraw_from_checking_small_amount() {
        bank.addAccount(new Checking("12345678", 1.0));
        bank.depositById("12345678", 100);
        boolean result = validator.validate("Withdraw 12345678 0.01");
        assertTrue(result);
    }

    // Valid Withdraw Tests - Savings Account
    @Test
    public void valid_withdraw_from_savings_within_limit() {
        bank.addAccount(new Savings("87654321", 2.0));
        bank.depositById("87654321", 1500);
        boolean result = validator.validate("Withdraw 87654321 500");
        assertTrue(result);
    }

    @Test
    public void valid_withdraw_from_savings_max_amount_1000() {
        bank.addAccount(new Savings("87654321", 2.0));
        bank.depositById("87654321", 1500);
        boolean result = validator.validate("Withdraw 87654321 1000");
        assertTrue(result);
    }

    @Test
    public void valid_withdraw_from_savings_decimal_amount() {
        bank.addAccount(new Savings("87654321", 2.0));
        bank.depositById("87654321", 1000);
        boolean result = validator.validate("Withdraw 87654321 250.75");
        assertTrue(result);
    }

    // Valid Withdraw Tests - CD Account
    @Test
    public void valid_withdraw_from_matured_cd() {
        CD cd = new CD("11111111", 3.0, 1000);
        cd.incrementMonths(12); // Mature the CD
        bank.addAccount(cd);
        boolean result = validator.validate("Withdraw 11111111 1000");
        assertTrue(result);
    }

    @Test
    public void valid_withdraw_from_matured_cd_full_balance() {
        CD cd = new CD("11111111", 3.0, 2000);
        cd.incrementMonths(15); // Mature beyond 12 months
        bank.addAccount(cd);
        boolean result = validator.validate("Withdraw 11111111 2000");
        assertTrue(result);
    }

    // Invalid Command Word Tests
    @Test
    public void lowercase_withdraw_command() {
        bank.addAccount(new Checking("12345678", 1.0));
        boolean result = validator.validate("withdraw 12345678 100");
        assertFalse(result);
    }

    @Test
    public void uppercase_withdraw_command() {
        bank.addAccount(new Checking("12345678", 1.0));
        boolean result = validator.validate("WITHDRAW 12345678 100");
        assertFalse(result);
    }

    @Test
    public void misspelled_withdraw_command() {
        bank.addAccount(new Checking("12345678", 1.0));
        boolean result = validator.validate("Withdraww 12345678 100");
        assertFalse(result);
    }

    @Test
    public void wrong_command_word() {
        bank.addAccount(new Checking("12345678", 1.0));
        boolean result = validator.validate("Take 12345678 100");
        assertFalse(result);
    }

    // Invalid Account ID Tests
    @Test
    public void withdraw_with_id_too_short() {
        boolean result = validator.validate("Withdraw 1234567 100");
        assertFalse(result);
    }

    @Test
    public void withdraw_with_id_too_long() {
        boolean result = validator.validate("Withdraw 123456789 100");
        assertFalse(result);
    }

    @Test
    public void withdraw_with_id_containing_letters() {
        boolean result = validator.validate("Withdraw 1234567a 100");
        assertFalse(result);
    }

    @Test
    public void withdraw_with_id_all_letters() {
        boolean result = validator.validate("Withdraw abcdefgh 100");
        assertFalse(result);
    }

    @Test
    public void withdraw_with_id_special_characters() {
        boolean result = validator.validate("Withdraw 1234-678 100");
        assertFalse(result);
    }

    @Test
    public void withdraw_with_decimal_id() {
        boolean result = validator.validate("Withdraw 12345678.5 100");
        assertFalse(result);
    }

    // Account Existence Tests
    @Test
    public void withdraw_from_nonexistent_account() {
        boolean result = validator.validate("Withdraw 99999999 100");
        assertFalse(result);
    }

    @Test
    public void withdraw_from_account_that_was_not_created() {
        bank.addAccount(new Checking("12345678", 1.0));
        boolean result = validator.validate("Withdraw 87654321 100");
        assertFalse(result);
    }

    // Invalid Amount Tests
    @Test
    public void withdraw_with_negative_amount() {
        bank.addAccount(new Checking("12345678", 1.0));
        boolean result = validator.validate("Withdraw 12345678 -100");
        assertFalse(result);
    }

    @Test
    public void withdraw_with_zero_amount() {
        bank.addAccount(new Checking("12345678", 1.0));
        boolean result = validator.validate("Withdraw 12345678 0");
        assertFalse(result);
    }

    @Test
    public void withdraw_with_negative_decimal_amount() {
        bank.addAccount(new Checking("12345678", 1.0));
        boolean result = validator.validate("Withdraw 12345678 -50.50");
        assertFalse(result);
    }

    @Test
    public void withdraw_with_amount_as_text() {
        bank.addAccount(new Checking("12345678", 1.0));
        boolean result = validator.validate("Withdraw 12345678 abc");
        assertFalse(result);
    }

    @Test
    public void withdraw_with_amount_with_text() {
        bank.addAccount(new Checking("12345678", 1.0));
        boolean result = validator.validate("Withdraw 12345678 100dollars");
        assertFalse(result);
    }

    // Checking Account Limit Tests
    @Test
    public void withdraw_from_checking_exceeds_400_limit() {
        bank.addAccount(new Checking("12345678", 1.0));
        bank.depositById("12345678", 1000);
        boolean result = validator.validate("Withdraw 12345678 401");
        assertFalse(result);
    }

    @Test
    public void withdraw_from_checking_exactly_401() {
        bank.addAccount(new Checking("12345678", 1.0));
        bank.depositById("12345678", 1000);
        boolean result = validator.validate("Withdraw 12345678 400.01");
        assertFalse(result);
    }

    @Test
    public void withdraw_from_checking_way_over_limit() {
        bank.addAccount(new Checking("12345678", 1.0));
        bank.depositById("12345678", 2000);
        boolean result = validator.validate("Withdraw 12345678 1000");
        assertFalse(result);
    }

    // Savings Account Limit Tests
    @Test
    public void withdraw_from_savings_exceeds_1000_limit() {
        bank.addAccount(new Savings("87654321", 2.0));
        bank.depositById("87654321", 2000);
        boolean result = validator.validate("Withdraw 87654321 1001");
        assertFalse(result);
    }

    @Test
    public void withdraw_from_savings_exactly_1000_point_01() {
        bank.addAccount(new Savings("87654321", 2.0));
        bank.depositById("87654321", 2000);
        boolean result = validator.validate("Withdraw 87654321 1000.01");
        assertFalse(result);
    }

    @Test
    public void withdraw_from_savings_already_withdrawn_this_month() {
        Savings savings = new Savings("87654321", 2.0);
        savings.deposit(1000);
        savings.setWithdrawnThisMonth(true);
        bank.addAccount(savings);
        boolean result = validator.validate("Withdraw 87654321 500");
        assertFalse(result);
    }

    @Test
    public void withdraw_from_savings_second_time_same_month() {
        Savings savings = new Savings("87654321", 2.0);
        savings.deposit(1000);
        bank.addAccount(savings);
        // First withdrawal marks it as withdrawn
        savings.setWithdrawnThisMonth(true);
        // Second withdrawal should fail
        boolean result = validator.validate("Withdraw 87654321 100");
        assertFalse(result);
    }

    // CD Account Tests
    @Test
    public void withdraw_from_cd_before_maturity() {
        CD cd = new CD("11111111", 3.0, 1000);
        cd.incrementMonths(6); // Only 6 months passed
        bank.addAccount(cd);
        boolean result = validator.validate("Withdraw 11111111 1000");
        assertFalse(result);
    }

    @Test
    public void withdraw_from_cd_at_11_months() {
        CD cd = new CD("11111111", 3.0, 1000);
        cd.incrementMonths(11); // Just before maturity
        bank.addAccount(cd);
        boolean result = validator.validate("Withdraw 11111111 1000");
        assertFalse(result);
    }

    @Test
    public void withdraw_from_cd_not_matured_at_all() {
        CD cd = new CD("11111111", 3.0, 1000);
        bank.addAccount(cd);
        boolean result = validator.validate("Withdraw 11111111 1000");
        assertFalse(result);
    }

    @Test
    public void withdraw_partial_amount_from_matured_cd() {
        CD cd = new CD("11111111", 3.0, 1000);
        cd.incrementMonths(12);
        bank.addAccount(cd);
        boolean result = validator.validate("Withdraw 11111111 500");
        assertFalse(result);
    }

    @Test
    public void withdraw_less_than_balance_from_matured_cd() {
        CD cd = new CD("11111111", 3.0, 2000);
        cd.incrementMonths(12);
        bank.addAccount(cd);
        boolean result = validator.validate("Withdraw 11111111 1999");
        assertFalse(result);
    }

    // Missing Parameters Tests
    @Test
    public void withdraw_missing_amount() {
        bank.addAccount(new Checking("12345678", 1.0));
        boolean result = validator.validate("Withdraw 12345678");
        assertFalse(result);
    }

    @Test
    public void withdraw_missing_id_and_amount() {
        boolean result = validator.validate("Withdraw");
        assertFalse(result);
    }

    @Test
    public void withdraw_with_only_amount() {
        boolean result = validator.validate("Withdraw 100");
        assertFalse(result);
    }

    // Extra Parameters Tests
    @Test
    public void withdraw_with_extra_parameter() {
        bank.addAccount(new Checking("12345678", 1.0));
        boolean result = validator.validate("Withdraw 12345678 100 extra");
        assertFalse(result);
    }

    @Test
    public void withdraw_with_multiple_extra_parameters() {
        bank.addAccount(new Checking("12345678", 1.0));
        boolean result = validator.validate("Withdraw 12345678 100 extra more");
        assertFalse(result);
    }

    // Empty/Null Command Tests
    @Test
    public void empty_command_string() {
        boolean result = validator.validate("");
        assertFalse(result);
    }

    @Test
    public void null_command() {
        boolean result = validator.validate(null);
        assertFalse(result);
    }

    @Test
    public void whitespace_only_command() {
        boolean result = validator.validate("   ");
        assertFalse(result);
    }

    // Edge Case Tests
    @Test
    public void withdraw_exact_balance_from_checking() {
        bank.addAccount(new Checking("12345678", 1.0));
        bank.depositById("12345678", 400);
        boolean result = validator.validate("Withdraw 12345678 400");
        assertTrue(result);
    }

    @Test
    public void withdraw_more_than_balance_but_within_limit_checking() {
        bank.addAccount(new Checking("12345678", 1.0));
        bank.depositById("12345678", 100);
        // Amount is within $400 limit, but exceeds balance (should still be valid in validator)
        boolean result = validator.validate("Withdraw 12345678 300");
        assertTrue(result);
    }

    @Test
    public void withdraw_from_savings_after_reset() {
        Savings savings = new Savings("87654321", 2.0);
        savings.deposit(1000);
        savings.setWithdrawnThisMonth(true);
        savings.resetWithdrawalLimit(); // Reset for new month
        bank.addAccount(savings);
        boolean result = validator.validate("Withdraw 87654321 500");
        assertTrue(result);
    }

    @Test
    public void withdraw_from_cd_exactly_at_12_months() {
        CD cd = new CD("11111111", 3.0, 1000);
        cd.incrementMonths(12); // Exactly at maturity
        bank.addAccount(cd);
        boolean result = validator.validate("Withdraw 11111111 1000");
        assertTrue(result);
    }
}