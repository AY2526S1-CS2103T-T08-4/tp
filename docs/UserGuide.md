---
layout: page
title: User Guide
---

## Introduction

**FirstImpressions** is a **desktop app for managing contacts, optimized for use via a Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI).

### Target Users
- **Team leaders and managers** who need to coordinate relationship professionals
- **Business professionals** who manage client relationships and appointments
- **Users comfortable with command-line interfaces** who prefer efficiency over graphical navigation

### User Proficiency Level
This application is designed for users who:
- Are comfortable with basic computer operations
- Can navigate using keyboard commands
- Have experience with command-line interfaces or are willing to learn
- Need efficient contact and appointment management tools

### What FirstImpressions Does
FirstImpressions helps you:
- **Manage your team of relationship professionals** by storing their contact information, skills, and specializations
- **Track client appointments and bookings** with detailed scheduling capabilities
- **Search and filter team members** by name, skills, or availability
- **Organize team members** with customizable tags and categories
- **Ensure perfect client-professional matches** through comprehensive search capabilities

With FirstImpressions, no client request is too hard to handle as our system is able to search through multiple preferences, ensuring the perfect match for our customers.

## Table of Contents

1. [Quick Start](#quick-start)
   - [Prerequisites](#prerequisites)
   - [Installation Steps](#installation-steps)
2. [Features](#features)
   - [Viewing help](#viewing-help--help)
   - [Adding a person](#adding-a-person-add)
   - [Adding a tag to an existing person](#adding-a-tag-to-an-existing-person-add)
   - [Listing all persons](#listing-all-persons--list)
   - [Editing a person](#editing-a-person--edit)
   - [Locating team members by name](#locating-team-members-by-name-find)
   - [Deleting a person](#deleting-a-person--delete)
   - [Clearing all entries](#clearing-all-entries--clear)
   - [Assigning a booking](#assigning-a-booking--book)
   - [Exiting the program](#exiting-the-program--exit)
3. [Data Management](#data-management)
   - [Saving the data](#saving-the-data)
   - [Editing the data file](#editing-the-data-file)
4. [FAQ](#faq)
5. [Known Issues](#known-issues)
6. [Command Summary](#command-summary)

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

### Prerequisites

1. **Ensure you have Java 17 or above installed in your Computer.**
   
   <div markdown="span" class="alert alert-info">:information_source: **Java Installation Guide:**
   
   **Windows users:** Download Java 17 from [Oracle's official website](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or use [OpenJDK 17](https://adoptium.net/temurin/releases/?version=17).
   
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).
   
   **Linux users:** Install OpenJDK 17 using your package manager:
   - Ubuntu/Debian: `sudo apt install openjdk-17-jdk`
   - CentOS/RHEL: `sudo yum install java-17-openjdk-devel`
   - Arch Linux: `sudo pacman -S jdk17-openjdk`
   </div>

2. **Verify Java Installation:**
   Open a command terminal and run:
   ```
   java -version
   ```
   You should see output similar to:
   ```
   openjdk version "17.0.x" 2023-xx-xx
   OpenJDK Runtime Environment (build 17.0.x+x)
   OpenJDK 64-Bit Server VM (build 17.0.x+x, mixed mode, sharing)
   ```

### Installation Steps

1. **Download the latest `.jar` file** from [here](https://github.com/AY2526S1-CS2103T-T08-4/tp/releases).

2. **Copy the file** to the folder you want to use as the _home folder_ for the app.

3. **Run the application:**
   
   **Windows:**
   - Open Command Prompt or PowerShell
   - Navigate to the folder: `cd C:\path\to\your\folder`
   - Run: `java -jar firstimpressions.jar`
   
   **Mac/Linux:**
   - Open Terminal
   - Navigate to the folder: `cd /path/to/your/folder`
   - Run: `java -jar firstimpressions.jar`

4. **Verify successful launch:** A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![defaultUI](images/defaultUI.jpg)
   <div markdown="span" class="alert alert-primary">:bulb: **Tip:** If the application doesn't start, ensure Java 17+ is properly installed and the .jar file is not corrupted.</div>

<div markdown="span" class="alert alert-warning">:exclamation: **Important:**
Always backup your `addressbook.json` file before making major changes. Data loss cannot be recovered without a backup.
</div>

5. **Try your first commands:** Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all contacts.

   * `add n/Alice Tan p/98765432 e/alicetan@gmail.com t/vip-handler` : Adds a contact named `Alice Tan` to the list of contacts.

   * `delete n/Alice Tan` : Deletes the contact named Alice Tan from the whole list of contacts.

   * `clear` : Deletes all contacts.

   * `exit` : Exits the app.

6. **Explore more features:** Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/PERSON_NAME`, `PERSON_NAME` is a parameter which can be used as `add n/Alice Tan`.

* Items in square brackets are optional.<br>
  e.g `n/PERSON_NAME [t/TAG]` can be used as `n/Alice Tan t/vip-handler` or as `n/Alice Tan`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG​]…` can be used as ` ` (i.e. 0 times), `t/vip-handler`, `t/vip-handler t/team-lead` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>

### Viewing help : `help`

Shows a short summary of available commands, with a link to access the User Guide.

![help message](images/helpMessage.jpg)
*Figure 1: Help window showing available commands and User Guide link*

Format: `help`


### Adding a person: `add`

Adds a person to the contact list.

Format: `add n/NAME [p/PHONE_NUMBER] [e/EMAIL] [t/TAG]…​`

* Consecutive spaces in name are removed. e.g. "Alice&nbsp;&nbsp;Tan" will be saved as `Alice Tan`.
* Phone numbers must be numeric only.
* Tags must be alphanumeric and have no spaces.

<div markdown="span" class="alert alert-primary">:bulb: **Tip:**
A person can have any number of tags (including 0). <br>
Use camelCase for multiword tags. e.g. teamLead
</div>

Examples:

* `add n/Alice Tan p/98702930 e/alicetan@gmail.com t/teamLead t/vipHandler` <br> will add a Person called `Alice Tan`, with phone number `98702930`, with email `alicetan@gmail.com`, with tags `teamLead` and `vipHandler` to the contact list.
* `add n/Bob Lee t/mandarinSpeaking` <br> will add a Person called `Bob Lee` with tag `mandarinSpeaking` to the contact list.
* `add n/Germaine` <br> will add a Person called `Germaine` to the contact list.

![add message](images\addMessage.jpg)
*Figure 2: Success message after adding a new person to the contact list*

### Adding a tag to an existing person: `add`

Adds a tag to an existing person.

Format: `add n/NAME t/TAG…​`

* Consecutive spaces in name are removed. e.g. "Alice&nbsp;&nbsp;Tan" will be saved as `Alice Tan`.
* Multiple  tags can be added at once by prefixing each tag with `t/`.
* Tags must be alphanumeric and have no spaces.
* If person does not already exist in FirstImpressions, the person is created.
* Other tags e.g. `e/email@example.com` here will be ignored.

Examples:

* `add n/Alice Tan t/sales t/manager` <br> will add tags `sales` and `manager` to the Person called `Alice Tan`.

![add tags](images\addTags.png)
*Figure 3: Success message after adding tags to an existing person*

### Listing all persons : `list`

Shows the list of all persons in the contact list.

Format: `list`

![list message](images\listMessage.jpg)
*Figure 4: Contact list showing all persons in the address book*

### Editing a person : `edit`

Edits an existing person in the contact list.

Format: `edit n/OLD_NAME [n/NEW_NAME] [p/PHONE] [e/EMAIL] [t/TAG]…​`

* Edits the person identified by `OLD_NAME`. The name must exactly match (case-sensitive) a person in the contact list.
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing tags, the existing tags of the person will be removed i.e adding of tags is not cumulative.
* You can remove all the person's tags by typing `t/` without
    specifying any tags after it.

<div markdown="span" class="alert alert-warning">:exclamation: **Warning:**
Person names are case-sensitive. `edit n/john doe` will not edit `John Doe`. Use the exact name as shown in the contact list.
</div>

Examples:
*  `edit n/John Doe p/91234567 e/johntan@gmail.com` Edits the phone number and email address of the person named `John Doe` to be `91234567` and `johntan@gmail.com` respectively.
*  `edit n/John Doe n/Jane Doe t/` Edits the name of `John Doe` to be `Jane Doe` and clears all existing tags.

![edit message](images\editMessage.jpg)
*Figure 5: Success message after editing a person's details*

### Locating team members by name: `find`

Search and lists all people whose name contains the given parameter. <br>
If given a tag or date, will search all tags and bookings to list all people with an exact match.

Format: <br>
`find n/NAME` <br>
`find t/TAG1 TAG2...` <br>
`find d/DATE1 DATE2...` <br>

* Only one field is allowed, subsequent fields will be ignored. e.g. `find n/Hans t/teamLead` will find a person named `Hans t/teamLead`
* For "t/" and "d/", multiple parameters are allowed, all tags requested and all bookings with the requested dates will be listed. e.g. `find t/teamLead vipHandler` or `find d/2026-11-15 2026-12-25` will list all team member's that contain the requested parameters
* The search for names and tags are case-insensitive. e.g `hans` will match `Hans`
* Partial matches will be shown e.g. `find n/Ali` will find a person named `Alice`
* Date must be in `YYYY-MM-DD` format.

<div markdown="span" class="alert alert-warning">:exclamation: **Warning:**
For "n/", only one parameter is allowed, subsequent parameters will be considered as part of the name. e.g. `find n/Ali n/Bob` will search for `Ali n/Bob`
</div>

Examples:
* `find n/John` returns `john` and `John Doe`
* `find d/2026-08-18` returns `Alex Yeoh`, `David Li`<br>
  ![result for 'find alex david'](images/findAlexDavidResult.jpg)
  *Figure 6: Search results showing team members matching the search criteria*

### Deleting a person : `delete`

Remove a person from the contact list, or remove specific tag(s) from a person.

Format: `delete n/PERSON_NAME [t/TAG]…​`

* If only `n/PERSON_NAME` is provided, then the person is removed from the contact list.
* If both `n/PERSON_NAME` and `t/TAG` is provided, then the specific tag(s) will be removed.
* Only tags that currently belong to the person will be deleted.
* The name provided must exactly match (case-sensitive) a person in the contact list. e.g. `delete n/Alex` will not delete `Alex Yeoh`

<div markdown="span" class="alert alert-warning">:exclamation: **Warning:**
Person names are case-sensitive. `delete n/alice` will not delete `Alice Tan`. Use the exact name as shown in the contact list.
</div>

Examples:
* `delete n/Alex t/vipHandler` will remove the tag `vipHandler` from `Alex` in the contact list.
* `delete n/Alex Yeoh` will remove `Alex Yeoh` from the contact list. <br> 
  ![delete message](images\deleteMessage.jpg)
  *Figure 7: Success message after deleting a person from the contact list*

### Clearing all entries : `clear`

Clears all entries from the contact list.

Format: `clear`

<div markdown="span" class="alert alert-warning">:exclamation: **Warning:**
The `clear` command permanently deletes ALL contacts and bookings. This action cannot be undone. Make sure to backup your data before using this command.
</div>

![clear message](images\clearMessage.jpg)
*Figure 8: Success message after clearing all entries from the contact list*

### Assigning a booking : `book`

Assigns a client meeting to a person at a given date and time, with an optional description.

Format: `book d/DATETIME c/CLIENT_NAME n/PERSON_NAME [desc/DESCRIPTION]`

* Name of person provided must be in the current contact list.
* Datetime must be in `YYYY-MM-DD HH:MM` format in 24-hour notation.

<div markdown="span" class="alert alert-warning">:exclamation: **Warning:**
Bookings must be scheduled for future dates and times. Past dates will be rejected with an error message.
</div>

Examples:

* `book d/2025-09-20 10:30 c/Madam Chen n/Bob Lee` will assign a booking on 20th September 2025 10.30am to Bob Lee. The client will be Madam Chen.
* `book d/2025-10-18 14:00 c/Mr Lim n/Alice Tan desc/first consultation` will assign a booking on 18th October 2025 2pm to Alice Tan. The client will be Mr Lim and the description is "first consultation". <br>
  ![book messsage](images\bookMessage.jpg)
  *Figure 9: Success message after creating a new booking*

<div markdown="span" class="alert alert-warning">:exclamation: **Warning:**
Double booking is not allowed. If you try to book the same person at the same time, the system will show an error message.
</div>

### Exiting the program : `exit`

Exits the program.

Format: `exit`

## Data Management

### Saving the data

FirstImpressions data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

FirstImpressions data are saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to update data directly by editing that data file.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes to the data file makes its format invalid, FirstImpressions will discard all data and start with an empty data file at the next run. Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause the FirstImpressions to behave in unexpected ways (e.g., if a value entered is outside of the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</div>

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous FirstImpressions home folder (your `addressbook.json` file from the `data` folder).

**Q**: Can I use FirstImpressions without an internet connection?<br>
**A**: Yes, FirstImpressions is a desktop application that works completely offline. All your data is stored locally on your computer.

**Q**: What happens if I accidentally delete a person?<br>
**A**: Unfortunately, there is no built-in undo feature. However, you can restore your data by copying a backup of your `addressbook.json` file from the `data` folder.

**Q**: Can I import contacts from other applications?<br>
**A**: Currently, FirstImpressions does not support direct import from other applications. You would need to manually add contacts using the `add` command.

**Q**: Is there a limit to the number of contacts I can store?<br>
**A**: There is no hard limit, but performance may decrease with very large datasets (thousands of contacts). The application is optimized for typical business use cases.

**Q**: Can I customize the date and time format?<br>
**A**: No, the application uses a fixed format: `YYYY-MM-DD HH:MM` for dates and times. This ensures consistency across all users.

**Q**: What if I forget the exact name of a person when using delete or edit commands?<br>
**A**: Use the `find` command first to locate the person, then use the exact name shown in the results for delete or edit operations.

**Q**: Can I book multiple appointments for the same person at different times?<br>
**A**: Yes, you can book multiple appointments for the same person as long as they are at different times. The system prevents double booking at the same time.

**Q**: How do I backup my data?<br>
**A**: Simply copy the `addressbook.json` file from the `data` folder in your FirstImpressions directory. Store this backup in a safe location.

**Q**: What should I do if the application crashes or won't start?<br>
**A**: First, ensure Java 17+ is properly installed. If the problem persists, try deleting the `preferences.json` file and restarting the application. If data corruption is suspected, restore from a backup of your `addressbook.json` file.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format, Examples
--------|------------------
**Help** | `help`
**Add** | `add n/NAME p/PHONE_NUMBER e/EMAIL [t/TAG]…​` <br> e.g., `add n/Alice Tan p/98702930 e/alicetan@gmail.com t/team-lead t/vip-handler`
**List** | `list`
**Edit** | `edit n/OLD_NAME [n/NEW_NAME] [p/PHONE_NUMBER] [e/EMAIL] [t/TAG]…​`<br> e.g.,`edit n/John Doe n/Jane Doe p/91234567 e/janedoe@example.com`
**Find** | `find n/NAME` or `find t/TAG1 t/TAG2...` or `find d/DATE1 d/DATE2...`<br> e.g., `find n/John` or `find d/2025-08-18`
**Delete** | `delete n/PERSON_NAME [t/TAG]…​`<br> e.g., `delete n/Alex Yeoh` or `delete n/Alex t/vipHandler`
**Clear** | `clear`
**Book** | `book d/DATETIME c/CLIENT_NAME n/PERSON_NAME [desc/DESCRIPTION]` <br> e.g., `book d/2025-09-18 14:00 c/Mr Lim n/Alice Tan desc/first consultation`
**Exit** | `exit`
