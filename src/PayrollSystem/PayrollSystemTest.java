package PayrollSystem;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

abstract class Employee implements Comparable<Employee>{
    String id;
    String level;
    double rate;

    public Employee(String id, String level, double rate) {
        this.id = id;
        this.level = level;
        this.rate = rate;
    }

    public static Employee createEmployee(String line, Map<String, Double> hourlyRateByLevel, Map<String, Double> ticketRateByLevel){
        //F;e911a6;level6;3;4;5;8;3;8;7;5;5
        //H;157f3d;level10;63.14
        String [] parts = line.split(";");
        String id = parts[1];
        String level = parts[2];
        String type = parts[0];
        if(type.equals("H")){
            return new HourlyEmployee(id, level, hourlyRateByLevel.get(level), Double.parseDouble(parts[3]));
        } else{
            List<Integer> ticketPoints = Arrays.stream(parts).skip(3)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            return new FreelanceEmployee(id, level, ticketRateByLevel.get(level), ticketPoints);
        }
    }

    abstract double salary();

    String getLevel(){
        return level;
    }

    @Override
    public String toString() {
        //Employee ID: 157f3d Level: level10 Salary: 2390.72
        return String.format("Employee ID: %s Level: %s Salary: %.2f ", id, level, salary());
    }
    @Override
    public int compareTo(Employee other) {
        return Comparator.comparing(Employee::salary).thenComparing(Employee::getLevel).reversed().compare(this, other);
    }
}

class HourlyEmployee extends Employee{

    double hours;
    double overtime;
    double regular;
    static double SALARY_COEFFICIENT = 1.5;

    public HourlyEmployee(String id, String level, double rate, double hours) {
        super(id, level, rate);
        this.hours = hours;
        this.overtime = Math.max(0, hours-40);
        this.regular = hours - overtime;
    }

    @Override
    double salary() {
        return regular * rate + overtime * rate * SALARY_COEFFICIENT;
    }

    @Override
    public String toString() {
        //Employee ID: 157f3d Level: level10 Salary: 2390.72 Regular hours: 40.00 Overtime hours: 23.14
        return super.toString() + String.format("Regular hours: %.2f Overtime hours: %.2f", regular, overtime);
    }
}
class FreelanceEmployee extends Employee{

    List<Integer> ticketPoints;

    public FreelanceEmployee(String id, String level, double rate, List<Integer> ticketPoints) {
        super(id, level, rate);
        this.ticketPoints = ticketPoints;
    }

    int sumOfTicketPoints(){
        return ticketPoints.stream().mapToInt(t -> t).sum();
    }
    @Override
    double salary() {
        return sumOfTicketPoints() * rate;
    }

    @Override
    public String toString() {
        //Employee ID: 596ed2 Level: level10 Salary: 1290.00 Tickets count: 9 Tickets points: 43
        return super.toString() + String.format("Tickets count: %d Tickets points: %d", ticketPoints.size(), sumOfTicketPoints());
    }
}

class PayrollSystem{
    Map<String, Double> hourlyRateByLevel;
    Map<String, Double> ticketRateByLevel;
    List<Employee> employees;

    public PayrollSystem(Map<String, Double> hourlyRateByLevel, Map<String, Double> ticketRateByLevel) {
        this.hourlyRateByLevel = hourlyRateByLevel;
        this.ticketRateByLevel = ticketRateByLevel;
    }
    void readEmployees (InputStream is){
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        employees = br.lines().map(line -> Employee.createEmployee(line, hourlyRateByLevel, ticketRateByLevel))
                .collect(Collectors.toList());
    }
    Map<String, Set<Employee>> printEmployeesByLevels (OutputStream os, Set<String> levels){
        Map<String, Set<Employee>> mapByLevel = employees.stream().collect(Collectors.groupingBy(
                Employee::getLevel,
                TreeMap::new,
                Collectors.toCollection(TreeSet::new)

        ));

        Set<String> keys = new HashSet<>(mapByLevel.keySet());
        keys.stream().filter(key -> !levels.contains(key)).forEach(mapByLevel::remove);

        return mapByLevel;
    }
}

public class PayrollSystemTest {

    public static void main(String[] args) {

        Map<String, Double> hourlyRateByLevel = new LinkedHashMap<>();
        Map<String, Double> ticketRateByLevel = new LinkedHashMap<>();
        for (int i = 1; i <= 10; i++) {
            hourlyRateByLevel.put("level" + i, 10 + i * 2.2);
            ticketRateByLevel.put("level" + i, 5 + i * 2.5);
        }

        PayrollSystem payrollSystem = new PayrollSystem(hourlyRateByLevel, ticketRateByLevel);

        System.out.println("READING OF THE EMPLOYEES DATA");
        payrollSystem.readEmployees(System.in);

        System.out.println("PRINTING EMPLOYEES BY LEVEL");
        Set<String> levels = new LinkedHashSet<>();
        for (int i=5;i<=10;i++) {
            levels.add("level"+i);
        }
        Map<String, Set<Employee>> result = payrollSystem.printEmployeesByLevels(System.out, levels);
        result.forEach((level, employees) -> {
            System.out.println("LEVEL: "+ level);
            System.out.println("Employees: ");
            employees.forEach(System.out::println);
            System.out.println("------------");
        });


    }
}