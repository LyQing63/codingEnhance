package ByteDance.LRU;

import java.util.HashMap;
import java.util.Map;

public class LRU {

    private final Map<Integer, Node> map;
    private final Node head;
    private final Node tail;
    private final int capacity;
    private int size;

    public LRU(int capacity) {
        this.map = new HashMap<>(capacity);
        this.head = new Node();
        this.tail = new Node();
        head.next = tail;
        tail.prev = head;
        this.capacity = capacity;
    }

    int get(int key) {
        if (!map.containsKey(key)) {
            return -1;
        }
        Node node = map.get(key);
        Node prev = node.prev;
        Node next = node.next;
        prev.next = next;
        next.prev = prev;
        node.next = head.next;
        node.prev = head;
        head.next = node;

        return node.value;
    }

    void put(int key, int value) {
        if (map.containsKey(key)) {
            Node node = map.get(key);
            node.value = value;
            return;
        }
        Node node = new Node();
        node.value = value;
        node.key = key;
        if (size >= capacity) {
            Node last = tail.prev;
            map.remove(last.key);
            last.prev.next = tail;
            size--;
        }
        Node temp = head.next;
        temp.prev = node;
        head.next = node;
        node.prev = head;
        node.next = temp;
        map.put(key, node);
        size++;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node p = head.next;
        while (p != tail) {
            sb.append("{" + p.key + ":" + p.value + "},");
            p = p.next;
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        LRU lru = new LRU(3);
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

class Node {
    Node next;
    Node prev;
    int key;
    int value;
}
