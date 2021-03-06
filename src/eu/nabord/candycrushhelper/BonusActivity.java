package eu.nabord.candycrushhelper;

import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import eu.nabord.classes.HexReader;

public class BonusActivity extends Activity {
	
	HexReader file = null;
	public static String general_filePath = "/data/data/com.king.candycrushsaga/app_storage/";
	public static String general_fileName = "save_*.dat";
	String backupPath = null;
	String savegameName = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bonus);
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		// then you use
		savegameName = general_filePath+prefs.getString("savegame_list", general_fileName);
		
		this.backupPath = "/storage/emulated/legacy/"+ getString(R.string.dir_backup); //getExternalCacheDir().getPath();
		this.requestFile(savegameName);
		this.refresh();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.bonus_activity_actions, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_refresh:
	        	this.refresh();
	            return true;
	        case R.id.action_save:
	        	this.save();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void refresh() {
		try {
			this.requestFile(savegameName);
			
			((EditText) findViewById(R.id.nbFreezeTime)).setText(file.getValueInFile(getString(R.string.addr_freeze_time)).toString());
			((EditText) findViewById(R.id.nbColorBomb)).setText(file.getValueInFile(getString(R.string.addr_color_bomb)).toString());
			((EditText) findViewById(R.id.nbJellyFish)).setText(file.getValueInFile(getString(R.string.addr_jelly_fish)).toString());
			((EditText) findViewById(R.id.nbCoconutWheel)).setText(file.getValueInFile(getString(R.string.addr_coconut_wheel)).toString());
			((EditText) findViewById(R.id.nbLollipopHammer)).setText(file.getValueInFile(getString(R.string.addr_lollipop_hammer)).toString());
			((EditText) findViewById(R.id.nbLuckyCandy)).setText(file.getValueInFile(getString(R.string.addr_lucky_candy)).toString());
			((EditText) findViewById(R.id.nbWrappedandStriped)).setText(file.getValueInFile(getString(R.string.addr_wrapped_and_strapped)).toString());
			((EditText) findViewById(R.id.nbFreeSwitch)).setText(file.getValueInFile(getString(R.string.addr_free_switch)).toString());
			((EditText) findViewById(R.id.nbMoonstruckBooster)).setText(file.getValueInFile(getString(R.string.addr_moonstruck_booster)).toString());
			((EditText) findViewById(R.id.nbLives)).setText(file.getValueInFile(getString(R.string.addr_lives)).toString());
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Cannot read file !", 
					   Toast.LENGTH_SHORT).show();
		}
	}
	
	private void save() {
		try {
			this.requestFile(savegameName);
			
			file.createBackup(file.PRE_BACKUP);
			
			file.setValueInFile(getString(R.string.addr_freeze_time), Integer.parseInt(((EditText) findViewById(R.id.nbFreezeTime)).getText().toString()));
			file.setValueInFile(getString(R.string.addr_color_bomb), Integer.parseInt(((EditText) findViewById(R.id.nbColorBomb)).getText().toString()));
			file.setValueInFile(getString(R.string.addr_jelly_fish), Integer.parseInt(((EditText) findViewById(R.id.nbJellyFish)).getText().toString()));
			file.setValueInFile(getString(R.string.addr_coconut_wheel), Integer.parseInt(((EditText) findViewById(R.id.nbCoconutWheel)).getText().toString()));
			file.setValueInFile(getString(R.string.addr_lollipop_hammer), Integer.parseInt(((EditText) findViewById(R.id.nbLollipopHammer)).getText().toString()));
			file.setValueInFile(getString(R.string.addr_lucky_candy), Integer.parseInt(((EditText) findViewById(R.id.nbLuckyCandy)).getText().toString()));
			file.setValueInFile(getString(R.string.addr_wrapped_and_strapped), Integer.parseInt(((EditText) findViewById(R.id.nbWrappedandStriped)).getText().toString()));
			file.setValueInFile(getString(R.string.addr_free_switch), Integer.parseInt(((EditText) findViewById(R.id.nbFreeSwitch)).getText().toString()));
			file.setValueInFile(getString(R.string.addr_moonstruck_booster), Integer.parseInt(((EditText) findViewById(R.id.nbMoonstruckBooster)).getText().toString()));
			file.setValueInFile(getString(R.string.addr_lives), Integer.parseInt(((EditText) findViewById(R.id.nbLives)).getText().toString()));
			
			file.createBackup(file.POST_BACKUP);
			file.save();
			file.close();
			file = null;
			Toast.makeText(getApplicationContext(), "File saved !", 
					   Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Cannot save file !", 
					   Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
	
	private void requestFile (String fileName) {
		if (file != null && !fileName.equals(file.getNameFile()))
			file.close();
		
		
		try {
			file = new HexReader(fileName, "rw", backupPath);
		} catch (FileNotFoundException e) {
			Toast.makeText(getApplicationContext(), (String)e.getMessage(), 
					   Toast.LENGTH_SHORT).show();
		}
	}
}
