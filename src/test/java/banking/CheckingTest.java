package banking;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CheckingTest {

    @Test
    public void checking_account_balannce_zero_start(){
        Checking checking = new Checking("12345678", 1.0);
        assertEquals(0, checking.getBalance());
    }

    @Test
    public void checking_account_apr(){
        Checking checking = new Checking("12345678",3.5);
        assertEquals(3.5, checking.getApr());
    }

    @Test
    public void checking_account_id(){
        Checking checking = new Checking("12345678",3.5);
        assertEquals("12345678", checking.getId());
    }
//    @Test
//    public void deposiitng_money_increasea_balance(){
//        banking.Checking checking = new banking.Checking("12345678", 1.0);
//        checking.deposit(100);
//        assertEquals(100, checking.getBalance());
//    }
//
//    @Test
//    public void depositing_twice_works(){
//        banking.Checking checking = new banking.Checking("12345678", 1.0);
//        checking.deposit(100);
//        checking.deposit(50);
//        assertEquals(150, checking.getBalance());
//    }
//
//    @Test
//    public void withdrawing_money_decreases_balance(){
//        banking.Checking checking = new banking.Checking("12345678", 1.0);
//        checking.deposit(200);
//        checking.withdraw(75);
//        assertEquals(125, checking.getBalance());
//    }
//
//    @Test
//    public void balance_cannot_go_below_zero(){
//        banking.Checking checking = new banking.Checking("12345678", 1.0);
//        checking.deposit(100);
//        checking.withdraw(200);
//        assertEquals(0, checking.getBalance());
//    }
//
//    @Test
//    public void withdrawing_twice_works(){
//        banking.Checking checking = new banking.Checking("12345678", 1.0);
//        checking.deposit(200);
//        checking.withdraw(50);
//        checking.withdraw(25);
//        assertEquals(125, checking.getBalance());
//    }

}
