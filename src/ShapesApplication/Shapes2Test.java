package ShapesApplication;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Shapes2Test {

    public static void main(String[] args) {

        ShapesApplication shapesApplication = new ShapesApplication(10000);

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
        shapesApplication.readCanvases(System.in);

        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        shapesApplication.printCanvases(System.out);


    }
}

enum TYPE{
    CIRCLE,
    SQUARE
}
class InvalidCanvasException extends Exception {
    public InvalidCanvasException(String id) {
        super(String.format("Canvas %s has a shape with area larger than %.2f", id, ShapesApplication.MAX_AREA));
    }
}

abstract class Shape {
    int size;

    public Shape(int size) {
        this.size = size;
    }

    public static Shape createShape(char type, int size) {
        switch (type){
            case 'S':
                return new Square(size);
            case 'C':
                return new Circle(size);
            default:
                return null;
        }
    }

    abstract double area();
    abstract TYPE getType();
}

class Square extends Shape {

    public Square(int size) {
        super(size);
    }

    @Override
    public double area() {
        return size * size;
    }

    @Override
    public TYPE getType() {
        return TYPE.SQUARE;
    }
}

class Circle extends Shape {

    public Circle(int size) {
        super(size);
    }

    @Override
    public double area() {
        return size * size * Math.PI;
    }
    @Override
    public TYPE getType() {
        return TYPE.CIRCLE;
    }
}

class Canvas implements Comparable<Canvas> {
    String id;
    List<Shape> shapes;

    public Canvas(String id, List<Shape> shapes) {
        this.id = id;
        this.shapes = shapes;
    }

    public static Canvas toCanvas(String line) throws InvalidCanvasException {
        //canvas_id type_1 size_1 type_2 size_2 type_3 size_3 …. type_n size_n
        //0cc31e47 C 27 C 13 C 29 C 15 C 22

        String[] parts = line.split("\\s+");
        String id = parts[0];
        List<Shape> shapes = new ArrayList<>();
        for (int i = 1; i < parts.length; i += 2) {
            Shape shape = Shape.createShape(parts[i].charAt(0), Integer.parseInt(parts[i + 1]));
            if(shape != null ){
                if (shape.area() > ShapesApplication.MAX_AREA)
                    throw new InvalidCanvasException(id);
                else shapes.add(shape);
            }
        }
        return new Canvas(id, shapes);
    }

    public int getNumberOfCircles() {
        return (int) shapes.stream().filter(shape -> shape.getType().equals(TYPE.CIRCLE)).count();
    }

    @Override
    public String toString() {
        //ID total_shapes total_circles total_squares min_area max_area average_area
        //5e28f402 11 5 6 100.00 2642.08 1007.35
        DoubleSummaryStatistics dss = shapes.stream().mapToDouble(Shape::area).summaryStatistics();
        return String.format("%s %d %d %d %.2f %.2f %.2f",
                id,
                shapes.size(),
                getNumberOfCircles(),
                shapes.size() - getNumberOfCircles(),
                dss.getMin(),
                dss.getMax(),
                dss.getAverage());
    }
    double sumOfAreas(){
        return shapes.stream().mapToDouble(Shape::area).sum();
    }

    @Override
    public int compareTo(Canvas other) {
        return Double.compare(
                this.sumOfAreas(), other.sumOfAreas());
    }
}

class ShapesApplication {

    static double MAX_AREA;
    List<Canvas> canvas;

    public ShapesApplication(double maxArea) {
        MAX_AREA = maxArea;
        canvas = new ArrayList<>();
    }

    public void readCanvases(InputStream inputStream) {

        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        canvas = br.lines().map(line -> {
                    try {
                        return Canvas.toCanvas(line);
                    } catch (InvalidCanvasException e) {
                        System.out.println(e.getMessage());
                        return null;
                    }
                }).filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void printCanvases(OutputStream os) {
        PrintWriter pw = new PrintWriter(os);

        canvas.stream().sorted(Comparator.reverseOrder()).forEach(pw::println);
        pw.flush();
    }

}