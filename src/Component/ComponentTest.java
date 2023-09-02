package Component;

import java.util.*;

public class ComponentTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        Window window = new Window(name);
        Component prev = null;
        while (true) {
            try {
                int what = scanner.nextInt();
                scanner.nextLine();
                if (what == 0) {
                    int position = scanner.nextInt();
                    window.addComponent(position, prev);
                } else if (what == 1) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev = component;
                } else if (what == 2) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                    prev = component;
                } else if (what == 3) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                } else if (what == 4) {
                    break;
                }

            } catch (InvalidPositionException e) {
                System.out.println(e.getMessage());
            }
            scanner.nextLine();
        }

        System.out.println("=== ORIGINAL WINDOW ===");
        System.out.println(window);
        int weight = scanner.nextInt();
        scanner.nextLine();
        String color = scanner.nextLine();
        window.changeColor(weight, color);
        System.out.println(String.format("=== CHANGED COLOR (%d, %s) ===", weight, color));
        System.out.println(window);
        int pos1 = scanner.nextInt();
        int pos2 = scanner.nextInt();
        System.out.println(String.format("=== SWITCHED COMPONENTS %d <-> %d ===", pos1, pos2));
        window.switchComponents(pos1, pos2);
        System.out.println(window);
    }
}

class Component implements Comparable<Component> {
    String color;
    int weight;
    Set<Component> innerComponents;

    public Component(String color, int weight) {
        this.color = color;
        this.weight = weight;
        innerComponents = new TreeSet<>();
    }

    void addComponent(Component component) {
        innerComponents.add(component);
    }

    public int getWeight() {
        return weight;
    }

    public String getColor() {
        return color;
    }

    void changeColor(int weight, String color) {
        if (this.weight < weight)
            this.color = color;
        if (innerComponents == null)
            return;
        for (Component component : innerComponents)
            component.changeColor(weight, color);
    }

    public void toString(int indent) {

        int ind = indent;
        String s = Indent.printIndent(ind);
        System.out.println(String.format("%s%d:%s", s, weight, color));
        if (innerComponents != null){
            for (Component component : innerComponents)
                component.toString(ind + 3);
        }


    }

    @Override
    public int compareTo(Component other) {
        return Comparator.comparing(Component::getWeight).thenComparing(Component::getColor).compare(this, other);
    }
}

class Window {
    String name;
    Map<Integer, Component> components;

    public Window(String name) {
        this.name = name;
        components = new TreeMap<>();
    }

    void addComponent(int position, Component component) throws InvalidPositionException {
        if (components.containsKey(position))
            throw new InvalidPositionException(position);
        components.put(position, component);
    }

    void changeColor(int weight, String color) {
        for (Component component : components.values()) {
            component.changeColor(weight, color);
        }
    }

    void switchComponents(int pos1, int pos2) {
        Component c1 = components.get(pos1);
        Component c2 = components.get(pos2);
        components.put(pos1, c2);
        components.put(pos2, c1);
    }

    @Override
    public String toString() {
        System.out.println(String.format("WINDOW %s", name));
        for(Integer position : components.keySet()) {
            System.out.print(String.format("%d:", position));
            components.get(position).toString(0);
        }
        return "";
    }
}

class InvalidPositionException extends Exception {
    public InvalidPositionException(int position) {
        super(String.format("Invalid position %d, alredy taken!", position));
    }
}

class Indent {
    static String printIndent(int indent) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            sb.append("-");
        }
        return sb.toString();
    }
}