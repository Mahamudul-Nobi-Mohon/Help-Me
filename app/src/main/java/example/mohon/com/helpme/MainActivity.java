package example.mohon.com.helpme;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    public Button butLogin, btnSignup;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = openOrCreateDatabase("helpme1", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS contactNumberSaver2(id INTEGER PRIMARY KEY AUTOINCREMENT, phonenumber Text ,name TEXT, about TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_setSms(sms_id INTEGER PRIMARY KEY AUTOINCREMENT, sms_text TEXT)");

//        btnSignup = (Button) findViewById(R.id.buttonSignUp);
//        btnSignup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent toy = new Intent(MainActivity.this, signup.class);
//                startActivity(toy);
//            }
//        });

    }



    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Do you want to exit ?");
        builder.setCancelable(true);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void exitProgram(View v){
        onBackPressed();
    }

    public void onClick2(View view) {
        Intent toy = new Intent(MainActivity.this, ContactNumberSaver.class);
        toy.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toy);
    }

    public void buttonSmsSend(View view) {
        Intent toy = new Intent(MainActivity.this, SmsSend.class);
        toy.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toy);
    }
    public void buttonSetSms(View view) {
        Intent toy = new Intent(MainActivity.this, SmsSetActivity.class);
        toy.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toy);
    }



}
