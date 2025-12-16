package banking;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CDTest {
    @Test
    public void cd_account_starts_with_specified_balance(){
        CD cd = new CD("12345678", 2.5, 1000);
        assertEquals(1000, cd.getBalance());
    }

    @Test
    public void cd_starts_with_zero_months_passed() {
        CD cd = new CD("12345678", 2.5, 1000);
        assertEquals(0, cd.getMonthsPassed());
    }

    @Test
    public void cd_increment_months_by_one() {
        CD cd = new CD("12345678", 2.5, 1000);
        cd.incrementMonths(1);
        assertEquals(1, cd.getMonthsPassed());
    }

    @Test
    public void cd_increment_months_by_twelve() {
        CD cd = new CD("12345678", 2.5, 1000);
        cd.incrementMonths(12);
        assertEquals(12, cd.getMonthsPassed());
    }

    @Test
    public void cd_increment_months_multiple_times() {
        CD cd = new CD("12345678", 2.5, 1000);
        cd.incrementMonths(6);
        cd.incrementMonths(6);
        assertEquals(12, cd.getMonthsPassed());
    }

    @Test
    public void cd_increment_months_accumulates() {
        CD cd = new CD("12345678", 2.5, 1000);
        cd.incrementMonths(3);
        cd.incrementMonths(4);
        cd.incrementMonths(5);
        assertEquals(12, cd.getMonthsPassed());
    }

    @Test
    public void cd_not_matured_initially() {
        CD cd = new CD("12345678", 2.5, 1000);
        assertFalse(cd.hasMatured());
    }

    @Test
    public void cd_not_matured_at_11_months() {
        CD cd = new CD("12345678", 2.5, 1000);
        cd.incrementMonths(11);
        assertFalse(cd.hasMatured());
    }

    @Test
    public void cd_matured_at_exactly_12_months() {
        CD cd = new CD("12345678", 2.5, 1000);
        cd.incrementMonths(12);
        assertTrue(cd.hasMatured());
    }

    @Test
    public void cd_matured_after_12_months() {
        CD cd = new CD("12345678", 2.5, 1000);
        cd.incrementMonths(15);
        assertTrue(cd.hasMatured());
    }

    @Test
    public void cd_matured_at_24_months() {
        CD cd = new CD("12345678", 2.5, 1000);
        cd.incrementMonths(24);
        assertTrue(cd.hasMatured());
    }

    @Test
    public void cd_maturity_period_is_12() {
        CD cd = new CD("12345678", 2.5, 1000);
        assertEquals(12, cd.getMaturityPeriod());
    }

    @Test
    public void cd_increment_zero_months() {
        CD cd = new CD("12345678", 2.5, 1000);
        cd.incrementMonths(0);
        assertEquals(0, cd.getMonthsPassed());
    }

    @Test
    public void cd_not_matured_at_6_months() {
        CD cd = new CD("12345678", 2.5, 1000);
        cd.incrementMonths(6);
        assertFalse(cd.hasMatured());
    }

    @Test
    public void cd_balance_stored_correctly() {
        CD cd = new CD("11111111", 3.0, 2500.50);
        assertEquals(2500.50, cd.getBalance());
    }

    @Test
    public void cd_apr_stored_correctly() {
        CD cd = new CD("11111111", 4.5, 1000);
        assertEquals(4.5, cd.getApr());
    }

    @Test
    public void cd_id_stored_correctly() {
        CD cd = new CD("11111111", 3.0, 1000);
        assertEquals("11111111", cd.getId());
    }
}
