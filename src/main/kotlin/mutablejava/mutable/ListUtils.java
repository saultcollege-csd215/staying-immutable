package mutablejava.mutable;

import java.util.List;
import java.util.ArrayList;

public class ListUtils {

    public static <T> List<T> add(List<T> list, T item) {
        return list; // TODO: Implement a copy-on-write 'add' operation for lists that adds elements to the end of the list
    }

    public static <T> List<T> removeFirst(List<T> list) {
        return list; // TODO: Implement a copy-on-write 'removeFirst' operation for lists
    }

    public static <T> List<T> removeLast(List<T> list) {
        return list; // TODO: Implement a copy-on-write 'removeLast' operation for lists
    }

    public static <T> List<T> remove(List<T> list, int idx) {
        return list; // TODO: Implement a copy-on-write 'remove' operation for lists that removes the element at index idx
    }

    public static <T> List<T> set(List<T> list, int idx, T value) {
        return list; // TODO: Implement a copy-on-write 'set' operation for lists that sets the element at at index idx
    }
}
