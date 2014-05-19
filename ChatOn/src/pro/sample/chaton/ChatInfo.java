package pro.sample.chaton;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class ChatInfo extends Activity {	//to see info of either the user or his chats

	TextView name, phone;
	String nameStr, phoneStr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_info);
		init();
		nameStr=getIntent().getStringExtra("contactName").toString();	//retrieve name
		phoneStr=getIntent().getStringExtra("contactNum").toString();	//retrieve phone number
		name.setText("Name:- "+nameStr);
		phone.setText("Contact:- "+ phoneStr);
	}

	public void init()	//initialization
	{
		name=(TextView) findViewById(R.id.person_name);
		phone=(TextView) findViewById(R.id.contact_num);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat_info, menu);
		return true;
	}

}
