package prog02;

import java.io.*;
import java.util.Scanner;

/**
 * This is an implementation of PhoneDirectory that uses a sorted
 * array to store the entries.
 *
 * @author 
 */
public class SortedPD extends ArrayBasedPD {

    /** Find an entry in the directory.
     @param name The name to be found
     @return The index of the entry with that name or, if it is not
     there, the index where it should be added.
     */
    protected int find (String name) {
        int low = 0;
        int middle;
        int high = size;

        while (low < high){
            middle = (low + high) / 2;
            if (theDirectory[middle].getName().compareToIgnoreCase(name) < 0){
                low = middle + 1;
            } else {
                high = middle;
            }
        }
        return high;

//        for (int i = 0; i < size; i++)
//            if (theDirectory[i].getName().equals(name)){
//                return i;
//            }
//            else if (name.compareTo(theDirectory[i].getName()) < 0){
//                return (i - 1);
//            }
//        return size;


//        for (int i = 0; i < size; i++)
//            if (theDirectory[i].getName().equals(name))
//                return i;
//
//        return size;
    }

    /** Determine if name is located at index.
     @param index The index to be checked.
     @param name The name that might be located at that index.
     @return true if a DirectoryEntry with that name is located at
     that index.
     */
    protected boolean found (int index, String name) {
        boolean found = index < size;
        if (found){
            if((theDirectory[index].getName()).equals(name)){
                return true;
            } else {
                return false;
            }
        }
        return false;

//            return index < size;
    }

    /** Add an entry to the directory.
     @param index The index at which to add the entry to theDirectory.
     @param newEntry The new entry to add.
     @return The DirectoryEntry that was just added.
     */
    protected DirectoryEntry add (int index, DirectoryEntry newEntry) {
        if (size == theDirectory.length){
            reallocate();
        }
        size++;
        for (int i = (size - 1);i > index;i--){
            theDirectory[i] = theDirectory[i - 1];
        }
        theDirectory[index] = newEntry;
        return newEntry;

//        if (size == theDirectory.length)
//            reallocate();
//        theDirectory[size] = theDirectory[index];
//        theDirectory[index] = newEntry;
//        size++;
//        return newEntry;
    }

    /** Remove an entry from the directory.
     @param index The index in theDirectory of the entry to remove.
     @return The DirectoryEntry that was just removed.
     */
    protected DirectoryEntry remove (int index) {
        DirectoryEntry entry = theDirectory[index];

        for (int i = index; i < (size - 1); i++){
            theDirectory[i] = theDirectory[i + 1];
        }
        size--;
        return entry;

//        theDirectory[index] = theDirectory[size-1];
//        size--;
//        return entry;
    }



}
