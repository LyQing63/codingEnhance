package ByteDance.LRU;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRU2<K, V>  extends LinkedHashMap<K, V> {
    private final Integer capacity;
    public LRU2(int capacity) {
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }

    public static void main(String[] args) {
        LRU2<Integer, Integer> lru = new LRU2<>(3);
        lru.put(1, 1);
        lru.put(2, 2);
        lru.put(3, 3);
        System.out.println(lru);
        lru.put(4, 4);
        System.out.println(lru);
        lru.get(3);
        System.out.println(lru);
    }
}
