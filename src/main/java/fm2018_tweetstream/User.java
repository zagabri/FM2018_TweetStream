/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fm2018_tweetstream;

/**
 *
 * @author zagabri
 */
import java.util.ArrayList;

public class User {
    
    //variables
    private final String id; // id is unique!
    private int numTweet, numMentions;
    private ArrayList<String> names = new ArrayList<String>();
    private ArrayList<String> screenNames = new ArrayList<String>();
    
    // constructor
    public User(String newId, String newName, String newScreenName){
        numTweet = 0;
        numMentions = 0;
        id = newId;
        names.add(newName);
        screenNames.add(newScreenName);
    }
    
    public String getID(){ 
        return id;
    }
    
    public ArrayList<String> getNames(){
        return names;
    }
    
    public void addName( String newName ){
        int found = 0;
        for( String name : names ){
            if( name.equals(newName) ){
                found = 1;
                break;
            }
        }
        if( found == 0 ){
            names.add(newName);
        }
    }
    
    public ArrayList<String> getScreenNAmes(){
        return screenNames;
    }
    
    public boolean addScreenName( String newScreenName ){
        for( String name : screenNames ){
            if( name.equals(newScreenName) ){
                return false;
            }
        }
        
        screenNames.add(newScreenName);
        return true;
    }
    
    public int getNumTweet(){
        return numTweet;
    }
    
    public void setNumTweet(int num){
            numTweet = numTweet + num;
    }
    
    public int getNumMentions(){
        return numMentions;
    }
    
    public void setNumMentions(int num){
        numMentions = numMentions + num;
    }
    
    // toString method
    public String toString(){
        String str = "ID: " + id + "\n";
        str += "Names: ";
        for( String name : names ){
            str += name + ", ";
        }
        str += "\n";
        str += "Screen names: ";
        for( String name : screenNames ){
            str += name + ", ";
        }
        str += "\n";
        str += "Number of Tweet: " + numTweet + "\n";
        str += "Number of Mentions: " + numMentions + "\n";
        
        return str;
    }
    
}
