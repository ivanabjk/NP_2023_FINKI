package TimeTable;

import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.*;

public class TimesTest {

    public static void main(String[] args) {
        TimeTable timeTable = new TimeTable();
        try {
            timeTable.readTimes(System.in);
        } catch (UnsupportedFormatException e) {
            System.out.println("UnsupportedFormatException: " + e.getMessage());
        } catch (InvalidTimeException e) {
            System.out.println("InvalidTimeException: " + e.getMessage());
        }
        System.out.println("24 HOUR FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_24);
        System.out.println("AM/PM FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_AMPM);
    }

}

enum TimeFormat {
    FORMAT_24, FORMAT_AMPM
}

class UnsupportedFormatException extends Exception{
    public UnsupportedFormatException(String s) {
        super(s);
    }
}
class InvalidTimeException extends Exception{
    public InvalidTimeException(String s) {
        super(s);
    }
}

class Time implements Comparable<Time>{
    int hour;
    int minutes;

    public Time(int hour, int minutes) {
        this.hour = hour;
        this.minutes = minutes;
    }

    public Time(String line) throws UnsupportedFormatException, InvalidTimeException {
        // 11:12
        String [] parts = line.split(":");
        if(parts.length == 1){
            parts = line.split("\\.");
        }
        if(parts.length == 1){
            throw new UnsupportedFormatException(line);
        }
        this.hour = Integer.parseInt(parts[0]);
        this.minutes = Integer.parseInt(parts[1]);
        if(hour < 0 || hour > 23 && minutes < 0 || minutes > 59){
            throw new InvalidTimeException(line);
        }
    }

    @Override
    public String toString() {
        return String.format("%2d:%02d",hour, minutes);
    }
    public String toStringAMPM(){

        String temp = "AM";
        int h = hour;

        if(h == 0){
            h += 12;
        }else if(h == 12){
            temp = "PM";
        }else if(h > 12){
            h -= 12;
            temp = "PM";
        }
        return String.format("%2d:%02d %s", h, minutes, temp);
    }

    @Override
    public int compareTo(Time other) {
        if(hour == other.hour){
            return minutes - other.minutes;
        }else{
            return hour - other.hour;
        }
    }
}
class TimeTable{

    List<Time> times;

    public TimeTable() {
        times = new ArrayList<>();
    }
    public void readTimes(InputStream in) throws InvalidTimeException, UnsupportedFormatException {
        Scanner scanner = new Scanner(in);
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            String [] parts = line.split("\\s+");
            for(String part : parts){
                Time time = new Time(part);
                times.add(time);
            }
        }
    }

    public void writeTimes(PrintStream out, TimeFormat timeFormat) {
        PrintWriter pw = new PrintWriter(out);
        Collections.sort(times);
        for(Time time : times){
            if(timeFormat.equals(TimeFormat.FORMAT_24)){
                pw.println(time);
            }
            else{
                pw.println(time.toStringAMPM());
            }
        }
        pw.flush();
    }
}