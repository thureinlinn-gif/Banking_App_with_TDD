package banking;

import java.util.ArrayList;
import java.util.List;

public abstract class Account {
    String id;
    double apr;
    double balance;
    private List<Transaction> transactionHistory;

    public Account(String id, double apr){
        this.id = id;
        this.apr = apr;
        this.balance = 0;
        this.transactionHistory = new ArrayList<>();
    }

    public double getBalance(){
        return balance;
    }

    public double getApr(){
        return apr;
    }

    public String getId(){
        return id;
    }

    public void deposit(double amount){
        balance += amount;
        addTransaction("deposit", amount);
    }

    public void withdraw(double amount){
        balance -= amount;
        if(balance<0){
            balance = 0;
        }
        addTransaction("withdraw", amount);
    }

    public void addTransaction(String type, double amount) {
        Transaction transaction = new Transaction(type, amount, System.currentTimeMillis());
        transactionHistory.add(transaction);
    }

    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }

    public void applyInterest(int months) {
        for (int i = 0; i < months; i++) {
            double monthlyInterest = (balance * (apr / 100)) / 12;
            balance += monthlyInterest;
        }
    }
}
