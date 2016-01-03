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
		jo.put("CONSUMER_KEY", "lK2k609IQGtUQudAu6CIencxB");
		jo.put("CONSUMER_KEY_SECRET", "ZFpvxvRywjFs6VHKoVCMr3H1dRePO7pkzgLrqeLYYP8P5p1xZT");
		jo.put("accessToken", "4322430433-4Yeu5vSZei7TMTeXYEJnyKP6AUVVQq3GP5Xj3fk");
		jo.put("accessTokenSecret", "3BQEqbtHZ8e13Nwx71nI3yyvRWo3moF4VLiYdyr7ojzJv");
		ja.put(jo);
		JSONObject jo1= new JSONObject();
		jo1.put("account_name","Goyal.7693@gmail.com");
		jo1.put("CONSUMER_KEY", "bhCNCrG7bubOpvGoDm11m4dov");
		jo1.put("CONSUMER_KEY_SECRET", "9l4RfZ9R2xpD4paj9rwDcVJCv4WR5VlDHeqTqE88axqubZcayv");
		jo1.put("accessToken", "210721436-O0tWqO0PKrhgcMxm0RzpNCxa0xUycxQWSt4EhJ0I");
		jo1.put("accessTokenSecret", "NQ8xyGLID8zJlcZ3oqfGfxkcNapmHaTuPaYOS7zJjEHhP");
		ja.put(jo1);


		System.out.println(ja);
		FileWriter file= new FileWriter("TwitterCredentials.json");
		file.write(ja.toString());
		file.close();
	}

}

