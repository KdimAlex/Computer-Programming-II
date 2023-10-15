package prog11;

import prog05.LinkedQueue;
import prog05.NotWordle;

import java.util.*;

public class NotGPT implements SearchEngine{
//    Put a Disk variable pageDisk inside NotGPT to store the
//    information about web pages.  Initialize pageDisk.
    Disk pageDisk = new Disk();
    Map<String,Long> url2index = new TreeMap<String,Long>();

    public Disk wordDisk = new Disk();

    public Map<String, Long> word2index = new HashMap<String, Long>();

    @Override
    public void collect(Browser browser, List<String> startingURLs) {
        ArrayDeque<Long> myQueue = new ArrayDeque<Long>();

        for (String url : startingURLs){
            if (!url2index.containsKey(url)) {
                Long index = indexPage(url);
                myQueue.offer(index);
            }
        }

        while(!myQueue.isEmpty()){
            Long index = myQueue.poll();
            InfoFile info = pageDisk.get(index);

            if(browser.loadPage(info.data)){
                List <String> URLsOnPage = browser.getURLs();
                Set<String> seenUrls = new HashSet<String>();
                for (String url: URLsOnPage) {
//                    if (!url2index.containsKey(url)) {
//                        myQueue.offer(indexPage(url));
//                    }
                    if (!seenUrls.contains(url)) {
                        seenUrls.add(url);
                        Long index2 = url2index.get(url);
                        if (index2 == null) {
                            index2 = indexPage(url);
                            myQueue.offer(index2);
                        }
                        info.indices.add(index2);
                    }

                }
                pageDisk.put(index, info);

                List<String> words = browser.getWords();
                Set<String> seenWords = new HashSet<>();
                for (String word: words){
                    if(!seenWords.contains(word)){
                        seenWords.add(word);
                        Long wordIndex = word2index.get(word);
                        if(wordIndex == null)
                            wordIndex = indexWord(word);
                        InfoFile wordFile = wordDisk.get(wordIndex);
                        wordFile.indices.add(index);
                        wordDisk.put(wordIndex, wordFile);
                    }
        //                        indexWord(word);
                }
            }
        }
        //        pageDisk.write("pagedisk.txt");
        //        wordDisk.write("worddisk.txt");
    }


    @Override
    public void rank(boolean fast) {
        for (Map.Entry<Long,InfoFile> entry : pageDisk.entrySet()) {
            long index = entry.getKey();
            InfoFile file = entry.getValue();
            file.priority = 1.0;
            file.tempPriority = 0.0;
        }
        long count = 0;
        for (Map.Entry<Long,InfoFile> entry : pageDisk.entrySet()) {
            long index = entry.getKey();
            InfoFile file = entry.getValue();
            if(file.indices.isEmpty()){
                count++;
            }
        }
        double defaultPriority = 1.0 * count / pageDisk.size();

        if(!fast){
            for(int i = 0; i < 20;i++){
                rankSlow(defaultPriority);
            }
        } else {
            for(int i = 0; i < 20;i++){
                rankFast(defaultPriority);
            }
        }

    }


    @Override
    public String[] search(List<String> searchWords, int numResults) {

        // Iterator into list of page indexes for each key word.
        Iterator<Long>[] wordFileIterators = (Iterator<Long>[]) new Iterator[searchWords.size()];
        PageComparator toCompare = new PageComparator();
        PriorityQueue<Long> bestPageIndexes = new PriorityQueue<>(numResults, toCompare);
        // Current page index in each list, just ``behind'' the iterator.
        long[] currentPageIndexes = new long[searchWords.size()];

        //           Initialize currentPageIndexes.  You just have to allocate the
        //            array.  You don't have to write a loop to initialize the elements
        //            of the array because all its elements will automatically be zero.

        //           Write a loop to initialize the entries of wordFileIterators.
        //           wordFileIterators[i] should be set to an iterator over the word file of searchWords[i].
        for (int i = 0; i < searchWords.size(); i++){
            String word = searchWords.get(i);
            wordFileIterators[i] = wordDisk.get(word2index.get(word)).indices.iterator();
        }

        while (getNextPageIndexes(currentPageIndexes, wordFileIterators)){
            if (allEqual(currentPageIndexes)){
                String url = pageDisk.get(currentPageIndexes[0]).data;
                System.out.println(url);
//Step 6:
                if(bestPageIndexes.size() != numResults){;
                    bestPageIndexes.offer(url2index.get(url));
                } else {
                    long peek = bestPageIndexes.peek();
                    if (peek < url2index.get(url)){
                        bestPageIndexes.poll();
                        bestPageIndexes.offer(url2index.get(url));
                    }
                }

            }
        }

//Step 7:
        String [] stringArray = new String[bestPageIndexes.size()];
        for (int i = stringArray.length - 1; i >= 0; i--) {
            long indexOfPage = bestPageIndexes.poll();
            stringArray[i] = String.valueOf(indexOfPage);
        }
//        String [] stringArray = new String[10];
//        Stack<Long> temporaryQueue = null;
//        for (int i = 0; i < 10; i++){
//            temporaryQueue.push(bestPageIndexes.poll());
//        }
//
//        for (int i = 0; i < bestPageIndexes.size(); i++){
//            stringArray[i] = pageDisk.get(temporaryQueue.pop()).data;
//        }

        return stringArray;
    }
    //    Write an indexPage method that takes the String url of the web page
    //    as input and returns the index of its newly created page file.
    public Long indexPage(String string){
        //    It gets the index of a new file from pageDisk, creates a new
        //    InfoFile, and stores the InfoFile in pageDisk.  Then it tells the
        //    Map url2index to map url to that index and returns the index.
        Long index = pageDisk.newFile();
        InfoFile information = new InfoFile(string);

        pageDisk.put(index, information);
        url2index.put(string, index);

        System.out.println("indexing page " + index + " " + information);

        return index;
    }

