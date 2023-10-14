package prog07;
import prog02.GUI;
import prog02.UserInterface;
import prog05.LinkedQueue;

import java.util.PriorityQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

public class NotWordle {
    UserInterface ui;
    List<Node> wordEntries = new ArrayList<Node>();

    NotWordle(UserInterface ui) {
        this.ui = ui;
    }

    protected class Node {
        String word;
        Node next;

        //constructor -->
        public Node(String word) {
            this.word = word;
        }
    }

    boolean loadWords(String fileName) {

        try {
            Scanner scanner = new Scanner(new File(fileName));
            while (scanner.hasNextLine()) {
                String word = scanner.nextLine();
                Node node = new Node(word);
                wordEntries.add(node);
            }
//                scanner.close();
            return true;
        } catch (Exception e) {
            ui.sendMessage("Error: " + e);
            return false;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        GUI ui = new GUI("Not Wordle Game");
        NotWordle game = new NotWordle(ui);
        String fileName;
        while (true) {
            fileName = ui.getInfo("What is the name of the word file?");
            if (fileName == null) {
                return;
            }
            if (fileName.length() == 0) {
                ui.sendMessage("This file was not found.");
                continue;
            }
            break;
        }
        game.loadWords(fileName);

        String start;
        String end;
        while (true) {
            start = ui.getInfo("What is your starting word");
            if (start == null) {
                return;
            }
            if (start.length() == 0) {
                ui.sendMessage("This word was not found.");
                continue;
            }
            if (game.find(start) == null) {
                ui.sendMessage("This is not a real word.");
                continue;
            }
            break;
        }
        while (true) {
            end = ui.getInfo("What is your ending word");
            if (end == null) {
                return;
            }
            if (end.length() == 0) {
                ui.sendMessage("This word was not found.");
                continue;
            }
            if (game.find(end) == null) {
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

        String[] commands = {"Human plays.", "Computer plays.", "Solve 2.", "Solve 3."};
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
            case 2:
                game.solve2(start,end);
                return;
            case 3:
                game.solve3(start,end);
                return;
        }
    }
    //if human
    //call play
    //if Computer
    //call solve


    void play(String start, String end) {
        //In play, do the following forever (until the return occurs).  Tell
        //   the user the current word (the start) and the target word.  Ask for
        //   the next word.  Set the start word variable to that next word.  If
        //   it equals the target, tell the user ``You win!'' and return.
        //   Otherwise keep looping.  Test.
        while (true) {
            try {
                if (start.equals(end)) {
                    ui.sendMessage("You win!");
                    return;
//                } else if(find(start) == null || find(end) == null){
//                    ui.sendMessage(start + " is not a word");
//                    continue;
                } else {
                    ui.sendMessage("your current word is " + start + '\n' + " your target word is " + end);
                    String candidateStart = ui.getInfo("Enter your next word.");
                    if (candidateStart.isEmpty()) {
                        ui.sendMessage("Blank guesses are not allowed");
                        continue;
                    }
                    if (find(candidateStart) == null) {
                        ui.sendMessage(candidateStart + " is not a word.");
                        continue;
                    }
                    if (!oneLetterDifferent(candidateStart, start)) {
                        ui.sendMessage("Sorry, " + candidateStart + " differs from " + start + " by more than one letter. Try again.");
                        continue;
                    }
                    start = candidateStart;
                }
            } catch (NullPointerException e) {
                ui.sendMessage("NullPointerException");
                return;
            }

        }
    }

    void solve(String start, String end) {
        Queue<Node> solverQueue = new ArrayDeque<Node>();
        Node startingNode = find(start);
        solverQueue.offer(startingNode);
        int timeQueueIsPolled = 0;
        while (!solverQueue.isEmpty()) {
            Node theNode = solverQueue.poll();
            timeQueueIsPolled++;

            for (Node nextNode : wordEntries) {
                if (oneLetterDifferent(theNode.word, nextNode.word) && (nextNode != startingNode) && (nextNode.next == null)) {
                    nextNode.next = theNode;
                    solverQueue.offer(nextNode);

                    if (nextNode.word.equals(end)) {
                        ui.sendMessage("Solution found between " + start + " and " + end);
                        System.out.println(timeQueueIsPolled + " is times polled.");
                        String s = "";
                        while (theNode != null) {
                            s = "\n" + theNode.word + s;
                            theNode = theNode.next;
                        }
                        ui.sendMessage(s);
                        return;
                    }
                }
            }
        }
        System.out.println(timeQueueIsPolled + " is times polled.");
        ui.sendMessage("Solution was not found.");


    }
    void solve2 (String start, String end) {
        PriorityQueue<Node> solverQueue = new PriorityQueue<>(new NodeComparator(end));
        Node startingNode = find(start);
        solverQueue.offer(startingNode);
        int timeQueueIsPolled = 0;
        while (!solverQueue.isEmpty()) {
            Node theNode = solverQueue.poll();
            timeQueueIsPolled++;

            for (Node nextNode : wordEntries) {
                if (oneLetterDifferent(theNode.word, nextNode.word) && (nextNode != startingNode) && (nextNode.next == null)) {
                    nextNode.next = theNode;
                    solverQueue.offer(nextNode);

                    if (nextNode.word.equals(end)) {
                        ui.sendMessage("Solution found between " + start + " and " + end);
                        System.out.println(timeQueueIsPolled + " is times polled.");
                        String s = "";
                        while (theNode != null) {
                            s = "\n" + theNode.word + s;
                            theNode = theNode.next;
                        }
                        ui.sendMessage(s);
                        return;
                    }
                }
            }
        }
        System.out.println(timeQueueIsPolled + " is times polled.");
        ui.sendMessage("Solution was not found.");
    }

    void solve3 (String start, String end) {
        Heap<Node> solverQueue = new Heap<>(new NodeComparator(end));
//        PriorityQueue<Node> solverQueue = new PriorityQueue<>(new NodeComparator(end));
        Node startingNode = find(start);
        solverQueue.offer(startingNode);
        int timeQueueIsPolled = 0;
        while (!solverQueue.isEmpty()) {
            Node theNode = solverQueue.poll();
            timeQueueIsPolled++;

            for (Node nextNode : wordEntries) {
                if (oneLetterDifferent(theNode.word, nextNode.word) && (nextNode != startingNode) && (nextNode.next == null)) {
                    nextNode.next = theNode;
                    solverQueue.offer(nextNode);

                    if (nextNode.word.equals(end)) {
                        ui.sendMessage("Solution found between " + start + " and " + end);
                        System.out.println(timeQueueIsPolled + " is times polled.");
                        String s = "";
                        while (theNode != null) {
                            s = "\n" + theNode.word + s;
                            theNode = theNode.next;
                        }
                        ui.sendMessage(s);
                        return;
                    }
                } else if((nextNode != startingNode) && (oneLetterDifferent(theNode.word, nextNode.word)) && (distanceToStart(nextNode) > (distanceToStart(theNode)+1)) ){
                    nextNode.next = theNode;
                    solverQueue.remove(nextNode);
                    solverQueue.offer(nextNode);
                }
            }
        }
        System.out.println(timeQueueIsPolled + " is times polled.");
        ui.sendMessage("Solution was not found.");
    }

    public static boolean oneLetterDifferent(String firstWord, String secondWord) {
        if (firstWord.length() != secondWord.length()) {
            return false;
        }
        int lettersDifferent = 0;
        for (int i = 0; i < firstWord.length(); i++) {
            if (firstWord.charAt(i) != secondWord.charAt(i)) {
                lettersDifferent++;
            }
        }
        return (lettersDifferent == 1);
    }

    public Node find(String candidateWord) {
        for (Node node: wordEntries){
           if(node.word.equals(candidateWord)){
               return node;
           }
        }
        return null;
//        int bottom = 0;
//        int top = wordEntries.size() - 1;
//        while (bottom <= top) {
//            int middle = (top + bottom) / 2;
//            if (wordEntries.get(middle).word.compareTo(candidateWord) == 0) {
//                return wordEntries.get(middle);
//            } else if (wordEntries.get(middle).word.compareTo(candidateWord) < 0) {
//                bottom = middle + 1;
//            } else if (wordEntries.get(middle).word.compareTo(candidateWord) > 0){
//                top = middle - 1;
//            }
//        }
//        return null;
    }

    static int lettersDifferent(String word, String target) {
        int differenceCount = 0;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) != target.charAt(i)) {
                differenceCount++;
            }
        }
        return differenceCount;
    }

    int distanceToStart(Node node) {
        int distanceToStartCount = 0;
        while (node != null) {
            distanceToStartCount++;
            node = node.next;
        }
        return distanceToStartCount;
    }


    protected class NodeComparator implements Comparator<Node> {
        private String target;

        public NodeComparator(String recievedTarget) {
            target = recievedTarget;
        }
        int priority (Node node) {
            int sumOfDistAndLettersDifferent = distanceToStart(node) + lettersDifferent(node.word,target);
            return sumOfDistAndLettersDifferent;
        }

        @Override
        public int compare(Node o1, Node o2) {
            return priority(o1) - priority(o2);
        }

        @Override
        public Comparator<Node> reversed() {
            return Comparator.super.reversed();
        }

        @Override
        public Comparator<Node> thenComparing(Comparator<? super Node> other) {
            return Comparator.super.thenComparing(other);
        }

        @Override
        public <U> Comparator<Node> thenComparing(Function<? super Node, ? extends U> keyExtractor, Comparator<? super U> keyComparator) {
            return Comparator.super.thenComparing(keyExtractor, keyComparator);
        }

        @Override
        public <U extends Comparable<? super U>> Comparator<Node> thenComparing(Function<? super Node, ? extends U> keyExtractor) {
            return Comparator.super.thenComparing(keyExtractor);
        }

        @Override
        public Comparator<Node> thenComparingInt(ToIntFunction<? super Node> keyExtractor) {
            return Comparator.super.thenComparingInt(keyExtractor);
        }

        @Override
        public Comparator<Node> thenComparingLong(ToLongFunction<? super Node> keyExtractor) {
            return Comparator.super.thenComparingLong(keyExtractor);
        }

        @Override
        public Comparator<Node> thenComparingDouble(ToDoubleFunction<? super Node> keyExtractor) {
            return Comparator.super.thenComparingDouble(keyExtractor);
        }
    }

}

