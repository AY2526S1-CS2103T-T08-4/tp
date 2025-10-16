package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person or tag specified from the displayed person list.\n"
            + "Parameters:\n NAME (must be a saved contact)\n"
            + "TAG (optional, an available tag saved with the contact) \n"
            + "Example:\n" + COMMAND_WORD + " n/" + " Alex" + " (to delete a whole contact)\n"
            + COMMAND_WORD + " n/" + " Alex" + " t/" + " tag_1" + " tag_2..."
            + " (to delete 1 or more specific tags from 'Alex')";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";
    public static final String MESSAGE_DELETE_PERSON_NOT_FOUND = "No such person found: %s";
    public static final String MESSAGE_DELETE_PERSON_MULTIPLE_MATCH = "Multiple matches for %s: \n%s\n";
    public static final String MESSAGE_DELETE_TAG_SUCCESS = "Removed tags %1$s from %2$s";
    public static final String MESSAGE_DELETE_TAG_PARTIAL = "Removed %1$s. Not found: %2$s from %3$s";
    public static final String MESSAGE_DELETE_TAG_NOT_FOUND = "'%1$s' does not have the tag(s) '%2$s'";
    public static final String MESSAGE_DELETE_TAG_USAGE = "Please provide a tag after 't/'!";

    private final Name targetName;
    private final Optional<Set<Tag>> tags;

    /**
     * @brief   Constructor for delete command
     * @param targetName    Name of the contact we are deleting from
     * @param tags          Optionals containing the set of Tags we want to delete from the contact
     */
    public DeleteCommand(Name targetName, Optional<Set<Tag>> tags) {
        requireNonNull(targetName);
        requireNonNull(tags);
        this.targetName = targetName;
        this.tags = tags;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        Person personToDelete = findUniquePerson(lastShownList, targetName);

        if (tags.isEmpty()) {
            model.deletePerson(personToDelete);
            return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete)));
        }

        Set<Tag> tagsToRemove = tags.get();
        Set<Tag> curr = new LinkedHashSet<>(personToDelete.getTags());

        Set<Tag> present = new LinkedHashSet<>();
        Set<Tag> missing = new LinkedHashSet<>();

        for (Tag t : tagsToRemove) {
            if (curr.contains(t)) {
                present.add(t);
            } else {
                missing.add(t);
            }
        }

        if (present.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_DELETE_TAG_NOT_FOUND,
                    personToDelete.getName().fullName, missing));
        }

        curr.removeAll(present);

        Person updatedPerson = new Person(
                personToDelete.getName(),
                personToDelete.getPhone(),
                personToDelete.getEmail(),
                curr,
                personToDelete.getBookings()
        );

        model.setPerson(personToDelete, updatedPerson);
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        String removed = tagsToRemove.stream().map(Tag::toString).collect(Collectors.joining(", "));

        if (!missing.isEmpty()) {
            return new CommandResult(String.format(MESSAGE_DELETE_TAG_PARTIAL, present,
                    missing, personToDelete.getName().fullName));
        }

        return new CommandResult(String.format(MESSAGE_DELETE_TAG_SUCCESS, removed, updatedPerson.getName().fullName));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return targetName.equals(otherDeleteCommand.targetName) && tags.equals(otherDeleteCommand.tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetName", targetName)
                .toString();
    }

    /**
     * @brief           This method checks if target name is contained in another name.
     * @param name      current name in list
     * @param target    name that we want to find
     * @return          true if current name in list contains the target, else return false
     */
    public Boolean containsIgnoreCase(Name name, Name target) {
        if (name == null) {
            return false;
        }
        String firstName = name.fullName.toLowerCase();
        String targetName = target.fullName.toLowerCase();
        return firstName.contains(targetName);
    }

    private Person findUniquePerson(List<Person> list, Name name) throws CommandException {
        String queryName = name.toString().trim().replaceAll("\\s+", " ");

        List<Person> exactMatch = list.stream()
                .filter(x -> x.getName().toString().replaceAll("\\s+", " ")
                        .equalsIgnoreCase(queryName))
                .toList();
        if (exactMatch.size() == 1) {
            return exactMatch.get(0);
        }
        if (exactMatch.size() > 1) {
            String allMatches = exactMatch.stream()
                    .map(Messages::format)
                    .collect(Collectors.joining("\n"));
            throw new CommandException(
                    String.format(MESSAGE_DELETE_PERSON_MULTIPLE_MATCH, name.fullName, allMatches));
        }

        List<Person> contains = list.stream()
                .filter(x -> x.getName().toString().toLowerCase().replaceAll("\\s+", " ")
                        .contains(queryName.toLowerCase()))
                .toList();
        if (contains.size() == 1) {
            return contains.get(0);
        }

        if (contains.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_DELETE_PERSON_NOT_FOUND, name.fullName));
        }
        String containsMultiple = contains.stream()
                .map(Messages::format)
                .collect(Collectors.joining("\n"));
        throw new CommandException(String.format(MESSAGE_DELETE_PERSON_MULTIPLE_MATCH,
                name.fullName, containsMultiple));
    }

}
