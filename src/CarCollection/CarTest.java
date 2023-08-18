package CarCollection;

import java.util.*;

public class CarTest {
    public static void main(String[] args) {
        CarCollection carCollection = new CarCollection();
        String manufacturer = fillCollection(carCollection);
        carCollection.sortByPrice(true);
        System.out.println("=== Sorted By Price ASC ===");
        print(carCollection.getList());
        carCollection.sortByPrice(false);
        System.out.println("=== Sorted By Price DESC ===");
        print(carCollection.getList());
        System.out.printf("=== Filtered By Manufacturer: %s ===\n", manufacturer);
        List<Car> result = carCollection.filterByManufacturer(manufacturer);
        print(result);
    }

    static void print(List<Car> cars) {
        for (Car c : cars) {
            System.out.println(c);
        }
    }

    static String fillCollection(CarCollection cc) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            if(parts.length < 4) return parts[0];
            Car car = new Car(parts[0], parts[1], Integer.parseInt(parts[2]),
                    Float.parseFloat(parts[3]));
            cc.addCar(car);
        }
        scanner.close();
        return "";
    }
}

class Car{
    String manufacturer;
    String model;
    float power;
    int price;

    public Car(String manufacturer, String model, int price, float power) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.power = power;
        this.price = price;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public float getPower() {
        return power;
    }

    public int getPrice() {
        return price;
    }
    //Renault Clio (96KW) 12100

    @Override
    public String toString() {
        return String.format("%s %s (%.0fKW) %d", manufacturer, model, power, price);
    }
}

class CarCollection{
    List<Car> cars;

    public CarCollection() {
        cars = new ArrayList<>();
    }
    public void addCar(Car car){
        cars.add(car);
    }
    public void sortByPrice(boolean ascending){
        if(!ascending){
            Collections.sort(cars, Collections.reverseOrder(new CompareByPrice()));
        }
        else Collections.sort(cars, new CompareByPrice());
    }
    public List<Car> filterByManufacturer(String manufacturer){
        List<Car> result = new ArrayList<>();
        for(Car c : cars){
            if(c.getManufacturer().equalsIgnoreCase(manufacturer)){
                result.add(c);
            }
        }
        Collections.sort(result, new CompareByModel());
        return result;
    }
    public List<Car> getList(){
        return cars;
    }
}

class CompareByPrice implements Comparator<Car>{

    @Override
    public int compare(Car car1, Car car2) {
        float c = car1.getPrice() - car2.getPrice();
        if(c == 0){
            return (int) (car1.getPower() - car2.getPower());
        }
        else return (int) c;
    }
}
class CompareByModel implements Comparator<Car>{

    @Override
    public int compare(Car car1, Car car2 ) {
        return car1.getModel().compareTo(car2.getModel());
    }
}