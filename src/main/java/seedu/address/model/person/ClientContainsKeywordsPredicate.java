package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code Name} matches any of the keywords given.  
 */public class ClientContainsKeywordsPredicate implements Predicate<Person> {
    public enum SearchType { NAME, TAG, DATE}

    private final List<String> keywords;
    private final SearchType type;

    public ClientContainsKeywordsPredicate(SearchType type, List<String> keywords) {
        this.keywords = keywords;
        this.type = type;
    }

    @Override
    public boolean test(Person person) {
        switch (type) {

            case NAME:
                return keywords.stream()
                        .anyMatch(keyword ->
                                person.getName().fullName.toLowerCase().contains(keyword.toLowerCase()));

            case TAG:
                return person.getTags().stream()
                        .map(tag -> tag.tagName.toLowerCase())
                        .anyMatch(tagName ->
                                keywords.stream()
                                        .anyMatch(keyword -> tagName.equals(keyword.toLowerCase())));


            case DATE:
                return keywords.stream()
                        .anyMatch(keyword ->
                                person.getBookings().stream()
                                        .map(booking -> booking.getDateTime().toLocalDate().toString())
                                        .anyMatch(date -> date.equals(keyword)));

        }            
        return false;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls  
        if (!(other instanceof ClientContainsKeywordsPredicate)) {
            return false;
        }

        ClientContainsKeywordsPredicate otherNameContainsKeywordsPredicate = (ClientContainsKeywordsPredicate) other;
        return (this.type == otherNameContainsKeywordsPredicate.type) && (
                keywords.equals(otherNameContainsKeywordsPredicate.keywords));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
