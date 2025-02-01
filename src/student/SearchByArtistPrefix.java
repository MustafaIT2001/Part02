/**
 * File: SearchByArtistPrefix.java 
 *****************************************************************************
 *                       Revision History
 *****************************************************************************
 * 01/ 29 Junting Zhang(Sarah) - add review comments based on Mustafa's code.
 * 8/2015 Anne Applin - Added formatting and JavaDoc
 * 2015 - Bob Boothe - starting code
 * 01/30/2025 Mustafa Qahtan Added search detail to the terminal
 * and fixed some bugs.
 *****************************************************************************
 */

package student;
import java.io.*;
import java.util.*;
import java.util.stream.Stream;
/**
 * Search by Artist Prefix searches the artists in the song database
 * for artists that begin with the input String
 * @author Bob Booth
 */

public class SearchByArtistPrefix {
    // keep a local direct reference to the song array
    private Song[] songs;

    /**
     * constructor initializes the property. [Done]
     * @param sc a SongCollection object
     */
    public SearchByArtistPrefix(SongCollection sc) {
        songs = sc.getAllSongs();
    }

    /**
     * find all songs matching artist prefix uses binary search should operate
     * in time log n + k (# matches)
     * converts artistPrefix to lowercase and creates a Song object with
     * artist prefix as the artist in order to have a Song to compare.
     * walks back to find the first "beginsWith" match, then walks forward
     * adding to the arrayList until it finds the last match.
     *
     * @param artistPrefix all or part of the artist's name
     * @return an array of songs by artists with substrings that match
     *    the prefix
     * @author Mustafa Qahtan
     *
     * Fixing bugs in search by artistPrefix method
     */
    public Song[] search(String artistPrefix) {

        // A temporary song obj with artistPrefix to use for binarySearch
        Song tempSong = new Song(artistPrefix, "", "");

        // Start measuring the time before the binary search.
        long startTime = System.nanoTime();
        int index = Arrays.binarySearch(songs, tempSong, new ArtistComparator());
        long binarySearchTime = System.nanoTime() - startTime;

        // Print out search details.
        System.out.println("Total Songs " + songs.length);
        System.out.println("Searching for '" + artistPrefix + "'");
        System.out.println("Index from binary search is: " + index);
        System.out.println("Binary search comparisons: " + binarySearchTime / 1000); // Time-based comparisons estimate (for illustration)

        // If no match is found, we return a negative number, so we adjust the index.
        if (index < 0) {
            // Adjust the index position to where the song should be inserted.
            index = -index - 1;
        }

        // We need to find all the songs that match the artistPrefix.
        List<Song> songResult = new ArrayList<>();

        // Looking for songs that match from the right side if we have a match.

        //  1. ******* since you are using same variable "index" to keep track of the index,
        //  the output might not be accurated when you start your 2nd while loop,
        //  i found duplicated output in my test (use shortSong list might easy to spot)  *****
        // MQ - How about this approach? Let me know if it works.

        int rightIndex = index;

        // Perform a case-sensitive check using equals method (instead of startsWith) for matching the prefix
        while (rightIndex < songs.length && songs[rightIndex].getArtist().startsWith(artistPrefix)) {
            songResult.add(songs[rightIndex]);
            rightIndex++;
        }

        // 2. ****** this jav doc might need to update , it's duplicated with the above one ****

        // Look in the right direction to see if there is an exact match.

        //  3. ******* i didn't find the addFirst() method for Arrays in docs.oracle website *
        //    it seems it's for LinkList or queue, please let me know if you find it.
        /*
        MQ - I Changed the method to be just array. Now you don't need to worry about the addFirst() Method.
        */
        //   **********

        // Now we check on the left side as well.

        int leftIndex = index - 1; // reset the counter.

        // Perform a case-sensitive check here as well using equals
        while (leftIndex >= 0 && songs[leftIndex].getArtist().startsWith(artistPrefix)) {
            songResult.add(0, songs[leftIndex]);
            leftIndex--;
        }

        // Print out how many comparisons were made to build the list.
        System.out.println("Front found at " + index);
        System.out.println("Comparisons to build the list: " + (rightIndex - index + leftIndex + 1));
        System.out.println("Actual complexity is: " + (binarySearchTime / 1000 + (rightIndex - index + leftIndex + 1)));

        // Convert the ArrayList to an array and return it.
        return songResult.toArray(new Song[0]);
    }

    // ArtistComparator should also compare in a case-sensitive manner.
    static class ArtistComparator implements Comparator<Song> {
        public int compare(Song s1, Song s2) {
            return s1.getArtist().compareToIgnoreCase(s2.getArtist()); // This is case-sensitive
        }
    }


    /**
     * testing method for this unit
     * @param args  command line arguments set in Project Properties -
     * the first argument is the data file name and the second is the partial
     * artist name, e.g. be which should return beatles, beach boys, bee gees,
     * etc.
     */

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("usage: prog song file [search string]");
            return;
        }

        SongCollection sc = new SongCollection(args[0]);
        SearchByArtistPrefix sbap = new SearchByArtistPrefix(sc);

//        Song[] songs = {
//                new Song("Beach Boys", "Surfin' USA", ""),
//                new Song("Beastie Boys", "Fight For Your Right", ""),
//                new Song("Beatles", "Hey Jude", ""),
//                new Song("Beethoven", "Symphony No 5", ""),
//        };

        // Create a searchByArtistPrefix obj with song array
        SearchByArtistPrefix searcher = new SearchByArtistPrefix(sc);

        /**
         * 5. ********* i am not sure if you need these lines of codes (145- 150 ) when are giving the starting code
         *   the step 4 instruction point out : " It takes 2 command line arguments: a song file name and the artist to search for"
         *   for now this line of code only output the sc object "Stream.of(sc).limit(10).forEach(System.out::println);"
         *  you might take a look the main method in SongCollection to update Steam.of() argument sc to your search result
         *  so it can print out the song list by modify the starting code;
         *
         *  MQ - That was just for test before getting the GUI or the text argument (allSongs.txt) to work on my IDE
         *
         *  also in instruction:
         * Finish the testing method by printing the to- tal number of matches
         * as well as the Artist and Title of the first 10 matches.
         * You can reuse your code from part 1 section 3.2 to do this. *******
         *
         * MQ - Important not all done.
         */



        //6.  ******* one more bug i experienced is when i searched with all lowercase or Upper case input, it won't work ****
        // MQ - Thanks for bringing this up.

        // Searching result by the artist prefixes.
        Song[] result = searcher.search("Pro");

        // Print the matching result
        for (Song song : result) {
            System.out.println(song.getArtist() + " - " + song.getTitle());
        }

        if (args.length > 1) {
            System.out.println("searching for: " + args[1]);
            Song[] byArtistResult = sbap.search(args[1]);
            Stream.of(sc).limit(10).forEach(System.out::println);
        }


    }
}
