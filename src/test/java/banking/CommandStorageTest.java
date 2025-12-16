package banking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

public class CommandStorageTest {

    private CommandStorage storage;

    @BeforeEach
    public void setUp() {
        storage = new CommandStorage();
    }

    @Test
    public void storage_initial_null() {
        List<String> commands = storage.getInvalidCommands();
        assertTrue(commands.isEmpty());
    }

    @Test
    public void adding_one_invalid_command() {
        storage.addInvalidCommand("Create xyz 12345678 1.5");
        List<String> commands = storage.getInvalidCommands();
        assertEquals(1, commands.size());
    }

    @Test
    public void getting_added_invalid_command() {
        storage.addInvalidCommand("Create xyz 12345678 1.5");
        List<String> commands = storage.getInvalidCommands();
        assertEquals("Create xyz 12345678 1.5", commands.get(0));
    }

    @Test
    public void adding_multiple_invalid_commands() {
        storage.addInvalidCommand("Create xyz 12345678 1.5");
        storage.addInvalidCommand("deposit 87654321 100");
        List<String> commands = storage.getInvalidCommands();
        assertEquals(2, commands.size());
    }

    @Test
    public void stored_in_order_invalid_commands() {
        storage.addInvalidCommand("First command");
        storage.addInvalidCommand("Second command");
        storage.addInvalidCommand("Third command");
        List<String> commands = storage.getInvalidCommands();
        assertEquals("First command", commands.get(0));
        assertEquals("Second command", commands.get(1));
        assertEquals("Third command", commands.get(2));
    }

    @Test
    public void adding_several_invalid_commands() {
        storage.addInvalidCommand("Command 1");
        storage.addInvalidCommand("Command 2");
        storage.addInvalidCommand("Command 3");
        storage.addInvalidCommand("Command 4");
        storage.addInvalidCommand("Command 5");

        List<String> commands = storage.getInvalidCommands();
        assertEquals(5, commands.size());
    }

    @Test
    public void returning_copy_storage_list() {
        storage.addInvalidCommand("Test command");

        List<String> commands1 = storage.getInvalidCommands();
        List<String> commands2 = storage.getInvalidCommands();

        //To prevent from the affect of updating one command over another command
        commands1.add("Extra command");
        assertEquals(1, commands2.size());
    }
}