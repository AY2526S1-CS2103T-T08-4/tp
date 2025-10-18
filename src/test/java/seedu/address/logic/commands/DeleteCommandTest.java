package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validDeletion_success() {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(personToDelete.getName(), Optional.empty());

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidNameUnfilteredList_throwsCommandException() {
        Name nonexistentName = new Name("Nonexistent Person");
        DeleteCommand deleteCommand = new DeleteCommand(nonexistentName, Optional.empty());

        assertCommandFailure(deleteCommand, model,
                String.format(DeleteCommand.MESSAGE_DELETE_PERSON_NOT_FOUND, nonexistentName.fullName));
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(personToDelete.getName(), Optional.empty());

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);
        showNoPerson(expectedModel);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deleteMultipleTags_success() throws CommandException {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person target = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Set<Tag> targetTag = new LinkedHashSet<>();
        targetTag.add(new Tag("tagOne"));
        targetTag.add(new Tag("tagTwo"));

        Person newPerson = new Person(
                target.getName(),
                target.getPhone(),
                target.getEmail(),
                targetTag,
                target.getBookings()
        );
        model.setPerson(target, newPerson);

        DeleteCommand deleteCommand = new DeleteCommand(newPerson.getName(),
                Optional.of(new LinkedHashSet<>(targetTag)));

        deleteCommand.execute(model);

        Person after = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        assertTrue(after.getTags().isEmpty());
    }

    @Test
    public void execute_deleteTags_someMissing() throws CommandException {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person target = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Set<Tag> targetTag = new LinkedHashSet<>();
        targetTag.add(new Tag("tag"));

        Set<Tag> testTag = Set.of(new Tag("tag"), new Tag("vip"), new Tag("friends"));

        Person newPerson = new Person(
                target.getName(),
                target.getPhone(),
                target.getEmail(),
                targetTag,
                target.getBookings()
        );
        model.setPerson(target, newPerson);

        DeleteCommand deleteCommand = new DeleteCommand(newPerson.getName(),
                Optional.of(new LinkedHashSet<>(testTag)));

        CommandResult res = deleteCommand.execute(model);
        String msg = res.getFeedbackToUser().toLowerCase();

        Person after = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        assertTrue(msg.contains("not found:"));
        assertTrue(msg.contains("vip"));
        assertTrue(msg.contains("friends"));
        assertFalse(after.getTags().contains(new Tag("tag")));
    }

    @Test
    public void execute_deleteTag_allMissing() throws CommandException {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person target = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Set<Tag> targetTag = new LinkedHashSet<>();
        targetTag.add(new Tag("tag"));

        Set<Tag> testTag = Set.of(new Tag("vip"), new Tag("friends"));

        Person newPerson = new Person(
                target.getName(),
                target.getPhone(),
                target.getEmail(),
                targetTag,
                target.getBookings()
        );
        model.setPerson(target, newPerson);

        DeleteCommand deleteCommand = new DeleteCommand(newPerson.getName(),
                Optional.of(new LinkedHashSet<>(testTag)));

        assertThrows(CommandException.class, () -> deleteCommand.execute(model));
    }


    @Test
    public void equals() {
        DeleteCommand deleteFirstCommand = new DeleteCommand(new Name("Alex Yeoh"), Optional.empty());
        DeleteCommand deleteSecondCommand = new DeleteCommand(new Name("Bernice Tan"), Optional.empty());

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new DeleteCommand(new Name("Alex Yeoh"), Optional.empty());
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Name targetName = new Name("Alex Yeoh");
        DeleteCommand deleteCommand = new DeleteCommand(targetName, Optional.empty());
        String expected = DeleteCommand.class.getCanonicalName() + "{targetName=" + targetName + "}";
        assertEquals(expected, deleteCommand.toString());
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);

        assertTrue(model.getFilteredPersonList().isEmpty());
    }
}
