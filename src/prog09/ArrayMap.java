package prog09;
import prog02.DirectoryEntry;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.AbstractSet;
import java.util.Set;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.lang.reflect.Array;

public class ArrayMap <K extends Comparable<K>>
    extends AbstractMap<K, Object> {

    protected class Entry
	implements Map.Entry<K, Object> {

	K key;
	Object value;
    
	Entry (K key, Object value) {
	    this.key = key;
	    this.value = value;
	}
    
	public K getKey () { return key; }
	public Object getValue () { return value; }
	public Object setValue (Object newValue) {
	    Object oldValue = value;
	    value = newValue;
	    return oldValue;
	}
    }

    Entry[] entries;
    int size = 0;

    ArrayMap () {
	Entry entry = new Entry(null, null);
	entries = (Entry[]) Array.newInstance(entry.getClass(), 4);
    }

    void reallocate () {
	System.err.println("reallocating " + entries.length);
	Entry entry = new Entry(null, null);
	Entry[] newEntries = (Entry[]) Array.newInstance(entry.getClass(), 2 * entries.length);
	System.arraycopy(entries, 0, newEntries, 0, size);
	entries= newEntries;
    }    

    public int size () { return size; }

    ArrayMap<K> split () {
	if (size != 4)
	    System.err.println("splitting with size < 4: " + size());
	ArrayMap<K> right = new ArrayMap<K>();
	right.entries[0] = entries[2];
	right.entries[1] = entries[3];
	size = 2;
	right.size = 2;
	return right;
    }

    int find (K key) {
	// EXERCISE
	// Return the index of the first entry with entry.key >= key
	// or size if there is no such entry.
	// Use binary search.

	int high = size;
	int low = 0;
	int middle;
	while (low < high){
		middle = (low + high) / 2;
		if (entries[middle].getKey().compareTo(key) < 0){
			low = middle + 1;
		} else {
			high = middle;
		}
	}
	return high;

//		int bottom = 0;
//		int top = size();
//		while(bottom <= top){
//			int middle = (top+bottom) / 2;
//			if(entries[middle].key.compareTo(key) == 0){
//				return middle;
//			} else if (entries[middle].key.compareTo(key) < 0){
//				bottom = middle + 1;
//			} else{
//				top = middle - 1;
//			}
//		}
//		return top;
    }

    boolean found (int index, K key) {
	return index < size && entries[index].key.equals(key);
    }

    public boolean containsKey (Object keyAsObject) {
	K key = (K) keyAsObject;
	int index = find(key);
	return found(index, key);
    }
  
    public Object get (Object keyAsObject) {
	K key = (K) keyAsObject;
	int index = find(key);
	if (found(index, key))
	    return entries[index].value;
	return null;
    }

    void add (int index, Entry newEntry) {
	if (size == entries.length)
	    reallocate();

	// EXERCISE
	// Insert newEntry at index while keeping the rest of the entries
	// in the same order.
	// Increment size.
	size++;
	for (int i = (size - 1);i > index;i--){
		entries[i] = entries[i - 1];
	}
	entries[index] = newEntry;

    }

    public Object put (K key, Object value) {
	int index = find(key);
	if (found(index, key))
	    return entries[index].setValue(value);
	add(index, new Entry(key, value));
	return null;
    }

    Entry remove (int index) {
	Entry entry = entries[index];

	// EXERCISE
	// Remove the entry at index while keeping the rest of the entries
	// in the same order.
	// Decrement size.

	for (int i = index; i < (size - 1); i++){
		entries[i] = entries[i + 1];
	}
	size--;
	return entry;

    }

    public Object remove (Object keyAsObject) {
	K key = (K) keyAsObject;
	int index = find(key);
	if (found(index, key))
	    return remove(index).value;
	return null;
    }

    protected class Iter implements Iterator<Map.Entry<K, Object>> {
	int index = 0;
    
	public boolean hasNext () { 
	    return index < size;
	}
    
	public Map.Entry<K, Object> next () {
	    if (!hasNext())
		throw new NoSuchElementException();
	    return entries[index++];
	}
    
	public void remove () {
	    throw new UnsupportedOperationException();
	}
    }
  
    protected class Setter extends AbstractSet<Map.Entry<K, Object>> {
	public Iterator<Map.Entry<K, Object>> iterator () {
	    return new Iter();
	}
    
	public int size () { return ArrayMap.this.size(); }
    }
  
    public Set<Map.Entry<K, Object>> entrySet () { return new Setter(); }

    public static void main (String[] args) {
	ArrayMap<String> map = new ArrayMap<String>();

	/*
	  System.out.println("map = " + map);
	  map.put("b", 1);
	  System.out.println("map = " + map);
	  map.put("a", 0);
	  System.out.println("map = " + map);
	  map.remove("b");
	  System.out.println("map = " + map);
	  map.put("c", 2);
	  System.out.println("map = " + map);
	  map.put("b", 1);
	  System.out.println("map = " + map);
	  map.remove("b");
	  System.out.println("map = " + map);
	  map.put("b", 1);
	  System.out.println("map = " + map);

	  map.put("d", 3);
	  System.out.println("map = " + map);
	  map.put("i", 8);
	  System.out.println("map = " + map);
	  map.put("h", 7);
	  System.out.println("map = " + map);
	  map.put("f", 5);
	  System.out.println("map = " + map);
	  map.put("e", 4);
	  System.out.println("map = " + map);
	  map.put("g", 6);
	  System.out.println("map = " + map);
	*/

	System.out.println("map = " + map);
	System.out.println("put(\"m\", 7) = " + map.put("m", 7));
	System.out.println("map = " + map);
	System.out.println("get(\"m\") = " + map.get("m"));
	System.out.println("put(\"m\", 9) = " + map.put("m", 9));
	System.out.println("map = " + map);
	System.out.println("remove(\"m\") = " + map.remove("m"));
	System.out.println("map = " + map);
	System.out.println("remove(\"m\") = " + map.remove("m"));
	System.out.println("map = " + map);
	System.out.println("put(\"m\", 2) = " + map.put("m", 2));
	System.out.println("map = " + map);
	System.out.println("put(\"g\", 7) = " + map.put("g", 7));
	System.out.println("map = " + map);
	System.out.println("get(\"g\") = " + map.get("g"));
	System.out.println("put(\"g\", 9) = " + map.put("g", 9));
	System.out.println("map = " + map);
	System.out.println("remove(\"g\") = " + map.remove("g"));
	System.out.println("map = " + map);
	System.out.println("put(\"s\", 8) = " + map.put("s", 8));
	System.out.println("map = " + map);
	System.out.println("get(\"s\") = " + map.get("s"));
	System.out.println("put(\"s\", 2) = " + map.put("s", 2));
	System.out.println("map = " + map);
	System.out.println("remove(\"s\") = " + map.remove("s"));
	System.out.println("map = " + map);
	System.out.println("remove(\"s\") = " + map.remove("s"));
	System.out.println("map = " + map);
	System.out.println("put(\"s\", 0) = " + map.put("s", 0));
	System.out.println("map = " + map);
	System.out.println("put(\"b\", 3) = " + map.put("b", 3));
	System.out.println("map = " + map);
	System.out.println("get(\"b\") = " + map.get("b"));
	System.out.println("put(\"b\", 9) = " + map.put("b", 9));
	System.out.println("map = " + map);
	System.out.println("remove(\"b\") = " + map.remove("b"));
	System.out.println("map = " + map);
	System.out.println("put(\"p\", 2) = " + map.put("p", 2));
	System.out.println("map = " + map);
	System.out.println("get(\"p\") = " + map.get("p"));
	System.out.println("put(\"p\", 5) = " + map.put("p", 5));
	System.out.println("map = " + map);
	System.out.println("remove(\"p\") = " + map.remove("p"));
	System.out.println("map = " + map);
	System.out.println("put(\"w\", 4) = " + map.put("w", 4));
	System.out.println("map = " + map);
	System.out.println("get(\"w\") = " + map.get("w"));
	System.out.println("put(\"w\", 7) = " + map.put("w", 7));
	System.out.println("map = " + map);
	System.out.println("remove(\"w\") = " + map.remove("w"));
	System.out.println("map = " + map);
	System.out.println("remove(\"w\") = " + map.remove("w"));
	System.out.println("map = " + map);
    }
}
