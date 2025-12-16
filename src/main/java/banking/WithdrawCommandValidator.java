package banking;

public class WithdrawCommandValidator {

    private Bank bank;

    public WithdrawCommandValidator(Bank bank) {
        this.bank = bank;
    }

    public boolean validate(String command) {
        if (command == null || command.trim().isEmpty()) {
            return false;
        }

        String[] parts = command.split(" ");

        // Withdraw command format: Withdraw <id> <amount>
        // Need exactly 3 parts
        if (parts.length != 3) {
            return false;
        }

        // Check command word
        if (!parts[0].equals("Withdraw")) {
            return false;
        }

        String id = parts[1];
        String amountString = parts[2];

        // Validate ID (must be exactly 8 digits)
        if (!isValidId(id)) {
            return false;
        }

        // Check if account exists
        Account account = bank.getAccountById(id);
        if (account == null) {
            return false;
        }

        // Validate withdrawal amount (must be positive)
        if (!isValidWithdrawAmount(amountString)) {
            return false;
        }

        double amount = Double.parseDouble(amountString);

        // Apply account-specific withdrawal rules
        if (account instanceof Checking) {
            return validateCheckingWithdrawal(account, amount);
        } else if (account instanceof Savings) {
            return validateSavingsWithdrawal((Savings) account, amount);
        } else if (account instanceof CD) {
            return validateCDWithdrawal((CD) account, amount);
        }

        return false;
    }

    private boolean isValidId(String id) {
        // Must be exactly 8 digits
        if (id.length() != 8) {
            return false;
        }

        // Must contain only digits
        for (char c : id.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }

        return true;
    }

    private boolean isValidWithdrawAmount(String amountString) {
        try {
            double amount = Double.parseDouble(amountString);
            // Withdrawal amount must be positive (greater than 0)
            return amount > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean validateCheckingWithdrawal(Account account, double amount) {
        // Checking accounts can withdraw up to $400 per transaction
        return amount <= 400;
    }

    private boolean validateSavingsWithdrawal(Savings account, double amount) {
        // Savings can withdraw up to $1000 per transaction
        if (amount > 1000) {
            return false;
        }

        // Check if savings has already withdrawn this month
        if (account.hasWithdrawnThisMonth()) {
            return false;
        }

        return true;
    }

    private boolean validateCDWithdrawal(CD account, double amount) {
        // CD can only be withdrawn if it has matured (12 months passed)
        if (!account.hasMatured()) {
            return false;
        }

        // CD must withdraw entire balance
        return amount >= account.getBalance();
    }
}