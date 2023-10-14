package prog05;
import prog02.GUI;
import prog02.UserInterface;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class NotWordle {
    UserInterface ui;
    List<Node> wordEntries = new ArrayList<Node>();

    NotWordle (UserInterface ui) {
        this.ui = ui;
    }
    private class Node{
        String word;
        Node next;

        //constructor -->
        public Node(String word) {
            this.word = word;
        }
    }
    public void loadWords(String fileName) {
       while(true) {
           try {
               File dictionaryFile = new File(fileName);
               Scanner scanner = new Scanner(dictionaryFile);
               while (scanner.hasNext()) {
                   String word = scanner.next();
                   Node node = new Node(word);
                   wordEntries.add(node);
               }
               scanner.close();
               return;
           } catch (FileNotFoundException e) {
               ui.sendMessage("FileNotFoundException. Try again.");
               fileName = ui.getInfo("What is the name of the word file?");
           }
       }

    }

    public static void main(String[] args) throws FileNotFoundException {
        GUI ui = new GUI("Not Wordle Game");
        NotWordle game = new NotWordle(ui);
        String fileName;
        while(true){
            fileName = ui.getInfo("What is the name of the word file?");
            if(fileName == null){
                return;
            }
            if(fileName.length() == 0){
                ui.sendMessage("This file was not found.");
                continue;
            }
            break;
        }
        game.loadWords(fileName);

        String start;
        String end;
        while(true){
            start = ui.getInfo("What is your starting word");
            if(start == null){
                return;
            }
            if(start.length() == 0){
                ui.sendMessage("This word was not found.");
                continue;
            }
            if (game.find(start) == null){
                ui.sendMessage("This is not a real word.");
                continue;
            }
            break;
        }
        while(true){
            end = ui.getInfo("What is your ending word");
            if(end == null){
                return;
            }
            if(end.length() == 0){
                ui.sendMessage("This word was not found.");
                continue;
            }
            if (game.find(end) == null){
                ui.sendMessage("This is not a real word.");
                continue;
            }
            break;
        }
//        start = ui.getInfo("What is your starting word?");
//        if (start == null) {
//            while(true){
//                start = ui.getInfo("Try again");
//                if (find(start) != null)
//                return;
//            }
//        }
//        end = ui.getInfo("What is your ending word?");
//        if (end == null) {
//            while(true){
//                end = ui.getInfo("Try again");
//                if (find(end) != null)
//                    return;
//            }
//        }

        String[] commands = {"Human plays.", "Computer plays."};
        int i = ui.getCommand(commands);
        switch (i) {
            case -1:
                return;
            case 0:
                game.play(start, end);
                return;
            case 1:
                game.solve(start, end);
                return;
        }
    }
        //if human
            //call play
        //if Computer
            //call solve


    void play(String start, String end){
        //In play, do the following forever (until the return occurs).  Tell
        //   the user the current word (the start) and the target word.  Ask for
        //   the next word.  Set the start word variable to that next word.  If
        //   it equals the target, tell the user ``You win!'' and return.
        //   Otherwise keep looping.  Test.
        while (true){
            try{
                if(start.equals(end)){
                    ui.sendMessage("You win!");
                    return;
//                } else if(find(start) == null || find(end) == null){
//                    ui.sendMessage(start + " is not a word");
//                    continue;
                } else {
                    ui.sendMessage("your current word is "+ start +'\n'+ " your target word is " + end);
                    String candidateStart = ui.getInfo("Enter your next word.");
                    if (candidateStart.isEmpty()){
                        ui.sendMessage("Blank guesses are not allowed");
                        continue;
                    }
                    if(find(candidateStart) == null){
                        ui.sendMessage(candidateStart + " is not a word.");
                        continue;

                    }
                    if(!oneLetterDifferent(candidateStart, start)){
                        ui.sendMessage("Sorry, "+ candidateStart + " differs from " + start+ " by more than one letter. Try again.");
                        continue;
                    }
                    start = candidateStart;
                }
            } catch (NullPointerException e){
                ui.sendMessage("NullPointerException");
                return;
            }

        }
    }

    void solve(String start, String end){
        LinkedQueue<Node> solverQueue = new LinkedQueue<Node>();
        Node startingNode = find(start);
        solverQueue.offer(startingNode);

        while (!solverQueue.isEmpty()){
            Node theNode = solverQueue.poll();
            if(theNode.word.equals(end)){
                ui.sendMessage("Solution found between "+ start +" and "+ end);
                String s = "";
                while(theNode != null){
                    s = "\n" + theNode.word + s;
                    theNode = theNode.next;
                }
                ui.sendMessage(s);
                return;
            }
            for(Node nextNode : wordEntries){
                if (oneLetterDifferent(theNode.word, nextNode.word) && (nextNode != startingNode) && (nextNode.next == null)){
                    nextNode.next = theNode;
                    solverQueue.offer(nextNode);
                }
            }
        }
        ui.sendMessage("Solution was not found.");


    }

    public static boolean oneLetterDifferent(String firstWord, String secondWord){
        int lettersDifferent = 0;
        if (firstWord.length() != secondWord.length()){
            return false;
        }
        for (int i = 0; i < firstWord.length(); i++){
            if (firstWord.charAt(i) != secondWord.charAt(i)){
                lettersDifferent++;
            }
        }
        if(lettersDifferent == 1){
            return true;
        }
        return false;
    }

    public Node find(String candidateWord){
        int bottom = 0;
        int top = wordEntries.size();
        while(bottom <= top){
            int middle = (top+bottom) / 2;
            if(wordEntries.get(middle).word.compareToIgnoreCase(candidateWord) == 0){
                return wordEntries.get(middle); 
            } else if (wordEntries.get(middle).word.compareToIgnoreCase(candidateWord) < 0){
                bottom = middle + 1;
            } else{
                top = middle - 1;
            }
        }
        return null;
    }

}
