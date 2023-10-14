package prog09;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.AbstractSet;
import java.util.Set;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Array2Map <K extends Comparable<K>> extends ArrayMap<K> {
    ArrayMap<K> getMap (int i)  {
	return (ArrayMap<K>) entries[i].value;
    }

    Array2Map<K> split () {
	if (size != 4)
	    System.err.println("splitting with size < 4: " + size);
	Array2Map<K> right = new Array2Map<K>();
	right.entries[0] = entries[2];
	right.entries[1] = entries[3];
	size = 2;
	right.size = 2;
	return right;
    }

    public int size () {
	int n = 0;
	for (int i = 0; i < size; ++i)
	    n += getMap(i).size();
	return n;
    }

    int find (K key) {
	int index = super.find(key);
	if (found(index, key))
	    return index;
	return index-1;
    }

    public boolean containsKey (Object keyAsObject) {
	K key = (K) keyAsObject;
	int index = find(key);
	if (index < 0)
	    return false;
	return getMap(index).containsKey(key);
    }
  
    public Object get (Object keyAsObject) {

	// EXERCISE
	// Look at containsKey and the override for find.
	// Implement get for the Map.
		K key = (K) keyAsObject;
		int index = find(key);
		if (index < 0){
			return null;
		}
		return getMap(index).get(key);


    }

    public Object put (K key, Object value) {
	int index = find(key);

	// EXERCISE
	// Adjust index to where key should go.

	if (index < 0){
		index = 0;
	}

	ArrayMap<K> map = getMap(index);
	Object oldValue = map.put(key, value);

	// Update the key of entries[index].
	entries[index].key = map.entries[0].key;

	if (map.size == 4) {
	    ArrayMap<K> right = map.split();

	    // Set rightEntry to a new Entry.  What key?  What value?
		Entry rightEntry = new Entry(right.entries[0].key, right);

	    // Call add() to add the new Entry.  What index?
		add(index + 1, rightEntry);
	}

	return oldValue;
    }

    public Object remove (Object keyAsObject) {
		K key = (K) keyAsObject;
		int index = find(key);
		if (index < 0)
			return null;
		ArrayMap<K> map = getMap(index);
		Object oldValue = map.remove(key);
		entries[index].key = map.entries[0].key;
		if (map.size == 1) {
			// EXERCISE

			// If there is no neighbor to the right of index,
			// decrement index and set map again.
			if (index == size - 1) {
				index--;
				map = getMap(index);
			}





			{
				ArrayMap<K> right; // wrong
				// Set right to the right neighbor of map.
				right = getMap(index+1);
				// While right is not empty
				// Use right.remove() and map.add() to make the
				// first Entry in right into the last Entry in map.
				while (right.size > 0){
					Entry entry = right.remove(0);
					map.add(map.size, entry);
				}
			}

			// Call remove() to remove the (now empty) neighbor to the
			// right.
			K keyThing = getMap(index+1).entries[0].key;
			int rightIndex = find(keyThing);
			remove(rightIndex);

			// If map has too many entries now, you know what to do.
			if (map.size == 4) {
				ArrayMap<K> rightMap = map.split();
				Entry rightEntry = new Entry(rightMap.entries[0].key, rightMap);
				add(index+1, rightEntry);
			}


		}
		return oldValue;
	}


	protected class Iter implements Iterator<Map.Entry<K, Object>> {
	int index = 0;
	Iterator<Map.Entry<K, Object>> iter;
    
	Iter () {
	    if (index < size)
		iter = getMap(index).entrySet().iterator();
	}

	public boolean hasNext () { 
	    return index < size && iter.hasNext();
	}
    
	public Map.Entry<K, Object> next () {
	    if (!hasNext())
		throw new NoSuchElementException();
	    Map.Entry<K, Object> entry = iter.next();
	    if (!iter.hasNext()) {
		index++;
		if (index < size)
		    iter = getMap(index).entrySet().iterator();
	    }
	    return entry;
	}
    
	public void remove () {
	    throw new UnsupportedOperationException();
	}
    }
  
    protected class Setter extends AbstractSet<Map.Entry<K, Object>> {
	public Iterator<Map.Entry<K, Object>> iterator () {
	    return new Iter();
	}
    
	public int size () { return Array2Map.this.size(); }
    }
  
    // public Set<Map.Entry<K, Object>> entrySet () { return new Setter(); }

    void putTest (K key, int value) {
	System.out.println("put(" + key + ", " + value + ") = " + put(key, value));
	System.out.println(this);
	if (!get(key).equals(value))
	    System.out.println("ERROR: get(" + key + ") = " + get(key));
    }

    void removeTest (K key) {
	Object v = get(key);
	Object value = remove(key);
	if (!v.equals(value))
	    System.out.print("ERROR: ");
	System.out.println("remove(" + key + ") = " + value);
	value = remove(key);
	if (value != null)
	    System.out.println("ERROR: remove(" + key + ") = " + value);
	System.out.println(this);
    }

    public static void main (String[] args) {
	ArrayMap<String> map = new ArrayMap<String>();
	map.put("b", 1);
	Array2Map<String> map2 = new Array2Map<String>();
	map2.add(0, map2.new Entry("b", map));
	map2.size = 1;
	System.out.println(map2);

	map2.putTest("c", 2);
	map2.putTest("c", 7);
	map2.putTest("a", 0);
	map2.putTest("d", 3);
	map2.putTest("d", 7);
	map2.removeTest("a");
	map2.putTest("e", 9);
	map2.putTest("f", 8);
	map2.removeTest("b");
	map2.removeTest("e");
	map2.putTest("e", 9);
	map2.putTest("b", 1);
	map2.removeTest("f");
	map2.putTest("f", 3);
	map2.putTest("g", 4);
	map2.removeTest("d");
	map2.putTest("d", 6);
	map2.putTest("h", 8);
	map2.removeTest("e");
    }
}
