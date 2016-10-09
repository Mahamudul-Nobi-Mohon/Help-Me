package example.mohon.com.helpme;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class ContactNumberSaver extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    EditText e1,e2,e3;
    Button buttonSave,buttonUpdate,buttonDelete;
    TextView t1,hidden_id;
    ListView ListMessages;

    String phoneNo = "";
    String id2 = "";

    SQLiteDatabase db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_number_saver);


        e1 = (EditText) findViewById(R.id.customerPhoneNo);
        e2 = (EditText) findViewById(R.id.customerName);
        //  e3 = (EditText)findViewById(R.id.customerEmail);
        e3 = (EditText) findViewById(R.id.customerAbout);
        hidden_id = (TextView) findViewById(R.id.hidden_id);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(this);

        buttonUpdate = (Button) findViewById(R.id.buttonUpdate);
        buttonUpdate.setOnClickListener(this);

//        buttonDelete = (Button)findViewById(R.id.buttonDelete);
//        buttonDelete.setOnClickListener(this);

        String id = "0";

        //t1 = (TextView)findViewById(R.id.textView);
        ListMessages = (ListView) findViewById(R.id.listView);
        ArrayList<String> Mymessages = new ArrayList<String>();

        // my = new ContactNumberDatabase(this);

        db = openOrCreateDatabase("helpme1", MODE_PRIVATE, null);
        //       db.execSQL("CREATE TABLE IF NOT EXISTS contactNumberSaver2(id INTEGER PRIMARY KEY AUTOINCREMENT, phonenumber int ,name TEXT, about TEXT)");


        // Cursor result = db.rawQuery("SELECT * from contactNumberSaver",null);
        Cursor result = db.rawQuery("SELECT * from contactNumberSaver2", null);
        result.moveToFirst();
        String text = "";
        String phoneno = "";
        String name = "";
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


        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Mymessages);
        ListMessages.setAdapter(adapter1);
        ListMessages.setOnItemClickListener(this);
    }



    @Override
    public void onClick(View Sender) {


        String phoneno = e1.getText().toString();
        String name = e2.getText().toString();
        String about = e3.getText().toString();
        //int phonenumber;

        if(Sender.getId() == R.id.buttonSave) {
            // db = openOrCreateDatabase("helpme", MODE_PRIVATE, null);
            //  db.execSQL("CREATE TABLE IF NOT EXISTS contactNumberSaver2(id INTEGER PRIMARY KEY AUTOINCREMENT, phonenumber TEXT UNIQUE ,name TEXT, about TEXT)");
            String checksms = e1.getText().toString();

            if(TextUtils.isEmpty(name)) {
                e2.setError("Please type a Name.");
                return;
            }
            if(TextUtils.isEmpty(phoneno)) {
                e1.setError("Please type a phone number.");
                return;
            }
                Cursor result = db.rawQuery("SELECT * from contactNumberSaver2 where phonenumber = '"+phoneno+"' ", null);
                result.moveToFirst();
                int temporary=0;
                while (result.isAfterLast() != true){
                   String phonenumber = result.getString(result.getColumnIndex("phonenumber"));
                   // String id3 = result.getString(result.getColumnIndex("id"));
                    int p = Integer.parseInt(phonenumber);
                    int n = Integer.parseInt(e1.getText().toString());

                    if( p == n ){
                        temporary =1;
                        Toast.makeText(this, "This phone number already inserted.", Toast.LENGTH_SHORT).show();
                    }

                    result.moveToNext();
                }

                    if(temporary == 0) {

                        db.execSQL("INSERT  INTO contactNumberSaver2(phonenumber,name,about) VALUES ('" + phoneno + "','" + name + "','" + about + "')");

                        Intent in = new Intent(this, MainActivity.class);
                        startActivity(in);
                        Toast.makeText(this, "Inserted Successfully.", Toast.LENGTH_LONG).show();
                    }

        }
        else if(Sender.getId() == R.id.buttonUpdate){
            // String  = EditTextSetSmsEdit.getText().toString();

            if(TextUtils.isEmpty(name)) {
                e2.setError("Please type a Name.");
                return;
            }
            if(TextUtils.isEmpty(phoneno)) {
                e1.setError("Please type a phone number.");
                return;
            }

            else {


                String temp_id = hidden_id.getText().toString();
                if(temp_id == null || temp_id == ""){
                    Toast.makeText(this, "Invalid phone number.", Toast.LENGTH_SHORT).show();
                }
                else {
                    db.execSQL("UPDATE contactNumberSaver2 SET phonenumber = '" + e1.getText() + "',name = '" + e2.getText() + "',about = '" + e3.getText() + "' WHERE id = " + hidden_id.getText());
                    Intent in = new Intent(this, ContactNumberSaver.class);
                    startActivity(in);
                    Toast.makeText(this, "Updated Successfully.", Toast.LENGTH_SHORT).show();
                }

            }

//        else if(Sender.getId() == R.id.buttonDelete){
//            AlertDialog.Builder msgBox = new AlertDialog.Builder(this);
//            msgBox.setTitle("Delete Confirm");
//            msgBox.setMessage("Are you sure to delete ?");
//            msgBox.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
//                @Override
//                public  void onClick(DialogInterface arg0, int arg1){
//
//                }
//            });
//            msgBox.setPositiveButton("Delete", new DialogInterface.OnClickListener(){
//                @Override
//                public  void onClick(DialogInterface arg0, int arg1){
//                    if(hidden_id.getText() == null || hidden_id.getText()==""){
//                        Toast.makeText(ContactNumberSaver.this,"Please select an Item for delete.",Toast.LENGTH_SHORT).show();
//                    }
//                    else {
//                        db.execSQL("DELETE FROM contactNumberSaver2 WHERE id = " + hidden_id.getText());
//                        Intent in = new Intent(ContactNumberSaver.this, ContactNumberSaver.class);
//                        startActivity(in);
//                        Toast.makeText(ContactNumberSaver.this, "Deleted Successfully.", Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//            });
//            msgBox.show();
        }
    }


    public void gotomainpage(){
        Intent toy = new Intent(ContactNumberSaver.this, MainActivity.class);
        toy.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toy);
    }

    @Override
    public void onBackPressed(){
        finish();
        gotomainpage();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View clickedView, int pos, long id) {
        TextView tv1 = (TextView)clickedView;

        int commaIndex = tv1.getText().toString().indexOf(",");
        String Contact_id = tv1.getText().toString().substring(0, commaIndex);

        Cursor result = db.rawQuery("select * from contactNumberSaver2 where id = "+Contact_id,null);
        result.moveToFirst();
        while (result.isAfterLast() != true){
            String hiddenId = result.getString(result.getColumnIndex("id"));
            String phone = result.getString(result.getColumnIndex("phonenumber"));
            String name = result.getString(result.getColumnIndex("name"));
            String about = result.getString(result.getColumnIndex("about"));

            hidden_id.setText(hiddenId);
            e1.setText(phone);
            e2.setText(name);
            e3.setText(about);
            result.moveToNext();

            // Intent in = new Intent();
            // in.putExtra("id", id2);
            //Toast.makeText(this,sms_id,Toast.LENGTH_SHORT).show();
            //startActivity(in);


        }
    }


}
