package nkhook.bot;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class GithubWrapper {
	private static GithubWrapper instance;
	public GithubWrapper() {
		instance = this;
	}
	public static GithubWrapper getInstance() {
		return instance;
	}
	
	
	@SuppressWarnings("unchecked")
	public String getLatestDownload() {
		try {
			URL url = new URL("https://api.github.com/repos/DisabledMallis/NKHook5/releases");
			URLConnection con = url.openConnection();
			InputStream in = con.getInputStream();
			String text = IOUtils.toString(in, "UTF-8");
			JSONArray jarr = (JSONArray)JSONValue.parse(text);
			ArrayList<JSONObject> jobjList = new ArrayList<>();
			jarr.forEach(res -> jobjList.add((JSONObject)res));
			String returner = "**Latest Release**\n";
			for(JSONObject jobj : jobjList) {
				String name = (String)jobj.get("name");
				for(Object val : ((JSONArray)(jobj.get("assets")))) {
					JSONObject jprop = (JSONObject)val;
					String dlName = (String) jprop.get("browser_download_url");
					returner += "Release: "+name+"\n";
					returner += "Download URL: "+dlName;
					return returner;
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
