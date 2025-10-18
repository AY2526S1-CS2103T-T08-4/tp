package seedu.address.testutil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.model.booking.Booking;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";

    private Name name;
    private Phone phone;
    private Email email;
    private Set<Tag> tags;
    private List<Booking> bookings;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        tags = new HashSet<>();
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        phone = personToCopy.getPhone();
        email = personToCopy.getEmail();
        tags = new HashSet<>(personToCopy.getTags());
        bookings = personToCopy.getBookings() != null ? new java.util.ArrayList<>(personToCopy.getBookings()) : null;
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     * Pass null to set phone as absent.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = phone != null ? new Phone(phone) : null;
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     * Pass null to set email as absent.
     */
    public PersonBuilder withEmail(String email) {
        this.email = email != null ? new Email(email) : null;
        return this;
    }

    /**
     * Adds multiple {@code Booking} objects.
     */
    public PersonBuilder withBookings(List<Booking> bookings) {
        this.bookings = new java.util.ArrayList<>(bookings);
        return this;
    }



    public Person build() {
        return new Person(name, phone, email, tags, bookings);
    }

}
