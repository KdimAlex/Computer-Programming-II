package prog09;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.AbstractSet;
import java.util.Set;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.List;
import java.util.ArrayList;

public class BTree <K extends Comparable<K>, V>
		extends AbstractMap<K, V> {

	ArrayMap<K> map = new ArrayMap<K>();

	public int size () { return map.size(); }

	public boolean containsKey (Object keyAsObject) {
		return map.containsKey(keyAsObject);
	}

	public V get (Object keyAsObject) {
		return (V) map.get(keyAsObject);
	}

	public V put (K key, V value) {
		V oldValue = (V) map.put(key, value);
		if (map.size == 4) {
			// EXERCISE
			// split map into two maps
			// Create new Array2Map with these maps as values.
			// Set map to the Array2Map.
			ArrayMap<K> right = map.split();
			Array2Map<K> newMap = new Array2Map<K>();

			// Replace nulls by correct key and value.
			ArrayMap<K>.Entry mapEntry = newMap.new Entry(map.entries[0].key, map);
			ArrayMap<K>.Entry rightEntry = newMap.new Entry(right.entries[0].key, right);

			// Add mapEntry and rightEntry to newMap using newMap.add();
			newMap.add(0,mapEntry);
			newMap.add(1,rightEntry);


			map = newMap;
		}
		return oldValue;
	}

	public V remove (Object keyAsObject) {
		V value = (V) map.remove(keyAsObject);
		if (map.size == 1 && map instanceof Array2Map) {
			//EXERCISE:
			// Replace map with value of the only entry.
			map = ((Array2Map<K>)map.entries[0].value);

		}
		return value;
	}

	void addEntries(ArrayMap<K> map, List<Map.Entry<K, V>> list) {
		if (map instanceof Array2Map) {
			Array2Map<K> map2 = (Array2Map<K>) map;
			for (int i = 0; i < map2.size; i++)
				addEntries(map2.getMap(i), list);
		}
		else {
			for (int i = 0; i < map.size; i++)
				list.add((Map.Entry<K, V>) map.entries[i]);
		}
	}

	Iterator<Map.Entry<K, V>> myIterator () {
		List<Map.Entry<K, V>> list = new ArrayList<Map.Entry<K, V>>();
		addEntries(map, list);
		return list.iterator();
	}

	int mySize () {
		List<Map.Entry<K, V>> list = new ArrayList<Map.Entry<K, V>>();
		addEntries(map, list);
		return list.size();
	}

	protected class Setter extends AbstractSet<Map.Entry<K, V>> {
		public Iterator<Map.Entry<K, V>> iterator () {
			return myIterator();
		}

		public int size () {
			return mySize();
		}
	}

	public Set<Map.Entry<K, V>> entrySet () { return new Setter(); }

	void print (ArrayMap map, String indent) {
		Set<Map.Entry<K, Object>> set = map.entrySet();
		for (Map.Entry<K, Object> entry : set) {
			System.out.print(indent + entry.getKey());
			if (entry.getValue() instanceof ArrayMap) {
				System.out.println();
				print((ArrayMap) entry.getValue(), indent + "  ");
			}
			else
				System.out.println(" " + entry.getValue());
		}
	}

	void putTest (K key, V value) {
		System.out.println("put(" + key + ", " + value + ") = " + put(key, value));
		if (!get(key).equals(value))
			System.out.println("ERROR: get(" + key + ") = " + get(key));
		System.out.println(map);
		print(map, "");
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
		System.out.println(map);
		print(map, "");
	}

	public static void main (String[] args) {
		BTree<String, Integer> tree = new BTree<String, Integer>();
		System.out.println("tree = " + tree);

		tree.putTest("a", 0);
		tree.putTest("b", 1);
		tree.putTest("c", 2);
		tree.putTest("d", 3);
		tree.putTest("e", 4);
		tree.putTest("f", 5);
		tree.putTest("g", 6);
		tree.putTest("h", 7);
		tree.putTest("i", 8);
		tree.removeTest("a");
	}
}
