package info.mischka.babysfirst;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;


public class LoginActivity extends Activity {

    public static final String LOGGED_IN_USER = "info.mischka.babysfirst.USERNAME";

    private LoginDbHelper mLoginDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        mLoginDbHelper = new LoginDbHelper(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    public void login(View v){
        String username = ((EditText)findViewById(R.id.username)).getText().toString();
        String password = ((EditText)findViewById(R.id.password)).getText().toString();

        if(username == null || username.equals("")){
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Please enter a valid username");
            //alertDialog.setButton(1, "OK", )
            alertDialog.show();
            return;
        }
        else if(password == null || password.equals("")){
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Please enter a valid password");
            //alertDialog.setButton(1, "OK", )
            alertDialog.show();
            return;
        }
        else{
            SQLiteDatabase db = mLoginDbHelper.getReadableDatabase();
            String[] columns = {"username", "password"};
            String selection = "username='" + username + "' AND password='" + password + "'";
            Cursor c = db.query("login", columns, selection, null, null, null, null);
            if(c.getCount() == 0){
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("No user exists with the entered credentials");
                //alertDialog.setButton(1, "OK", )
                alertDialog.show();
                return;
            }
            else if(c.getCount() == 1){
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(LOGGED_IN_USER, username);
                startActivity(intent);

            }

        }

    }

    public void register(View v){
        String username = ((EditText)findViewById(R.id.username)).getText().toString();
        String password = ((EditText)findViewById(R.id.password)).getText().toString();

        SQLiteDatabase db = mLoginDbHelper.getWritableDatabase();
        String[] columns = {"username", "password"};
        String selection = "username='"+username+"'";
        Cursor c = db.query("login", columns, selection, null, null, null, null);

        if(username == null || username.equals("")){
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Please enter a valid username");
            //alertDialog.setButton(1, "OK", )
            alertDialog.show();
            return;
        }
        else if(password == null || password.equals("")){
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Please enter a valid password");
            //alertDialog.setButton(1, "OK", )
            alertDialog.show();
            return;
        }
        if(c.getCount() != 0){
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("User already exists with the entered username");
            //alertDialog.setButton(1, "OK", )
            alertDialog.show();
            return;
        }
        else{
            
            ContentValues values = new ContentValues();
            values.put("username", username);
            values.put("password", password);
            db.insertOrThrow("login", null, values);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(LOGGED_IN_USER, username);
            startActivity(intent);
        }

    }

}
