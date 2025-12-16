package banking;

public class CommandValidator {

    private final CreateCommandValidator createValidator;
    private final DepositCommandValidator depositValidator;
    private final WithdrawCommandValidator withdrawValidator;
    private final TransferCommandValidator transferValidator;
    private final PassCommandValidator passValidator;
    private Bank bank;

    public CommandValidator(Bank bank) {
        this.bank = bank;
        this.createValidator = new CreateCommandValidator(bank);
        this.depositValidator = new DepositCommandValidator(bank);
        this.withdrawValidator = new WithdrawCommandValidator(bank);
        this.transferValidator = new TransferCommandValidator(bank);
        this.passValidator = new PassCommandValidator();
    }

    public boolean validateCommand(String command) {
        if (command == null || command.trim().isEmpty()) {
            return false;
        }

        String[] parts = command.split(" ");

        // No need to check parts.length < 1, split always returns at least 1 element
        String commandType = parts[0];

        // Make command type case-insensitive
        if (commandType.equalsIgnoreCase("Create")) {
            return createValidator.validate(command);
        } else if (commandType.equalsIgnoreCase("Deposit")) {
            return depositValidator.validate(command);
        } else if (commandType.equalsIgnoreCase("Withdraw")) {
            return withdrawValidator.validate(command);
        } else if (commandType.equalsIgnoreCase("Transfer")) {
            return transferValidator.validate(command);
        } else if (commandType.equalsIgnoreCase("Pass")) {
            return passValidator.validate(command);
        } else {
            return false;
        }
    }
}