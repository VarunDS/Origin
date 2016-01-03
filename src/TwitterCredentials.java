import java.io.FileWriter;
import java.io.IOException;

import twitter4j.JSONArray;
import twitter4j.JSONException;
import twitter4j.JSONObject;


public class TwitterCredentials {

	public static void main(String args[]) throws JSONException, IOException{
		JSONObject jo= new JSONObject();
		JSONArray ja= new JSONArray();
		jo.put("account_name","aaryan.varun@gmail.com");
		jo.put("CONSUMER_KEY", "ENTER YOUR DETAILS HERE");
		jo.put("CONSUMER_KEY_SECRET", "ENTER YOUR DETAILS HERE");
		jo.put("accessToken", "ENTER YOUR DETAILS HERE");
		jo.put("accessTokenSecret", "ENTER YOUR DETAILS HERE");
		ja.put(jo);
		JSONObject jo1= new JSONObject();
		jo1.put("account_name","Goyal.7693@gmail.com");
		jo1.put("CONSUMER_KEY", "ENTER YOUR DETAILS HERE");
		jo1.put("CONSUMER_KEY_SECRET", "ENTER YOUR DETAILS HERE");
		jo1.put("accessToken", "ENTER YOUR DETAILS HERE");
		jo1.put("accessTokenSecret", "ENTER YOUR DETAILS HERE");
		ja.put(jo1);


		System.out.println(ja);
		FileWriter file= new FileWriter("TwitterCredentials.json");
		file.write(ja.toString());
		file.close();
	}

}

