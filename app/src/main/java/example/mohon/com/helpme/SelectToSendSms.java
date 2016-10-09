package example.mohon.com.helpme;

import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

public class SelectToSendSms extends ListActivity implements AdapterView.OnItemClickListener {

    ListView ListMessages;
    Button send, cancel;
    // Bundle extras = getIntent().getExtras();

    String id2 ;


    SQLiteDatabase db;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_to_send_sms);

        //Show all contact

        // ListMessages = (ListView) findViewById(R.id.listView);
        ListMessages= getListView();

        ArrayList<String> Mymessages = new ArrayList<String>();

        db = openOrCreateDatabase("helpme1", MODE_PRIVATE, null);
//        db.execSQL("CREATE TABLE IF NOT EXISTS contactNumberSaver2(id INTEGER PRIMARY KEY AUTOINCREMENT, phonenumber int ,name TEXT, about TEXT)");
//        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_setSms(sms_id INTEGER PRIMARY KEY AUTOINCREMENT, sms_text TEXT)");


        // Cursor result = db.rawQuery("SELECT * from contactNumberSaver",null);
        Cursor result = db.rawQuery("SELECT * from contactNumberSaver2", null);
        result.moveToFirst();
        String text = "";
        String phoneno = "";
        String name = "";
        String id = "0";
        String aboutperson = "";
        while (result.isAfterLast() != true) {
            // text += " "+result.getString(result.getColumnIndex("PRO_NAME"));
            id = result.getString(result.getColumnIndex("id"));
            phoneno = result.getString(result.getColumnIndex("phonenumber"));
            name = result.getString(result.getColumnIndex("name"));
            aboutperson = result.getString(result.getColumnIndex("about"));
            Mymessages.add(id + ",  " + phoneno + " , " + name + " , " + aboutperson);
            result.moveToNext();
            //Toast.makeText(this,"has value",Toast.LENGTH_SHORT).show();
        }
        // Toast.makeText(this,"blank",Toast.LENGTH_SHORT).show();




        //	listview.setChoiceMode(listview.CHOICE_MODE_NONE);
        //	listview.setChoiceMode(listview.CHOICE_MODE_SINGLE);
        ListMessages.setChoiceMode(ListMessages.CHOICE_MODE_MULTIPLE);

        ListMessages.setTextFilterEnabled(true);


        // ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Mymessages);
        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_checked,Mymessages));

        // ListMessages.setAdapter(adapter1);
        ListMessages.setOnItemClickListener(this);
        //end show all Contact


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            if(extras.getString("sms_id") != null){

                // Integer proId2 = Integer.parseInt(extras.getString("pro_id"));
                id2 =extras.getString("sms_id");

                //Toast.makeText(this,id2,Toast.LENGTH_SHORT).show();

            }
        }


    }


    public void gotomainpage(){
        Intent toy = new Intent(SelectToSendSms.this, MainActivity.class);
        toy.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toy);
    }

    @Override
    public void onBackPressed(){
        finish();
        gotomainpage();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, final View clickedView, int pos, long id) {

//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Warning !!");
//        builder.setMessage("Are you sure to send SMS ??");
//        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String message = "";
//                String message_phone = "";
//                TextView tv1 = (TextView) clickedView;
//
//                int commaIndex = tv1.getText().toString().indexOf(",");
//                String smsId = tv1.getText().toString().substring(0, commaIndex);
//
//
//                Cursor result = db.rawQuery("SELECT sms_text FROM tbl_setSms where sms_id = "+id2, null);
//                result.moveToFirst();
//                while (result.isAfterLast() != true) {
//                    // text += " "+result.getString(result.getColumnIndex("PRO_NAME"));
//                    message = result.getString(result.getColumnIndex("sms_text"));
//                    //products.add(ProId+ " , "+ProName);
//
//                    result.moveToNext();
//                }
//
//                Cursor result2 = db.rawQuery("SELECT phonenumber FROM contactNumberSaver2 where id = " + smsId, null);
//                result2.moveToFirst();
//                while (result2.isAfterLast() != true) {
//                    // text += " "+result.getString(result.getColumnIndex("PRO_NAME"));
//                    message_phone = result2.getString(result2.getColumnIndex("phonenumber"));
//                    //products.add(ProId+ " , "+ProName);
//
//
//
////
//
//
//                    if (message_phone.length() > 0 && message.length() > 0) {
//                        // call the sms manager
//                        PendingIntent pi = PendingIntent.getActivity(SelectToSendSms.this, 0,
//                                new Intent(SelectToSendSms.this, SmsSend.class), 0);
//                        SmsManager sms = SmsManager.getDefault();
//                        // this is the function that does all the magic
//                        sms.sendTextMessage(message_phone, null, message, pi, null);
//                        Toast.makeText(getBaseContext(), "SMS Send successfully.", Toast.LENGTH_SHORT).show();
//
//
//                    } else {
//                        // display message if text fields are empty
//                        Toast.makeText(getBaseContext(),  "SMS not Send !", Toast.LENGTH_SHORT).show();
//
//                    }
//                    Intent toy = new Intent(SelectToSendSms.this, SmsSend.class);
//                    startActivity(toy);
//                     result2.moveToNext();
//                }
//
//            }
//        });
//        builder.setNegativeButton("No", null);
//        builder.create();
//        builder.show();


    }

    public void mainpage(View view){
        Intent toy = new Intent(SelectToSendSms.this, MainActivity.class);
        startActivity(toy);
    }

    public void sendSms(View v){
        final long[] checkedIds2 = ListMessages.getCheckItemIds();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Warning !!");
        builder.setMessage("Are you sure to send SMS ??");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String message = "";
                Cursor result = db.rawQuery("SELECT sms_text FROM tbl_setSms where sms_id = " + id2, null);
                result.moveToFirst();
                while (result.isAfterLast() != true) {
                    // text += " "+result.getString(result.getColumnIndex("PRO_NAME"));
                    message = result.getString(result.getColumnIndex("sms_text"));
                    //products.add(ProId+ " , "+ProName);

                    result.moveToNext();
                }
               // Toast.makeText(SelectToSendSms.this,message,Toast.LENGTH_SHORT).show();


                for (int i = 0; i < checkedIds2.length; i++) {
                    // Toast.makeText(SelectToSendSms.this,1+checkedIds2[i]+"",Toast.LENGTH_SHORT).show();

                    //  String message_phone = "";
                    // TextView tv1 = (TextView) clickedView;

                    //  int commaIndex = tv1.getText().toString().indexOf(",");
                    // String smsId = tv1.getText().toString().substring(0, commaIndex);



//                        String details[] = new String[2];
                    String phoneNumber = "";
//                        String msg[] = new String[details.length];
//                        int n = 0;
//                        int perfect_send = 0;
                    Cursor result2 = db.rawQuery("SELECT phonenumber FROM contactNumberSaver2 where id = "+(1+checkedIds2[i]), null);
                    result2.moveToFirst();
                    while (result2.isAfterLast() != true) {
                        // text += " "+result.getString(result.getColumnIndex("PRO_NAME"));
                        phoneNumber = result2.getString(result2.getColumnIndex("phonenumber"));
                        //products.add(ProId+ " , "+ProName);
                        Toast.makeText(SelectToSendSms.this,phoneNumber,Toast.LENGTH_SHORT).show();





                        if (phoneNumber.length() > 0 && message.length() > 0) {
                            // call the sms manager
                            PendingIntent pi = PendingIntent.getActivity(SelectToSendSms.this, 0,
                                    new Intent(SelectToSendSms.this, SelectToSendSms.class), 0);
                            SmsManager sms = SmsManager.getDefault();
                            // this is the function that does all the magic
                            sms.sendTextMessage(phoneNumber, null, message, pi, null);
                            Toast.makeText(getBaseContext(), "SMS Send successfully.", Toast.LENGTH_SHORT).show();


                        } else {
                            // display message if text fields are empty
                            Toast.makeText(getBaseContext(), "SMS not Send !", Toast.LENGTH_SHORT).show();

                        }

                        result2.moveToNext();
                    }
                }

            }
        });
        builder.setNegativeButton("No", null);
        builder.create();
        builder.show();



    }
    // Toast.makeText(SelectToSendSms.this,"hello",Toast.LENGTH_SHORT).show();
}

