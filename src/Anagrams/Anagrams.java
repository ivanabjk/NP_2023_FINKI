package Anagrams;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class Anagrams {

    public static void main(String[] args) {
        findAll(System.in);
    }

    public static void findAll(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        Map<String, Set<String>> anagrams = new TreeMap<>();
        ArrayList<String> order = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String word = scanner.nextLine();
            char[] w = word.toCharArray();
            Arrays.sort(w);
            String sortedWord = new String(w);

            if (anagrams.containsKey(sortedWord))
                anagrams.get(sortedWord).add(word);
            else {
                order.add(sortedWord);
                Set<String> wordSet = new TreeSet<>();
                wordSet.add(word);
                anagrams.put(sortedWord, wordSet);
            }
        }
        scanner.close();
        for(String o : order){
            Set<String> values = anagrams.get(o);
            Iterator<String> it = values.iterator();

            while(it.hasNext()){
                System.out.print(it.next());
                if(it.hasNext())
                    System.out.print(" ");
            }
            System.out.println();
        }
    }
}

