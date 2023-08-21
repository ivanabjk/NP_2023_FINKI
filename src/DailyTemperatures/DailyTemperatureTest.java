package DailyTemperatures;

import java.io.*;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class DailyTemperature {
    int day;
    List<Double> temperatures;
    DoubleSummaryStatistics doubleSummaryStatistics;

    public DailyTemperature(int day, List<Double> temperatures) {
        this.day = day;
        this.temperatures = temperatures;
        doubleSummaryStatistics = temperatures.stream().collect(Collectors.summarizingDouble(i -> i));
    }

    public int getDay() {
        return day;
    }
    public static DailyTemperature fromLine(String line) {
        String[] parts = line.split("\\s+");
        List<Double> list = IntStream.range(1, parts.length)
                .mapToObj(i -> toTemp(parts[i]))
                .collect(Collectors.toList());
        return new DailyTemperature(Integer.parseInt(parts[0]), list);
    }

    public static double toTemp(String temp) {
        double temperature = Double.parseDouble(temp.substring(0, temp.length() - 1));
        if (temp.endsWith("C")) {
            return temperature;
        } else return toCelsius(temperature);
    }

    public static double toFahrenheit(double celsius) {
        return celsius * 9 / 5 + 32;
    }

    public static double toCelsius(double fahrenheit) {
        return (fahrenheit - 32) * 5 / 9;
    }
    public String statistics(char type){
        double max = doubleSummaryStatistics.getMax();
        double min = doubleSummaryStatistics.getMin();
        double average = doubleSummaryStatistics.getAverage();
        if(type == 'F'){
            max = toFahrenheit(max);
            min = toFahrenheit(min);
            average = toFahrenheit(average);
        }
        return String.format("Count: %3d Min: %6.2f%c Max: %6.2f%c Avg: %6.2f%c\n",
                temperatures.size(), min, type, max, type, average, type);
    }

    @Override
    public String toString() {
        return String.format("%3d", day);
    }
}

class DailyTemperatures {
    List<DailyTemperature> temperatures;

    public DailyTemperatures() {
    }

    public void readTemperatures(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        temperatures = br.lines()
                .map(DailyTemperature::fromLine)
                .collect(Collectors.toList());

    }

    public void writeDailyStats(PrintStream out, char f) {
        PrintWriter pw = new PrintWriter(out);
        temperatures.stream()
                .sorted(Comparator.comparing(DailyTemperature::getDay))
                .forEach(temp -> pw.printf(String.format("%s: %s", temp, temp.statistics(f))));
        pw.flush();
    }
}

public class DailyTemperatureTest {
    public static void main(String[] args) {
        DailyTemperatures dailyTemperatures = new DailyTemperatures();
        dailyTemperatures.readTemperatures(System.in);
        System.out.println("=== Daily temperatures in Celsius (C) ===");
        dailyTemperatures.writeDailyStats(System.out, 'C');
        System.out.println("=== Daily temperatures in Fahrenheit (F) ===");
        dailyTemperatures.writeDailyStats(System.out, 'F');
    }
}
