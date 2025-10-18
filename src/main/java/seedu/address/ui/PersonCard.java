package seedu.address.ui;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import seedu.address.model.booking.Booking;
import seedu.address.model.person.Person;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    // Array of color styles for tags
    private static final String[] TAG_COLORS = {
        "-fx-background-color: #FF6B6B; -fx-text-fill: white;", // Red
        "-fx-background-color: #4ECDC4; -fx-text-fill: black;", // Teal
        "-fx-background-color: #45B7D1; -fx-text-fill: white;", // Blue
        "-fx-background-color: #FFA07A; -fx-text-fill: white;", // Light Salmon
        "-fx-background-color: #98D8C8; -fx-text-fill: white;", // Mint
        "-fx-background-color: #F7DC6F; -fx-text-fill: black;", // Yellow
        "-fx-background-color: #BB8FCE; -fx-text-fill: white;", // Purple
        "-fx-background-color: #85C1E2; -fx-text-fill: white;", // Sky Blue
        "-fx-background-color: #F8B88B; -fx-text-fill: black;", // Peach
        "-fx-background-color: #52BE80; -fx-text-fill: white;", // Green
        "-fx-background-color: #EC7063; -fx-text-fill: white;", // Coral
        "-fx-background-color: #5DADE2; -fx-text-fill: white;", // Ocean Blue
        "-fx-background-color: #AF7AC5; -fx-text-fill: white;", // Lavender
        "-fx-background-color: #48C9B0; -fx-text-fill: black;", // Turquoise
        "-fx-background-color: #F5B041; -fx-text-fill: black;" // Orange
    };

    public final Person person;

    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;
    @FXML
    private VBox bookings; // deprecated display; retained to avoid FXML injection failures if older FXML is loaded

    // Table for bookings
    @FXML
    private TableView<BookingRow> bookingTable;
    @FXML
    private TableColumn<BookingRow, String> colBookingId;
    @FXML
    private TableColumn<BookingRow, String> colDate;
    @FXML
    private TableColumn<BookingRow, String> colTime;
    @FXML
    private TableColumn<BookingRow, String> colClient;
    @FXML
    private TableColumn<BookingRow, String> colDesc;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone() != null ? person.getPhone().value : "-");
        email.setText(person.getEmail() != null ? person.getEmail().value : "-");
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> {
                    Label tagLabel = new Label(tag.tagName);
                    tagLabel.setStyle(getColorForTag(tag.tagName));
                    tags.getChildren().add(tagLabel);
                });

        // Populate booking table (if present in FXML)
        if (bookingTable != null) {
            setupBookingTable(person);
        } else {
            // Fallback to legacy label-based rendering
            person.getBookings().stream()
                    .sorted(Comparator.comparing(Booking::getDateTime))
                    .forEach(booking -> {
                        Label bookingLabel = new Label(String.format("📅 %s with %s - %s",
                                booking.getDateTimeString(),
                                booking.getClientName(),
                                booking.getDescription()));
                        bookingLabel.setWrapText(true);
                        bookingLabel.setStyle("-fx-font-size: 6px; -fx-text-fill: #3e7b91;");
                        bookings.getChildren().add(bookingLabel);
                    });
        }
    }

    /**
     * Returns a color style for a tag based on its name.
     * The same tag name will always get the same color.
     */
    private String getColorForTag(String tagName) {
        int hash = Math.abs(tagName.hashCode());
        int colorIndex = hash % TAG_COLORS.length;
        return TAG_COLORS[colorIndex];
    }

    private void setupBookingTable(Person person) {
        // Define column mappings
        colBookingId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colClient.setCellValueFactory(new PropertyValueFactory<>("client"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));

        colDesc.setCellFactory(column -> new TableCell<BookingRow, String>() {
            private final Text text = new Text();
            {
                text.wrappingWidthProperty().bind(column.widthProperty().subtract(10));
                text.setFill(Color.WHITE);
                setGraphic(text);
                setPrefHeight(Region.USE_COMPUTED_SIZE);
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    text.setText(item);
                    setGraphic(text);
                }
            }
        });

        ObservableList<BookingRow> rows = FXCollections.observableArrayList();
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");

        final int[] counter = {1};
        person.getBookings().stream()
                .sorted(Comparator.comparing(Booking::getDateTime))
                .forEach(b -> rows.add(new BookingRow(
                        String.valueOf(counter[0]++),
                        b.getDateTime().format(dateFmt),
                        b.getDateTime().format(timeFmt),
                        b.getClientName(),
                        b.getDescription())));

        bookingTable.setItems(rows);
        bookingTable.setFixedCellSize(-1);
        bookingTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        bookingTable.setTableMenuButtonVisible(false);
    }

    /**
     * Row model for displaying a booking in the table.
     */
    public static class BookingRow {
        private final String id;
        private final String date;
        private final String time;
        private final String client;
        private final String desc;

        /**
         * Creates a row representation of a booking.
         */
        public BookingRow(String id, String date, String time, String client, String desc) {
            this.id = id;
            this.date = date;
            this.time = time;
            this.client = client;
            this.desc = desc;
        }


        public String getId() {
            return id;
        }

        public String getDate() {
            return date;
        }

        public String getTime() {
            return time;
        }

        public String getClient() {
            return client;
        }

        public String getDesc() {
            return desc;
        }
    }
}
