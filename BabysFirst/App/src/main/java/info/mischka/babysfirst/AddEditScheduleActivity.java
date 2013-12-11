package info.mischka.babysfirst;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

public class AddEditScheduleActivity extends ActionBarActivity {

    ScheduleDbHelper mScheduleDbHelper;
    String id, date, time, description;
    boolean recurring;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_event);
        mScheduleDbHelper = new ScheduleDbHelper(this);

        Intent intent = getIntent();
        username = intent.getStringExtra(LoginActivity.LOGGED_IN_USER);

        if(intent.getStringExtra(ScheduleScrapbookActivity.ADD_EDIT_TYPE).equals("edit")){
            //grabs info for event if user is brought here to edit the event
            id = intent.getStringExtra(ScheduleScrapbookActivity.EVENT_ID);
            date = intent.getStringExtra(ScheduleScrapbookActivity.EVENT_DATE);
            time = intent.getStringExtra(ScheduleScrapbookActivity.EVENT_TIME);
            description = intent.getStringExtra(ScheduleScrapbookActivity.EVENT_DESC);
            recurring = intent.getBooleanExtra(ScheduleScrapbookActivity.EVENT_RECURRING, false);
            EditText enteredDate = (EditText)findViewById(R.id.enteredDate);
            EditText enteredTime = (EditText)findViewById(R.id.enteredTime);
            EditText eventDescription = (EditText)findViewById(R.id.eventDescription);
            CheckBox checkBox = (CheckBox)findViewById(R.id.checkBox);
            enteredDate.setText(date);
            enteredTime.setText(time);
            eventDescription.setText(description);
            checkBox.setChecked(recurring);
        }
        else if(intent.getStringExtra(ScheduleScrapbookActivity.ADD_EDIT_TYPE).equals("add")){
            //changes UI to allow adding of new entry instead of editing of existing one
            findViewById(R.id.deleteEventButton).setVisibility(View.GONE);
            Button myDateButton = (Button) findViewById(R.id.dateButton);
            Button myTimeButton = (Button) findViewById(R.id.timeButton);
            EditText myDate = (EditText) findViewById(R.id.enteredDate);
            EditText myTime = (EditText) findViewById(R.id.enteredTime);
            Button saveToScrapbookButton = (Button)findViewById(R.id.saveToScrapbookButton);
            saveToScrapbookButton.setVisibility(View.GONE);
            myDateButton.setText("Set Date");
            myTimeButton.setText("Set Time");
            myDate.setVisibility(View.INVISIBLE);
            myTime.setVisibility(View.INVISIBLE);
            findViewById(R.id.saveEventButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveScheduleEvent(view);
                }
            });
        }







    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_edit_schedule, menu);
        getActionBar().setDisplayHomeAsUpEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_add_edit_schedule, container, false);
            return rootView;
        }
    }

    public void saveScheduleEvent(View view){
        //creates a new event and saves it to the database
        String date, time, description;
        boolean recurring;

        date = ((EditText)findViewById(R.id.enteredDate)).getText().toString();
        time = ((EditText)findViewById(R.id.enteredTime)).getText().toString();
        description = ((EditText)findViewById(R.id.eventDescription)).getText().toString();

        recurring = ((CheckBox)findViewById(R.id.checkBox)).isChecked();

        if(date.equals("") || time.equals("") || description.equals("")){
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Please enter all fields");
            //alertDialog.setButton(1, "OK", )
            alertDialog.show();
            return;

        }

        SQLiteDatabase db = mScheduleDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("username", username);
        values.put("time", time);
        values.put("description", description);
        values.put("recurring", Boolean.toString(recurring));
        db.insert("schedule", null, values);
        db.close();
        finish();

    }

    public void cancelEventClick(View view){
        //exits the page without committing any modified or entered data
        finish();
    }

    //Dialog Fragment for the Date Picker
    public class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            //Display Date in EditTxtField
            //Change Date Button Text
            EditText myDate = (EditText) findViewById(R.id.enteredDate);
            Button myButton = (Button) findViewById(R.id.dateButton);
            findViewById(R.id.enteredDate).setVisibility(View.VISIBLE);
            String monthString = Integer.toString(month), dayString = Integer.toString(day), yearString = Integer.toString(year);
            if(monthString.length() < 2)
                monthString = "0"+monthString;
            if(dayString.length() < 2)
                dayString = "0"+dayString;
            myDate.setText("" + monthString + "/" + dayString + "/" + year + "");
            myButton.setText("Change Date");
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    //Dialog Fragment for Time Picker
    public class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Display Time in EditTimeField
            // Add AM & PM to Display
            String ampm = "AM";
            if (hourOfDay > 12)
            {
                ampm = "PM";
                hourOfDay = hourOfDay - 12;
            }

            //Add a '0' to single digit minutes and hours
            String modMinute = Integer.toString(minute);
            int length = String.valueOf(minute).length();

            if (length < 2)
            {
                modMinute = ("0" + modMinute);
            }
            String hourString = Integer.toString(hourOfDay);
            if(hourString.length() < 2)
               hourString = "0"+hourString;
            EditText myTime = (EditText) findViewById(R.id.enteredTime);
            Button myButton = (Button) findViewById(R.id.timeButton);
            findViewById(R.id.enteredTime).setVisibility(View.VISIBLE);
            myTime.setText("" + hourString + ":" + modMinute + " " + ampm + "");
            myButton.setText("Change Time");

        }
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void editScheduleEvent(View view){
        //commit any changes made to an existing event to the database
        SQLiteDatabase db = mScheduleDbHelper.getWritableDatabase();
        String date = ((EditText)findViewById(R.id.enteredDate)).getText().toString();
        String time = ((EditText)findViewById(R.id.enteredTime)).getText().toString();
        String description = ((EditText)findViewById(R.id.eventDescription)).getText().toString();
        boolean recurring = ((CheckBox)findViewById(R.id.checkBox)).isChecked();

        String selection = "id = " + id;

        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("time", time);
        values.put("description", description);
        values.put("recurring", Boolean.toString(recurring));
        db.update("schedule", values, selection, null);
        db.close();
        finish();

    }

    public void deleteScheduleEvent(View view){
        //deletes the current event from the database
        SQLiteDatabase db = mScheduleDbHelper.getWritableDatabase();
        String selection = "id = " + id;
        db.delete("schedule", selection, null);
        db.close();
        finish();

    }

    public void addToScrapbook(View view){
        //takes information from current event and sends it to the scrapbook entry page so the user can
        //save it in their scrapbook as well
        Intent intent = new Intent(this, AddEditScrapbookActivity.class);
        intent.putExtra(ScheduleScrapbookActivity.EVENT_DATE, date);
        intent.putExtra(ScheduleScrapbookActivity.EVENT_TIME, time);
        intent.putExtra(ScheduleScrapbookActivity.EVENT_COMMENTS, description);
        intent.putExtra(LoginActivity.LOGGED_IN_USER, username);
        intent.putExtra(ScheduleScrapbookActivity.ADD_EDIT_TYPE, "add_from_schedule");
        intent.putExtra(ScheduleScrapbookActivity.EVENT_ID, id);
        startActivity(intent);


    }

    public void logout(MenuItem item){
        //logs user out
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }


}
