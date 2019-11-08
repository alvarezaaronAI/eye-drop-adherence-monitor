package com.alvarezaaronai.edam.Dots;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.alvarezaaronai.edam.R;

public class Settings extends AppCompatActivity {
    //Log Cats
    final String TAG = "Settigs_Info";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

    }

    public void colorRadioClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_blue:
                if (checked) {
                    // Blue is checked
                    //Todo: Switch Blue
                    Log.i(TAG, "Color : Blue");
                    Toast.makeText(this, "Blue", Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.radio_pastel:
                if (checked) {
                    // Pastel is Checked
                    //Todo: Switch Pastel
                    Log.i(TAG, "Color : Pastel");
                    Toast.makeText(this, "Pastel", Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.radio_brights:
                if (checked) {
                    // Brights is Checked
                    //Todo: Switch Brights
                    Log.i(TAG, "Color : Brights");
                    Toast.makeText(this, "Brights", Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.radio_daylights:
                if (checked) {
                    // Daylights is Checked
                    //Todo: Switch Daylights
                    Log.i(TAG, "Color : Daylights");
                    Toast.makeText(this, "Daylights", Toast.LENGTH_SHORT).show();
                    break;
                }
        }

    }

    //Add Functionality

    //Todo : Create a method that shows a Toast Fast
}
