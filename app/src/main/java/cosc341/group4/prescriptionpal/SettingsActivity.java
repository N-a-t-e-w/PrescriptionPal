package cosc341.group4.prescriptionpal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private static String sizeButton = "Medium";
    private static boolean silent = true;
    private static boolean notif = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        RadioButton small = findViewById(R.id.settings_small_radio);
        RadioButton med = findViewById(R.id.settings_medium_radio);
        RadioButton large = findViewById(R.id.settings_large_radio);

        switch (sizeButton){
            case "Small" :
                small.setChecked(true);
                break;
            case "Medium":
                med.setChecked(true);
                break;
            case "Large":
                large.setChecked(true);
                break;
        }

        Switch silentSwitch = findViewById(R.id.notification_silent_switch);
        silentSwitch.setChecked(silent);
        Switch notifSwitch = findViewById(R.id.notifications_off_switch);
        notifSwitch.setChecked(notif);

        Switch caretakerSwitch = findViewById(R.id.caretaker_switch);
        caretakerSwitch.setChecked(HomepageActivity.CARETAKER_MODE);

    }

    @Override
    protected void onStop() {

        RadioButton small = findViewById(R.id.settings_small_radio);
        RadioButton med = findViewById(R.id.settings_medium_radio);

        if (small.isChecked()) sizeButton = "Small";
        else if (med.isChecked()) sizeButton = "Medium";
        else sizeButton = "Large";

        Switch silentSwitch = findViewById(R.id.notification_silent_switch);
        silent = silentSwitch.isChecked();

        Switch notifSwitch = findViewById(R.id.notifications_off_switch);
        notif = notifSwitch.isChecked();

        super.onStop();

    }

    public void CaretakerMode(View view){
        Switch caretakerSwitch = findViewById(R.id.caretaker_switch);

        //Update visibility of all elements to show the warning message and buttons
        LinearLayout linearLayout = findViewById(R.id.settings_main_layout);
        linearLayout.setVisibility(View.INVISIBLE);

        TextView textView = findViewById(R.id.settings_rusure_textview);

        //Update textview text to match whether caretaker mode is being turned on/off
        if(caretakerSwitch.isChecked())
            textView.setText(R.string.rusure);
        else textView.setText(R.string.rusureoff);

        textView.setVisibility(View.VISIBLE);

        Button yes = findViewById(R.id.rusure_yes);
        yes.setVisibility(View.VISIBLE);

        Button no = findViewById(R.id.rusure_no);
        no.setVisibility(View.VISIBLE);

    }

    public void No(View view){
        Switch caretakerSwitch = findViewById(R.id.caretaker_switch);
        //change the switch back to it's previous value
        caretakerSwitch.setChecked(!caretakerSwitch.isChecked());
        setVisible();
    }

    public void Yes(View view){
        setVisible();
        //Invert current value of caretaker mode
        HomepageActivity.CARETAKER_MODE = !HomepageActivity.CARETAKER_MODE;
    }

    //Update the visibility to show the main settings page again
    private void setVisible(){
        LinearLayout linearLayout = findViewById(R.id.settings_main_layout);
        linearLayout.setVisibility(View.VISIBLE);

        TextView textView = findViewById(R.id.settings_rusure_textview);
        textView.setVisibility(View.INVISIBLE);

        Button yes = findViewById(R.id.rusure_yes);
        yes.setVisibility(View.INVISIBLE);

        Button no = findViewById(R.id.rusure_no);
        no.setVisibility(View.INVISIBLE);
    }

    public void NotAvailable(View view){
        Toast toast = Toast.makeText( getApplicationContext(),"This setting is not implemented yet", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void GoHome(View view){
        finish();
    }
}
