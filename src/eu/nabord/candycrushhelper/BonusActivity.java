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
			
			((EditText) findViewById(R.id.nbColorBomb)).setText(file.getValueInFile("50").toString());
		} catch (FileNotFoundException e) {
			Toast.makeText(getApplicationContext(), (String)e.getMessage(), 
					   Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
