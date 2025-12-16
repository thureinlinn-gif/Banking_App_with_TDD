package banking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class PassCommandValidatorTest {

    private PassCommandValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new PassCommandValidator();
    }

    // Valid passes
    @Test
    public void valid_pass_one_month() {
        assertTrue(validator.validate("Pass 1"));
    }

    @Test
    public void valid_pass_twelve_months() {
        assertTrue(validator.validate("Pass 12"));
    }

    @Test
    public void valid_pass_max_months() {
        assertTrue(validator.validate("Pass 60"));
    }

    @Test
    public void valid_pass_middle_value() {
        assertTrue(validator.validate("Pass 30"));
    }

    // Invalid - months out of range
    @Test
    public void invalid_pass_zero_months() {
        assertFalse(validator.validate("Pass 0"));
    }

    @Test
    public void invalid_pass_negative_months() {
        assertFalse(validator.validate("Pass -1"));
    }

    @Test
    public void invalid_pass_over_max() {
        assertFalse(validator.validate("Pass 61"));
    }

    @Test
    public void invalid_pass_way_over_max() {
        assertFalse(validator.validate("Pass 100"));
    }

    // Invalid - command format
    @Test
    public void invalid_pass_wrong_command() {
        assertFalse(validator.validate("Time 1"));
    }

//    @Test
//    public void invalid_pass_case_sensitive() {
//        assertFalse(validator.validate("PASS 1"));
//    }

//    @Test
//    public void invalid_pass_lowercase() {
//        assertFalse(validator.validate("pass 1"));
//    }

    @Test
    public void invalid_pass_missing_months() {
        assertFalse(validator.validate("Pass"));
    }

    @Test
    public void invalid_pass_extra_parameter() {
        assertFalse(validator.validate("Pass 1 extra"));
    }

    @Test
    public void invalid_pass_not_a_number() {
        assertFalse(validator.validate("Pass abc"));
    }

    @Test
    public void invalid_pass_decimal() {
        assertFalse(validator.validate("Pass 1.5"));
    }

    @Test
    public void invalid_pass_empty_command() {
        assertFalse(validator.validate(""));
    }

    @Test
    public void invalid_pass_null_command() {
        assertFalse(validator.validate(null));
    }

    @Test
    public void invalid_pass_whitespace_only() {
        assertFalse(validator.validate("   "));
    }
}