package eu.nabord.candycrushhelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		((Button) findViewById(R.id.button1)).setOnClickListener(this);
		((Button) findViewById(R.id.button2)).setOnClickListener(this);
	}

	public void onClick(View v) {
		
		Intent intent;
		
		switch (v.getId()) {
		case R.id.button1:
			intent = new Intent(this, TimeChangerActivity.class);
			startActivity(intent);
			break;
		case R.id.button2:
			intent = new Intent(this, BonusActivity.class);
			startActivity(intent);
			break;
		}
	}
}
