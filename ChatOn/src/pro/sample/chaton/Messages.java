package pro.sample.chaton;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Messages extends Activity {

	SharedPreferences chats;
	String[] msgArray;
	int numMsg;
	TextView name;
	String person,phone;
	ListView lv;
	Intent msg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messages);
		init();

	}

	public void init() {	//initialization
		name = (TextView) findViewById(R.id.person);
		person = getIntent().getStringExtra("person");
		phone=getIntent().getStringExtra("phoneperson");
		name.setText(person);
		lv = (ListView) findViewById(R.id.listView2);
		chats = getSharedPreferences("DATA", MODE_PRIVATE);
		numMsg = chats.getInt(phone + "num", 0);
		msgArray = new String[numMsg];
		for (int i = 0; i < numMsg; i++) {	//displaying the chats of the selected person
			msgArray[i] = chats.getString(phone + i, "");
		}
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				msgArray);
		lv.setAdapter(adapter);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {	
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.messages, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {	//to check the contact's info
		// TODO Auto-generated method stub
		if(item.getItemId()==R.id.chat_info)
		{
			msg=new Intent(Messages.this, ChatInfo.class);
			msg.putExtra("contactNum", person);
			msg.putExtra("contactName", phone);
			startActivity(msg);
		}
		return super.onOptionsItemSelected(item);
	}

}
