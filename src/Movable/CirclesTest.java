package Movable;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

enum TYPE {
    POINT,
    CIRCLE
}

enum DIRECTION {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

class ObjectCanNotBeMovedException extends Exception {
    public ObjectCanNotBeMovedException(int x, int y) {
        super(String.format("Point (%d,%d) is out of bounds", x, y));
    }
}

class MovableObjectNotSuitableException extends Exception {
    public MovableObjectNotSuitableException(String s) {
        super(s);
    }
}

interface Movable {
    void moveUp() throws ObjectCanNotBeMovedException;

    void moveDown() throws ObjectCanNotBeMovedException;

    void moveLeft() throws ObjectCanNotBeMovedException;

    void moveRight() throws ObjectCanNotBeMovedException;

    int getCurrentXPosition();

    int getCurrentYPosition();

    boolean canFitWithinRange();

    TYPE getType();

    String messageException();
}

class MovablePoint implements Movable {
    int x, y;
    int xSpeed, ySpeed;

    public MovablePoint(int x, int y, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        if (y + ySpeed > MovablesCollection.Y_MAX) {
            throw new ObjectCanNotBeMovedException(x, y + ySpeed);
        }
        y = y + ySpeed;
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        if (y - ySpeed < 0) {
            throw new ObjectCanNotBeMovedException(x, y - ySpeed);
        }
        y = y - ySpeed;
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        if (x - xSpeed < 0) {
            throw new ObjectCanNotBeMovedException(x - xSpeed, y);
        }
        x = x - xSpeed;
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        if (x + xSpeed > MovablesCollection.X_MAX) {
            throw new ObjectCanNotBeMovedException(x + xSpeed, y);
        }
        x = x + xSpeed;
    }

    public boolean canFitWithinRange() {
        return (x >= 0 && x <= MovablesCollection.X_MAX && y >= 0 && y <= MovablesCollection.Y_MAX);
    }

    public TYPE getType() {
        return TYPE.POINT;
    }

    @Override
    public String messageException() {
        return String.format("Movable point with coordinates (%d,%d) can not be fitted into the collection", x, y);
    }

    @Override
    public int getCurrentXPosition() {
        return x;
    }

    @Override
    public int getCurrentYPosition() {
        return y;
    }

    @Override
    public String toString() {
        return String.format("Movable point with coordinates (%d,%d)", x, y);
    }

}

class MovableCircle implements Movable {
    int radius;
    MovablePoint center;

    public MovableCircle(int radius, MovablePoint center) {
        this.radius = radius;
        this.center = center;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        center.moveUp();
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        center.moveDown();
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        center.moveLeft();
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        center.moveRight();
    }

    public boolean canFitWithinRange() {
        return ((center.x - radius) >= 0 && (center.x + radius) <= MovablesCollection.X_MAX && (center.y - radius) >= 0 && (center.y + radius) <= MovablesCollection.Y_MAX);
    }

    public TYPE getType() {
        return TYPE.CIRCLE;
    }

    @Override
    public String messageException() {
        return String.format("Movable circle with center (%d,%d) and radius %d can not be fitted into the collection",
                center.x, center.y, radius);
    }

    @Override
    public int getCurrentXPosition() {
        return center.getCurrentXPosition();
    }

    @Override
    public int getCurrentYPosition() {
        return center.getCurrentYPosition();
    }

    @Override
    public String toString() {
        return String.format("Movable circle with center coordinates (%d,%d) and radius %d", center.x, center.y, radius);
    }
}

class MovablesCollection {
    List<Movable> movables;
    static int X_MAX;
    static int Y_MAX;

    public MovablesCollection(int xMax, int yMax) {
        movables = new ArrayList<>();
        X_MAX = xMax;
        Y_MAX = yMax;
    }

    public static void setxMax(int xMax) {
        X_MAX = xMax;
    }

    public static void setyMax(int yMax) {
        Y_MAX = yMax;
    }

    public void addMovableObject(Movable m) throws MovableObjectNotSuitableException {
        if (!m.canFitWithinRange())
            throw new MovableObjectNotSuitableException(m.messageException());
        movables.add(m);
    }

    void moveObjectsFromTypeWithDirection(TYPE type, DIRECTION direction) {
        movables.stream().filter(i -> i.getType().equals(type))
                .forEach(i -> {
                    try {
                        switch (direction) {
                            case UP:
                                i.moveUp();
                                break;
                            case DOWN:
                                i.moveDown();
                                break;
                            case LEFT:
                                i.moveLeft();
                                break;
                            case RIGHT:
                                i.moveRight();
                                break;
                        }
                    } catch (ObjectCanNotBeMovedException o) {
                        System.out.println(o.getMessage());
                    }
                });
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Collection of movable objects with size %d:\n", movables.size()));
        movables.stream().forEach(i -> sb.append(i.toString()+"\n"));
        return sb.toString();
    }
}

public class CirclesTest {

    public static void main(String[] args) {

        System.out.println("===COLLECTION CONSTRUCTOR AND ADD METHOD TEST===");
        MovablesCollection collection = new MovablesCollection(100, 100);
        Scanner sc = new Scanner(System.in);
        int samples = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < samples; i++) {
            String inputLine = sc.nextLine();
            String[] parts = inputLine.split(" ");

            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int xSpeed = Integer.parseInt(parts[3]);
            int ySpeed = Integer.parseInt(parts[4]);

            try{
                if (Integer.parseInt(parts[0]) == 0) { //point
                    collection.addMovableObject(new MovablePoint(x, y, xSpeed, ySpeed));
                } else { //circle
                    int radius = Integer.parseInt(parts[5]);
                    collection.addMovableObject(new MovableCircle(radius, new MovablePoint(x, y, xSpeed, ySpeed)));

                }
            }
            catch(MovableObjectNotSuitableException m){
                System.out.println(m.getMessage());
            }

        }
        System.out.println(collection.toString());

        System.out.println("MOVE POINTS TO THE LEFT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.LEFT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES DOWN");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.DOWN);
        System.out.println(collection.toString());

        System.out.println("CHANGE X_MAX AND Y_MAX");
        MovablesCollection.setxMax(90);
        MovablesCollection.setyMax(90);

        System.out.println("MOVE POINTS TO THE RIGHT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.RIGHT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES UP");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.UP);
        System.out.println(collection.toString());


    }


}

