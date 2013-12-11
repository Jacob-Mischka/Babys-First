package info.mischka.babysfirst;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends ActionBarActivity {

    public final static String SCHEDULE_SCRAPBOOK_TYPE = "info.mischka.babysfirst.TYPE";
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        username = intent.getStringExtra(LoginActivity.LOGGED_IN_USER);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    public void openSchedule(View view){
        //opens schedule/scrapbook activity, displaying the schedule initially
        Intent intent = new Intent(this, ScheduleScrapbookActivity.class);
        intent.putExtra(SCHEDULE_SCRAPBOOK_TYPE, "schedule");
        intent.putExtra(LoginActivity.LOGGED_IN_USER, username);
        startActivity(intent);


    }

    public void openScrapbook(View view){
        //opens schedule/scrapbook activity, displaying scrapbook initially
        Intent intent = new Intent(this, ScheduleScrapbookActivity.class);
        intent.putExtra(SCHEDULE_SCRAPBOOK_TYPE, "scrapbook");
        intent.putExtra(LoginActivity.LOGGED_IN_USER, username);
        startActivity(intent);
    }

    public void logout(View view){
        //logs user out when button is pressed
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    public void logout(MenuItem item){
        //logs user out when menu item is selected
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

}
