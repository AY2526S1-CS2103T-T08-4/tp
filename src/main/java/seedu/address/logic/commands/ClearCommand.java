package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Address book has been cleared!";

    private static final Logger logger = LogsCenter.getLogger(ClearCommand.class);


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        logger.warning("Executing ClearCommand - this will delete all data!");

        // Log the number of persons being deleted for audit trail
        int personCount = model.getAddressBook().getPersonList().size();
        logger.info(String.format("Clearing address book with %d persons", personCount));

        model.setAddressBook(new AddressBook());

        logger.warning("Address book successfully cleared - all data deleted");
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
