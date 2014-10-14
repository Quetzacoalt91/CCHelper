package eu.nabord.candycrushhelper;

import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import eu.nabord.classes.HexReader;

public class BonusActivity extends Activity {
	
	HexReader file = null;
	String general_fileName = Environment.getExternalStorageDirectory().getPath()+"/SaveCandyCrush/backup/save_1066067012.dat";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bonus);
		
		((EditText) findViewById(R.id.nbFreezeTime)).setOnFocusChangeListener(focusListener);
		((EditText) findViewById(R.id.nbColorBomb)).setOnFocusChangeListener(focusListener);
		((EditText) findViewById(R.id.nbJellyFish)).setOnFocusChangeListener(focusListener);
		((EditText) findViewById(R.id.nbCoconutWheel)).setOnFocusChangeListener(focusListener);
		((EditText) findViewById(R.id.nbLollipopHammer)).setOnFocusChangeListener(focusListener);
		((EditText) findViewById(R.id.nbLuckyCandy)).setOnFocusChangeListener(focusListener);
		((EditText) findViewById(R.id.nbWrappedandStriped)).setOnFocusChangeListener(focusListener);
		((EditText) findViewById(R.id.nbFreeSwitch)).setOnFocusChangeListener(focusListener);
		((EditText) findViewById(R.id.nbMoonstruckBooster)).setOnFocusChangeListener(focusListener);
		((EditText) findViewById(R.id.nbLives)).setOnFocusChangeListener(focusListener);
		
		this.requestFile(general_fileName);
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
			this.requestFile(general_fileName);
			
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
			this.requestFile(general_fileName);
			
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
			file = new HexReader(fileName, "rw");
		} catch (FileNotFoundException e) {
			Toast.makeText(getApplicationContext(), (String)e.getMessage(), 
					   Toast.LENGTH_SHORT).show();
		}
	}
	
	private OnFocusChangeListener focusListener = new OnFocusChangeListener() {
	      @Override
	      public void onFocusChange(View v, boolean hasFocus) {
	    	  Resources res = getResources();
	    	  String checkBoxName = res.getResourceEntryName(v.getId()).replace("nb", "checkBox");
	    	  int id = res.getIdentifier(checkBoxName, "id", null);
	    	  if (id > 0)
	    		  ((CheckBox) findViewById(id)).setChecked(true);
	    	  /*else
	    		  Toast.makeText(getApplicationContext(), checkBoxName, 
					   Toast.LENGTH_SHORT).show();*/
	      }
	};
}
