package cosc341.group4.prescriptionpal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class HomepageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
    }

    public void goToday(View view){
        Intent intent = new Intent(getApplicationContext(), TodayActivity.class);
        startActivity(intent);
    }

    public void goPrescriptions(View view){
        Intent intent = new Intent(getApplicationContext(), PrescriptionActivity.class);
        startActivity(intent);
    }
}
