package DeliveryApp;

import java.util.*;

class Information {
    String id;
    String name;
    Location location;
    int orders;
    float amount;

    public Information(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
        orders = 0;
        amount = 0;
    }

    public Location getLocation() {
        return location;
    }

    public String getId() {
        return id;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    void setOrders() {
        orders++;
    }

    public int getOrders() {
        return orders;
    }

    public float averageAmount() {
        if (orders != 0)
            return amount / orders;
        return 0.00F;
    }

    public float getAmount() {
        return amount;
    }

    void setAmount(float cost) {
        amount += cost;
    }
}

class DeliveryPerson extends Information implements Comparable<DeliveryPerson> {

    public DeliveryPerson(String id, String name, Location currentLocation) {
        super(id, name, currentLocation);
    }

    void addTip(int tip) {
        amount += (tip * 10);
    }

    @Override
    public int compareTo(DeliveryPerson other) {
        return Comparator.comparing(DeliveryPerson::getId).compare(this, other);
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total deliveries: %d Total delivery fee: %.2f Average delivery fee: %.2f",
                id, name, orders, amount, averageAmount());
    }
}

class Restaurant extends Information implements Comparable<Restaurant> {

    public Restaurant(String id, String name, Location currentLocation) {
        super(id, name, currentLocation);
    }

    @Override
    public int compareTo(Restaurant other) {
        return Comparator.comparing(Restaurant::averageAmount).compare(this, other);
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total orders: %d Total amount earned: %.2f Average amount earned: %.2f",
                id, name, orders, amount, averageAmount());
    }
}

class User extends Information implements Comparable<User> {
    Map<String, Location> addresses;

    public User(String id, String name) {
        super(id, name, null);
        addresses = new HashMap<>();
    }

    void addLocation(String address, Location location) {
        addresses.put(address, location);
    }

    @Override
    public int compareTo(User other) {
        return Comparator.comparing(User::getAmount).reversed().thenComparing(User::getId).compare(this, other);
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total orders: %d Total amount spent: %.2f Average amount spent: %.2f",
                id, name, orders, amount, averageAmount());
    }
}

class DeliveryApp {
    String name;
    Map<String, Restaurant> restaurants; // by id
    Map<String, User> users; // by id
    Set<DeliveryPerson> deliveryPeople;

    public DeliveryApp(String name) {
        this.name = name;
        restaurants = new TreeMap<>();
        users = new TreeMap<>();
        deliveryPeople = new TreeSet<>();
    }

    void registerDeliveryPerson(String id, String name, Location currentLocation) {
        DeliveryPerson deliveryPerson = new DeliveryPerson(id, name, currentLocation);
        deliveryPeople.add(deliveryPerson);
    }

    void registerDeliveryPerson2(DeliveryPerson deliveryPerson) {
        deliveryPeople.add(deliveryPerson);
    }

    public DeliveryPerson nearestDeliveryPerson(Set<DeliveryPerson> people, Location restaurantLocation) {

        int minDistance = Integer.MAX_VALUE;
        List<DeliveryPerson> list = new ArrayList<>();

        for (DeliveryPerson person : people)
            list.add(person);

        DeliveryPerson nearestDeliveryPerson = list.get(0);
        for (int i = 0; i < list.size(); i++) {
            DeliveryPerson person = list.get(i);
            int currentDistance = person.location.distance(restaurantLocation);
            if (currentDistance < minDistance) {
                minDistance = currentDistance;
                nearestDeliveryPerson = person;
            }
            if (currentDistance == minDistance) {
                if (person.getOrders() < nearestDeliveryPerson.getOrders()) {
                    minDistance = currentDistance;
                    nearestDeliveryPerson = person;
                }
            }
        }
        return nearestDeliveryPerson;

    }

    public void addRestaurant(String id, String name, Location location) {
        restaurants.put(id, new Restaurant(id, name, location));
    }

    void addUser(String id, String name) {
        users.put(id, new User(id, name));
    }

    void addAddress(String id, String addressName, Location location) {
        User user = users.get(id);
        if (user != null) {
            user.addLocation(addressName, location);
            users.put(id, user);
        }
    }

    void orderFood(String userId, String userAddressName, String restaurantId, float cost) {

        Restaurant restaurant = restaurants.get(restaurantId);
        restaurant.setOrders();
        restaurant.setAmount(cost);

        Location restaurantLocation = restaurant.location;
        User user = users.get(userId);
        user.setAmount(cost);
        user.setOrders();

        DeliveryPerson deliveryPerson = nearestDeliveryPerson(deliveryPeople, restaurantLocation);
        Location address = user.addresses.get(userAddressName);

        deliveryPerson.setOrders();
        deliveryPerson.setAmount(90);

        int distance = deliveryPerson.location.distance(restaurantLocation);
        deliveryPerson.addTip(distance / 10);

        deliveryPerson.setLocation(address);

        deliveryPeople.remove(deliveryPerson);
        registerDeliveryPerson2(deliveryPerson);

        users.put(userId, user);
        restaurants.put(restaurantId, restaurant);

    }

    void printUsers() {
        users.values().stream().sorted(Comparator.comparing(User::getAmount).thenComparing(User::getId).reversed())
                .forEach(System.out::println);
    }

    void printRestaurants() {
        restaurants.values().stream().sorted(
                        Comparator.comparing(Restaurant::averageAmount).thenComparing(Restaurant::getId).reversed())
                .forEach(System.out::println);
    }

    void printDeliveryPeople() {
        deliveryPeople.stream()
                .sorted(Comparator.comparing(DeliveryPerson::getAmount).thenComparing(DeliveryPerson::getId).reversed())
                .forEach(person -> System.out.println(person.toString()));
    }
}

interface Location {
    int getX();

    int getY();

    default int distance(Location other) {
        int xDiff = Math.abs(getX() - other.getX());
        int yDiff = Math.abs(getY() - other.getY());
        return xDiff + yDiff;
    }
}

class LocationCreator {
    public static Location create(int x, int y) {

        return new Location() {
            @Override
            public int getX() {
                return x;
            }

            @Override
            public int getY() {
                return y;
            }
        };
    }
}

public class DeliveryAppTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String appName = sc.nextLine();
        DeliveryApp app = new DeliveryApp(appName);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(" ");

            if (parts[0].equals("addUser")) {
                String id = parts[1];
                String name = parts[2];
                app.addUser(id, name);
            } else if (parts[0].equals("registerDeliveryPerson")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.registerDeliveryPerson(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("addRestaurant")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.addRestaurant(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("addAddress")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.addAddress(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("orderFood")) {
                String userId = parts[1];
                String userAddressName = parts[2];
                String restaurantId = parts[3];
                float cost = Float.parseFloat(parts[4]);
                app.orderFood(userId, userAddressName, restaurantId, cost);
            } else if (parts[0].equals("printUsers")) {
                app.printUsers();
            } else if (parts[0].equals("printRestaurants")) {
                app.printRestaurants();
            } else {
                app.printDeliveryPeople();
            }

        }
    }
}
