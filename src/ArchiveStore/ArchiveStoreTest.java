package ArchiveStore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ArchiveStoreTest {
    public static void main(String[] args) {
        ArchiveStore store = new ArchiveStore();
        LocalDate date = LocalDate.of(2013, 10, 7);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();

            LocalDate dateToOpen = date.atStartOfDay().plusSeconds(days * 24 * 60 * 60).toLocalDate();
            LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
            store.archiveItem(lockedArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            int maxOpen = scanner.nextInt();
            SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
            store.archiveItem(specialArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        while(scanner.hasNext()) {
            int open = scanner.nextInt();
            try {
                store.openItem(open, date);
            } catch(NonExistingItemException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(store.getLog());
    }
}
class InvalidArchiveOpenException extends Exception{
    public InvalidArchiveOpenException(String s) {
        super(s);
    }
}
class NonExistingItemException extends Exception{
    public NonExistingItemException(int id) {
        super(String.format("Item with id %d doesn't exist", id));
    }
}
abstract class Archive{
    int id;
    LocalDate archivedDate;

    public Archive(int id) {
        this.id = id;
        archivedDate = LocalDate.MIN;
    }

    public void archive(LocalDate date){
        archivedDate = date;
    }

    public int getId() {
        return id;
    }

    abstract LocalDate open(LocalDate date) throws InvalidArchiveOpenException;
}
class LockedArchive extends Archive{
    LocalDate dateToOpen;

    public LockedArchive(int id, LocalDate dateToOpen) {
        super(id);
        this.dateToOpen = dateToOpen;
    }

    @Override
    LocalDate open(LocalDate date) throws InvalidArchiveOpenException {
        if(date.isBefore(dateToOpen))
            throw new InvalidArchiveOpenException(String.format("Item %d cannot be opened before %s", id, dateToOpen));
        return date;
    }
}
class SpecialArchive extends Archive{
    int maxToOpen;
    int counter;

    public SpecialArchive(int id, int maxToOpen) {
        super(id);
        this.maxToOpen = maxToOpen;
        counter = 0;
    }

    @Override
    LocalDate open(LocalDate date) throws InvalidArchiveOpenException {
        if(counter >= maxToOpen)
            throw new InvalidArchiveOpenException(String.format("Item %d cannot be opened more than %d times", id, maxToOpen));
        counter++;
        return date;
    }
}
class ArchiveStore{
    List<Archive> items;
    StringBuilder log;

    public ArchiveStore() {
        items = new ArrayList<>();
        log = new StringBuilder();
    }
    void archiveItem(Archive item, LocalDate date){
        item.archive(date);
        items.add(item);
        log.append(String.format("Item %d archived at %s\n", item.getId(), date));
    }
    void openItem(int id, LocalDate date) throws NonExistingItemException {
        for(Archive item : items){
            if(item.getId() == id){
                try {
                    item.open(date);
                } catch (InvalidArchiveOpenException e) {
                    log.append(e.getMessage()).append("\n");
                    return;
                }
                log.append(String.format("Item %d opened at %s\n",
                        item.getId(), date));
                return;
            }
        }
        throw new NonExistingItemException(id);
    }

    public String getLog() {
        return log.toString();
    }
}

