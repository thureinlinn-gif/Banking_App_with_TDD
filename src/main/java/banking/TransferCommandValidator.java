package banking;

public class TransferCommandValidator {

    private Bank bank;

    public TransferCommandValidator(Bank bank) {
        this.bank = bank;
    }

    public boolean validate(String command) {
        if (command == null || command.trim().isEmpty()) {
            return false;
        }

        String[] parts = command.split(" ");

        if (parts.length != 4) {
            return false;
        }

        if (!parts[0].equalsIgnoreCase("Transfer")) {
            return false;
        }

        String fromId = parts[1];
        String toId = parts[2];
        String amountString = parts[3];

        if (!isValidId(fromId) || !isValidId(toId)) {
            return false;
        }

        Account fromAccount = bank.getAccountById(fromId);
        Account toAccount = bank.getAccountById(toId);

        if (fromAccount == null || toAccount == null) {
            return false;
        }

        if (!isValidAmount(amountString)) {
            return false;
        }

        double amount = Double.parseDouble(amountString);

        if (amount <= 0) {
            return false;
        }

        if (fromAccount instanceof Checking) {
            return amount <= 400;
        } else if (fromAccount instanceof Savings) {
            Savings savings = (Savings) fromAccount;
            if (savings.hasWithdrawnThisMonth()) {
                return false;
            }
            return amount <= 1000;
        } else if (fromAccount instanceof CD) {
            CD cd = (CD) fromAccount;
            if (!cd.hasMatured()) {
                return false;
            }
            return amount >= cd.getBalance();
        }

        return false;
    }

    private boolean isValidId(String id) {
        if (id.length() != 8) {
            return false;
        }
        for (char c : id.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidAmount(String amountString) {
        try {
            Double.parseDouble(amountString);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}