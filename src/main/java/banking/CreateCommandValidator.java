package banking;

public class CreateCommandValidator {

    private Bank bank;

    public CreateCommandValidator(Bank bank){
        this.bank = bank;
    }

    public boolean validate(String command) {
        if (command == null || command.trim().isEmpty()) {
            return false;
        }

        String[] parts = command.split(" ");

        if (parts.length < 4) {
            return false;
        }

        if (!parts[0].equalsIgnoreCase("Create")) {
            return false;
        }

        String accountType = parts[1];
        String id = parts[2];
        String aprString = parts[3];

        if (!isValidAccountType(accountType)) {
            return false;
        }

        if (!isValidId(id)) {
            return false;
        }

        if(idAlreadyExists(id)){
            return false;
        }

        if (!isValidApr(aprString)) {
            return false;
        }

        if (accountType.equalsIgnoreCase("cd")) {
            if (parts.length < 5) {
                return false;
            }
            String balanceString = parts[4];
            if (!isValidBalance(balanceString)) {
                return false;
            }
            if (parts.length > 5) {
                return false;
            }
        } else {
            if (parts.length > 4) {
                return false;
            }
        }

        return true;
    }

    private boolean isValidAccountType(String type) {
        return type.equalsIgnoreCase("checking") || type.equalsIgnoreCase("savings") || type.equalsIgnoreCase("cd");
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

    private boolean idAlreadyExists(String id){
        Account account = bank.getAccountById(id);
        return account != null;
    }

    private boolean isValidApr(String aprString) {
        try {
            double apr = Double.parseDouble(aprString);
            return apr >= 0 && apr <= 10;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidBalance(String balanceString) {
        try {
            double balance = Double.parseDouble(balanceString);
            return balance > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}