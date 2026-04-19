package ai.idealistic.spartan.utils.java;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentList<E> implements List<E> {

    private final Map<Integer, E> map;

    public ConcurrentList(int size) {
        this.map = new ConcurrentHashMap<>(size);
    }

    public ConcurrentList() {
        this(2);
    }

    @Override
    public int size() {
        return this.map.size();
    }

    @Override
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.map.containsValue(o);
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return this.map.values().iterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return this.map.values().toArray();
    }

    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T[] a) {
        return this.map.values().toArray(a);
    }

    @Override
    public boolean add(E e) {
        this.map.put(this.map.size(), e);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        for (Map.Entry<Integer, E> entry : this.map.entrySet()) {
            if (entry.getValue().equals(o)) {
                this.map.remove(entry.getKey());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        for (Object o : c) {
            if (!this.contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends E> c) {
        for (E e : c) {
            this.add(e);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends E> c) {
        for (E e : c) {
            this.add(index++, e);
        }
        return true;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        for (Object o : c) {
            this.remove(o);
        }
        return true;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        boolean modified = false;

        for (Iterator<E> it = this.map.values().iterator(); it.hasNext(); ) {
            E e = it.next();

            if (!c.contains(e)) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public void clear() {
        this.map.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof List)) {
            return false;
        }
        List<?> list = (List<?>) o;

        if (this.size() != list.size()) {
            return false;
        }
        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).equals(list.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return this.map.hashCode();
    }

    @Override
    public E get(int index) {
        return this.map.get(index);
    }

    @Override
    public E set(int index, E element) {
        return this.map.put(index, element);
    }

    @Override
    public void add(int index, E element) {
        for (int i = this.map.size(); i > index; i--) {
            this.map.put(i, this.map.get(i - 1));
        }
        this.map.put(index, element);
    }

    @Override
    public E remove(int index) {
        return this.map.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        for (Map.Entry<Integer, E> entry : this.map.entrySet()) {
            if (entry.getValue().equals(o)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        int lastIndex = -1;

        for (Map.Entry<Integer, E> entry : this.map.entrySet()) {
            if (entry.getValue().equals(o)) {
                lastIndex = entry.getKey();
            }
        }
        return lastIndex;
    }

    @NotNull
    @Override
    public ListIterator<E> listIterator() {
        return new ArrayList(this.map.values()).listIterator();
    }

    @NotNull
    @Override
    public ListIterator<E> listIterator(int index) {
        return new ArrayList(this.map.values()).listIterator(index);
    }

    @NotNull
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return new ArrayList<>(this.map.values()).subList(fromIndex, toIndex);
    }
}
