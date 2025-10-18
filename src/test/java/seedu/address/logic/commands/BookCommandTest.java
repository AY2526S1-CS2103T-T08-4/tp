package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.FIONA;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class BookCommandTest {

    private static final LocalDateTime VALID_DATETIME = LocalDateTime.of(2025, 12, 25, 10, 0);
    private static final LocalDateTime VALID_DATETIME_2 = LocalDateTime.of(2025, 12, 26, 14, 30);
    private static final String VALID_CLIENT_NAME = "John Doe";
    private static final String VALID_DESCRIPTION = "Consultation";

    @Test
    public void constructor_nullPersonName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new BookCommand(null, VALID_CLIENT_NAME,
                VALID_DATETIME, VALID_DESCRIPTION));
    }

    @Test
    public void constructor_nullClientName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new BookCommand(ALICE.getName(), null,
                VALID_DATETIME, VALID_DESCRIPTION));
    }

    @Test
    public void constructor_nullDateTime_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new BookCommand(ALICE.getName(), VALID_CLIENT_NAME,
                null, VALID_DESCRIPTION));
    }

    @Test
    public void constructor_nullDescription_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new BookCommand(ALICE.getName(), VALID_CLIENT_NAME,
                VALID_DATETIME, null));
    }

    @Test
    public void execute_personNotFound_throwsCommandException() {
        ModelStubWithPersons modelStub = new ModelStubWithPersons();
        BookCommand bookCommand = new BookCommand(new Name("Nonexistent Person"), VALID_CLIENT_NAME,
                VALID_DATETIME, VALID_DESCRIPTION);

        assertCommandFailure(bookCommand, modelStub, String.format(BookCommand.MESSAGE_PERSON_NOT_FOUND,
                "Nonexistent Person"));
    }

    @Test
    public void execute_validBooking_success() throws Exception {
        ModelStubAcceptingBooking modelStub = new ModelStubAcceptingBooking();
        Person personWithBooking = new PersonBuilder(ALICE).build();
        modelStub.addPerson(personWithBooking);

        BookCommand bookCommand = new BookCommand(ALICE.getName(), VALID_CLIENT_NAME,
                VALID_DATETIME, VALID_DESCRIPTION);

        CommandResult commandResult = bookCommand.execute(modelStub);

        String expectedMessage = String.format(BookCommand.MESSAGE_SUCCESS, ALICE.getName(),
                VALID_CLIENT_NAME, "2025-12-25 10:00", VALID_DESCRIPTION);
        assertEquals(expectedMessage, commandResult.getFeedbackToUser());
        assertTrue(modelStub.personsUpdated.size() == 1);
    }

    @Test
    public void execute_doubleBooking_throwsCommandException() {
        // Person with existing booking
        Person personWithExistingBooking = new PersonBuilder(CARL).build();
        ModelStubWithPersons modelStub = new ModelStubWithPersons();
        modelStub.addPerson(personWithExistingBooking);

        // Try to book at the same time as existing booking
        BookCommand bookCommand = new BookCommand(CARL.getName(), "Different Client",
                LocalDateTime.of(2025, 10, 20, 10, 0), "Different Description");

        String expectedMessage = String.format(BookCommand.MESSAGE_DOUBLE_BOOKING, CARL.getName(),
                "2025-10-20 10:00", "Carl Kurz", "Haircut");
        assertCommandFailure(bookCommand, modelStub, expectedMessage);
    }

    @Test
    public void execute_differentTimeSamePerson_success() throws Exception {
        // Person with existing booking
        Person personWithExistingBooking = new PersonBuilder(CARL).build();
        ModelStubAcceptingBooking modelStub = new ModelStubAcceptingBooking();
        modelStub.addPerson(personWithExistingBooking);

        // Book at different time - should succeed
        BookCommand bookCommand = new BookCommand(CARL.getName(), VALID_CLIENT_NAME,
                VALID_DATETIME_2, VALID_DESCRIPTION);

        CommandResult commandResult = bookCommand.execute(modelStub);

        String expectedMessage = String.format(BookCommand.MESSAGE_SUCCESS, CARL.getName(),
                VALID_CLIENT_NAME, "2025-12-26 14:30", VALID_DESCRIPTION);
        assertEquals(expectedMessage, commandResult.getFeedbackToUser());
        assertTrue(modelStub.personsUpdated.size() == 1);
    }

    @Test
    public void execute_sameTimeDifferentPerson_success() throws Exception {
        ModelStubAcceptingBooking modelStub = new ModelStubAcceptingBooking();
        modelStub.addPerson(new PersonBuilder(ALICE).build());
        modelStub.addPerson(new PersonBuilder(FIONA).build());

        // Book Alice at same time as Fiona's existing booking - should succeed (different people)
        BookCommand bookCommand = new BookCommand(ALICE.getName(), VALID_CLIENT_NAME,
                LocalDateTime.of(2025, 10, 20, 14, 0), VALID_DESCRIPTION);

        CommandResult commandResult = bookCommand.execute(modelStub);

        String expectedMessage = String.format(BookCommand.MESSAGE_SUCCESS, ALICE.getName(),
                VALID_CLIENT_NAME, "2025-10-20 14:00", VALID_DESCRIPTION);
        assertEquals(expectedMessage, commandResult.getFeedbackToUser());
        assertTrue(modelStub.personsUpdated.size() == 1);
    }

    @Test
    public void equals() {
        BookCommand bookAliceCommand = new BookCommand(ALICE.getName(), VALID_CLIENT_NAME,
                VALID_DATETIME, VALID_DESCRIPTION);
        BookCommand bookAliceCommandCopy = new BookCommand(ALICE.getName(), VALID_CLIENT_NAME,
                VALID_DATETIME, VALID_DESCRIPTION);
        BookCommand bookBobCommand = new BookCommand(new Name("Bob"), VALID_CLIENT_NAME,
                VALID_DATETIME, VALID_DESCRIPTION);

        // same object -> returns true
        assertTrue(bookAliceCommand.equals(bookAliceCommand));

        // same values -> returns true
        assertTrue(bookAliceCommand.equals(bookAliceCommandCopy));

        // different types -> returns false
        assertFalse(bookAliceCommand.equals(1));

        // null -> returns false
        assertFalse(bookAliceCommand.equals(null));

        // different person -> returns false
        assertFalse(bookAliceCommand.equals(bookBobCommand));

        // different client name -> returns false
        BookCommand differentClientCommand = new BookCommand(ALICE.getName(), "Different Client",
                VALID_DATETIME, VALID_DESCRIPTION);
        assertFalse(bookAliceCommand.equals(differentClientCommand));

        // different datetime -> returns false
        BookCommand differentDateTimeCommand = new BookCommand(ALICE.getName(), VALID_CLIENT_NAME,
                VALID_DATETIME_2, VALID_DESCRIPTION);
        assertFalse(bookAliceCommand.equals(differentDateTimeCommand));

        // different description -> returns false
        BookCommand differentDescriptionCommand = new BookCommand(ALICE.getName(), VALID_CLIENT_NAME,
                VALID_DATETIME, "Different Description");
        assertFalse(bookAliceCommand.equals(differentDescriptionCommand));
    }

    @Test
    public void toStringMethod() {
        BookCommand bookCommand = new BookCommand(ALICE.getName(), VALID_CLIENT_NAME,
                VALID_DATETIME, VALID_DESCRIPTION);
        String expected = BookCommand.class.getCanonicalName() + "{personName=" + ALICE.getName()
                + ", clientName=" + VALID_CLIENT_NAME
                + ", datetime=" + VALID_DATETIME
                + ", description=" + VALID_DESCRIPTION + "}";
        assertEquals(expected, bookCommand.toString());
    }

    /**
     * Executes the given {@code command}, confirms that
     * - a {@code CommandException} is thrown
     * - the CommandException message matches {@code expectedMessage}
     * - the address book, filtered person list and selected person in {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        AddressBook expectedAddressBook = new AddressBook(actualModel.getAddressBook());
        List<Person> expectedFilteredList = new ArrayList<>(actualModel.getFilteredPersonList());

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(actualModel));
        assertEquals(expectedAddressBook, actualModel.getAddressBook());
        assertEquals(expectedFilteredList, actualModel.getFilteredPersonList());
    }

    /**
     * A Model stub that always accepts the person being added.
     */
    private static class ModelStubAcceptingBooking extends ModelStub {
        final ArrayList<Person> personsAdded = new ArrayList<>();
        final ArrayList<Person> personsUpdated = new ArrayList<>();

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return personsAdded.stream().anyMatch(person::isSamePerson);
        }

        @Override
        public void addPerson(Person person) {
            requireNonNull(person);
            personsAdded.add(person);
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            requireNonNull(target);
            requireNonNull(editedPerson);
            personsUpdated.add(editedPerson);
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return FXCollections.observableArrayList(personsAdded);
        }
    }

    /**
     * A Model stub that contains a single person.
     */
    private static class ModelStubWithPersons extends ModelStubAcceptingBooking {
        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return FXCollections.observableArrayList(personsAdded);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            AddressBook addressBook = new AddressBook();
            for (Person person : personsAdded) {
                addressBook.addPerson(person);
            }
            return addressBook;
        }
    }

    /**
     * A default model stub that have all methods failing.
     */
    private static class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(seedu.address.commons.core.GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public seedu.address.commons.core.GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(java.nio.file.Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public java.nio.file.Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook addressBook) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(java.util.function.Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }
    }
}
