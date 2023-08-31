package BlockContainer;

import java.util.*;

public class BlockContainerTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int size = scanner.nextInt();
        BlockContainer<Integer> integerBC = new BlockContainer<Integer>(size);
        scanner.nextLine();
        Integer lastInteger = null;
        for (int i = 0; i < n; ++i) {
            int element = scanner.nextInt();
            lastInteger = element;
            integerBC.add(element);
        }
        System.out.println("+++++ Integer Block Container +++++");
        System.out.println(integerBC);
        System.out.println("+++++ Removing element +++++");
        integerBC.remove(lastInteger);
        System.out.println("+++++ Sorting container +++++");
        integerBC.sort();
        System.out.println(integerBC);
        BlockContainer<String> stringBC = new BlockContainer<String>(size);
        String lastString = null;
        for (int i = 0; i < n; ++i) {
            String element = scanner.next();
            lastString = element;
            stringBC.add(element);
        }
        System.out.println("+++++ String Block Container +++++");
        System.out.println(stringBC);
        System.out.println("+++++ Removing element +++++");
        stringBC.remove(lastString);
        System.out.println("+++++ Sorting container +++++");
        stringBC.sort();
        System.out.println(stringBC);
    }
}

class BlockContainer<T extends Comparable<T>> {
    // [1,2,3], [4,5,6]....
    List<Set<T>> elements;
    int maxElems;

    public BlockContainer(int maxElems) {
        this.maxElems = maxElems;
        elements = new ArrayList<>();
    }

    public void add(T a) {
        if (elements.size() == 0) {
            Set<T> set = new TreeSet<>();
            set.add(a);
            elements.add(set);
        } else {
            Set<T> set = elements.get(elements.size() - 1);
            if (set.size() < maxElems) {
                set.add(a);
                //elements.get(elements.size()-1).add(a);
            } else {
                set = new TreeSet<>();
                set.add(a);
                elements.add(set);
            }
        }
    }

    public boolean remove(T a) {
        boolean result = false;
        if (elements.size() > 0) {
            Set<T> set = elements.get(elements.size() - 1);
            result = set.remove(a);
            if (set.size() == 0)
                elements.remove(elements.size() - 1);
        }
        return result;
    }

    public void sort() {
        ArrayList<T> arrayList = new ArrayList<>();
        for (Set<T> element : elements) {
            arrayList.addAll(element);
        }
        Collections.sort(arrayList);
        elements = new ArrayList<>();
        for (T elem : arrayList) {
            add(elem);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Set<T> element : elements) {
            sb.append(element).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}



