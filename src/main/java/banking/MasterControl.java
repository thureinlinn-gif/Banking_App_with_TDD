package banking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterControl {
    private CommandValidator commandValidator;
    private CommandProcessor commandProcessor;
    private CommandStorage commandStorage;
    private Bank bank;
    private Map<String, String> firstDepositPerAccount;
    private List<String> allValidTransfers;

    public MasterControl(CommandValidator commandValidator, CommandProcessor commandProcessor, CommandStorage commandStorage) {
        this.commandValidator = commandValidator;
        this.commandProcessor = commandProcessor;
        this.commandStorage = commandStorage;
        this.firstDepositPerAccount = new HashMap<>();
        this.allValidTransfers = new ArrayList<>();
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public List<String> start(List<String> input) {
        for (String command : input) {
            if (commandValidator.validateCommand(command)) {
                String[] parts = command.split(" ");
                String commandType = parts[0];

                // Track first valid deposit for each account
                if (commandType.equalsIgnoreCase("Deposit")) {
                    String id = parts[1];
                    if (!firstDepositPerAccount.containsKey(id)) {
                        firstDepositPerAccount.put(id, command);
                    }
                } else if (commandType.equalsIgnoreCase("Transfer")) {
                    allValidTransfers.add(command);
                }

                commandProcessor.processCommand(command);
            } else {
                commandStorage.addInvalidCommand(command);
            }
        }

        return generateOutput();
    }

    private List<String> generateOutput() {
        List<String> output = new ArrayList<>();

        // Get accounts that survived Pass
        if (bank != null) {
            List<Account> accounts = bank.getAllAccounts();
            for (int i = 0; i < accounts.size(); i++) {
                Account account = accounts.get(i);
                output.add(formatAccount(account));

                // Add first deposit for this account immediately after account line
                String deposit = firstDepositPerAccount.get(account.getId());
                if (deposit != null) {
                    output.add(deposit);
                }

                // After first account and its deposit, add all transfers before next account
                if (i == 0) {
                    output.addAll(allValidTransfers);
                }
            }
        }

        // Add invalid commands
        output.addAll(commandStorage.getInvalidCommands());

        return output;
    }

    private String formatAccount(Account account) {
        String type = "";
        if (account instanceof Checking) {
            type = "Checking";
        } else if (account instanceof Savings) {
            type = "Savings";
        } else if (account instanceof CD) {
            type = "Cd";
        }

        return String.format("%s %s %.2f %.2f", type, account.getId(), account.getBalance(), account.getApr());
    }
}