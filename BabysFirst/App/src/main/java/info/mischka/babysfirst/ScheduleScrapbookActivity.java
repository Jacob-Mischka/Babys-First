package info.mischka.babysfirst;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ScheduleScrapbookActivity extends ActionBarActivity implements ActionBar.TabListener {

    public final static String EVENT_ID = "info.mischka.babysfirst.ID";
    public final static String ADD_EDIT_TYPE = "info.mischka.babysfirst.TYPE";
    public final static String EVENT_DATE = "info.mischka.babysfirst.DATE";
    public final static String EVENT_TIME = "info.mischka.babysfirst.TIME";
    public final static String EVENT_DESC = "info.mischka.babysfirst.DESC";
    public final static String EVENT_RECURRING = "info.mischka.babysfirst.RECURRING";
    public final static String EVENT_TITLE = "info.mischka.babysfirst.TITLE";
    public final static String EVENT_COMMENTS = "info.mischka.babysfirst.COMMENTS";

    ScheduleDbHelper mScheduleDbHelper;
    ScrapbookDbHelper mScrapbookDbHelper;

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
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_scrapbook);
        mScheduleDbHelper = new ScheduleDbHelper(this);
        mScrapbookDbHelper = new ScrapbookDbHelper(this);
        Intent intent = getIntent();
        String scheduleScrapbookType = intent.getStringExtra(MainActivity.SCHEDULE_SCRAPBOOK_TYPE);
        username = intent.getStringExtra(LoginActivity.LOGGED_IN_USER);
        System.out.println(username);


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
        if(scheduleScrapbookType.equals("scrapbook"))
            actionBar.getTabAt(1).select();
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
            if(position == 0)
                return new ScheduleFragment();
            else
                return new ScrapbookFragment();
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
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

    public class ScheduleFragment extends Fragment{
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){

            return inflater.inflate(R.layout.fragment_schedule, container, false);
        }

        @Override
        public void onResume(){
            super.onResume();
            loadSchedule(getView());
        }

    }

    public class ScrapbookFragment extends Fragment{
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){

            return inflater.inflate(R.layout.fragment_scrapbook, container, false);
        }

        @Override
        public void onResume(){
            super.onResume();
            loadScrapbook(getView());
        }

    }

    public void addEventClick(View view)
    {
        Intent intent = new Intent(this, AddEditScheduleActivity.class);
        intent.putExtra(ADD_EDIT_TYPE, "add");
        intent.putExtra(LoginActivity.LOGGED_IN_USER, username);
        startActivity(intent);
    }

    public void addItemClick(View view){
        Intent intent = new Intent(this, AddEditScrapbookActivity.class);
        intent.putExtra(ADD_EDIT_TYPE, "add");
        intent.putExtra(LoginActivity.LOGGED_IN_USER, username);
        startActivity(intent);

    }


    @Override
    public void onResume(){
        super.onResume();
        //setContentView(R.layout.fragment_schedule);
        //loadSchedule();


    }


    public void loadSchedule(View view){
        SQLiteDatabase db = mScheduleDbHelper.getReadableDatabase();
        ArrayList<String> scheduleList = new ArrayList<String>();
        String[] columns = {"id", "username", "date", "time", "description", "recurring"};
        String selection = "username='"+username+"'";
        final Cursor c = db.query("schedule", columns, selection, null, null, null, null);
        final Intent intent = new Intent(this, AddEditScheduleActivity.class);
        intent.putExtra(LoginActivity.LOGGED_IN_USER, username);
        c.moveToFirst();
        if(c.getCount() == 0){
            ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, scheduleList);
            ListView listView = (ListView)view.findViewById(R.id.scheduleList);
            listView.setAdapter(adapter);
            return;

        }
        for(int i = 0; i < c.getCount(); i++){
            scheduleList.add(c.getString(c.getColumnIndexOrThrow("description")));
            if(!c.isLast())
                c.moveToNext();
        }


        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, scheduleList);
        ListView listView = (ListView)view.findViewById(R.id.scheduleList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


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
                intent.putExtra(EVENT_ID, Integer.toString(id));
                intent.putExtra(ADD_EDIT_TYPE, "edit");
                intent.putExtra(EVENT_DATE, date);
                intent.putExtra(EVENT_TIME, time);
                intent.putExtra(EVENT_DESC, description);
                intent.putExtra(EVENT_RECURRING, recurring);
                startActivity(intent);

            }
        });
        db.close();
        listView.setAdapter(adapter);


    }

    public void loadScrapbook(View view){
        SQLiteDatabase db = mScrapbookDbHelper.getReadableDatabase();
        ArrayList<String> scrapbookList = new ArrayList<String>();
        String[] columns = {"id", "username", "date", "time", "title", "comments"};
        String selection = "username='"+username+"'";
        final Cursor c = db.query("scrapbook", columns, selection, null, null, null, null);
        final Intent intent = new Intent(this, AddEditScrapbookActivity.class);
        intent.putExtra(LoginActivity.LOGGED_IN_USER, username);
        c.moveToFirst();
        if(c.getCount() == 0){
            ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, scrapbookList);
            ListView listView = (ListView)view.findViewById(R.id.scrapbookList);
            listView.setAdapter(adapter);
            return;

        }
        for(int i = 0; i < c.getCount(); i++){
            scrapbookList.add(c.getString(c.getColumnIndexOrThrow("title")));
            if(!c.isLast())
                c.moveToNext();
        }


        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, scrapbookList);
        ListView listView = (ListView)view.findViewById(R.id.scrapbookList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                c.moveToPosition(i);
                int id = c.getInt(c.getColumnIndexOrThrow("id"));
                String date = c.getString(c.getColumnIndexOrThrow("date"));
                String time = c.getString(c.getColumnIndexOrThrow("time"));
                String title = c.getString(c.getColumnIndexOrThrow("title"));
                String comments = c.getString(c.getColumnIndexOrThrow("comments"));
                intent.putExtra(EVENT_ID, Integer.toString(id));
                intent.putExtra(ADD_EDIT_TYPE, "edit");
                intent.putExtra(EVENT_DATE, date);
                intent.putExtra(EVENT_TIME, time);
                intent.putExtra(EVENT_TITLE, title);
                intent.putExtra(EVENT_COMMENTS, comments);
                startActivity(intent);

            }
        });
        db.close();
        listView.setAdapter(adapter);


    }








}
