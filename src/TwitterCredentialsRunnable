import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import twitter4j.JSONException;
import twitter4j.conf.ConfigurationBuilder;

class TwitterCredentialsRunnable {
	public Integer returnJsonSize() throws IOException, ParseException{
		JSONParser parser= new JSONParser();
		FileReader fileReader= new FileReader("TwitterCredentials.json");
		Object obj = parser.parse(fileReader);
		JSONArray jar= (JSONArray)obj;
		return jar.size();
	}

	public ConfigurationBuilder readJson(int index) throws IOException, ParseException, JSONException
	{
		JSONParser parser= new JSONParser();
		FileReader fileReader= new FileReader("TwitterCredentials.json");
		Object obj = parser.parse(fileReader);
		JSONArray jar= (JSONArray)obj;
		JSONObject jsonobject = (JSONObject)jar.get(index);
		String CONSUMER_KEY = (String)jsonobject.get("CONSUMER_KEY");
		String CONSUMER_KEY_SECRET=(String)jsonobject.get("CONSUMER_KEY_SECRET");
		String accessToken=(String)jsonobject.get("accessToken");
		String accessTokenSecret=(String)jsonobject.get("accessTokenSecret");
		return buildAccount(CONSUMER_KEY,CONSUMER_KEY_SECRET,accessToken,accessTokenSecret);
	}

	private ConfigurationBuilder buildAccount(String CONSUMER_KEY,String CONSUMER_KEY_SECRET,String accessToken,String accessTokenSecret) throws IOException, ParseException, JSONException{
		ConfigurationBuilder builder= new ConfigurationBuilder();
		builder.setOAuthConsumerKey(CONSUMER_KEY);
		builder.setOAuthConsumerSecret(CONSUMER_KEY_SECRET);
		builder.setOAuthAccessToken(accessToken);
		builder.setOAuthAccessTokenSecret(accessTokenSecret);
		builder.setDebugEnabled(true);
		builder.setJSONStoreEnabled(true);
		return builder;
	}	
}

