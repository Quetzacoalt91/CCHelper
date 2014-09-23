package eu.nabord.candycrushhelper;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class TimeChangerActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_changer);

		((Button) findViewById(R.id.button1)).setOnClickListener(this);
		((Button) findViewById(R.id.button2)).setOnClickListener(this);
	}

	public void onClick(View v) {
		boolean timeChanged = false;
		
		switch (v.getId()) {
		case R.id.button1:
			timeChanged = changeDateAndTime(3600000);
			break;
		case R.id.button2:
			timeChanged = changeDateAndTime(3600000*24);
			break;
		}
		
		if(timeChanged) {
			// Execute Candy Crush
		}
	}
	
	private boolean changeDateAndTime(long time) {
		try {
			Calendar c = Calendar.getInstance(); 
			AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
			am.setTime(c.getTimeInMillis()+time);
		}
		catch (Exception e) {
			Toast.makeText(getApplicationContext(), (String)e.getMessage(), 
					   Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
}
