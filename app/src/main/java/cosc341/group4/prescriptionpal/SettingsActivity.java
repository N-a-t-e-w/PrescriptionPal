package cosc341.group4.prescriptionpal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
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
