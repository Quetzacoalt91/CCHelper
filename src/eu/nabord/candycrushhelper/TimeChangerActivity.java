package eu.nabord.candycrushhelper;

import java.util.ArrayList;
import java.util.Calendar;

import eu.nabord.classes.ExecuteAsRootBase;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class TimeChangerActivity extends Activity implements OnClickListener {
	
	private boolean root_access = false;
	private long diff;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_changer);
		
		diff = 0;

		((Button) findViewById(R.id.button1)).setOnClickListener(this);
		((Button) findViewById(R.id.button2)).setOnClickListener(this);
	}
	
	public void onResume() {
		  super.onResume();
		  
		  if(diff != 0) {
			  changeDateAndTime(-diff);
			  diff = 0;
		  }
	}

	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.button1:
			diff = 3600000;
			break;
		case R.id.button2:
			diff = 3600000*24;
			break;
		}
		

		if(changeDateAndTime(diff))
		{
			// Execute Candy Crush
			Intent launchIntent = getPackageManager().getLaunchIntentForPackage(getString(R.string.apk_candy_crush));
			//launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			launchIntent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
			launchIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			launchIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(launchIntent);
			
		}
	}
	
	private boolean changeDateAndTime(long time) {
		try {
			Calendar c = Calendar.getInstance(); 
			/*AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
			am.setTime(c.getTimeInMillis()+time);*/
			if (root_access || ExecuteAsRootBase.canRunRootCommands()) {
				root_access = true;
				ExecuteAsRootBase.execute("chmod 666 /dev/alarm");
				SystemClock.setCurrentTimeMillis(c.getTimeInMillis()+time);
				ExecuteAsRootBase.execute("chmod 664 /dev/alarm");
		    }
		}
		catch (Exception e) {
			Toast.makeText(getApplicationContext(), (String)e.getMessage(), 
					   Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
}
