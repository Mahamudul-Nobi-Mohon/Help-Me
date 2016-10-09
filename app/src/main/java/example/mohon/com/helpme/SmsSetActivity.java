package example.mohon.com.helpme;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SmsSetActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    EditText TextSms;
    Button ButtonSmsSave;
    ListView ListMessages;
    SQLiteDatabase db ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_set);

        TextSms = (EditText) findViewById(R.id.editTextSetSms);
        ButtonSmsSave = (Button) findViewById(R.id.buttonSmsSave);

        ButtonSmsSave.setOnClickListener(this);

        //select all sms
        ListMessages = (ListView)findViewById(R.id.listMessages);
        ArrayList<String> products = new ArrayList<String>();

           db = openOrCreateDatabase("helpme1",MODE_PRIVATE,null);
//        db = openOrCreateDatabase("helpme1",MODE_PRIVATE,null);
//        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_setSms(sms_id INTEGER PRIMARY KEY AUTOINCREMENT, sms_text TEXT)");

        Cursor result = db.rawQuery("SELECT sms_id,sms_text from tbl_setSms order by sms_id desc",null);
        result.moveToFirst();
        String text = "";
        String smsid = "";
        String smstext = "";
        while (result.isAfterLast() != true){
            // text += " "+result.getString(result.getColumnIndex("PRO_NAME"));
            smsid = result.getString(result.getColumnIndex("sms_id"));
            smstext = result.getString(result.getColumnIndex("sms_text"));
            products.add(smsid+ " , "+smstext);
            result.moveToNext();
        }
        //Toast.makeText(this,text,Toast.LENGTH_SHORT).show();




        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,products);
        ListMessages.setAdapter(adapter1);
        ListMessages.setOnItemClickListener(this);



        //end select all sms

    }

    public void gotomainpage(){
        Intent toy = new Intent(SmsSetActivity.this, MainActivity.class);
        toy.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toy);
    }

    @Override
    public void onBackPressed(){
        finish();
        gotomainpage();
    }


    @Override
    public void onClick(View view) {

        String textSms = TextSms.getText().toString();
        // db = openOrCreateDatabase("helpme",MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_setSms(sms_id INTEGER PRIMARY KEY AUTOINCREMENT, sms_text TEXT)");
        String checksms = TextSms.getText().toString();

        if(TextUtils.isEmpty(checksms)) {
            TextSms.setError("Please type your SMS.");
            return;
        }
        else {
        db.execSQL("INSERT INTO tbl_setSms(sms_text) VALUES ('" + textSms + "')");

        Intent in = new Intent(this, MainActivity.class);
        startActivity(in);
        Toast.makeText(this, "Inserted Successfully", Toast.LENGTH_LONG).show();
        }
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View clickedView, int pos, long id) {
        TextView tv1 = (TextView)clickedView;

        int commaIndex = tv1.getText().toString().indexOf(",");
        String sms_id = tv1.getText().toString().substring(0, commaIndex);
        Intent in = new Intent(SmsSetActivity.this, SmsEditActivity.class);
        in.putExtra("sms_id", sms_id);
        //Toast.makeText(this,sms_id,Toast.LENGTH_SHORT).show();
        startActivity(in);
    }
}
