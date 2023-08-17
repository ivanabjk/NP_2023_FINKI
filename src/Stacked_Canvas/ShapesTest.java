package Stacked_Canvas;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ShapesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Canvas canvas = new Canvas();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            int type = Integer.parseInt(parts[0]);
            String id = parts[1];
            if (type == 1) {
                Color color = Color.valueOf(parts[2]);
                float radius = Float.parseFloat(parts[3]);
                canvas.add(id, color, radius);
            } else if (type == 2) {
                Color color = Color.valueOf(parts[2]);
                float width = Float.parseFloat(parts[3]);
                float height = Float.parseFloat(parts[4]);
                canvas.add(id, color, width, height);
            } else if (type == 3) {
                float scaleFactor = Float.parseFloat(parts[2]);
                System.out.println("ORIGNAL:");
                System.out.print(canvas);
                canvas.scale(id, scaleFactor);
                System.out.printf("AFTER SCALING: %s %.2f\n", id, scaleFactor);
                System.out.print(canvas);
            }

        }
    }
}

enum Color {
    RED, GREEN, BLUE
}

interface Scalable{
    void scale(float scaleFactor);
}
interface Stackable{
    float weight();
}

abstract class Shape implements Stackable, Scalable{
    String id;
    Color color;

    public Shape(String id, Color color) {
        this.id = id;
        this.color = color;
    }

}

class Circle extends Shape{

    float radius;

    public Circle(String id, Color color, float radius) {
        super(id, color);
        this.radius = radius;
    }

    @Override
    public void scale(float scaleFactor) {
        radius = radius * scaleFactor;
    }

    @Override
    public float weight() {
        return (float) (Math.PI * radius * radius);
    }

    @Override
    public String toString() {
        //C: c1   RED           706.86
        return String.format("C: %-5s%-10s%10.2f\n", id, color, weight());
    }
}

class Rectangle extends Shape{
    float width;
    float height;

    public Rectangle(String id, Color color, float width, float height) {
        super(id, color);
        this.width = width;
        this.height = height;
    }

    @Override
    public void scale(float scaleFactor) {
        height = height * scaleFactor;
        width = width * scaleFactor;
    }

    @Override
    public float weight() {
        return height * width;
    }

    @Override
    public String toString() {
        return String.format("R: %-5s%-10s%10.2f\n", id, color, weight());
    }
}
class Canvas {
    List<Shape> shapes;

    public Canvas() {
        shapes = new ArrayList<Shape>();
    }

    int find(float weight){
        for(int i = 0; i < shapes.size(); i++){
            if(shapes.get(i).weight() < weight){
                return i;
            }
        }
        return shapes.size();
    }
    public void add(String id, Color color, float radius){
        Circle c = new Circle(id, color, radius);
        int index = find(c.weight());
        shapes.add(index, c);
    }
    public void add(String id, Color color, float width, float height){
        Rectangle r = new Rectangle(id, color, width, height);
        int index = find(r.weight());
        shapes.add(index, r);
    }
    void scale(String id, float scaleFactor){

        Shape shape = null;
        for(int i = 0; i < shapes.size(); i++){
            if(shapes.get(i).id.equals(id)){
                shape = shapes.get(i);
                shapes.remove(i);
                break;
            }
        }

            shape.scale(scaleFactor);
            int index = find(shape.weight());
            shapes.add(index, shape);


    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Shape shape : shapes) {
            sb.append(shape.toString());
        }
        return sb.toString();

    }
}