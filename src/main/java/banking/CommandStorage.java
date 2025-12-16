package banking;

import java.util.ArrayList;
import java.util.List;

// A Class to store invalid or incorrect command strings
public class CommandStorage {


    // A list that keeps all invalid commands entered by the user
    private List<String> invalidCommands;

    // Constructor - creates an empty list to store invalid commands
    public CommandStorage() {
        this.invalidCommands = new ArrayList<>();
    }

    // Adds an invalid command to the list
    public void addInvalidCommand(String command) {
        invalidCommands.add(command);
    }

    // Returns a copy of the list of invalid commands
    public List<String> getInvalidCommands() {
        return new ArrayList<>(invalidCommands);
    }
}