package prog06;
import java.util.Map;
import java.util.Random;

public class SkipMap<K extends Comparable<K>, V> extends LinkedMap<K, V> {
    // SkipMap containing half the elements chosen at random.
    SkipMap<K, Entry> skip;

    // Coin flipping code.
    Random random = new Random(1);
    /** Flip a coin.
     * @return true if you flip heads.
     */
    boolean heads () {
	return random.nextInt() % 2 == 0;
    }

    protected void add (Entry nextEntry, Entry newEntry) {
	super.add(nextEntry, newEntry);

	// EXERCISE
	// Flip a coin.  If you flip heads, put newEntry into skip, using
	// its own key as key.  Don't forget to allocate skip if it hasn't
	// been allocated yet.
	///
        if (heads()) {
            if (skip == null) {
                skip = new SkipMap<K, Entry>();
            }
            skip.put(newEntry.getKey(), newEntry);
        }

	///
    }

    protected Entry find (K key) {
	Entry entry = null, previous = last;
	// EXERCISE
	// Call find for the key in skip.
	// Set entry to the value of that Entry in skip,
	// and previous to entry.previous.
	// Check for nulls so you don't crash.
	// If you can't set entry and previous because of nulls,
	// leave them with the values above.
	///

     //Homework lab:
        if (skip != null){
            Map.Entry<K, Entry> skipEntry = skip.find(key);
            if(skipEntry != null){
                entry = skipEntry.getValue();
                previous = entry.previous;
            }
        }

//    if (skip != null){
//        entry = skip.get(key);
//        if(entry != null){
//            previous = entry.previous;
//        }
//    }

	// EXERCISE
	// entry is an Entry that is >= key (or null),
	// and previous is its predecessor.
	// Keep moving them back while doing so would keep this true.
	// We want entry to be the earliest Entry that is >= key,
	// or null if there aren't any that are >= key.
	///

        //Homework Lab
        while (previous != null && key.compareTo(previous.key) <= 0){
            entry = previous;
            previous = entry.previous;
        }

//        while (previous != null && previous.next != null) {
//            if (previous.next.key.compareTo(key) >= 0) {
//                entry = previous.next;
//                break;
//            }
//            previous = previous.next;
//        }

	return entry;
    }

    protected void remove (Entry entry) {

	super.remove(entry);
	// EXERCISE
	// Remove the key of entry from skip.  (Use public remove.)
	///
    if (skip != null) {
        skip.remove(entry.getKey());
    }

	///
    }

    public String toString () {
	if (skip == null)
	    return super.toString();
	return skip.toString() + "\n" + super.toString();
    }

    public static void main (String[] args) {
	Map<String, Integer> map = new SkipMap<String, Integer>();
	test(map);
    }
}

