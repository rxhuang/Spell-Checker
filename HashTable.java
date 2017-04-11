/**
 *  NAME: Ruoxin Huang
 *  ID: A99084753
 *  LOGIN: cs12whl
 * */
import java.util.*;
import java.io.*;
/**
 *  Title: class HashTable
 *  Description: creates a hash table 
 *  @author Ruoxin Huang
 *  @version 1.0
 *  @since 03-06-2016
 * */
public class HashTable implements IHashTable {
    String[] array = new String[1000000];
    int nelems;
    static double loadFactor;
    static int collisions;
    static int resizes=0;
    /** 
     * adds a string to the hash table
     *
     * @param  value   the word to add
     * @return  true if successfully added, false otherwise
     */
    @Override
    public boolean insert(String value) {
        loadFactor = nelems/array.length; // load factor
        if(loadFactor>0.7){ // if load factor too large double size
            doubleSize();
        }
        int hashVal = convert(value); // get hash value
        if(array[hashVal]==null || array[hashVal]=="deleted"){ // if empty or deleted
            array[hashVal] = value;
            nelems++;
            return true;
        }
        else if(array[hashVal].equals(value)){ // if value already in table
            return false;
        }
        else{ //  if we need to double hash the tring
            hashVal = doubleHash(value);
            if(hashVal == -1){ // if double hash return -1 then false
                return false;
            }
            array[hashVal] = value;
            nelems++;
            return true;
        }
    }

    /** 
     * doubles the size of the hash table
     */
    public void doubleSize()
    {
        String[] oldArray = array;
        array = new String[array.length*2]; // Make new array with double the size plus 1
        nelems = 0;
        loadFactor = 0;
        // rehash the old array into the new array
        for(int i=0; i<oldArray.length; i++)
        {
            if(oldArray[i] != null&&oldArray[i] != "deleted"){
                insert(oldArray[i]);
            }
        }
        resizes++;
    }

    /** 
     * converts the word to a numeric value
     *
     * @param  value   the word to convert
     * @return  the numeric number converted
     */
    private int convert(String value) {
        int hashVal = 0;
        for(int i=0; i<value.length(); i++){ // loop through avery char in the word
            int small = hashVal << 5;
            int large = hashVal>>27;// convert cahracter to number
            hashVal = (small|large)^value.charAt(i); // hash function
        }
        return Math.abs(hashVal%array.length);
    }

    /** 
     * converts the word to a numeric value with double hash function
     *
     * @param  value   the word to convert
     * @return  the numeric number converted
     */
    private int doubleHash(String value) {
        int hashVal = convert(value); // get hash value
        collisions = 0;
        // as long as not equal to null or deleted or the value we need
        while(array[hashVal]!=null&&!array[hashVal].equals(value)&&!array[hashVal].equals("deleted")){ 
            collisions++;
            hashVal = (hashVal + collisions*(hashVal*23%(array.length-1))+13)%array.length; // double hash function
            if(collisions>array.length){ //  cannot be too many collisions
                return -1;
            }
        }
        if(hashVal%2==0) // make it a odd number
        {
            hashVal = hashVal+1;
        }
        return hashVal;
    }

    /** 
     * deletes a string from the hash table
     *
     * @param  value   the word to delete
     * @return  true if successfully deleted, false otherwise
     */
    @Override
    public boolean delete(String value) {
        int hashVal = convert(value);
        if(array[hashVal]==null || array[hashVal]=="deleted"){ // if empty or deleted
            return false;
        }
        else if(array[hashVal].equals(value)){ // if value found, deleted it
            array[hashVal] = "deleted";
            nelems--;
            return true;
        }
        else{ // if need to delete after double hash
            hashVal = doubleHash(value);
            array[hashVal] = "deleted";
            nelems--;
            return true;
        }
    }

    /** 
     * search for a string from the hash table
     *
     * @param  value   the word to search
     * @return  true if word found, false otherwise
     */
    @Override
    public boolean lookup(String value) {
        int hashVal = convert(value);
        if(array[hashVal]==null || array[hashVal]=="deleted"){ // if empty or deleted
            return false;
        }
        else if(array[hashVal].equals(value)){ //if value found
            return true;
        }
        else{ // if need to double hash and look for it
            hashVal = doubleHash(value);
            return true;
        }
    }

    /** 
     * prints out the hash table
     */
    @Override
    public void print() {
        for(int i=0; i<array.length; i++){ // loop through table
            if(array[i]!=null&&!array[i].equals("deleted")){ // if not empty or deleted print it
                System.out.println(i+": "+array[i]);
            }
            else{
                System.out.println(i+": ");
            }
        }
    }

    /** 
     * the main method that reads files and prints output
     */
    @SuppressWarnings("resource")
    public static void main(String[] args) throws FileNotFoundException
    {
        if(args.length != 1) // Check if number of arguments correct
        {
            System.err.println("Incorrect number of arguments: "+args.length);
            System.err.println("java hw8 <input-file>");
            System.exit(1); 
        }
        // Read file from command line
        File file = new File(args[0]);
        HashTable hash = new HashTable();
        Scanner sc = new Scanner(new BufferedReader(new FileReader(file))); // Create scanner object
        while(sc.hasNext()) // While there exist more words in the file
        {
            String s1 = sc.next(); // Store the next word
            if(s1.equals("insert")) // If the word is schedule creat record and add to queue
            {
                String s2 = sc.next();
                if(hash.insert(s2.substring(1,s2.length()-1))==true){
                    System.out.println("item "+s2.substring(1,s2.length()-1)+" successfully inserted");
                }
                else{
                    System.out.println("item "+s2.substring(1,s2.length()-1)+" already present");
                }
            }
            else if(s1.equals("lookup")) // If the word is run run the time in the system
            {
                String s2 = sc.next();
                if(hash.lookup(s2.substring(1,s2.length()-1))==true){
                    System.out.println("item " + s2.substring(1,s2.length()-1) + " found");
                }
                else{
                    System.out.println("item " + s2.substring(1,s2.length()-1) + " not found");
                }
            }
            else if(s1.equals("delete")) // If the word is run run the time in the system
            {
                String s2 = sc.next();
                if(hash.delete(s2.substring(1,s2.length()-1))==true){
                    System.out.println("item" + s2.substring(1,s2.length()-1) + "successfully deleted");
                }
                else{
                    System.out.println("item" + s2.substring(1,s2.length()-1) + "not found");
                }
            }
            else// (s1.equals("print")) // If the word is run run the time in the system
            {
                hash.print();
            }   
        }
    }
}
