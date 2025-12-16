package banking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

//Test class to validate create validation
public class CreateCommandValidatorTest {

    private CreateCommandValidator validator;
    //----------------------//
    private Bank bank;
    //----------------------//

    @BeforeEach
    public void setUp() {
        //----------------------//
        bank = new Bank();
        validator = new CreateCommandValidator(bank);
        //----------------------//
    }

    //Validate Test Cases
    //1
    @Test
    public void valid_checking_with_decimal_apr() {
        boolean result = validator.validate("Create checking 12345678 1.5");
        assertTrue(result);
    }

    //2
    @Test
    public void valid_savings_with_decimal_apr() {
        boolean result = validator.validate("Create savings 87654321 3.5");
        assertTrue(result);
    }

    //3
    @Test
    public void valid_cd_with_all_parameters() {
        boolean result = validator.validate("Create cd 12345678 3 1000");
        assertTrue(result);
    }

    //4
    @Test
    public void valid_checking_with_zero_apr() {
        boolean result = validator.validate("Create checking 12345678 0");
        assertTrue(result);
    }

    //5
    @Test
    public void valid_checking_with_max_apr() {
        boolean result = validator.validate("Create checking 12345678 10");
        assertTrue(result);
    }

    //6
    @Test
    public void valid_cd_with_decimal_balance() {
        boolean result = validator.validate("Create cd 11111111 4.0 2005.54");
        assertTrue(result);
    }

    //7
    @Test
    public void valid_savings_with_integer_apr() {
        boolean result = validator.validate("Create savings 12345678 5");
        assertTrue(result);
    }

    //8
    @Test
    public void valid_savings_with_zero_apr() {
        boolean result = validator.validate("Create savings 11111111 0");
        assertTrue(result);
    }

    //9
    @Test
    public void valid_savings_with_max_apr() {
        boolean result = validator.validate("Create savings 22222222 10");
        assertTrue(result);
    }

    //10
    @Test
    public void valid_cd_with_integer_values() {
        boolean result = validator.validate("Create cd 12345678 3 1000");
        assertTrue(result);
    }

    //11
    @Test
    public void valid_cd_with_decimal_apr() {
        boolean result = validator.validate("Create cd 87654321 2.5 1500");
        assertTrue(result);
    }

    //12
    @Test
    public void valid_cd_with_zero_apr() {
        boolean result = validator.validate("Create cd 33333333 0 5000");
        assertTrue(result);
    }

    //13
    @Test
    public void valid_cd_with_max_apr() {
        boolean result = validator.validate("Create cd 44444444 10 10000");
        assertTrue(result);
    }

    //Invalid Command
    //14
//    @Test
//    public void lowercase_create() {
//        boolean result = validator.validate("create checking 12345678 1.5");
//        assertFalse(result);
//    }

    //15
//    @Test
//    public void uppercase_create() {
//        boolean result = validator.validate("CREATE checking 12345678 1.5");
//        assertFalse(result);
//    }

    //16
    @Test
    public void misspelled_command() {
        boolean result = validator.validate("Creat checking 12345678 1.5");
        assertFalse(result);
    }

    //17
    @Test
    public void wrong_command_word_make() {
        boolean result = validator.validate("Make checking 12345678 1.5");
        assertFalse(result);
    }

    //18
    @Test
    public void wrong_command_word_add() {
        boolean result = validator.validate("Add checking 12345678 1.5");
        assertFalse(result);
    }

    //19
    @Test
    public void empty_command() {
        boolean result = validator.validate("checking 12345678 1.5");
        assertFalse(result);
    }

    //Invalid banking.Account Type
    //20
    @Test
    public void cd_in_caps() {
        boolean result = validator.validate("Create banking.CD 12345678 2.0 1000");
        assertFalse(result);
    }

    //21
    @Test
    public void empty_account_type() {
        boolean result = validator.validate("Create 12345678 1.5");
        assertFalse(result);
    }

    //Invalid banking.Account ID
    //22
    @Test
    public void id_too_short_seven_digits() {
        boolean result = validator.validate("Create checking 1234567 1.5");
        assertFalse(result);
    }

    //23
    @Test
    public void id_too_short_six_digits() {
        boolean result = validator.validate("Create checking 123456 1.5");
        assertFalse(result);
    }

    //24
    @Test
    public void id_too_long_nine_digits() {
        boolean result = validator.validate("Create checking 123456789 1.5");
        assertFalse(result);
    }

    //25
    @Test
    public void id_with_letters() {
        boolean result = validator.validate("Create checking 1234567a 1.5");
        assertFalse(result);
    }

    //26
    @Test
    public void id_with_all_letters() {
        boolean result = validator.validate("Create checking abcdefgh 1.5");
        assertFalse(result);
    }

    //27
    @Test
    public void id_with_special_characters() {
        boolean result = validator.validate("Create checking 1234-678 1.5");
        assertFalse(result);
    }

