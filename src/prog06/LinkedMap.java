package prog06;
import java.util.*;
import java.util.Map.Entry;

public class LinkedMap <K extends Comparable<K>, V>
    extends AbstractMap<K, V> {

    protected class Entry implements Map.Entry<K, V> {
	K key;
	V value;
	Entry previous, next;
    
	Entry (K key, V value) {
	    this.key = key;
	    this.value = value;
	}
    
	public K getKey () { return key; }
	public V getValue () { return value; }
	public V setValue (V newValue) {
	    V oldValue = value;
	    value = newValue;
	    return oldValue;
	}

	public String toString () {
	    return "{" + key + "=" + value + "}";
	}
    }
  
    protected Entry first, last;
  
    /**
     * Find the Entry e with e.key equal to key.
     * @param key The Key to be found.
     * @return The Entry e with e.key.equals(key)
     * or null if there isn't one.
     */
    protected Entry find (K key) {
	// EXERCISE
	// Return the earliest Entry that is >= to key
	// or null if there is no such Entry.
	///
	for (Entry entry = first; entry != null; entry = entry.next)
	    if (entry.key.compareTo(key) >= 0)
			return entry;
	///
	return null; // Did not find the entry.
    }    
  
    /**
     * Determine if the Entry returned by find is the one we are looking
     * for.
     * @param entry The Entry returned by find.
     * @param key The Key to be found.
     * @return true if find found the entry with that key
     * or false otherwise
     */
    protected boolean found (Entry entry, K key) {
	// EXERCISE
	///
		if (entry ==null){
			return false;
		}
		if(entry.getKey().equals(key)){
			return true;
		} else{
			return false;
		} // Fix this.
	///
    }

    public boolean containsKey (Object keyAsObject) {
	K key = (K) keyAsObject;
	Entry entry = find(key);
	return found(entry, key);
    }
  
    /**
     * Add newEntry just before nextEntry or as last Entry if
     * nextEntry is null.
     * @param nextEntry Entry after newEntry or null if there isn't one.
     * @param newEntry The new Entry to be inserted previous to nextEntry.
     */
    protected void add (Entry nextEntry, Entry newEntry) {
	// EXERCISE
	Entry previousEntry = null;
	///
	// Set previousEntry.  Two cases. If at tail
		if(nextEntry == null){
			previousEntry = last;
		} else {
			previousEntry = nextEntry.previous;
		}
	// Set previousEntry.next or first to newEntry. If at head
		if(previousEntry == null){
			first = newEntry;
		} else {
			previousEntry.next = newEntry;
		}
	// Set nextEntry.previous or last to newEntry.
		if (nextEntry == null){
			last = newEntry;
		} else{
			nextEntry.previous = newEntry;

		}
	// Set newEntry.previous and newEntry.next.
		newEntry.previous = previousEntry;
		newEntry.next = nextEntry;
	//
    }

    public V get (Object keyAsObject) {
	// EXERCISE
	// Look at containsKey.
	// If Entry with key was found, return its value.
	///
		K key = (K) keyAsObject;
		Entry entry = find(key);

		if(found(entry, key)){
			return entry.getValue();
		}



	///
	return null;
    }
  
    public V put (K key, V value) {
	Entry entry = find(key);
	// EXERCISE
	// Handle the case that the key is already there.
	// Save yourself typing:  setValue returns the old value!
	///
	if(found(entry,key)){
		return entry.setValue(value);
	}
	///
	// key is not there:
	Entry newEntry = new Entry(key, value);
	add(entry, newEntry);
	return null;
    }      
  
    protected class Iter implements Iterator<Map.Entry<K, V>> {
	Entry entry = first;
    
	public boolean hasNext () { 
	    // EXERCISE
	    ///
			if (entry == null) {
				return false;
			}

			return true; // wrong}
				///

		}
    
	public Map.Entry<K, V> next () {
	    // EXERCISE
	    // Entry implements Map.Entry<K, V> so you return entry,
	    // not entry.key or entry.value.
	    // What else do you do?
	    ///

		if (!hasNext()){
			throw new NoSuchElementException();
		}
		Entry givenEntry = entry;
		entry = entry.next;
		return givenEntry;


	    ///
	}
    
	public void remove () {
	    throw new UnsupportedOperationException();
	}
    }
  
    /**
     * Remove Entry entry from list.
     * @param entry The entry to remove.
     */
    protected void remove (Entry entry) {
	// EXERCISE
	///

		if (entry == null) {
			return;
		}

		if (entry.previous == null) {
			first = entry.next;
		} else {
			entry.previous.next = entry.next;
		}

		if (entry.next == null) {
			last = entry.previous;
		} else {
			entry.next.previous = entry.previous;
		}
    }

    public V remove (Object keyAsObject) {
	// EXERCISE
	// Use find, but make sure you got the right Entry!
	// If you do, then remove it and return its value.
	///
		K key = (K) keyAsObject;
		Entry entry = find(key);

		if(found(entry, key)){
			V value = entry.getValue();
			remove(entry);
			return value;
		}


	///
	return null;
    }      

    public int size () {
	int count = 0;
	for (Entry entry = first; entry != null; entry = entry.next)
	    count++;
	return count;
    }

    protected class Setter extends AbstractSet<Map.Entry<K, V>> {
	public Iterator<Map.Entry<K, V>> iterator () {
	    return new Iter();
	}
    
	public int size () { return LinkedMap.this.size(); }
    }
  
    public Set<Map.Entry<K, V>> entrySet () { return new Setter(); }
  
    static void test (Map<String, Integer> map) {
	if (false) {
	    map.put("Victor", 50);
	    map.put("Irina", 45);
	    map.put("Lisa", 47);
    
	    for (Map.Entry<String, Integer> pair : map.entrySet())
		System.out.println(pair.getKey() + " " + pair.getValue());
    
	    System.out.println(map.put("Irina", 55));

	    for (Map.Entry<String, Integer> pair : map.entrySet())
		System.out.println(pair.getKey() + " " + pair.getValue());

	    System.out.println(map.remove("Irina"));
	    System.out.println(map.remove("Irina"));
	    System.out.println(map.get("Irina"));
    
	    for (Map.Entry<String, Integer> pair : map.entrySet())
		System.out.println(pair.getKey() + " " + pair.getValue());
	}
	else {
	    String[] keys = { "Vic", "Ira", "Sue", "Zoe", "Bob", "Ann", "Moe" };
	    for (int i = 0; i < keys.length; i++) {
		System.out.print("put(" + keys[i] + ", " + i + ") = ");
		System.out.println(map.put(keys[i], i));
		System.out.println(map);
		System.out.print("put(" + keys[i] + ", " + -i + ") = ");
		System.out.println(map.put(keys[i], -i));
		System.out.println(map);
		System.out.print("get(" + keys[i] + ") = ");
		System.out.println(map.get(keys[i]));
		System.out.print("remove(" + keys[i] + ") = ");
		System.out.println(map.remove(keys[i]));
		System.out.println(map);
		System.out.print("get(" + keys[i] + ") = ");
		System.out.println(map.get(keys[i]));
		System.out.print("remove(" + keys[i] + ") = ");
		System.out.println(map.remove(keys[i]));
		System.out.println(map);
		System.out.print("put(" + keys[i] + ", " + i + ") = ");
		System.out.println(map.put(keys[i], i));
		System.out.println(map);
	    }
	    for (int i = keys.length; --i >= 0;) {
		System.out.print("remove(" + keys[i] + ") = ");
		System.out.println(map.remove(keys[i]));
		System.out.println(map);
		System.out.print("put(" + keys[i] + ", " + i + ") = ");
		System.out.println(map.put(keys[i], i));
		System.out.println(map);
	    }
	}
    }

    public static void main (String[] args) {
	Map<String, Integer> map = new LinkedMap<String, Integer>();
	test(map);
    }
}
