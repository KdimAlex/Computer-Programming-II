package prog10;

import java.util.Map;
import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Set;
import java.util.AbstractSet;
import java.util.NoSuchElementException;
import java.io.PrintWriter;
import java.io.FileWriter;

public class HashTable<K, V> extends AbstractMap<K, V> {
  private class Entry implements Map.Entry<K, V> {
    K key;
    V value;
    Entry next;

    public K getKey () { return key; }
    public V getValue () { return value; }
    public V setValue (V value) { 
	V oldValue = this.value;
	this.value = value;
	return oldValue;
    }

    Entry (K key, V value) {
      this.key = key;
      this.value = value;
    }
  }

  private final static int DEFAULT_CAPACITY = 5;
  private Entry[] table = new HashTable.Entry[DEFAULT_CAPACITY];
  private int size;

  private int hashIndex (Object key) {
    int index = key.hashCode() % table.length;
    if (index < 0)
      index += table.length;
    return index;
  }

  public V put (K key, V value) {
    // System.out.println("put " + key + " " + value + " hash index " + hashIndex(key));

    // EXERCISE 1
    Entry entry = find(key);
    if (entry != null) {
      // What do you do if you find the Entry with that key?
      entry.setValue(value);
      return value;
    }

    if (size == table.length)
      rehash(2 * table.length);

    // Create a new Entry and add it to the appropriate list.
    Entry newEntry = new Entry(key, value);
    newEntry.next = table[hashIndex(key)];
    table[hashIndex(key)] = newEntry;


    // Increment size.
    size++;
    return null;
  }
	
  private Entry find (Object key) {
    int index = hashIndex(key);
    
    // EXERCISE 2
    // Search the list stored in table[index]
    // for an Entry with that key.
    // If you find it, return it.
    Entry entry = table[index];
    while(entry != null){
      if(entry.key.equals(key)){
        return entry;
      }
      entry = entry.next;
    }

    return null;
  }

  public boolean containsKey (Object key) {
    return find(key) != null;
  }

  public V get (Object key) {
    Entry entry = find(key);
    if (entry == null)
      return null;
    return entry.value;
  }

  private void rehash (int newCapacity) {
    System.out.println("rehash " + newCapacity);
    Entry[] oldTable = table;
    table = new HashTable.Entry[newCapacity];

    for (int i = 0; i < oldTable.length; i++) {
      // EXERCISE 3
      // Move every Entry in oldTable[i] to the correct index list in table.
      Entry entry = oldTable[i];
      while(entry != null){
        put(entry.key, entry.value);
        size--;
        entry = entry.next;
      }
  
    }

  }

  public V remove (Object key) {
    // System.out.println("remove " + key + " hash index " + hashIndex(key));

    // EXERCISE 4
    // Get the index for the key.
    int index = hashIndex(key);
 
    // What do you do if the linked list at that index is empty?
    if (table[index] == null) {
      return null;
    }


    // What do you do if the first element of the list has the key?
    // Rehash to half the current length if size is 1/4 the length or less.
    if (table[index].key.equals(key)){
      Entry removedEntry = table[index];
      table[index] = table[index].next;
      size--;

      if (size <= table.length / 4) {
        rehash(table.length / 2);
      }

      return removedEntry.value;
    }









    // If the key is farther down the list, make sure you keep track
    // of the pointer to the previous entry, because you will need to
    // change its next variable.
    // Rehash to half the current length if size is 1/4 the length or less.
//    Entry currentEntry = table[index];
//    while (currentEntry != null){
//       Entry nextEntry = currentEntry.next;
//
//      if(nextEntry.key.equals(key)){
//        size--;
//        Entry removedEntry = nextEntry;
//        currentEntry.next = nextEntry.next;
//
//        if (size <= table.length / 4) {
//          rehash(table.length / 2);
//        }
//
//        return removedEntry.value;
//      }
//
//      currentEntry = currentEntry.next;
//    }
    Entry previousEntry = table[index];
    Entry currentEntry = previousEntry.next;

    while (currentEntry != null && !currentEntry.key.equals(key)) {
      previousEntry = currentEntry;
      currentEntry = currentEntry.next;
    }

    if (currentEntry != null) {
      previousEntry.next = currentEntry.next;
      size--;

      // Rehash to half the current length if size is 1/4 the length or less.
      if (size <= table.length / 4) {
        rehash(table.length / 2);
      }

      return currentEntry.value;
    }








    // Return null otherwise.
    return null;
  }

  private Iterator<Map.Entry<K, V>> entryIterator () {
    return new EntryIterator();
  }

  private class EntryIterator implements Iterator<Map.Entry<K, V>> {
    // EXERCISE 5
    int index;
    Entry entry;

