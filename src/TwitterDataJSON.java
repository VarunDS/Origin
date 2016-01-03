import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import twitter4j.JSONException;
import twitter4j.Query;
import twitter4j.Query.ResultType;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterObjectFactory;
import twitter4j.conf.ConfigurationBuilder;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;


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
		accessToken="4322430433-4Yeu5vSZei7TMTeXYEJnyKP6AUVVQq3GP5Xj3fk";
		accessTokenSecret="3BQEqbtHZ8e13Nwx71nI3yyvRWo3moF4VLiYdyr7ojzJv";		
		builder.setOAuthAccessToken(accessToken);
		builder.setOAuthAccessTokenSecret(accessTokenSecret);
		builder.setDebugEnabled(true);
		builder.setJSONStoreEnabled(true);
	}

	public void getTweets(String searchQuery) throws TwitterException, IOException, JSONException{
		twitter= new TwitterFactory(builder.build()).getInstance();
		Query query = new Query(searchQuery);
		query.setResultType(ResultType.popular);
		QueryResult result;
		new File("Tweets").mkdir();
		do {
			result = twitter.search(query);
			List<Status> tweets=result.getTweets();
			for (Status tweet :tweets) {
				String json= TwitterObjectFactory.getRawJSON(tweet);
				new File("Tweets/"+query.getQuery()).mkdir();
				String fileName = "Tweets/" + query.getQuery()+"/"+tweet.getId()+ ".json";
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

	private static void storeRefinedTweet(String refinedTweets,String refinedTweetFileName) throws IOException{
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		try {
			fos = new FileOutputStream(refinedTweetFileName);
			osw = new OutputStreamWriter(fos, "UTF-8");
			bw = new BufferedWriter(osw);
			bw.write(refinedTweets);
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

	private static String removeStopWords(String tweetRegex) throws IOException{
		String [] words= tweetRegex.split(" ");
		BufferedReader br= new BufferedReader(new FileReader("stopwords.txt"));
		List<String> stopWordList= new ArrayList<String>();
		List<String> tweetList= new ArrayList<String>();
		String str=null;
		String stopWordsRemoved="";
		while((str=br.readLine())!=null)
		{	
			stopWordList.add(str);

		}
		for(String word:words){
			if(!stopWordList.contains(word))
			{
				tweetList.add(word);
			}
		}
		for(String text:tweetList)
		{
			stopWordsRemoved+=" "+text;
		}
		br.close();
		return stopWordsRemoved;

	}

	public static String posTagger(String textToBeTagged) throws IOException{
		MaxentTagger tagger= new MaxentTagger("/Users/varungupta/Downloads/stanford-postagger-2015-04-20/models/english-left3words-distsim.tagger");
		String taggedText=tagger.tagString(textToBeTagged);
		return taggedText;

	}
	public static void main(String args[]) throws TwitterException, IOException, JSONException, ClassNotFoundException, SQLException
	{
		TwitterDataJSON tweet= new TwitterDataJSON();
		String searchQuery= new String("Bajirao Mastani");
		tweet.getTweets(searchQuery);
		DrillJDBCCon con = new DrillJDBCCon();
		ResultSet rs=con.executeQuery("Select id,text FROM dfs.`/Users/varungupta/git/OriginNew/Tweets/"+searchQuery+"/`");
		while(rs.next()){
			System.out.println(rs.getLong("id"));
			Long Id=rs.getLong("id");
			String tweetRegex=rs.getString("text").replaceAll("@[a-zA-Z1-9]+|#[a-zA-Z1-9]+|(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]+|(\n)", "");
			String stopWordsRemoved=removeStopWords(tweetRegex);
			String taggedText=posTagger(stopWordsRemoved);
			String refinedTweetFileName = "Tweets/" + searchQuery+"/"+Id+"Refined.txt";
			storeRefinedTweet(taggedText,refinedTweetFileName);
		}

	}
}
