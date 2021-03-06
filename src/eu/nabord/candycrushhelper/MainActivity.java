package eu.nabord.candycrushhelper;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		((Button) findViewById(R.id.button1)).setOnClickListener(this);
		((Button) findViewById(R.id.button2)).setOnClickListener(this);
		((Button) findViewById(R.id.button3)).setOnClickListener(this);
		
		LinearLayout linearlayout = ((LinearLayout) findViewById(R.id.generalLinearLayout));
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
		     // landscape
		     linearlayout.setOrientation(LinearLayout.HORIZONTAL); 
		} else {
		    // portrait  
		    linearlayout.setOrientation(LinearLayout.VERTICAL); 
		}
		
		if (appInstalledOrNot(getString(R.string.apk_candy_crush)) == false)
		{
			Toast.makeText(getApplicationContext(), "Candy Crush in not installed", 
					   Toast.LENGTH_LONG).show();
			finish();
		}
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
		case R.id.button3:
			intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			break;
		}
	}
	
	private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed ;
    }
}
