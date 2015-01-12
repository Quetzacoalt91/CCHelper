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

public class SettingsActivity extends Activity implements OnClickListener {

	private String levels_path = "assets/res_output/levels/*";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		((Button) findViewById(R.id.button1)).setOnClickListener(this);
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			try {
			generate();
			}
			catch (Exception e) {}
			break;
		}
	}
	
	public void generate() throws FileNotFoundException {
		List<String> l = ExecuteAsRootBase.execute("ls /data/app/"+ getString(R.string.apk_candy_crush)+"*", true);
		if(l.size() == 0)
			throw new FileNotFoundException("Nope ! Cannot find results of ls !");
		String apk = l.get(l.size()-1);
		
		ExecuteAsRootBase.execute("unzip "+apk+" "+levels_path+" -d /storage/emulated/legacy/"+ getString(R.string.dir_backup));
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {}
		ExecuteAsRootBase.close();
		
		
	}
}
