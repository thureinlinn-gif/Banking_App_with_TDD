package banking;

public class PassCommandValidator {

    public boolean validate(String command) {
        if (command == null || command.trim().isEmpty()) {
            return false;
        }

        String[] parts = command.split(" ");

        if (parts.length != 2) {
            return false;
        }

        if (!parts[0].equalsIgnoreCase("Pass")) {
            return false;
        }

        try {
            int months = Integer.parseInt(parts[1]);
            return months >= 1 && months <= 60;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}