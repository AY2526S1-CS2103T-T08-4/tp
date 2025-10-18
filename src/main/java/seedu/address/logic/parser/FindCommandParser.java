package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;
import java.util.List;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.ClientContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public FindCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        String prefix = extractPrefix(trimmedArgs);
        String keywordArgs = extractKeywords(trimmedArgs, prefix);
        List<String> keywords = Arrays.asList(keywordArgs.split("\\s+"));

        return createFindCommand(prefix, keywords);
    }

    /**
     * Extracts the prefix from the arguments.
     *
     * @param args The trimmed arguments string.
     *
     * @return The prefix string (e.g., "n/", "t/", "d/").
     * @throws ParseException if no valid prefix is found.
     */
    private String extractPrefix(String args) throws ParseException {
        if (args.startsWith("n/")) {
            return "n/";
        } else if (args.startsWith("t/")) {
            return "t/";
        } else if (args.startsWith("d/")) {
            return "d/";
        } else {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Extracts the keywords from the arguments after removing the prefix.
     *
     * @param args The trimmed arguments string.
     * @param prefix The prefix to remove.
     *
     * @return The keyword string.
     * @throws ParseException if no keywords are provided.
     */
    private String extractKeywords(String args, String prefix) throws ParseException {
        String keywordArgs = args.substring(prefix.length()).trim();
        if (keywordArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }
        return keywordArgs;
    }

    /**
     * Creates a FindCommand based on the prefix and keywords.
     *
     * @param prefix The search prefix.
     * @param keywords The list of keywords.
     *
     * @return A FindCommand with the appropriate predicate.
     * @throws ParseException if the prefix is invalid.
     */
    private FindCommand createFindCommand(String prefix, List<String> keywords) throws ParseException {
        switch (prefix) {
        case "n/":
            return new FindCommand(
                    new ClientContainsKeywordsPredicate(ClientContainsKeywordsPredicate.SearchType.NAME,
                            keywords));
        case "t/":
            return new FindCommand(
                    new ClientContainsKeywordsPredicate(ClientContainsKeywordsPredicate.SearchType.TAG,
                            keywords));
        case "d/":
            return new FindCommand(
                    new ClientContainsKeywordsPredicate(ClientContainsKeywordsPredicate.SearchType.DATE,
                            keywords));
        default:
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }
    }

}
