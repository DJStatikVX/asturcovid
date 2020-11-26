package es.uniovi.eii.asturcovid;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import es.uniovi.eii.asturcovid.R;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // en la ActionBar activar una flecha para volver hacia atrás
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

/*        sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        name = sharedPreferences.getString("keyCategoria", "");
        Log.i("Categoría", name);

        MainRecycler.filtrocategoria = name;*/
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }
}