    private EntryIterator () {
      // Set index to the the index of the first non-empty list
      // or table.length if there isn't one.
      // Set entry to the first entry in that list.

      index = 0;
      while (index < table.length && table[index] == null) {
        index++;
      }

//      index = 0;
//      do {
//        if (table[index] != null){
//          index++;
//        }
//      } while(index < table.length && table[index] == null);

      if (index < table.length) {
        entry = table[index];
      }


    }

    public boolean hasNext () {
      // You just need to use the value of index.
      return entry != null;
    }

    public Map.Entry<K, V> next () {
      if (!hasNext())
        throw new NoSuchElementException();

      Entry nextEntry = entry;

      // Set entry to the next entry in the list.
      // Or if there isn't one,
      // set index to the index of the next non-null list,
      // or table.length if there isn't one,
      // and set entry to the first entry in that list.
      if (entry.next!= null) {
        entry = entry.next;
      } else{
        index++;
        while (index < table.length && table[index] == null) {
          index++;
        }
        if (index < table.length) {
          entry = table[index];
        } else {
          entry = null;
        }
      }







      return nextEntry;
    }

    public void remove () {}
  }

  public Set<Map.Entry<K, V>> entrySet() { return new EntrySet(); }

  private class EntrySet extends AbstractSet<Map.Entry<K, V>> {
    public int size() { return size; }

    public Iterator<Map.Entry<K, V>> iterator () {
      return entryIterator();
    }

    public void remove () {}
  }

  public String toString () {
    String ret = super.toString() + "\n";
    for (int i = 0; i < table.length; i++) {
      ret = ret + i + ":";
      for (Entry entry = table[i]; entry != null; entry = entry.next)
        ret = ret + " " + entry.key + " " + entry.value;
      ret = ret + "\n";
    }
    return ret;
  }

  static void print (PrintWriter out, Object s) {
    out.println(s);
    System.out.println(s);
  }

  public static void main (String[] args) {
    PrintWriter out = null;
    try {
      out = new PrintWriter(new FileWriter("hashtable-output.txt"));
    } catch (Exception e) {
      System.out.println(e);
      return;
    }

    HashTable<String, Integer> table =
      new HashTable<String, Integer>();

    print(out, "put Brad 46 hash index " + table.hashIndex("Brad") + " returns " + table.put("Brad", 46));
    print(out, table);
    print(out, "put Hal 10 hash index " + table.hashIndex("Hal") + " returns " + table.put("Hal", 10));
    print(out, table);
    print(out, "put Brad 60 hash index " + table.hashIndex("Brad") + " returns " + table.put("Brad", 60));
    print(out, table);
    print(out, "put Hal 24 hash index " + table.hashIndex("Hal") + " returns " + table.put("Hal", 24));
    print(out, table);
    print(out, "put Kyle 6 hash index " + table.hashIndex("Kyle") + " returns " + table.put("Kyle", 6));
    print(out, table);
    print(out, "put Lisa 43 hash index " + table.hashIndex("Lisa") + " returns " + table.put("Lisa", 43));
    print(out, table);
    print(out, "put Lynne 43 hash index " + table.hashIndex("Lynne") + " returns " + table.put("Lynne", 43));
    print(out, table);
    print(out, "hash code of Victor is " + "Victor".hashCode());
    print(out, "put Victor 46 hash index " + table.hashIndex("Victor") + " returns " + table.put("Victor", 46));
    print(out, table);
    print(out, "put Zoe 6 hash index " + table.hashIndex("Zoe") + " returns " + table.put("Zoe", 6));
    print(out, table);
    print(out, "put Zoran 76 hash index " + table.hashIndex("Zoran") + " returns " + table.put("Zoran", 76));
    print(out, table);

    for (String key : table.keySet())
      System.out.print(key + " ");
    System.out.println();

    print(out, "remove Fred returns " + table.remove("Fred"));
    print(out, table);
    print(out, "remove Hal returns " + table.remove("Hal"));
    print(out, table);
    print(out, "remove Brad returns " + table.remove("Brad"));
    print(out, table);
    print(out, "remove Lynne returns " + table.remove("Lynne"));
    print(out, table);
    print(out, "remove Lisa returns " + table.remove("Lisa"));
    print(out, table);
    print(out, "remove Lisa returns " + table.remove("Lisa"));
    print(out, table);
    print(out, "remove Zoran returns " + table.remove("Zoran"));
    print(out, table);
    print(out, "remove Zoe returns " + table.remove("Zoe"));
    print(out, table);
    print(out, "remove Victor returns " + table.remove("Victor"));
    print(out, table);
    print(out, "remove Hal returns " + table.remove("Hal"));
    print(out, table);

    out.close();
  }
}
			     
