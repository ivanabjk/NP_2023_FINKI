package Audition;

import java.util.*;

class Participant implements Comparable<Participant>{
    String code;
    String name;
    int age;

    public Participant(String code, String name, int age) {
        this.code = code;
        this.name = name;
        this.age = age;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Participant)) return false;
        Participant that = (Participant) o;
        return getCode().equals(that.getCode());
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return code + " " + name + " " + age;
    }

    @Override
    public int compareTo(Participant participant) {
        int x = name.compareTo(participant.name);
        if( x == 0){
            int y = Integer.compare(age, participant.age);
            if(y == 0){
                return code.compareTo(code);
            }
            return y;
        }
        return x;
    }
}
class Audition{

    HashMap<String, HashSet<Participant>> participants;

    public Audition() {
        participants = new HashMap<String, HashSet<Participant>>();
    }

    public void addParticipant(String city, String code, String name, int age) {
        Participant participant = new Participant(code, name, age);

        participants.putIfAbsent(city, new HashSet<Participant>());
        participants.get(city).add(participant);
    }

    public void listByCity(String city) {
        HashSet<Participant> set = participants.get(city);

        List<Participant> participantList = new ArrayList<Participant>(set);

        Collections.sort(participantList);

        for(Participant p : participantList){
            System.out.println(p);
        }
    }
}

public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticipant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}