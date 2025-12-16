package banking;

public class DepositCommandValidator {

    private Bank bank;

    public DepositCommandValidator(Bank bank) {
        this.bank = bank;
    }

    public DepositCommandValidator() {
        this.bank = null;
    }

    public boolean validate(String command) {
        if (command == null || command.trim().isEmpty()) {
            return false;
        }

        String[] parts = command.split(" ");

        if (parts.length != 3) {
            return false;
        }

        if (!parts[0].equalsIgnoreCase("Deposit")) {
            return false;
        }

        String id = parts[1];
        String amountString = parts[2];

        if (!isValidId(id)) {
            return false;
        }

        if (!isValidDepositAmount(amountString)) {
            return false;
        }

        // Check account-specific deposit limits if bank is available
        if (bank != null) {
            Account account = bank.getAccountById(id);
            if (account != null) {
                double amount = Double.parseDouble(amountString);

                if (account instanceof Checking) {
                    if (amount > 1000) {
                        return false;
                    }
                } else if (account instanceof Savings) {
                    if (amount > 2500) {
                        return false;
                    }
                } else if (account instanceof CD) {
                    // CD cannot accept deposits
                    return false;
                }
            }
        }

        return true;
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

    private boolean isValidDepositAmount(String amountString) {
        try {
            double amount = Double.parseDouble(amountString);
            return amount > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}