/**
 * File: SearchByArtistPrefix.java 
 *****************************************************************************
 *                       Revision History
 *****************************************************************************
 * 01/ 29 Junting Zhang(Sarah) - add review comments based on Mustafa's code.
 * 8/2015 Anne Applin - Added formatting and JavaDoc 
 * 2015 - Bob Boothe - starting code  
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
     */
    public Song[] search(String artistPrefix) {

        // A temporary song obj with aristPrefix to use for binarySearch
        Song tempSong = new Song(artistPrefix, "", "");

        int index = Arrays.binarySearch(songs, tempSong, new ArtistComparator());

        // If the search doesn't find the exact match, it return 0.
        if (index < 0) { 
            // if not found, calculate the position that would be inserted at.
            index = -index - 1;
        }

        // Collect the matching songs inside an arrayList.

        List<Song> songResult = new ArrayList<>();

        // look in the right if the exact match found.
        
        //  1. ******* since you are using same variable "index" to keep track of the index, 
        //  the output might not be accurated when you start your 2nd while loop, 
        //  i found dulplicted output in my test (use shortSong list might easy to spot)  ***** 
        
        while (index < songs.length && songs[index].getArtist().startsWith(artistPrefix)) {

            songResult.add(songs[index]);
            index++;
        }
        
        // 2. ****** this jav doc might need to update , it's duplcated with the above one ****
        
        // Look in the right direction to see if there is an exact match.
        index--;

        //  3. ******* i didn't find the addFirst() method for Arrays in docs.oracle website  
          //    it seems it's  for LinkList or queue, please let me know if you find it. 
          //   **********
          
        while (index >= 0 && songs[index].getArtist().startsWith(artistPrefix)) {
            songResult.add(0, songs[index]);
            index--;
        }
        
        // convert the ArrayList to a song array and return it.

        return songResult.toArray(new Song[0]);
    }

    // 4. ******* i have already created a Comparator class inside song class 
    //         according to part 2- task 2 instruction .*****
    
    // A Comparator for sorting by the artist
    static class ArtistComparator implements Comparator<Song> {
        public int compare(Song s1, Song s2) {
            return s1.getArtist().compareToIgnoreCase(s2.getArtist());
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
         *  also in instruction: 
          * Finish the testing method by printing the to- tal number of matches 
          * as well as the Artist and Title of the first 10 matches. 
          * You can reuse your code from part 1 section 3.2 to do this. *******
          */
        
        
        //6.  ******* one more bug i experienced is when i searched with all lowercase or Upper case input, it won't work ****
        
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
