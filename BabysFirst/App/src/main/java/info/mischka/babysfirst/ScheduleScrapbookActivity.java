package info.mischka.babysfirst;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ScheduleScrapbookActivity extends ActionBarActivity implements ActionBar.TabListener {

    ScheduleDbHelper mScheduleDbHelper;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_scrapbook);
        mScheduleDbHelper = new ScheduleDbHelper(this);


        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.schedule_scrapbook, menu);
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

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "Schedule".toUpperCase(l);
                case 1:
                    return "Scrapbook".toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView;
            rootView = inflater.inflate(R.layout.fragment_schedule_scrapbook, container, false);
            if(getArguments().getInt(ARG_SECTION_NUMBER) == 1){
                rootView = inflater.inflate(R.layout.fragment_schedule, container, false);
            }
            else{
                rootView = inflater.inflate(R.layout.fragment_scrapbook, container, false);
            }

            return rootView;
        }
    }

    public void addEventClick(View view)
    {
        Button btn = (Button)findViewById(R.id.newScheduleEntryButton);
        setContentView(R.layout.fragment_event_new);
    }

    public void cancelEventClick(View view)
    {
        Button btn = (Button)findViewById(R.id.cancelButton);
        setContentView(R.layout.fragment_schedule);
        loadSchedule();
    }

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
            EditText myDate = (EditText) findViewById(R.id.enteredDate);
            myDate.setText("" + month + "/" + day + "/" + year + "");
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

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

            String ampm = "AM";
            if (hourOfDay > 12)
            {
                ampm = "PM";
                hourOfDay = hourOfDay - 12;
            }
            EditText myTime = (EditText) findViewById(R.id.enteredTime);
            myTime.setText("" + hourOfDay + ":" + minute + " " + ampm + "");
        }
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void addEventDescription(View view){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if(prev != null){
            ft.remove(prev);

        }
        ft.addToBackStack(null);

        DialogFragment newFragment = new DescriptionDialogFragment();
        newFragment.show(ft, "dialog");
        EditText eventDescription = (EditText)view.findViewById(R.id.eventDescription);
        //eventDescription.setText(((DescriptionDialogFragment)newFragment).getDescription().toString());

    }

    public class DescriptionDialogFragment extends DialogFragment{
        private String eventDescription;

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            View v = inflater.inflate(R.layout.fragment_dialog, container, false);
            EditText et = (EditText)v.findViewById(R.id.newEventDescriptionDialog);

            Button button = (Button)v.findViewById(R.id.newEventDescriptionDialogButton);

            return v;


        }

        public void saveEventDescription(View view){
            EditText et = (EditText)view.findViewById(R.id.newEventDescriptionDialog);
            eventDescription = et.getText().toString();



        }

        String getDescription(){
            return eventDescription;
        }



    }

    public void saveScheduleEvent(View view){
        String date, time, description;
        boolean recurring;
        //String filename = "schedule";
        //FileOutputStream fos;


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
        values.put("time", time);
        values.put("description", description);
        values.put("recurring", Boolean.toString(recurring));
        db.insert("schedule", null, values);
        db.close();
        setContentView(R.layout.fragment_schedule);
        loadSchedule();

    }

    public void editScheduleEvent(View view){
        SQLiteDatabase db = mScheduleDbHelper.getReadableDatabase();
        String id = ((EditText)findViewById(R.id.eventEditId)).getText().toString();
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
        setContentView(R.layout.fragment_schedule);
        loadSchedule();

    }

    public void deleteScheduleEvent(View view){
        SQLiteDatabase db = mScheduleDbHelper.getReadableDatabase();
        String id = ((TextView)findViewById(R.id.eventEditId)).getText().toString();
        String selection = "id = " + id;
        db.delete("schedule", selection, null);
        setContentView(R.layout.fragment_schedule);
        loadSchedule();

    }

    @Override
    public void onResume(){
        super.onResume();
        //SQLiteDatabase db = mScheduleDbHelper.getReadableDatabase();
        setContentView(R.layout.fragment_schedule);
        loadSchedule();


    }


    public void loadSchedule(){
        SQLiteDatabase db = mScheduleDbHelper.getReadableDatabase();
        ArrayList<String> scheduleList = new ArrayList<String>();
        String[] columns = {"id", "date", "time", "description", "recurring"};
        final Cursor c = db.query("schedule", columns, null, null, null, null, null);
        c.moveToFirst();
        if(c.getCount() == 0)
            return;
        for(int i = 0; i < c.getCount(); i++){
            scheduleList.add(c.getString(c.getColumnIndexOrThrow("description")));
            if(!c.isLast())
                c.moveToNext();
        }
        String desc = c.getString(c.getColumnIndexOrThrow("description"));


        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, scheduleList);
        ListView listView = (ListView)findViewById(R.id.scheduleList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setContentView(R.layout.fragment_event_edit);
                c.moveToPosition(i);
                int id = c.getInt(c.getColumnIndexOrThrow("id"));
                String date = c.getString(c.getColumnIndexOrThrow("date"));
                String time = c.getString(c.getColumnIndexOrThrow("time"));
                String description = c.getString(c.getColumnIndexOrThrow("description"));
                String recurringString = c.getString(c.getColumnIndexOrThrow("recurring"));
                boolean recurring;
                if(recurringString.equals("true"))
                    recurring = true;
                else
                    recurring = false;
                EditText eventEditId = (EditText)findViewById(R.id.eventEditId);
                EditText enteredDate = (EditText)findViewById(R.id.enteredDate);
                EditText enteredTime = (EditText)findViewById(R.id.enteredTime);
                EditText eventDescription = (EditText)findViewById(R.id.eventDescription);
                CheckBox checkBox = (CheckBox)findViewById(R.id.checkBox);
                eventEditId.setText(Integer.toString(id));
                enteredDate.setText(date);
                enteredTime.setText(time);
                eventDescription.setText(description);
                checkBox.setChecked(recurring);

            }
        });
        listView.setAdapter(adapter);

    }

    //public static abstract class ScheduleEntry

    public class ScheduleDbHelper extends SQLiteOpenHelper{
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "BabysFirst.db";

        public ScheduleDbHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db){
            db.execSQL("CREATE TABLE schedule (id INTEGER AUTO_INCREMENT PRIMARY KEY, date TEXT, time TEXT, description TEXT, recurring TEXT);");

        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.execSQL("DROP TABLE IF EXISTS schedule");
            onCreate(db);

        }
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){

        }
    }



}
