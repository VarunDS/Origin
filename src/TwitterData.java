import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.json.DataObjectFactory;



public class TwitterData {
	private Twitter twitter;
	private static final String CONSUMER_KEY="lK2k609IQGtUQudAu6CIencxB";
	private static final String CONSUMER_KEY_SECRET="ZFpvxvRywjFs6VHKoVCMr3H1dRePO7pkzgLrqeLYYP8P5p1xZT";
	private String accessToken;
	private String accessTokenSecret;

	

	public void getTweets() throws TwitterException, IOException{
		
		twitter=new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_KEY_SECRET);
		accessToken="4322430433-RiyQd99LzEfUl4RhgYU3X8hDK8VzKbSglWl8PxL";
		accessTokenSecret="U9Xciy5jSU8gSYZI77l0O5TBYvKRTwNeptvrTIs4XOopc";
		AccessToken oathAccessToken = new AccessToken(accessToken, accessTokenSecret);
		twitter.setOAuthAccessToken(oathAccessToken);
		Query query = new Query("avengers");
		query.setSince("2015-11-22");
		QueryResult result;
		do {
			result = twitter.search(query);
			List<Status> tweets = result.getTweets(); 
			for (Status tweet : tweets) {
				
				System.out.println("@"+tweet.getUser().getScreenName() + "|" + tweet.getText()+"|"+ tweet.isRetweeted() );
			}
		} while ((query = result.nextQuery()) != null);

	}

	public static void main(String args[]) throws TwitterException, IOException
	{
		TwitterData tweet= new TwitterData();
		tweet.getTweets();
	}

}
