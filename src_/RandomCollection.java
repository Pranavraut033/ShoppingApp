package lib;

/*
  https://stackoverflow.com/questions/6409652/random-weighted-selection-in-java
 */
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class RandomCollection<E> {

    private final NavigableMap<Double, E> map = new TreeMap<>();
    private final Random random;
    private double total = 0;

    public RandomCollection() {
        this.random = new Random();
    }

    public void add(double weight, E result) {
        if (weight <= 0) {
            return;
        }
        total += weight;
        map.put(total, result);
    }

    public E next() {
        double value = random.nextDouble() * total;
        return map.higherEntry(value).getValue();
    }
}
