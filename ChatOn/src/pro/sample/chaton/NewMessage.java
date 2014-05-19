package pro.sample.chaton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewMessage extends Activity implements OnClickListener {

	int numChats;
	String nameStr, messageStr, phoneStr;
	EditText name, message, phone;
	Button submit;
	SharedPreferences chats;
	Editor e;
	Intent chatsPage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_message);
		init();
		submit.setOnClickListener(this);
	}

	public void init() {	//initialization
		chats = getSharedPreferences("DATA", MODE_PRIVATE);
		submit = (Button) findViewById(R.id.sendMsg);
		name = (EditText) findViewById(R.id.name);
		phone = (EditText) findViewById(R.id.phonenumber);
		message = (EditText) findViewById(R.id.message);
	}

	@Override
	public void onClick(View v) {	//send message after pressing submit button
		// TODO Auto-generated method stub
		nameStr = name.getText().toString();
		messageStr = message.getText().toString();
		phoneStr = phone.getText().toString();
		if (validate() && isConnected()) {	//to check whether data is valid and is the person connected to the internet
			Toast.makeText(NewMessage.this, "Your message is being sent!",
					Toast.LENGTH_SHORT).show();
			new HttpAsync().execute();
		} else if (!validate()) {
			Toast.makeText(this.getApplicationContext(),
					"Fields cannot be left empty!", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this.getApplicationContext(),
					"Cannot contact the server!", Toast.LENGTH_LONG).show();
		}
		
	}
	
	public boolean isConnected() {	//check if person is conneccted to internet
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected())
			return true;
		else
			return false;
	}

	private boolean validate() {	//to validate form data
		if (nameStr.trim().equals(""))
			return false;
		else if (phoneStr.trim().equals(""))
			return false;
		else if (messageStr.trim().equals(""))
			return false;
		else
			return true;
	}


	class HttpAsync extends AsyncTask<String, Void, String> {

		@Override
		protected void onPostExecute(String result) {	//after message is sent, store the new message in the appropriate chat
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Toast.makeText(NewMessage.this, "Message Sent !!", Toast.LENGTH_SHORT).show();
			e = chats.edit();
			int f = 0;
			numChats = chats.getInt("numChats", 0);
			for (int i = 0; i < numChats && f == 0; i++) {
				if (chats.getString(chats.getString("phone" + i, "") + "name", "").equals(nameStr)
						&& chats.getString("phone" + i, "").equals(phoneStr)) {
					f = 1;
				}
			}
			if (f == 0) {
				e.putInt("numChats", numChats + 1);
				e.putString("phone" + numChats, phoneStr);
				e.putString(phoneStr + "name", nameStr);
			}
			int personChatsNum = chats.getInt(phoneStr + "num", 0);
			e.putInt(phoneStr + "num", personChatsNum + 1);
			e.putString(phoneStr + personChatsNum, messageStr);
			e.commit();
			chatsPage = new Intent();
			setResult(RESULT_CANCELED, chatsPage);	//RESULT_CANCELED because no data is being returned
			finish();
		}

		@Override
		protected String doInBackground(String... params) {	//Connection established with server, request sent and the received data has been logged
			// TODO Auto-generated method stub
			InputStream inputStream = null;
			String result = "";
			try {

				HttpClient httpclient = new DefaultHttpClient();

				HttpPost httpPost = new HttpPost("http://www.google.com");
				httpPost.setHeader("Content-type", "application/json");
				String json = "";

				JSONObject jsonObject = new JSONObject();
				jsonObject.put("Contact", phoneStr);
				jsonObject.put("Message", messageStr);

				json = jsonObject.toString();
				Log.d("content", json);
				StringEntity se = new StringEntity(json);
				se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
						"application/json"));
				httpPost.setEntity(se);

				HttpResponse httpResponse = httpclient.execute(httpPost);

				inputStream = httpResponse.getEntity().getContent();

				if (inputStream != null) {
					result = convertInputStreamToString(inputStream);

				} else {
					result = "Did not work!";
				}

				Log.d("title", result);

			} catch (Exception e) {
				e.printStackTrace();
			}

			return result;
		}

		private String convertInputStreamToString(InputStream inputStream)
				throws IOException {
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(inputStream));
			String line = "";
			String result = "";
			while ((line = bufferedReader.readLine()) != null)
				result += line;

			inputStream.close();
			return result;

		}
	}

}