    public Long indexWord(String word){
        Long index = wordDisk.newFile();
        InfoFile information = new InfoFile(word);

        wordDisk.put(index, information);
        word2index.put(word, index);

        System.out.println("indexing word " + index + " " + information);

        return index;
    }


    void rankSlow (double defaultPriority){
        for (Map.Entry<Long,InfoFile> entry : pageDisk.entrySet()) {
            long index = entry.getKey();
            InfoFile file = entry.getValue();

            double priorityPerIndex =  file.priority / file.indices.size() ;
            for (Long currentIndex : file.indices){
                pageDisk.get(currentIndex).tempPriority += priorityPerIndex;
            }
        }

        for (Map.Entry<Long,InfoFile> entry : pageDisk.entrySet()) {
            long index = entry.getKey();
            InfoFile file = entry.getValue();
            file.priority = file.tempPriority + defaultPriority;
            file.tempPriority = 0.0;
        }
    }


    void rankFast (double defaultPriority){
        List <Vote> voteList = new ArrayList<Vote>();

        for (Map.Entry<Long, InfoFile> entry : pageDisk.entrySet()) {
            long index = entry.getKey();
            InfoFile file = entry.getValue();

            double priorityPerIndex = file.priority / file.indices.size();
            for (Long currentIndex : file.indices) {
                voteList.add(new Vote(currentIndex, priorityPerIndex));
            }
        }

        Collections.sort(voteList);

        Iterator<Vote> iterator = voteList.iterator();
        Vote vote = iterator.next();
        for (Map.Entry<Long, InfoFile> entry : pageDisk.entrySet()) {
            long index = entry.getKey();
            InfoFile file = entry.getValue();

            double tempPriority = 0.0;
            while (vote.index == index) {
                tempPriority += vote.vote;
                if (iterator.hasNext()) {
                    vote = iterator.next();
                } else  {
                    break;
                }
            }
            file.priority = tempPriority + defaultPriority;
            file.tempPriority = 0;
        }
    }


    /** Check if all elements in an array of long are equal.
     @param array an array of numbers
     @return true if all are equal, false otherwise
     */
    private boolean allEqual (long[] array) {
        long number = array[0];
        for (int i = 0; i < array.length; i++){
            if (array[i] != number){
                return false;
            }
            number = array[i];
        }
        return true;
    }


    /** Get the largest element of an array of long.
     @param array an array of numbers
     @return largest element
     */
    private long getLargest (long[] array){
        long number = array[0];
        for (int i = 0; i < array.length; i++){
            if (number < array[i]){
                number = array[i];
            }
        }
        return number;
    }


    /** If all the elements of currentPageIndexes are equal,
     set each one to the next() of its Iterator,
     but if any Iterator hasNext() is false, just return false.

     Otherwise, do that for every element not equal to the largest element.

     Return true.

     @param currentPageIndexes array of current page indexes
     @param wordFileIterators array of iterators with next page indexes
     @return true if all page indexes are updated, false otherwise
     */
    private boolean getNextPageIndexes (long[] currentPageIndexes, Iterator<Long>[] wordFileIterators) {
        if(allEqual(currentPageIndexes)){
            for(int i = 0; i < currentPageIndexes.length; i++){

                if (!wordFileIterators[i].hasNext()){
                    return false;
                }
                currentPageIndexes[i] = wordFileIterators[i].next();

            }
        } else {
            for (int i = 0; i < currentPageIndexes.length; i++){
                long largestNumber = getLargest(currentPageIndexes);

                if(currentPageIndexes[i] != largestNumber) {
                    if (!wordFileIterators[i].hasNext()) {
                        return false;
                    }
                    currentPageIndexes[i] = wordFileIterators[i].next();
                }

            }
        }
        return true;
    }

//Step 7
    class PageComparator implements Comparator<Long> {
        @Override
        public int compare(Long pageIndex1, Long pageIndex2) {
            InfoFile file1 = pageDisk.get(pageIndex1);
            InfoFile file2 = pageDisk.get(pageIndex2);
            if (file1.priority < file2.priority)
                return -1;
            else if (file1.priority > file2.priority)
                return 1;
            else
                return 0;
        }
    }
}



class Vote implements Comparable<Vote> {
    Long index;
    double vote;

    public Vote(Long index, double vote) {
        this.index = index;
        this.vote = vote;
    }

    @Override
    public int compareTo(Vote o) {
        return index.compareTo(o.index);
    }
}







