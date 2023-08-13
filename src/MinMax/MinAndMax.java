package MinMax;

import java.util.Scanner;

class MinMax<T extends Comparable<T>> {

    T min;
    T max;
    int minCount;
    int maxCount;
    int total;

    public MinMax() {
        minCount = 0;
        maxCount = 0;
        total = 0;
    }

    public void update(T elem) {
        if (total == 0) {
            min = elem;
            max = elem;
        }
        ++total;
        if (elem.compareTo(min) < 0) {
            min = elem;
            minCount = 1;
        } else {
            if (elem.compareTo(min) == 0)
                minCount++;
        }
        if (elem.compareTo(max) > 0) {
            max = elem;
            maxCount = 1;
        } else {
            if (elem.compareTo(max) == 0)
                maxCount++;
        }

    }

    public T getMin() {
        return min;
    }

    public T getMax() {
        return max;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        T min = getMin();
        T max = getMax();

        sb.append(String.format("%s %s %d\n", min, max, total - (minCount + maxCount)));

        return sb.toString();
    }
}

public class MinAndMax {
    public static void main(String[] args) throws ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        MinMax<String> strings = new MinMax<String>();
        for (int i = 0; i < n; ++i) {
            String s = scanner.next();
            strings.update(s);
        }
        System.out.println(strings);
        MinMax<Integer> ints = new MinMax<Integer>();
        for (int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            ints.update(x);
        }
        System.out.println(ints);
    }
}