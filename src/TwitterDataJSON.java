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

import org.json.simple.parser.ParseException;

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


public class TwitterDataJSON implements Runnable {
	public void run(){
		TwitterDataJSON tweet= new TwitterDataJSON();
		TwitterCredentialsRunnable credentialsRunnable= new TwitterCredentialsRunnable();
		try {
			ConfigurationBuilder builder=credentialsRunnable.readJson(Integer.parseInt(Thread.currentThread().getName()));
			String searchQuery= new String("Varun");
			getTweets(searchQuery, builder);
			DrillJDBCCon con = new DrillJDBCCon();
			tweet.tweetProcessing(con, searchQuery);
		} catch (IOException e) {
			System.out.println("IO Exception encountered");
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("Parse Exception encountered");
			e.printStackTrace();
		} catch (JSONException e) {
			System.out.println("JSONException encountered");
			e.printStackTrace();}
		catch (TwitterException e) {
			System.out.println("TwitterException encountered");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException encountered");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("SQLException encountered");
			e.printStackTrace();
		}
	}

	private static void getTweets(String searchQuery,ConfigurationBuilder builder) throws TwitterException, IOException, JSONException{
		Twitter twitter= new TwitterFactory(builder.build()).getInstance();
		
		Query query = new Query(searchQuery);
		query.setResultType(ResultType.popular);
		QueryResult result;
		new File("Tweets").mkdir();
		do {
			//System.out.println(Thread.currentThread().getName());
			result = twitter.search(query);
			List<Status> tweets=result.getTweets();
			for (Status tweet :tweets) {
				String json= TwitterObjectFactory.getRawJSON(tweet);
				new File("Tweets/"+query.getQuery()).mkdir();
				String fileName = "Tweets/" + query.getQuery()+"/"+tweet.getId()+ ".json";
				writeData(json, fileName);
			}	
		} while ((query = result.nextQuery()) != null);
	}

	private static void writeData(String json,String fileName) throws IOException{
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

		private static String removeStopWords(String tweetRegex) throws IOException{
		String [] words= tweetRegex.split(" ");
		BufferedReader br= new BufferedReader(new FileReader("stopwords"));
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

	public  void tweetProcessing(DrillJDBCCon con,String searchQuery) throws ClassNotFoundException, SQLException, IOException{
		ResultSet rs=con.executeQuery("Select id,text FROM dfs.`/Users/varungupta/git/Origin/Tweets/"+searchQuery+"/`");
		new File("TweetsData").mkdir();
		new File("TweetsData/"+searchQuery).mkdir();
		while(rs.next()){
			//System.out.println(rs.getLong("id")+" "+Thread.currentThread());
			Long Id=rs.getLong("id");
			String tweetRegex=rs.getString("text").replaceAll("@[a-zA-Z1-9]+|#[a-zA-Z1-9]+|(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]+|(\n)", "");
			String stopWordsRemoved=removeStopWords(tweetRegex);
			String taggedText=posTagger(stopWordsRemoved);
			//new File("TweetsData/"+searchQuery+"/Data").mkdir();
			String refinedTweetFileName = "TweetsData/"+searchQuery+"/"+Id+"Refined.txt";
			writeData(taggedText, refinedTweetFileName);
		}
	}
	public static void main(String args[]) throws TwitterException, IOException, JSONException, ClassNotFoundException, SQLException, ParseException
	{
		int threadNumber=new TwitterCredentialsRunnable().returnJsonSize();
		Thread [] threads= new Thread[threadNumber];
		for(int i=0; i<threads.length; i++)
		{	
			threads[i]=new Thread(new TwitterDataJSON());
			threads[i].setName(""+i);
			threads[i].start();
		}
	}
}
