import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLException;

import twitter4j.JSONException;
import twitter4j.JSONObject;
import twitter4j.Query;
import twitter4j.Query.ResultType;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterObjectFactory;
import twitter4j.conf.ConfigurationBuilder;


public class TwitterDataJSON {
	private Twitter twitter;
	private ConfigurationBuilder builder;
	private static final String CONSUMER_KEY="lK2k609IQGtUQudAu6CIencxB";
	private static final String CONSUMER_KEY_SECRET="ZFpvxvRywjFs6VHKoVCMr3H1dRePO7pkzgLrqeLYYP8P5p1xZT";
	private String accessToken;
	private String accessTokenSecret;
	
	public TwitterDataJSON(){
		builder= new ConfigurationBuilder();
		builder.setOAuthConsumerKey(CONSUMER_KEY);
		builder.setOAuthConsumerSecret(CONSUMER_KEY_SECRET);
		accessToken="4322430433-RiyQd99LzEfUl4RhgYU3X8hDK8VzKbSglWl8PxL";
		accessTokenSecret="U9Xciy5jSU8gSYZI77l0O5TBYvKRTwNeptvrTIs4XOopc";		
		builder.setOAuthAccessToken(accessToken);
		builder.setOAuthAccessTokenSecret(accessTokenSecret);
		builder.setDebugEnabled(true);
		builder.setJSONStoreEnabled(true);
	}
	
	public void getTweets() throws TwitterException, IOException, JSONException{
		twitter= new TwitterFactory(builder.build()).getInstance();
		Query query = new Query("Bollywood");
		
		query.setSince("2015-11-27");
		query.setResultType(ResultType.popular);
		QueryResult result;
		do {
			result = twitter.search(query);
					for (Status tweet : result.getTweets()) {
						String json= TwitterObjectFactory.getRawJSON(tweet);
						int i=10;
						//System.out.println(json);
						 new File("Tweets/"+query.getQuery()).mkdir();
						 String fileName = "Tweets/" + query.getQuery()+"/"+tweet.getId()+ ".json";
						 JSONObject jsonObject= new JSONObject(json);
						 storeJSON(json, fileName);
					}	
		} while ((query = result.nextQuery()) != null);
	}
	
	private static void storeJSON(String json,String fileName) throws IOException{
		 FileOutputStream fos = null;
	        OutputStreamWriter osw = null;
	        BufferedWriter bw = null;
	        try {
	            fos = new FileOutputStream(fileName);
	            osw = new OutputStreamWriter(fos, "UTF-8");
	            bw = new BufferedWriter(osw);
	            bw.write(json);
	            bw.flush();
	        } finally {
	            if (bw != null) {
	                try {
	                    bw.close();
	                } catch (IOException ignore) {
	                }
	            }
	            if (osw != null) {
	                try {
	                    osw.close();
	                } catch (IOException ignore) {
	                }
	            }
	            if (fos != null) {
	                try {
	                    fos.close();
	                } catch (IOException ignore) {
	                }
	            }
	        }

	}
	
	public static void main(String args[]) throws TwitterException, IOException, JSONException, ClassNotFoundException, SQLException
	{
		TwitterDataJSON tweet= new TwitterDataJSON();
		tweet.getTweets();
		DrillJDBCCon con = new DrillJDBCCon();
		con.executeQuery("Select * FROM dfs.`/Users/varungupta/Documents/workspace/SentimentAnalysis/Tweets/Bollywood/`");
	}








}
