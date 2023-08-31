package Airports;

import java.util.*;

public class AirportsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Airports airports = new Airports();
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] codes = new String[n];
        for (int i = 0; i < n; ++i) {
            String al = scanner.nextLine();
            String[] parts = al.split(";");
            airports.addAirport(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
            codes[i] = parts[2];
        }
        int nn = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < nn; ++i) {
            String fl = scanner.nextLine();
            String[] parts = fl.split(";");
            airports.addFlights(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }
        int f = scanner.nextInt();
        int t = scanner.nextInt();
        String from = codes[f];
        String to = codes[t];
        System.out.printf("===== FLIGHTS FROM %S =====\n", from);
        airports.showFlightsFromAirport(from);
        System.out.printf("===== DIRECT FLIGHTS FROM %S TO %S =====\n", from, to);
        airports.showDirectFlightsFromTo(from, to);
        t += 5;
        t = t % n;
        to = codes[t];
        System.out.printf("===== DIRECT FLIGHTS TO %S =====\n", to);
        airports.showDirectFlightsTo(to);
    }
}

class Flight implements Comparable<Flight>{
    String from;
    String to;
    int time;
    int duration;

    public Flight(String from, String to, int time, int duration) {
        this.from = from;
        this.to = to;
        this.time = time;
        this.duration = duration;
    }

    @Override
    public String toString() {
        //HND;PVG;1273;256
        //HND-PVG 21:13-01:29 +1d 4h16m
        int end = time + duration;
        int days = end / (24 * 60);
        end = end % (24 * 60);
        return String.format("%s-%s %02d:%02d-%02d:%02d%s %dh%02dm",
                from, to, time / 60, time % 60,
                end / 60, end % 60,
                days > 0 ? " +1d" : "",
                duration / 60, duration % 60);
    }

    @Override
    public int compareTo(Flight other) {
        int x = Integer.compare(this.time, other.time);
        if (x == 0){
            return this.from.compareTo(other.from);
        }
        return x;
        //The flights are sorted in a TreeSet.
        //If 2 flights are completely different, but have the same time, they will not be added in the TreeSet.
        //In that case we compare the time of their arrival time.
    }
}

class Airport {
    String name;
    String country;
    String code;
    int passengers;
    Map<String, Set<Flight>> flights;


    public Airport(String name, String country, String code, int passengers) {
        this.name = name;
        this.country = country;
        this.code = code;
        this.passengers = passengers;
        flights = new TreeMap<>();
    }

    public void addFlight(String from, String to, int time, int duration) {
        flights.putIfAbsent(to, new TreeSet<>());
        flights.get(to).add(new Flight(from, to, time, duration));
    }

    @Override
    public String toString() {
        //Tokyo International (HND)
        //Japan
        //66795178
        return String.format("%s (%s)\n%s\n%d", name, code, country, passengers);
    }
}

class Airports {

    Map<String, Airport> airports;

    public Airports() {
        airports = new HashMap<>();
    }

    public void addAirport(String name, String country, String code, int passengers) {
        airports.put(code, new Airport(name, country, code, passengers));
    }

    public void addFlights(String from, String to, int time, int duration) {
        //MIA;CDG;1140;66
        airports.get(from).addFlight(from, to, time, duration);
    }

    public void showFlightsFromAirport(String code) {
        Airport airport = airports.get(code);
        System.out.println(airport);
        int i = 0;
        for (String to : airport.flights.keySet()) {
            Set<Flight> flightSet = airport.flights.get(to);
            for (Flight flight : flightSet)
                System.out.printf("%d. %s\n", ++i, flight);
        }

    }

    public void showDirectFlightsFromTo(String from, String to) {
        Airport airport = airports.get(from);
        Set<Flight> flightSet = airport.flights.get(to);
        if(flightSet != null){
            for (Flight flight : flightSet)
                System.out.printf("%s\n", flight);
        }
        else{
            System.out.printf("No flights from %s to %s\n",from, to);
        }
    }

    public void showDirectFlightsTo(String to) {
        Set<Flight> sortedFlights = new TreeSet<>();
        for(Airport airport : airports.values()){
            if(airport.flights.containsKey(to)){
                Set<Flight> flightSet = airport.flights.get(to);
                sortedFlights.addAll(flightSet);
            }
        }
        for(Flight flight : sortedFlights){
            System.out.printf("%s\n", flight);
        }
    }
}


