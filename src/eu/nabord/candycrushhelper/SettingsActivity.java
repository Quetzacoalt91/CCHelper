package eu.nabord.candycrushhelper;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import eu.nabord.classes.ExecuteAsRootBase;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {

	private final String levels_path = "assets/res_output/levels/*";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_general);

		// montre la valeur courrante sur l'écran des préférences
		for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++) {
			initSummary(getPreferenceScreen().getPreference(i));
		}
		
		Preference button = (Preference)findPreference("btn_generate");
		button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference arg0) { 
            	try {
					generate();
					Toast.makeText(getApplicationContext(), "Levels extracted successfully", 
							   Toast.LENGTH_SHORT).show();
				} catch (FileNotFoundException e) {
					Toast.makeText(getApplicationContext(), "Cannot extract levels !", 
							   Toast.LENGTH_SHORT).show();
				}
                return true;
            }
        });
	}

	@Override
	protected void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}
	
	public List<String> getSaveGames () {
		List<String> savegames = new ArrayList<String>();
		try {
			List<String> l = ExecuteAsRootBase.execute("ls "+BonusActivity.general_filePath+BonusActivity.general_fileName, true);
			if(l == null || l.size() == 0)
					throw new FileNotFoundException("Nope ! Cannot find results of ls !");
			
			for (String savegame : l)
				savegames.add(savegame.replace(BonusActivity.general_filePath, ""));
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return savegames;
	}

	public void generate() throws FileNotFoundException {
		List<String> l = ExecuteAsRootBase.execute("ls /data/app/"
				+ getString(R.string.apk_candy_crush) + "*", true);
		if (l.size() == 0)
			throw new FileNotFoundException(
					"Nope ! Cannot find results of ls !");
		String apk = l.get(l.size() - 1);

		ExecuteAsRootBase.execute("unzip " + apk + " " + levels_path
				+ " -d /storage/emulated/legacy/"
				+ getString(R.string.dir_backup));
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
		}
		ExecuteAsRootBase.close();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			// TODO: If Settings has multiple levels, Up should navigate up
			// that hierarchy.
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		updatePreferences(findPreference(key));
	}

	private void initSummary(Preference p) {
		if (p instanceof PreferenceCategory) {
			PreferenceCategory cat = (PreferenceCategory) p;
			for (int i = 0; i < cat.getPreferenceCount(); i++) {
				initSummary(cat.getPreference(i));
			}
		} else if (p.getKey().equals("savegame_list")) {
			ArrayList<String> savegames = (ArrayList<String>) getSaveGames();
			if(savegames.size() > 0)
			{
				ListPreference p2 = (ListPreference)p;
				CharSequence[] values = savegames.toArray(new String[savegames.size()]);
				p2.setEntries(values);
				p2.setEntryValues(values);
			}
		} else {
			updatePreferences(p);
		}
	}

	private void updatePreferences(Preference p) {
		if (p instanceof EditTextPreference) {
			EditTextPreference editTextPref = (EditTextPreference) p;
			p.setSummary(editTextPref.getText());
		}
		
	}
}
