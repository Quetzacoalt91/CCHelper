package eu.nabord.candycrushhelper;

import java.io.FileNotFoundException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.widget.EditText;
import android.widget.Toast;
import eu.nabord.classes.HexReader;

public class BonusActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bonus);
		
		
		
		this.refresh();
	}
	
	private void refresh() {
		try {
			HexReader file = new HexReader(Environment.getExternalStorageDirectory().getPath()+"/SaveCandyCrush/backup/save_1066067012.dat", "r");
			
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
		} catch (FileNotFoundException e) {
			Toast.makeText(getApplicationContext(), (String)e.getMessage(), 
					   Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
