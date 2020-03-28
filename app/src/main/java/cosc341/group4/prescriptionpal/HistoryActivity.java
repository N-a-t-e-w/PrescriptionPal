package cosc341.group4.prescriptionpal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HistoryActivity extends AppCompatActivity {

    private CompactCalendarView compactCalendar;
    private SimpleDateFormat sdf = new SimpleDateFormat("MMMM - yyyy", Locale.getDefault());
    private TextView date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        date = findViewById(R.id.history_date_textview);

        setDate();

        compactCalendar = findViewById(R.id.history_compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                date.setText(sdf.format(firstDayOfNewMonth));
            }
        });

    }

    private void setDate(){
        //Format the date
        Date d = new Date();
        String monthYear = sdf.format(d);
        //Update the textview
        date.setText(monthYear);
    }


    public void goHome(View view){
        finish();
    }
}

