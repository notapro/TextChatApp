package pro.sample.chaton;

import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class ChatPage extends Activity implements View.OnClickListener,
		OnItemClickListener {

	int numChats;
	String[] chatNamesStringArray;
	SharedPreferences chats;
	Set<String> phone;
	final static int msgCode = 0;
	Intent msg, messages;
	Button newMsg;
	ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_page);
		init();
		lv.setOnItemClickListener(this);
		newMsg.setOnClickListener(this);
	}

	public void init() {	//initializations
		chats = getSharedPreferences("DATA", MODE_PRIVATE);
		newMsg = (Button) findViewById(R.id.newmsgbtn);
		lv = (ListView) findViewById(R.id.listView1);
		numChats = chats.getInt("numChats", 0);
		chatNamesStringArray = new String[numChats];
		for (int i = 0; i < numChats; i++) {	//retrieve the names of people to which some message has been sent
			chatNamesStringArray[i] = chats.getString(chats.getString("phone"+ i, "") + "name" , "");	//every contact has unique contact number
		}
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				chatNamesStringArray);
		lv.setAdapter(adapter);	//set the list adapter
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat_page, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId()==R.id.personal_info)	//Menu option to see personal info
		{
			msg=new Intent(ChatPage.this, ChatInfo.class);
			msg.putExtra("contactNum", chats.getString("phone", ""));
			msg.putExtra("contactName", chats.getString("name", ""));
			startActivity(msg);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode==0)
		{
			if(resultCode==RESULT_CANCELED)
			{
				onCreate(null);	//after returning from creating a new message, call onCreate to refresh list on screen
			}
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0.getId() == R.id.newmsgbtn) {	//compose new message
			msg = new Intent("android.intent.action.NEWMESSAGE");
			startActivityForResult(msg, msgCode);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,	//to see chats sent to selected person
			long arg3) {
		// TODO Auto-generated method stub
		messages = new Intent(ChatPage.this, Messages.class);
		messages.putExtra("person", chatNamesStringArray[position]);
		messages.putExtra("phoneperson", chats.getString("phone"+ position, ""));
		startActivity(messages);
	}

}
