package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class ClientContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstKeywordList = Collections.singletonList("first");
        List<String> secondKeywordList = Arrays.asList("first", "second");

        ClientContainsKeywordsPredicate firstPredicate =
                new ClientContainsKeywordsPredicate(ClientContainsKeywordsPredicate.SearchType.NAME, firstKeywordList);
        ClientContainsKeywordsPredicate secondPredicate =
                new ClientContainsKeywordsPredicate(ClientContainsKeywordsPredicate.SearchType.NAME, secondKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        ClientContainsKeywordsPredicate firstPredicateCopy =
                new ClientContainsKeywordsPredicate(ClientContainsKeywordsPredicate.SearchType.NAME, firstKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different keyword list -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));

        // different search type -> returns false
        ClientContainsKeywordsPredicate tagPredicate =
                new ClientContainsKeywordsPredicate(ClientContainsKeywordsPredicate.SearchType.TAG, firstKeywordList);
        assertFalse(firstPredicate.equals(tagPredicate));
    }

    // ===============================
    // NAME TESTS
    // ===============================

    @Test
    public void test_nameContainsKeywords_returnsTrue() {
        ClientContainsKeywordsPredicate predicate =
                new ClientContainsKeywordsPredicate(ClientContainsKeywordsPredicate.SearchType.NAME,
                        Collections.singletonList("Alice"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        predicate = new ClientContainsKeywordsPredicate(ClientContainsKeywordsPredicate.SearchType.NAME,
                Arrays.asList("Alice", "Bob"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        predicate = new ClientContainsKeywordsPredicate(ClientContainsKeywordsPredicate.SearchType.NAME,
                Arrays.asList("Bob", "Carol"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Carol").build()));

        predicate = new ClientContainsKeywordsPredicate(ClientContainsKeywordsPredicate.SearchType.NAME,
                Arrays.asList("aLIce", "bOB"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));
    }

    @Test
    public void test_nameDoesNotContainKeywords_returnsFalse() {
        ClientContainsKeywordsPredicate predicate =
                new ClientContainsKeywordsPredicate(ClientContainsKeywordsPredicate.SearchType.NAME,
                        Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").build()));

        predicate = new ClientContainsKeywordsPredicate(ClientContainsKeywordsPredicate.SearchType.NAME,
                Arrays.asList("Carol"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").build()));
    }

    // ===============================
    // TAG TESTS
    // ===============================

    @Test
    public void test_tagContainsKeywords_returnsTrue() {
        ClientContainsKeywordsPredicate predicate =
                new ClientContainsKeywordsPredicate(ClientContainsKeywordsPredicate.SearchType.TAG,
                        Collections.singletonList("vip"));
        assertTrue(predicate.test(new PersonBuilder().withTags("vip", "friendly").build()));

        predicate = new ClientContainsKeywordsPredicate(ClientContainsKeywordsPredicate.SearchType.TAG,
                Arrays.asList("vip", "regular"));
        assertTrue(predicate.test(new PersonBuilder().withTags("regular", "active").build()));

        predicate = new ClientContainsKeywordsPredicate(ClientContainsKeywordsPredicate.SearchType.TAG,
                Arrays.asList("ViP"));
        assertTrue(predicate.test(new PersonBuilder().withTags("vip").build())); // case-insensitive
    }

    @Test
    public void test_tagDoesNotContainKeywords_returnsFalse() {
        ClientContainsKeywordsPredicate predicate =
                new ClientContainsKeywordsPredicate(ClientContainsKeywordsPredicate.SearchType.TAG,
                        Collections.singletonList("vip"));
        assertFalse(predicate.test(new PersonBuilder().withTags("friend").build()));

        predicate = new ClientContainsKeywordsPredicate(ClientContainsKeywordsPredicate.SearchType.TAG,
                Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withTags("vip").build()));
    }

    // ===============================
    // DATE TESTS
    // ===============================

    @Test
    public void test_dateContainsKeywords_returnsTrue() {
        // Assuming booking date stored as "2025-10-15T10:00" in your Booking class
        ClientContainsKeywordsPredicate predicate =
                new ClientContainsKeywordsPredicate(ClientContainsKeywordsPredicate.SearchType.DATE,
                        Collections.singletonList("2025-10-15"));
        assertTrue(predicate.test(new PersonBuilder().withBooking("2025-10-15T10:00").build()));

        // multiple possible dates
        predicate = new ClientContainsKeywordsPredicate(ClientContainsKeywordsPredicate.SearchType.DATE,
                Arrays.asList("2025-10-15", "2025-11-01"));
        assertTrue(predicate.test(new PersonBuilder().withBooking("2025-11-01T09:00").build()));
    }

    @Test
    public void test_dateDoesNotContainKeywords_returnsFalse() {
        ClientContainsKeywordsPredicate predicate =
                new ClientContainsKeywordsPredicate(ClientContainsKeywordsPredicate.SearchType.DATE,
                        Collections.singletonList("2025-10-15"));
        assertFalse(predicate.test(new PersonBuilder().withBooking("2025-12-20T12:00").build()));

        predicate = new ClientContainsKeywordsPredicate(ClientContainsKeywordsPredicate.SearchType.DATE,
                Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withBooking("2025-12-20T12:00").build()));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        ClientContainsKeywordsPredicate predicate =
                new ClientContainsKeywordsPredicate(ClientContainsKeywordsPredicate.SearchType.NAME, keywords);

        String expected = ClientContainsKeywordsPredicate.class.getCanonicalName()
                + "{searchType=NAME, keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
