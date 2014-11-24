package eu.nabord.candycrushhelper;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import eu.nabord.classes.ExecuteAsRootBase;

public class TimeChangerActivity extends Activity implements OnClickListener {

	private long diff;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_changer);
		
		diff = 0;

		((Button) findViewById(R.id.button1)).setOnClickListener(this);
		((Button) findViewById(R.id.button2)).setOnClickListener(this);
	}
	
	@Override
	public void onResume() {
		  if(diff != 0 && changeDateAndTime(-diff) == true)
			  diff = 0;
		  super.onResume();
	}

	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.button1:
			diff = 3600000;
			break;
		case R.id.button2:
			diff = 3600000*24;
			break;
		case R.id.button3:
			diff = 3600000*24*7;
			break;
		}
		

		if(changeDateAndTime(diff))
		{
			ExecuteAsRootBase.close();
			
			// Execute Candy Crush
			Intent launchIntent = getPackageManager().getLaunchIntentForPackage(getString(R.string.apk_candy_crush));
			//launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			launchIntent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
			launchIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			launchIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(launchIntent);
			
		}
		else diff = 0;
	}
	
	private boolean changeDateAndTime(long time) {
		try {
			Calendar c = Calendar.getInstance(); 

			ExecuteAsRootBase.execute("chmod 666 /dev/alarm");
			if (!SystemClock.setCurrentTimeMillis(c.getTimeInMillis()+time))
				throw new Exception("Set time has failed !");
			ExecuteAsRootBase.execute("chmod 664 /dev/alarm");
		}
		catch (Exception e) {
			Toast.makeText(getApplicationContext(), (String)e.getMessage(), 
					   Toast.LENGTH_SHORT).show();
			e.getStackTrace();
			return false;
		}
		return true;
	}
}
