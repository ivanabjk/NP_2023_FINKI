package EventCalendar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class EventCalendarTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        int year = scanner.nextInt();
        scanner.nextLine();
        EventCalendar eventCalendar = new EventCalendar(year);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            String name = parts[0];
            String location = parts[1];
            Date date = df.parse(parts[2]);
            try {
                eventCalendar.addEvent(name, location, date);
            } catch (WrongDateException e) {
                System.out.println(e.getMessage());
            }
        }
        Date date = df.parse(scanner.nextLine());
        eventCalendar.listEvents(date);
        eventCalendar.listByMonth();
    }
}

class Event implements Comparable<Event> {
    String name;
    String location;
    Date date;

    public Event(String name, String location, Date date) {
        this.name = name;
        this.location = location;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public long getTime() {
        return date.getTime();
    }

    @Override
    public String toString() {
        DateFormat df = new SimpleDateFormat("dd MMM, yyy HH:mm");
        return String.format("%s at %s, %s", df.format(date), location, name);
    }

    @Override
    public int compareTo(Event other) {
        return Comparator.comparing(Event::getTime).thenComparing(Event::getName).compare(this, other);
    }
}

class EventCalendar {
    Map<Integer, Integer> months;
    Map<Integer, TreeSet<Event>> events;
    int year;

    public EventCalendar(int year) {
        this.year = year;
        months = new HashMap<>();
        events = new HashMap<>();
    }

    public int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    public int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }

    public int getDayOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_YEAR);
    }

    public void addEvent(String name, String location, Date date) throws WrongDateException {
        if (year != getYear(date))
            throw new WrongDateException(date);
        Event event = new Event(name, location, date);

        int day = getDayOfYear(date);
        events.putIfAbsent(day, new TreeSet<>());
        events.get(day).add(event);

        int month = getMonth(date);
        Integer countDays = months.get(month);
        if (countDays == null)
            countDays = 0;
        countDays++;
        months.put(month, countDays);
    }

    public void listEvents(Date date) {
        Set<Event> eventSet = events.get(getDayOfYear(date));
        if (eventSet == null) {
            System.out.println("No events on this day!");
            return;
        }
        for (Event event : eventSet)
            System.out.println(event);
    }

    public void listByMonth() {
        for (int i = 0; i < 12; i++) {
            System.out.printf("%d : %d%n", i + 1, months.get(i) == null ? 0 : months.get(i));
        }
    }

}

class WrongDateException extends Exception {
    public WrongDateException(Date date) {
        super(String.format("Wrong date: %s", date));
    }
}