package Triple;

import java.util.*;

public class TripleTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        Triple<Integer> tInt = new Triple<Integer>(a, b, c);
        System.out.printf("%.2f\n", tInt.max());
        System.out.printf("%.2f\n", tInt.average());
        tInt.sort();
        System.out.println(tInt);
        float fa = scanner.nextFloat();
        float fb = scanner.nextFloat();
        float fc = scanner.nextFloat();
        Triple<Float> tFloat = new Triple<Float>(fa, fb, fc);
        System.out.printf("%.2f\n", tFloat.max());
        System.out.printf("%.2f\n", tFloat.average());
        tFloat.sort();
        System.out.println(tFloat);
        double da = scanner.nextDouble();
        double db = scanner.nextDouble();
        double dc = scanner.nextDouble();
        Triple<Double> tDouble = new Triple<Double>(da, db, dc);
        System.out.printf("%.2f\n", tDouble.max());
        System.out.printf("%.2f\n", tDouble.average());
        tDouble.sort();
        System.out.println(tDouble);
    }
}

class Triple<T extends Number>{
    List<T> numbers;

    public Triple(T first, T second, T third) {

        numbers = new ArrayList<>();
        numbers.add(first);
        numbers.add(second);
        numbers.add(third);
    }
    public double max(){
        return numbers.stream().mapToDouble(Number::doubleValue).max().getAsDouble();

    }
    public double average(){
        return numbers.stream().mapToDouble(Number::doubleValue).average().getAsDouble();
    }
    public void sort(){
        Collections.sort(numbers, new CompareNumbers());
    }

    @Override
    public String toString() {
        return String.format("%.2f %.2f %.2f", numbers.get(0).doubleValue(),
                numbers.get(1).doubleValue(), numbers.get(2).doubleValue());
    }
}
class CompareNumbers implements Comparator<Number> {

    @Override
    public int compare(Number number1, Number number2) {
        return (int) ((int) number1.doubleValue() - number2.doubleValue());
    }
}

