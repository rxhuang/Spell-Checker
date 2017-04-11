/**
 *  NAME: Ruoxin Huang
 *  ID: A99084753
 *  LOGIN: cs12whl
 * */
import java.util.*;
import java.io.*;
/**
 *  Title: class SpellChecker
 *  Description: creates a spell checker to spell check words 
 *  @author Ruoxin Huang
 *  @version 1.0
 *  @since 03-06-2016
 * */
;public class SpellChecker implements ISpellChecker {
    static HashTable hashTable = new HashTable();

    /**
     * method to read a dictionary and put it into hash table
     *
     * @param  reader   a reader
     */
    @Override
    public void readDictionary(Reader reader) {
        BufferedReader in = new BufferedReader(reader); // create buffer reader
        try{
            String add = in.readLine(); // read a line
            while(add!=null){ // while there are more lines
                hashTable.insert(add); // insert the word
                add = in.readLine(); // read next line
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * method to spell check a word
     * 
     * @param  word   the word to spell check
     * @return  a string array of the possible corrections
     */
    @Override
    public String[] checkWord(String word) {
        String[] corrections = new String[10000]; // creat array for possible corrections
        int h =0;
        // creat array with alphabet
        char[] alphabet =  {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
        word = word.toLowerCase(); // make words lower case
        if(hashTable.lookup(word)){ // if word spelled corrctly
            return null;
        }
        else{
            char[] charArray = word.toCharArray();
            char[] copyArray = word.toCharArray();
            for(int i=0; i<word.length(); i++){ // loop through every char of a word
                for(int j=0; j<26; j++){ // loop through every possible letter
                    charArray[i] = alphabet[j]; // subsitute a char with another letter
                    word = new String(charArray); // create new word
                    if(hashTable.lookup(word)){ // check new word in dictionsary
                        corrections[h] = word; // if found, add it to corrections
                        h++;
                    }
                }
                charArray = word.toCharArray();// put the initiale character back
            }

            charArray = word.toCharArray(); // handle extra letter
            for(int i=0; i<word.length(); i++){ // loop through every char of a word
                int counter = 0;
                for(int j=0; j<counter; j++){
                    charArray[i] = alphabet[j]; // subsitute a char with another letter
                }
                for(int j=counter+1; j<word.length(); j++){
                    charArray[i] = alphabet[j]; // subsitute a char with another letter
                }
                word = new String(charArray); // create new word
                if(hashTable.lookup(word)){ // check new word in dictionsary
                    corrections[h] = word; // if found, add it to corrections
                    h++;
                }
                charArray = word.toCharArray();// put the initiale character back
                counter++;
            }

            charArray = word.toCharArray(); // handle missing letter
            for(int i=0; i<word.length(); i++){ // loop through every char of a word
                int counter=0;
                for(int j=0; j<26; j++){ // loop through every possible letter
                    charArray[counter] = alphabet[j];
                    for(int k=counter+1; k<word.length()+1; k++){
                        charArray[k] = copyArray[k-1]; // subsitute a char with another letter
                    }
                    word = new String(charArray); // create new word
                    if(hashTable.lookup(word)){ // check new word in dictionsary
                        corrections[h] = word; // if found, add it to corrections
                        h++;
                    }
                }
                charArray = word.toCharArray();// put the initiale character back
                counter++;
            }
            
            charArray = word.toCharArray(); // handle adjacent transposed letters
            for(int i=0; i<word.length()-1; i++){ // loop through every char of a word
                int counter=0;
                char temp = charArray[counter];
                charArray[counter] = charArray[counter+1];
                charArray[counter+1] = temp;
                word = new String(charArray); // create new word
                if(hashTable.lookup(word)){ // check new word in dictionsary
                    corrections[h] = word; // if found, add it to corrections
                    h++;
                }
                charArray = word.toCharArray();// put the initiale character back
                counter++;
            }
        }
        String[] corrections2 = new String[h];
        for(int i=0; i<h; i++){ // create a correction array with just the right size
            corrections2[i] = corrections[i];
        }
        return corrections2;
    }

    /**
     * the main method to read files and print output
     */
    public static void main(String[] args) throws FileNotFoundException, IOException
    {
        String dictionary = new String(args[0]); // read first command line arguement, should be dicitonary
        SpellChecker spellChecker = new SpellChecker(); // create apellchecker
        spellChecker.readDictionary(new FileReader(dictionary)); // read dictionary

        String input = new String (args[1]); // read second command line arguement, should be list of words to check
        Reader inputFile = new FileReader(input);
        BufferedReader inputBuffer = new BufferedReader(inputFile); // create buffer reader
        String wordToCheck = inputBuffer.readLine(); // read a line
        while(wordToCheck!=null){ // as long as more line exist
            String[] print = spellChecker.checkWord(wordToCheck); // add all corections of a word to print
            if(print!=null){ // if corrections exist, print them out
                System.out.print(wordToCheck+": ");
                if(print.length!=0){
                    System.out.print(print[0]);
                }
                for (int i = 1; i < print.length; i++){
                    System.out.print(", "+print[i]);
                }
                System.out.println();
            }
            wordToCheck = inputBuffer.readLine(); // read another line
        }
        try(  PrintWriter output = new PrintWriter( "stats.txt" )  ){ // output a stats.txt file with the stats
            output.println( hashTable.resizes + " resizes, " + hashTable.loadFactor + "alpha, "+ hashTable.collisions + " collisions" );
        }
    }
}