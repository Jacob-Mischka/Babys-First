package info.mischka.babysfirst;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
    public final static String EVENT_IMAGE = "info.mischka.babysfirst.IMAGE";
    public final static String EVENT_VIDEO = "info.mischka.babysfirst.VIDEO";

    ScheduleDbHelper mScheduleDbHelper;
    ScrapbookDbHelper mScrapbookDbHelper;
    ArrayAdapter<String> scheduleAdapter;
    ArrayAdapter<String> scrapbookAdapter;


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
        else if(id == R.id.home){
            finish();
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
            //sets titles for tabs
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
        //custom fragment that holds the list of schedule entries
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){

            return inflater.inflate(R.layout.fragment_schedule, container, false);
        }


        @Override
        public void onResume(){
            super.onResume();
            //reloads schedule when activity is resumed
            loadSchedule(getView());
        }

    }

    public class ScrapbookFragment extends Fragment{
        //custom fragment that holds the list of scrapbook entries
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){

            return inflater.inflate(R.layout.fragment_scrapbook, container, false);
        }


        @Override
        public void onResume(){
            super.onResume();
            //reloads scrapbook when activity is resumed
            loadScrapbook(getView());
        }

    }

    public void addEventClick(View view){
        //brings user to page to add a new schedule entry
        Intent intent = new Intent(this, AddEditScheduleActivity.class);
        intent.putExtra(ADD_EDIT_TYPE, "add");
        intent.putExtra(LoginActivity.LOGGED_IN_USER, username);
        startActivity(intent);
    }

    public void addItemClick(View view){
        //brings user to page to add a new scrapbook entry
        Intent intent = new Intent(this, AddEditScrapbookActivity.class);
        intent.putExtra(ADD_EDIT_TYPE, "add");
        intent.putExtra(LoginActivity.LOGGED_IN_USER, username);
        startActivity(intent);

    }


    @Override
    public void onResume(){
        super.onResume();

    }

    class ScheduleEntry{
        //class that represents a schedule entry
        String id, date, time, description, recurring;

        ScheduleEntry(String id, String date, String time, String description, String recurring){
            this.id = id; this.date = date; this.time = time; this.description = description; this.recurring = recurring;

        }

        @Override
        public String toString(){
            return description;

        }

    }

    class ScheduleArrayAdapter extends ArrayAdapter<ScheduleEntry>{
        //custom ArrayAdapter for schedule entries
        View view;
        public ScheduleArrayAdapter(Context context, List<ScheduleEntry> list){
            super(context, R.layout.schedule_row, list);
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent){
            view = convertView;
            if(view == null){
                LayoutInflater li = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = li.inflate(R.layout.schedule_row, null);
            }

            if(getItem(pos).description != null){
                TextView descView = (TextView)view.findViewById(R.id.rowEventDescription);
                descView.setText(getItem(pos).description);
            }
            if(getItem(pos).date != null){
                TextView dateView = (TextView)view.findViewById(R.id.rowEventDate);
                dateView.setText(getItem(pos).date);
            }



            return view;
        }

    }

    class ScrapbookEntry{
        //class that represents a scrapbook entry
        String id, date, time, title, comments, image, video;


        ScrapbookEntry(String id, String date, String time, String title, String comments, String image, String video){
            this.id = id; this.date = date; this.time = time; this.title = title; this.comments = comments; this.image = image;  this.video = video;

        }



    }

    class ScrapbookArrayAdapter extends ArrayAdapter<ScrapbookEntry>{
        //custom ArrayAdapter for scrapbook entries
        View view;

        public ScrapbookArrayAdapter(Context context, List<ScrapbookEntry> list){
            super(context, R.layout.scrapbook_row, list);


        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent){
            view = convertView;
            if(view == null){
                LayoutInflater li = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = li.inflate(R.layout.scrapbook_row, null);
            }

            if(getItem(pos).title != null){
                TextView textView = (TextView)view.findViewById(R.id.rowItemTitle);
                textView.setText(getItem(pos).title);
            }


            File imageFile = new File(getExternalFilesDir(null), getItem(pos).image);

            if(getItem(pos).image != null && imageFile.isFile()){
                //shows image if it exists
                ImageView imageView = (ImageView)view.findViewById(R.id.rowItemImage);
                imageView.setImageURI(new Uri.Builder().path(imageFile.toString()).build());
                imageView.setVisibility(View.VISIBLE);

            }

            return view;
        }

    }



    public void loadSchedule(View view){
        //rebuilds schedule ListView with new data retrieved from the database in chronological order (mostly)
        SQLiteDatabase db = mScheduleDbHelper.getReadableDatabase();
        ArrayList<String> scheduleList = new ArrayList<String>();
        final ArrayList<ScheduleEntry> scheduleEntryList = new ArrayList<ScheduleEntry>();
        String[] columns = {"id", "username", "date", "time", "description", "recurring"};
        String selection = "username='"+username+"'";
        String orderBy = "date, time";
        final Cursor c = db.query("schedule", columns, selection, null, null, null, orderBy);
        final Intent intent = new Intent(this, AddEditScheduleActivity.class);
        intent.putExtra(LoginActivity.LOGGED_IN_USER, username);
        c.moveToFirst();
        if(c.getCount() == 0){
            scheduleAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, scheduleList);
            ListView listView = (ListView)view.findViewById(R.id.scheduleList);
            listView.setAdapter(scheduleAdapter);
            return;

        }
        for(int i = 0; i < c.getCount(); i++){
            scheduleEntryList.add(new ScheduleEntry(Integer.toString(c.getInt(c.getColumnIndexOrThrow("id"))),c.getString(c.getColumnIndexOrThrow("date")),c.getString(c.getColumnIndexOrThrow("time")),
                    c.getString(c.getColumnIndexOrThrow("description")),c.getString(c.getColumnIndexOrThrow("recurring"))));
            if(!c.isLast())
                c.moveToNext();
        }

        ScheduleArrayAdapter adapter = new ScheduleArrayAdapter(this, scheduleEntryList);
        ListView listView = (ListView)view.findViewById(R.id.scheduleList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String id = scheduleEntryList.get(i).id;
                String date = scheduleEntryList.get(i).date;
                String time = scheduleEntryList.get(i).time;
                String description = scheduleEntryList.get(i).description;
                String recurringString = scheduleEntryList.get(i).recurring;



                boolean recurring;
                if(recurringString.equals("true"))
                    recurring = true;
                else
                    recurring = false;

                intent.putExtra(EVENT_ID, id);
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
        //rebuilds scrapbook ListView with new data retrieved from the database
        SQLiteDatabase db = mScrapbookDbHelper.getReadableDatabase();
        final ArrayList<ScrapbookEntry> scrapbookEntryList = new ArrayList<ScrapbookEntry>();
        String[] columns = {"id", "username", "date", "time", "title", "comments", "image", "video"};
        String selection = "username='"+username+"'";
        Cursor c = db.query("scrapbook", columns, selection, null, null, null, null);
        final Intent intent = new Intent(this, AddEditScrapbookActivity.class);
        intent.putExtra(LoginActivity.LOGGED_IN_USER, username);
        c.moveToFirst();
        if(c.getCount() == 0){
            ScrapbookArrayAdapter adapter = new ScrapbookArrayAdapter(this, scrapbookEntryList);
            ListView listView = (ListView)view.findViewById(R.id.scrapbookList);
            listView.setAdapter(adapter);
            return;

        }
        for(int i = 0; i < c.getCount(); i++){
            scrapbookEntryList.add(new ScrapbookEntry(Integer.toString(c.getInt(c.getColumnIndexOrThrow("id"))), c.getString(c.getColumnIndexOrThrow("date")),
                    c.getString(c.getColumnIndexOrThrow("time")), c.getString(c.getColumnIndexOrThrow("title")), c.getString(c.getColumnIndexOrThrow("comments")),
                    c.getString(c.getColumnIndexOrThrow("image")), c.getString(c.getColumnIndexOrThrow("video"))));
            if(!c.isLast())
                c.moveToNext();
        }


        ScrapbookArrayAdapter adapter = new ScrapbookArrayAdapter(this, scrapbookEntryList);
        ListView listView = (ListView)view.findViewById(R.id.scrapbookList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //when an entry in the scrapbook is selected, sends data to scrapbook edit page
                String id = scrapbookEntryList.get(i).id;
                String date = scrapbookEntryList.get(i).date;
                String time = scrapbookEntryList.get(i).time;
                String title = scrapbookEntryList.get(i).title;
                String comments = scrapbookEntryList.get(i).comments;
                String imageFileName = scrapbookEntryList.get(i).image;
                String videoFileName = scrapbookEntryList.get(i).video;

                intent.putExtra(EVENT_ID, id);
                intent.putExtra(ADD_EDIT_TYPE, "edit");
                intent.putExtra(EVENT_DATE, date);
                intent.putExtra(EVENT_TIME, time);
                intent.putExtra(EVENT_TITLE, title);
                intent.putExtra(EVENT_COMMENTS, comments);
                intent.putExtra(EVENT_IMAGE, imageFileName);
                intent.putExtra(EVENT_VIDEO, videoFileName);
                startActivity(intent);

            }
        });
        db.close();
        listView.setAdapter(adapter);


    }

    public void logout(MenuItem item){
        //logs user out
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }








}
