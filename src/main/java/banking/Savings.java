package banking;

public class Savings extends Account {
//    private double balance;
//    private double apr;

    private boolean hasWithdrawnThisMonth;

    public Savings(String id, double apr){
        super(id, apr);
        this.hasWithdrawnThisMonth = false;
    }

    public boolean hasWithdrawnThisMonth() {
        return hasWithdrawnThisMonth;
    }

    public void setWithdrawnThisMonth(boolean hasWithdrawn) {
        this.hasWithdrawnThisMonth = hasWithdrawn;
    }

    public void resetWithdrawalLimit() {
        this.hasWithdrawnThisMonth = false;
    }
//    public double getBalance() {
//        return balance;
//    }
//
//    public double getApr() {
//        return apr;
//    }
//
//    public void deposit(double amount) {
//        balance = balance + amount;
//    }
//
//    public void withdraw(double amount) {
//        balance = balance - amount;
//        if (balance < 0){
//            balance = 0;
//        }
//    }

}
