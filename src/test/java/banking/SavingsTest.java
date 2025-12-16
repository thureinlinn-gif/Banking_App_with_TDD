package banking;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SavingsTest {
    @Test
    public void savings_account_balance_zero_start(){
        Savings savings = new Savings("12345678",1.0);
        assertEquals(0,savings.getBalance());
    }

    @Test
    public void savings_account_apr(){
        Savings savings = new Savings("12345678",2.5);
        assertEquals(2.5,savings.getApr());
    }

    @Test
    public void savings_account_id(){
        Savings savings = new Savings("12345678",2.0);
        assertEquals("12345678", savings.getId());
    }

    @Test
    public void savings_starts_with_no_withdrawal_this_month() {
        Savings savings = new Savings("12345678", 2.0);
        assertFalse(savings.hasWithdrawnThisMonth());
    }

    @Test
    public void savings_can_mark_as_withdrawn() {
        Savings savings = new Savings("12345678", 2.0);
        savings.setWithdrawnThisMonth(true);
        assertTrue(savings.hasWithdrawnThisMonth());
    }

    @Test
    public void savings_can_reset_withdrawal_limit() {
        Savings savings = new Savings("12345678", 2.0);
        savings.setWithdrawnThisMonth(true);
        savings.resetWithdrawalLimit();
        assertFalse(savings.hasWithdrawnThisMonth());
    }

    @Test
    public void savings_withdrawal_status_persists() {
        Savings savings = new Savings("12345678", 2.0);
        savings.setWithdrawnThisMonth(true);
        assertTrue(savings.hasWithdrawnThisMonth());
        assertTrue(savings.hasWithdrawnThisMonth()); // Check again
    }

    @Test
    public void savings_can_set_to_false_explicitly() {
        Savings savings = new Savings("12345678", 2.0);
        savings.setWithdrawnThisMonth(true);
        savings.setWithdrawnThisMonth(false);
        assertFalse(savings.hasWithdrawnThisMonth());
    }

    @Test
    public void savings_reset_after_already_reset() {
        Savings savings = new Savings("12345678", 2.0);
        savings.resetWithdrawalLimit();
        savings.resetWithdrawalLimit();
        assertFalse(savings.hasWithdrawnThisMonth());
    }

    @Test
    public void savings_multiple_set_and_reset_operations() {
        Savings savings = new Savings("12345678", 2.0);
        savings.setWithdrawnThisMonth(true);
        assertTrue(savings.hasWithdrawnThisMonth());
        savings.resetWithdrawalLimit();
        assertFalse(savings.hasWithdrawnThisMonth());
        savings.setWithdrawnThisMonth(true);
        assertTrue(savings.hasWithdrawnThisMonth());
    }
}
