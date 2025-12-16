package banking;

public class CommandProcessor {
    private Bank bank;

    public CommandProcessor(Bank bank) {
        this.bank = bank;
    }

    public void processCommand(String command) {
        String[] parts = command.split(" ");
        String commandType = parts[0];

        if (commandType.equalsIgnoreCase("Create")) {
            processCreate(parts);
        } else if (commandType.equalsIgnoreCase("Deposit")) {
            processDeposit(parts);
        } else if (commandType.equalsIgnoreCase("Withdraw")) {
            processWithdraw(parts);
        } else if (commandType.equalsIgnoreCase("Transfer")) {
            processTransfer(parts);
        } else if (commandType.equalsIgnoreCase("Pass")) {
            processPass(parts);
        }
    }

    private void processCreate(String[] parts) {
        String accountType = parts[1];
        String id = parts[2];
        double apr = Double.parseDouble(parts[3]);

        Account account;

        if (accountType.equalsIgnoreCase("checking")) {
            account = new Checking(id, apr);
        } else if (accountType.equalsIgnoreCase("savings")) {
            account = new Savings(id, apr);
        } else if (accountType.equalsIgnoreCase("cd")) {
            double balance = Double.parseDouble(parts[4]);
            account = new CD(id, apr, balance);
        } else {
            return;
        }

        bank.addAccount(account);
    }

    private void processDeposit(String[] parts) {
        String id = parts[1];
        double amount = Double.parseDouble(parts[2]);
        bank.depositById(id, amount);
    }

    private void processWithdraw(String[] parts) {
        String id = parts[1];
        double amount = Double.parseDouble(parts[2]);

        Account account = bank.getAccountById(id);
        if (account != null) {
            account.withdraw(amount);

            if (account instanceof Savings) {
                ((Savings) account).setWithdrawnThisMonth(true);
            }
        }
    }

    private void processTransfer(String[] parts) {
        String fromId = parts[1];
        String toId = parts[2];
        double amount = Double.parseDouble(parts[3]);

        Account fromAccount = bank.getAccountById(fromId);
        Account toAccount = bank.getAccountById(toId);

        if (fromAccount != null && toAccount != null) {
            fromAccount.withdraw(amount);
            toAccount.deposit(amount);

            if (fromAccount instanceof Savings) {
                ((Savings) fromAccount).setWithdrawnThisMonth(true);
            }
        }
    }

    private void processPass(String[] parts) {
        int months = Integer.parseInt(parts[1]);
        bank.passTime(months);
    }
}