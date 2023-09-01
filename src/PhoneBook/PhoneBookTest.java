package PhoneBook;

import java.util.*;

public class PhoneBookTest {

    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            try {
                phoneBook.addContact(parts[0], parts[1]);
            } catch (DuplicateNumberException e) {
                System.out.println(e.getMessage());
            }
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            String[] parts = line.split(":");
            if (parts[0].equals("NUM")) {
                phoneBook.contactsByNumber(parts[1]);
            } else {
                phoneBook.contactsByName(parts[1]);
            }
        }
    }

}

class PhoneBook {
    public Map<String, Set<Contact>> contactsByName;
    public Map<String, Set<Contact>> contactsByNumber;
    Set<String> numbers;

    public PhoneBook() {
        contactsByName = new TreeMap<>();
        contactsByNumber = new TreeMap<>();
        numbers = new HashSet<>();
    }

    void addContact(String name, String number) throws DuplicateNumberException {
        if (numbers.contains(number))
            throw new DuplicateNumberException(number);
        numbers.add(number);
        Contact contact = new Contact(name, number);

        List<String> keys = contact.getKeys();
        for (String key : keys) {
            contactsByNumber.putIfAbsent(key, new TreeSet<>());
            contactsByNumber.get(key).add(contact);
        }
        contactsByName.putIfAbsent(name, new TreeSet<>());
        contactsByName.get(name).add(contact);
    }

    void contactsByNumber(String number) {
        if (contactsByNumber.containsKey(number)) {
            for (Contact contact : contactsByNumber.get(number)) {
                System.out.println(contact);
            }
        } else {
            System.out.println("NOT FOUND");
        }

    }

    void contactsByName(String name) {
        if (contactsByName.containsKey(name)) {
            for (Contact contact : contactsByName.get(name)) {
                System.out.println(contact);
            }
        } else {
            System.out.println("NOT FOUND");
        }
    }
}


class Contact implements Comparable<Contact> {
    String name;
    String number;

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public List<String> getKeys() {
        ArrayList<String> numberKeys = new ArrayList<>();
        int len = number.length();

        for (int i = 0; i <= len - 3; i++) {
            for (int j = i + 3; j <= len; j++) {
                numberKeys.add(number.substring(i, j));
            }
        }
        return numberKeys;
    }

    @Override
    public int compareTo(Contact other) {
        return Comparator.comparing(Contact::getName).thenComparing(Contact::getNumber).compare(this, other);
    }

    @Override
    public String toString() {
        return String.format("%s %s", name, number);
    }
}

class DuplicateNumberException extends Exception {
    public DuplicateNumberException(String number) {
        super(String.format("Duplicate number: %s", number));
    }
}
