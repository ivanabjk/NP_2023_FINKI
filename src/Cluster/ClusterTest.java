package Cluster;

import java.util.*;

/**
 * January 2016 Exam problem 2
 */

public class ClusterTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Cluster<Point2D> cluster = new Cluster<Point2D>();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            long id = Long.parseLong(parts[0]);
            float x = Float.parseFloat(parts[1]);
            float y = Float.parseFloat(parts[2]);
            cluster.addItem(new Point2D(id, x, y));
        }
        int id = scanner.nextInt();
        int top = scanner.nextInt();
        cluster.near(id, top);
        scanner.close();
    }
}
interface Clusterable<T> {
    Long id();
    Double distance(T element);
}
@SuppressWarnings("rawtypes")
class Cluster<T extends Clusterable>{
    Map<Long, T> items;

    public Cluster() {
        items = new HashMap<>();
    }
    void addItem(T element){
        items.put(element.id(),element);
    }
    void near(long id, int top){

        List<T> itemList = new ArrayList<>(items.values());
        T element = items.get(id);
        itemList.sort(Comparator.comparingDouble(t -> t.distance(element)));

        for(int i=0; i<top && i<itemList.size(); i++){
            T e = itemList.get(i);
            System.out.printf("%d. %d -> %.3f", i, e.id(), e.distance(element));
        }
    }
}
class Point2D implements Clusterable<Point2D>{
    long id;
    float x, y;

    public Point2D(long id, float x, float y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    @Override
    public Long id() {
        return id;
    }

    @Override
    public Double distance(Point2D element) {
        //$\sqrt{{(x1 - x2)^2} + {(y1 - y2)^2}}$
        return Math.sqrt((x - element.x) * (x - element.x) + (y - element.y) * (y - element.y));
    }

}