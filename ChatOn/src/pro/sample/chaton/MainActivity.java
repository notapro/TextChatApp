package pro.sample.chaton;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	EditText name, phone, status;
	Editor e;
	Bitmap bmp;
	final static int camData = 0;
	ImageButton dp;
	Button b;
	Intent chatsPage, cam;
	SharedPreferences userData; // stores user general data

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init(); // initialize variables
		if (!userData.contains("name")) {	//check if shared preference already has data
			dp.setOnClickListener(this);
			b.setOnClickListener(this);

		} else {	//launch chat page
			chatsPage = new Intent(MainActivity.this, ChatPage.class);
			startActivity(chatsPage);
			finish();
		}
	}

	public void init() {	//initializations
		dp = (ImageButton) findViewById(R.id.imageButton1);
		b = (Button) findViewById(R.id.button1);
		userData = getSharedPreferences("DATA", MODE_PRIVATE);
		name = (EditText) findViewById(R.id.name);
		phone = (EditText) findViewById(R.id.editText2);
		status = (EditText) findViewById(R.id.editText3);
	}

	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.imageButton1:	//open camera intent to capture image
			cam = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(cam, camData);
			break;
		case R.id.button1:	//storing the data
			e = userData.edit();
			if (name.getText().toString().isEmpty() || phone.getText().toString().isEmpty()) {	//check if name and contact number has been entered
				Toast.makeText(MainActivity.this,
						"Username and Phone Number required !!!",
						Toast.LENGTH_LONG).show();
			} else {
				e.putString("name", name.getText().toString());
				e.putString("phone", phone.getText().toString());
				e.putString("status", status.getText().toString());
				e.commit();	//save details in shared preference
				chatsPage = new Intent(MainActivity.this, ChatPage.class);
				startActivity(chatsPage);	//start chat intent
				finish();
			}
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {	//retrieve image from cam intent
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Bundle b = data.getExtras();
			bmp = (Bitmap) b.get("data");
			dp.setImageBitmap(bmp);
		}
	}

}