    //28
    @Test
    public void decimal_id() {
        boolean result = validator.validate("Create checking 12345678.5 1.5");
        assertFalse(result);
    }

    //29
    @Test
    public void empty_id() {
        boolean result = validator.validate("Create checking 1.5");
        assertFalse(result);
    }

    //30
    @Test
    public void id_with_symbols() {
        boolean result = validator.validate("Create checking 12345@78 1.5");
        assertFalse(result);
    }

    //Invalid APR
    //31
    @Test
    public void apr_below_minimum() {
        boolean result = validator.validate("Create checking 12345678 -1");
        assertFalse(result);
    }

    //32
    @Test
    public void apr_below_minimum_decimal() {
        boolean result = validator.validate("Create checking 12345678 -0.5");
        assertFalse(result);
    }

    //33
    @Test
    public void apr_above_maximum() {
        boolean result = validator.validate("Create checking 12345678 11");
        assertFalse(result);
    }

    //34
    @Test
    public void apr_above_maximum_decimal() {
        boolean result = validator.validate("Create checking 12345678 10.1");
        assertFalse(result);
    }

    //35
    @Test
    public void apr_with_mixed_text() {
        boolean result = validator.validate("Create checking 12345678 1.5percent");
        assertFalse(result);
    }

    //36
    @Test
    public void apr_with_special_characters() {
        boolean result = validator.validate("Create checking 12345678 5%");
        assertFalse(result);
    }

    //37
    @Test
    public void empty_apr() {
        boolean result = validator.validate("Create checking 12345678");
        assertFalse(result);
    }

    //Missing Parameters
    //38
    @Test
    public void missing_all_parameters() {
        boolean result = validator.validate("Create");
        assertFalse(result);
    }

    //39
    @Test
    public void missing_apr_for_checking() {
        boolean result = validator.validate("Create checking 12345678");
        assertFalse(result);
    }

    //40
    @Test
    public void missing_apr_for_savings() {
        boolean result = validator.validate("Create savings 12345678");
        assertFalse(result);
    }

    //41
    @Test
    public void missing_initial_balance_for_cd() {
        boolean result = validator.validate("Create cd 12345678 2.5");
        assertFalse(result);
    }

    // Extra Parameters
    //42
    @Test
    public void extra_parameter_for_checking() {
        boolean result = validator.validate("Create checking 12345678 1.5 1000");
        assertFalse(result);
    }

    //43
    @Test
    public void extra_parameter_for_savings() {
        boolean result = validator.validate("Create savings 12345678 2.0 500");
        assertFalse(result);
    }

    //44
    @Test
    public void extra_parameter_for_cd() {
        boolean result = validator.validate("Create cd 12345678 2.5 1000 extra");
        assertFalse(result);
    }

    // banking.CD Invalid Cases
    //44
    @Test
    public void cd_with_negative_balance() {
        boolean result = validator.validate("Create cd 12345678 2.5 -1000");
        assertFalse(result);
    }

    //45
    @Test
    public void cd_with_zero_balance() {
        boolean result = validator.validate("Create cd 12345678 2.5 0");
        assertFalse(result);
    }

    //46
    @Test
    public void cd_with_letter_balance() {
        boolean result = validator.validate("Create cd 12345678 2.5 abc");
        assertFalse(result);
    }

    //47
    @Test
    public void cd_balance_with_text() {
        boolean result = validator.validate("Create cd 12345678 2.5 1000dollars");
        assertFalse(result);
    }

    // New Test cases for Same duplicated ID

    //49
    @Test
    public void cannot_create_account_with_existing_id() {
        // Create an account
        Account checking = new Checking("12345678", 1.5);
        bank.addAccount(checking);

        // Try to create another account with same ID (should be invalid)
        boolean result = validator.validate("Create savings 12345678 2.0");
        assertFalse(result);
    }

    //50
    @Test
    public void cannot_create_checking_when_savings_exists_with_same_id() {
        // Create a savings account
        Account savings = new Savings("87654321", 2.0);
        bank.addAccount(savings);

        // Try to create checking with same ID (should be invalid)
        boolean result = validator.validate("Create checking 87654321 1.5");
        assertFalse(result);
    }

    //51
    @Test
    public void cannot_create_cd_when_checking_exists_with_same_id() {
        // Create a checking account
        Account checking = new Checking("11111111", 1.0);
        bank.addAccount(checking);

        // Try to create banking.CD with same ID (should be invalid)
        boolean result = validator.validate("Create cd 11111111 3.0 1000");
        assertFalse(result);
    }

    //52
    @Test
    public void can_create_account_with_different_id() {
        // Create first account
        Account checking = new Checking("12345678", 1.5);
        bank.addAccount(checking);

        // Create second account with DIFFERENT ID (should be valid)
        boolean result = validator.validate("Create savings 87654321 2.0");
        assertTrue(result);
    }

}