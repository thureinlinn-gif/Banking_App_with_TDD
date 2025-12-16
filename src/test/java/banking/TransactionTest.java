package banking;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransactionTest {

    @Test
    public void transaction_stores_type() {
        Transaction transaction = new Transaction("deposit", 100.0, System.currentTimeMillis());
        assertEquals("deposit", transaction.getType());
    }

    @Test
    public void transaction_stores_amount() {
        Transaction transaction = new Transaction("withdraw", 50.0, System.currentTimeMillis());
        assertEquals(50.0, transaction.getAmount());
    }

    @Test
    public void transaction_stores_timestamp() {
        long timestamp = System.currentTimeMillis();
        Transaction transaction = new Transaction("deposit", 100.0, timestamp);
        assertEquals(timestamp, transaction.getTimestamp());
    }

    @Test
    public void transaction_formatted_date_not_null() {
        Transaction transaction = new Transaction("deposit", 100.0, System.currentTimeMillis());
        assertNotNull(transaction.getFormattedDate());
    }

    @Test
    public void transaction_formatted_date_contains_year() {
        Transaction transaction = new Transaction("deposit", 100.0, System.currentTimeMillis());
        String formatted = transaction.getFormattedDate();
        assertTrue(formatted.contains("2024") || formatted.contains("2025"));
    }

    @Test
    public void transaction_toString_contains_type() {
        Transaction transaction = new Transaction("deposit", 100.0, System.currentTimeMillis());
        String result = transaction.toString();
        assertTrue(result.contains("DEPOSIT"));
    }

    @Test
    public void transaction_toString_contains_amount() {
        Transaction transaction = new Transaction("withdraw", 75.50, System.currentTimeMillis());
        String result = transaction.toString();
        assertTrue(result.contains("75.50"));
    }

    @Test
    public void transaction_with_decimal_amount() {
        Transaction transaction = new Transaction("deposit", 123.45, System.currentTimeMillis());
        assertEquals(123.45, transaction.getAmount());
    }

    @Test
    public void transaction_with_large_amount() {
        Transaction transaction = new Transaction("deposit", 10000.0, System.currentTimeMillis());
        assertEquals(10000.0, transaction.getAmount());
    }

    @Test
    public void transaction_with_small_amount() {
        Transaction transaction = new Transaction("deposit", 0.01, System.currentTimeMillis());
        assertEquals(0.01, transaction.getAmount());
    }

    @Test
    public void transaction_type_withdraw() {
        Transaction transaction = new Transaction("withdraw", 100.0, System.currentTimeMillis());
        assertEquals("withdraw", transaction.getType());
    }

    @Test
    public void transaction_type_transfer() {
        Transaction transaction = new Transaction("transfer_in", 200.0, System.currentTimeMillis());
        assertEquals("transfer_in", transaction.getType());
    }

    @Test
    public void different_transactions_have_different_timestamps() {
        long timestamp1 = System.currentTimeMillis();
        Transaction transaction1 = new Transaction("deposit", 100.0, timestamp1);

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            // Ignore
        }

        long timestamp2 = System.currentTimeMillis();
        Transaction transaction2 = new Transaction("deposit", 100.0, timestamp2);

        assertTrue(transaction2.getTimestamp() > transaction1.getTimestamp());
    }
}