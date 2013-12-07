package info.mischka.babysfirst;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.CheckBox;
import android.widget.EditText;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddEditScrapbookActivity extends ActionBarActivity {

    ScrapbookDbHelper mScrapbookDbHelper;
    String id, date, time, title, comments;
    String username;

    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_item);
        mScrapbookDbHelper = new ScrapbookDbHelper(this);

        /*
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        */
        Intent intent = getIntent();
        username = intent.getStringExtra(LoginActivity.LOGGED_IN_USER);
        if(intent.getStringExtra(ScheduleScrapbookActivity.ADD_EDIT_TYPE) != null){
            if(intent.getStringExtra(ScheduleScrapbookActivity.ADD_EDIT_TYPE).equals("edit")){
                id = intent.getStringExtra(ScheduleScrapbookActivity.EVENT_ID);
                date = intent.getStringExtra(ScheduleScrapbookActivity.EVENT_DATE);
                time = intent.getStringExtra(ScheduleScrapbookActivity.EVENT_TIME);
                title = intent.getStringExtra(ScheduleScrapbookActivity.EVENT_TITLE);
                comments = intent.getStringExtra(ScheduleScrapbookActivity.EVENT_COMMENTS);
                EditText enteredTitle = (EditText)findViewById(R.id.enteredTitle);
                EditText enteredComments = (EditText)findViewById(R.id.enteredComments);
                enteredTitle.setText(title);
                enteredComments.setText(comments);


            }
            else if(intent.getStringExtra(ScheduleScrapbookActivity.ADD_EDIT_TYPE).equals("add")){
                findViewById(R.id.deleteButton).setVisibility(View.GONE);
                findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        saveScrapbookItem(view);
                    }
                });
            }
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_edit_scrapbook, menu);
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
    public void onResume(){
        super.onResume();

        //Intent intent = getIntent();
        //if()

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
            View rootView = inflater.inflate(R.layout.fragment_add_edit_scrapbook, container, false);
            return rootView;
        }
    }

    public void saveScrapbookItem(View view){
        boolean recurring;
        //String filename = "schedule";
        //FileOutputStream fos;
        Calendar cal = Calendar.getInstance();

        date = cal.get(Calendar.MONTH)+"/"+cal.get(Calendar.DATE)+"/"+cal.get(Calendar.YEAR);
        time = cal.get(Calendar.HOUR)+":"+cal.get(Calendar.MINUTE)+" "+cal.get(Calendar.AM_PM);
        title = ((EditText)findViewById(R.id.enteredTitle)).getText().toString();
        comments = ((EditText)findViewById(R.id.enteredComments)).getText().toString();


        if(title.equals("") || comments.equals("")){
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Please enter all fields");
            //alertDialog.setButton(1, "OK", )
            alertDialog.show();
            return;

        }

        SQLiteDatabase db = mScrapbookDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("username", username);
        values.put("time", time);
        values.put("title", title);
        values.put("comments", comments);
        db.insert("scrapbook", null, values);
        db.close();
        finish();

    }

    public void cancelItemClick(View view){
        finish();

    }

    public void editScrapbookItem(View view){
        SQLiteDatabase db = mScrapbookDbHelper.getWritableDatabase();
        title = ((EditText)findViewById(R.id.enteredTitle)).getText().toString();
        comments = ((EditText)findViewById(R.id.enteredComments)).getText().toString();

        String selection = "id = " + id;
        System.out.println(id);

        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("username", username);
        values.put("comments", comments);
        db.update("scrapbook", values, selection, null);
        db.close();
        finish();


    }

    public void deleteScrapbookItem(View view){
        SQLiteDatabase db = mScrapbookDbHelper.getWritableDatabase();
        String selection = "id = " + id;
        db.delete("scrapbook", selection, null);
        db.close();
        finish();

    }

    public void addPicture(View view){
        PackageManager packageManager = getPackageManager();
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> list = packageManager.queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if(list.size() > 0){
            String imageFileName = JPEG_FILE_PREFIX + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + "_" + JPEG_FILE_SUFFIX;
            File pictureFile = new File(getFilesDir() + "images/" + imageFileName);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(pictureFile));
            startActivityForResult(takePictureIntent, 0);


        }

    }

}
