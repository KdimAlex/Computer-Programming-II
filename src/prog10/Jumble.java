package prog10;

import java.io.File;
import java.util.*;

import prog02.UserInterface;
import prog02.GUI;

public class Jumble {
  /**
   * Sort the letters in a word.
   * @param word Input word to be sorted, like "computer".
   * @return Sorted version of word, like "cemoptru".
   */
  public static String sort (String word) {
    char[] sorted = new char[word.length()];
    for (int n = 0; n < word.length(); n++) {
      char c = word.charAt(n);
      int i = n;

      while (i > 0 && c < sorted[i-1]) {
        sorted[i] = sorted[i-1];
        i--;
      }

      sorted[i] = c;
    }

    return new String(sorted, 0, word.length());
  }

  public static void main (String[] args) {
    UserInterface ui = new GUI("Jumble");
    // UserInterface ui = new ConsoleUI();

    // Map<String,String> map = new TreeMap<String,String>();
    // Map<String,String> map = new PDMap();
    //Map<String,String> map = new LinkedMap<String,String>();
    //Map<String,String> map = new SkipMap<String,String>();
    Map<String, List<String>> map = new HashTable<>();

    Scanner in = null;
    do {
      try {
        in = new Scanner(new File(ui.getInfo("Enter word file.")));
      } catch (Exception e) {
        System.out.println(e);
        System.out.println("Try again.");
      }
    } while (in == null);

    int n = 0;
    while (in.hasNextLine()) {
      String word = in.nextLine();
      if (n++ % 1000 == 0)
        System.out.println(word + " sorted is " + sort(word));

///Test for Remove Version 2:
//      List<String> removeWordList = new ArrayList<String>();
//      removeWordList.add(word);
//      String s = sort(word);
//      Object wordRemoved = s;

//      System.out.println(map.put(sort(word), removeWordList));
//
//      map.remove(wordRemoved);
//
//      map.get(wordRemoved);
//
//      map.remove(wordRemoved);
///

      // EXERCISE: Insert an entry for word into map.
      // What is the key?  What is the value?
      ///

      if (map.containsKey(sort(word))) {
        List<String> words = map.get(sort(word));
        words.add(word);
        map.put(sort(word), words);
      } else {
        List<String> words = new ArrayList<String>();
        words.add(word);
        map.put(sort(word), words);
      }

      ///
    }
    while (true) {
      String jumble = ui.getInfo("Enter jumble.");
      if (jumble == null)
        break;

      // EXERCISE:  Look up the jumble in the map.
      // What key do you use?
      ///
      List<String> word = map.get(sort(jumble));
      ///
      if (word == null)
        ui.sendMessage("No match for " + jumble);
      else
        ui.sendMessage(jumble + " unjumbled is " + word);

//      //TEST FOR REMOVE:
      String s = "-cdegillmnooprw";
      Object wordRemoved = s;
      System.out.println(map.remove(wordRemoved) +" should be gone and false: " + map.containsKey(wordRemoved));
      System.out.println("Should be null:" + map.get(wordRemoved));
      System.out.println("Should be null:" + map.remove(wordRemoved));
    }



    while (true) {
      String hint = ui.getInfo("What are your letters?");
      if (hint == null){
        return;
      }
      String sortedLetters = sort(hint);
      String letterCount = ui.getInfo("How many letters?");
      if (letterCount == null) {
        break;
      }

      for(String key1 : map.keySet()) {
        String key2 = "";
        int key1index = 0;

        for (int i = 0; i < sortedLetters.length(); i++) {
          if (key1index < key1.length() && sortedLetters.charAt(i) == key1.charAt(key1index)) {
            key1index++;
          } else {
            key2 = key2 + sortedLetters.charAt(i);
          }
        }

        if (key1index == key1.length() && map.containsKey(key2)) {
          ui.sendMessage(map.get(key1) + " " + map.get(key2));
        }


      }
    }
  }





}


        
    

