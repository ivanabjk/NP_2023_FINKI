package Discounts;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Discounts
 */
public class DiscountsTest {
    public static void main(String[] args) {
        Discounts discounts = new Discounts();
        int stores = discounts.readStores(System.in);
        System.out.println("Stores read: " + stores);
        System.out.println("=== By average discount ===");
        discounts.byAverageDiscount().forEach(System.out::println);
        System.out.println("=== By total discount ===");
        discounts.byTotalDiscount().forEach(System.out::println);
    }
}

class Discounts {
    List<Store> stores;
    public Discounts() {
    }
    public int readStores(InputStream inputStream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        stores = br.lines().map(Store::toStore).collect(Collectors.toList());
        //return (int) stores.stream().count();
        return stores.size();
    }
    public List<Store> byAverageDiscount() {
        return stores.stream()
                .sorted(Comparator.comparing(Store::averageDiscount).reversed().thenComparing(Store::getName))
                .limit(3)
                .collect(Collectors.toList());
    }
    public List<Store> byTotalDiscount() {
        return stores.stream()
                .sorted(Comparator.comparing(Store::totalDiscount).thenComparing(Store::getName))
                .limit(3)
                .collect(Collectors.toList());
    }
}

class Store {
    String name;
    List<Product> products;

    public Store(String name, List<Product> products) {
        this.name = name;
        this.products = products;
    }

    public String getName() {
        return name;
    }

    public static Store toStore(String line) {
        //GAP 501:593  6135:7868  1668:2582  3369:4330  9702:15999  8252:13674  3944:5707  2896:4392  9169:17391
        String[] parts = line.split("\\s+");
//        String name = parts[0];
//        List<Product> products = Arrays.stream(parts)
//                .skip(1)
//                .map(Product::toProduct)
//                .collect(Collectors.toList());
//        return new Store(name, products);
        return new Store(parts[0], Arrays.stream(parts).skip(1)
                .map(Product::toProduct)
                .collect(Collectors.toList()));
    }

    public double averageDiscount(){
        return products.stream()
                .mapToDouble(Product::discount)
                .average()
                .orElse(0);
    }

    public int totalDiscount(){
        return products.stream()
                .mapToInt(Product::absoluteDiscount)
                .sum();
    }

    @Override
    public String toString() {
//        Levis
//        Average discount: 35.8%
//        Total discount: 21137
//        48% 2579/4985
//        47% 9988/19165
//        36% 7121/11287
//        35% 1501/2316
//        32% 6385/9497
//        17% 6853/8314

        String productSortedByAvrDisc = products.stream()
                .sorted(Comparator.comparing(Product::discount)
                        .thenComparing(Product::absoluteDiscount).reversed())
                .map(Product::toString)
                .collect(Collectors.joining("\n"));

        double roundedAvr = Math.round(averageDiscount() * 10) / 10.;

        return String.format("%s\nAverage discount: %.1f%%\nTotal discount: %d\n%s",
                name, roundedAvr, totalDiscount(), productSortedByAvrDisc);
    }
}

class Product {
    int price;
    int discountedPrice;

    public Product(int price, int discount) {
        this.price = price;
        this.discountedPrice = discount;
    }

    public static Product toProduct(String line) {
        //501:593
        String[] parts = line.split(":");
        return new Product(Integer.parseInt(parts[1]), Integer.parseInt(parts[0]));
    }

    public int discount(){
        return (price - discountedPrice) * 100 / price ;
    }

    public int absoluteDiscount(){
        return price - discountedPrice;
    }

    @Override
    public String toString() {
        return String.format("%2d%% %d/%d ", discount(), discountedPrice, price);
    }
}

