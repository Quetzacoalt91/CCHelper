package eu.nabord.candycrushhelper;

import java.io.FileNotFoundException;
import java.util.List;

import eu.nabord.classes.ExecuteAsRootBase;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class tempActivity extends Activity implements OnClickListener {

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_settings);
		
		((Button) findViewById(R.id.button1)).setOnClickListener(this);
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			try {
			}
			catch (Exception e) {}
			break;
		}
	}
	
	
}
