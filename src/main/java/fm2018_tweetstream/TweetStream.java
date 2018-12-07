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

import java.io.File;  // Import the File class
import java.io.IOException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.HashMap;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.GZIPInputStream;

/*
    TweetStream - Future MEdia assignment

    The program search for the number of tweets made by a particular account
    and the number of time it is mentioned by someone other.
    The account is uniquely identifyed by its user ID. All its names 
    and screen_names the user had used, are shown in the final table.

    It is expected that the database of the TweetStream is wothin the folder
        src/db/[DBname].gz.

    INPUT parameter:
        -n [UserName] -i [DBname.gz]
    
        [Username] is case insensitive

    EXAMPLE:
        java fm2018_tweetstream/TweetStream -n CHRiS -i tweets.gz

*/
public class TweetStream {
    
    private static final String DB_PATH = "../../src/db/";
    private static HashMap<String, User> userMap = new HashMap<String, User>();
    
    public static void main(String[] args) {
      
        String name = "";
        String id = "";
        String dbPath = DB_PATH;
        ArrayList IDList = new ArrayList();

        for( int i=0; i < args.length; i = i + 2){
            switch(args[i]){
                case "-n": name = args[i+1].toLowerCase(); break;
                case "-i": dbPath += args[i+1].substring(0, args[i+1].length() - 3); break;
            }
        }

        IDList = loadData(name, dbPath);
        loadMentions();
        
        // Show user statistics
        String format = "| %1$-10s | %2$-20s | %3$-20s | %4$-10s | %5$-10s |\n";
        System.out.format(format,"Names", "ID", "Screen Names", "#Tweets", "#Mentions");
        System.out.println("--------------------------------------------------------------------------------------");
        for( int i=0; i<IDList.size(); i++ ){
            String userID = IDList.get(i).toString();
            User user = userMap.get(userID);
            //str = user.getNames().toString() + "     " + user.getID() + "     " + user.getScreenNAmes().toString() + "     " + user.getNumTweet() + "     " + user.getNumMentions();
            //System.out.println(str);
            System.out.format(format, user.getNames().toString(), user.getID(), user.getScreenNAmes().toString(), user.getNumTweet(), user.getNumMentions());
        }
        
  
    }
    
    private static ArrayList loadData(String name, String dbPath){
        ArrayList dataList = new ArrayList();
        
        // Unzip Db
        byte[] buffer = new byte[1024];
        try {
            FileInputStream fileIn = new FileInputStream(dbPath + ".gz");
            GZIPInputStream gZIPInputStream = new GZIPInputStream(fileIn);
            FileOutputStream fileOutputStream = new FileOutputStream(dbPath + ".txt");
            int bytes_read;
            while ((bytes_read = gZIPInputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, bytes_read);
            }
            gZIPInputStream.close();
            fileOutputStream.close();
        } 
        catch (IOException ex) {
            ex.printStackTrace();
        }

        // Read db and load all Data, collecting the useful 
        try {
          File file = new File(dbPath + ".txt");
          Scanner reading = new Scanner(file);

          int count = 0;
          while (reading.hasNextLine()) {
            String data = reading.nextLine();
            count ++;
            
            JSONJavaMaster.JSONObject tweetData = new JSONJavaMaster.JSONObject(data);
            String actualID = tweetData.getJSONObject("user").get("id").toString() ;
            String actualName = tweetData.getJSONObject("user").get("name").toString().toLowerCase();
            String actualScreenName = tweetData.getJSONObject("user").get("screen_name").toString();
            JSONJavaMaster.JSONArray actualMention = new JSONJavaMaster.JSONArray();
            actualMention = tweetData.getJSONObject("entities").getJSONArray("user_mentions");
           
            if(actualName.equals(name) && ! dataList.contains( actualID ) ){
                dataList.add(actualID);
            }
            User oldUser, newUser;
            if( userMap.containsKey( actualID ) ){
                oldUser = userMap.get(actualID);
                oldUser.setNumTweet(1);
                oldUser.addName(actualName);
                oldUser.addScreenName(actualScreenName);

            }
            else{
                newUser = new User(actualID, actualName, actualScreenName);
                newUser.setNumTweet(1);
                userMap.put(actualID, newUser);
            }
            
            if( !actualMention.isEmpty() ){
                for(int i=0; i<actualMention.length(); i++){
                    String mentionID = actualMention.getJSONObject(i).get("id").toString();
                    String mentionScreenName = actualMention.getJSONObject(i).get("screen_name").toString();
                    String mentionName = actualMention.getJSONObject(i).get("name").toString().toLowerCase();
                    if( userMap.containsKey( mentionID ) ){
                        oldUser = userMap.get(mentionID);
                        oldUser.setNumMentions(1);
                        oldUser.addName(mentionName);
                        oldUser.addScreenName(mentionScreenName);

                    }
                    else{
                        newUser = new User(mentionID, mentionName, mentionScreenName);
                        newUser.setNumMentions(1);
                        userMap.put(mentionID, newUser);
                    }
                }   
            }
           
          }
          reading.close();
        } 
        catch (IOException e) {
          System.out.println("An error occurred.");
          e.printStackTrace();
        }
        
        return dataList;
    }
    
    private static loadMentions(){
        
        for( int i=0; i<userMap.size(); i++){
            
        }
        
    }
}

