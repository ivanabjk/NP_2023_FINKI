package ResizableArray;

import java.util.Arrays;
import java.util.Scanner;
import java.util.LinkedList;

public class ResizableArrayTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int test = jin.nextInt();
        if ( test == 0 ) { //test ResizableArray on ints
            ResizableArray<Integer> a = new ResizableArray<Integer>();
            System.out.println(a.count());
            int first = jin.nextInt();
            a.addElement(first);
            System.out.println(a.count());
            int last = first;
            while ( jin.hasNextInt() ) {
                last = jin.nextInt();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
        }
        if ( test == 1 ) { //test ResizableArray on strings
            ResizableArray<String> a = new ResizableArray<String>();
            System.out.println(a.count());
            String first = jin.next();
            a.addElement(first);
            System.out.println(a.count());
            String last = first;
            for ( int i = 0 ; i < 4 ; ++i ) {
                last = jin.next();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
            ResizableArray<String> b = new ResizableArray<String>();
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));

            System.out.println(a.removeElement(first));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
        }
        if ( test == 2 ) { //test IntegerArray
            IntegerArray a = new IntegerArray();
            System.out.println(a.isEmpty());
            while ( jin.hasNextInt() ) {
                a.addElement(jin.nextInt());
            }
            jin.next();
            System.out.println(a.sum());
            System.out.println(a.mean());
            System.out.println(a.countNonZero());
            System.out.println(a.count());
            IntegerArray b = a.distinct();
            System.out.println(b.sum());
            IntegerArray c = a.increment(5);
            System.out.println(c.sum());
            if ( a.sum() > 100 )
                ResizableArray.copyAll(a, a);
            else
                ResizableArray.copyAll(a, b);
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.contains(jin.nextInt()));
            System.out.println(a.contains(jin.nextInt()));
        }
        if ( test == 3 ) { //test insanely large arrays
            LinkedList<ResizableArray<Integer>> resizable_arrays = new LinkedList<ResizableArray<Integer>>();
            for ( int w = 0 ; w < 500 ; ++w ) {
                ResizableArray<Integer> a = new ResizableArray<Integer>();
                int k =  2000;
                int t =  1000;
                for ( int i = 0 ; i < k ; ++i ) {
                    a.addElement(i);
                }

                a.removeElement(0);
                for ( int i = 0 ; i < t ; ++i ) {
                    a.removeElement(k-i-1);
                }
                resizable_arrays.add(a);
            }
            System.out.println("You implementation finished in less then 3 seconds, well done!");
        }
    }

}

class ResizableArray <T>{

    private T[] arr;
    private int size;

    @SuppressWarnings("unchecked")
    public ResizableArray() {
        arr = (T[]) new Object[1];
        size = 0;
    }
    public void addElement(T element){
        if(size == arr.length)
            arr = Arrays.copyOf(arr, size<<1);
        arr[size] = element;
        size++;
    }
    public boolean removeElement(T element){
        int index = find(element);
        if(index == -1)
            return false;
        size--;
        arr[index] = arr[size];
        if (size<<2 <= arr.length)
            arr = Arrays.copyOf(arr, size > 0 ? size<<1 : 1);
        return true;
    }
    private int find(T element){
        for(int i = 0; i < size; i++)
            if(element.equals(arr[i]))
                return i;
        return -1;
    }
    public boolean contains(T element){
        return find(element) != -1;
    }
    public Object[] toArray(){
        return Arrays.copyOf(arr, size);
    }
    public boolean isEmpty(){
        return size == 0;
    }
    public int count(){
        return size;
    }
    public T elementAt(int index){
        return arr[index];
    }
    public static <T> void copyAll(ResizableArray<? super T> dest, ResizableArray<? extends T> src){
        int count = src.count();
        for(int i = 0; i < count; i++){
            dest.addElement(src.elementAt(i));
        }
    }
}
class IntegerArray extends ResizableArray<Integer>{

    public IntegerArray() {
        super();
    }
    public double sum(){

        int sum = 0;
        Object[] a = toArray();
        for(int i = 0; i < a.length; i++)
            sum = sum + (Integer) a[i];
        return sum;
    }
    public double mean(){
        return sum()/count();
    }
    public int countNonZero(){
        int counter = 0;
        Object[] a = toArray();
        for(int i = 0; i < a.length; i++)
            counter += (Integer) a[i] == 0 ? 0:1;
        return counter;
    }
    public IntegerArray distinct(){
        IntegerArray result = new IntegerArray();
        Object[] a = toArray();
        Arrays.sort(a);
        for( int i = 0; i < a.length; i++){
            while( i < a.length-1 && a[i].equals(a[i+1])) {
                i++;
            }
            result.addElement((Integer) a[i]);
        }
        return result;
    }
    public IntegerArray increment(int offset){
        IntegerArray result = new IntegerArray();
        Object[] a = toArray();
        for( int i = 0; i < a.length; i++){
            result.addElement((Integer) a[i] + offset);
        }
        return result;
    }
}