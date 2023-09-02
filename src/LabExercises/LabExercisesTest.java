package LabExercises;

import java.util.*;
import java.util.stream.Collectors;

public class LabExercisesTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LabExercises labExercises = new LabExercises();
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            String[] parts = input.split("\\s+");
            String index = parts[0];
            List<Integer> points = Arrays.stream(parts).skip(1)
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    .collect(Collectors.toList());

            labExercises.addStudent(new Student(index, points));
        }

        System.out.println("===printByAveragePoints (ascending)===");
        labExercises.printByAveragePoints(true, 100);
        System.out.println("===printByAveragePoints (descending)===");
        labExercises.printByAveragePoints(false, 100);
        System.out.println("===failed students===");
        labExercises.failedStudents().forEach(System.out::println);
        System.out.println("===statistics by year");
        labExercises.getStatisticsByYear().entrySet().stream()
                .map(entry -> String.format("%d : %.2f", entry.getKey(), entry.getValue()))
                .forEach(System.out::println);

    }
}

class Student {
    String index;
    List<Integer> labPoints;

    public Student(String index, List<Integer> labPoints) {
        this.index = index;
        this.labPoints = labPoints;
    }

    public String getIndex() {
        return index;
    }

    Double getAverage() {
        return labPoints.stream().mapToInt(i -> i).sum() / 10.0;
    }

    boolean lostSignature() {
        return labPoints.size() < 8;
    }

    boolean hasSignature() {
        return !lostSignature();
    }

    int getYear() {
        return 20 - Integer.parseInt(index.substring(0, 2));
    }

    @Override
    public String toString() {
        return String.format("%s %s %.2f",
                index,
                hasSignature() ? "YES" : "NO",
                getAverage());
    }
}

class LabExercises {
    Set<Student> students;

    public LabExercises() {
        students = new HashSet<>();
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void printByAveragePoints(boolean ascending, int n) {

        Comparator<Student> studentComparator = Comparator
                .comparingDouble(Student::getAverage)
                .thenComparing(Student::getIndex);

        if (!ascending)
            studentComparator = studentComparator.reversed();

        students.stream().sorted(studentComparator)
                .limit(n)
                .forEach(System.out::println);
    }

    public List<Student> failedStudents() {
        Comparator<Student> studentComparator = Comparator
                .comparing(Student::getIndex)
                .thenComparing(Student::getAverage);
        return students.stream().filter(Student::lostSignature)
                .sorted(studentComparator)
                .collect(Collectors.toList());
    }

    public Map<Integer, Double> getStatisticsByYear() {
        Map<Integer, Double> byAverage = new TreeMap<>();
        Map<Integer, Integer> byNumStudents = new TreeMap<>();

        students.stream().filter(Student::hasSignature)
                .forEach(student -> {
                    int year = student.getYear();
                    byAverage.putIfAbsent(year, 0.00);
                    byNumStudents.putIfAbsent(year, 0);

                    int num = byNumStudents.get(year);
                    num++;
                    byNumStudents.put(year, num);

                    double average = byAverage.get(year);
                    average += student.getAverage();
                    byAverage.put(year, average);
                });
        for (Integer year : byAverage.keySet()) {
            double average = byAverage.get(year);
            average /= byNumStudents.get(year);
            byAverage.put(year, average);
        }
        return byAverage;
    }
}