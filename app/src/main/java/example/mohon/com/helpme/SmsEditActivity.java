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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SmsEditActivity extends AppCompatActivity implements View.OnClickListener {

    EditText EditTextSetSmsEdit;
    Button ButtonSmsUpdate,ButtonDeleteSms;

    String id2 = "0";
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_edit);

        db = openOrCreateDatabase("helpme1",MODE_PRIVATE,null);
//        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_setSms(sms_id INTEGER PRIMARY KEY AUTOINCREMENT, sms_text TEXT)");

        EditTextSetSmsEdit = (EditText)findViewById(R.id.editTextSetSmsEdit);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            if(extras.getString("sms_id") != null){

                // Integer proId2 = Integer.parseInt(extras.getString("pro_id"));
                id2 =extras.getString("sms_id");
                readData(id2);

                // Toast.makeText(this,id2,Toast.LENGTH_SHORT).show();

            }
        }

        ButtonSmsUpdate = (Button)findViewById(R.id.buttonSmsUpdate);
        ButtonDeleteSms = (Button)findViewById(R.id.buttonDeleteSms);

        ButtonSmsUpdate.setOnClickListener(this);
        ButtonDeleteSms.setOnClickListener(this);
    }

    public void gotomainpage(){
        Intent toy = new Intent(SmsEditActivity.this, MainActivity.class);
        toy.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toy);
    }

    @Override
    public void onBackPressed(){
        finish();
        gotomainpage();
    }


    public void readData(String sms_id){
        // Toast.makeText(this,"id= "+pro_id,Toast.LENGTH_SHORT).show();
       // db = openOrCreateDatabase("ProductMGT2",MODE_PRIVATE,null);

        Cursor result = db.rawQuery("SELECT sms_text FROM tbl_setSms where sms_id = "+sms_id,null);
        result.moveToFirst();
        while (result.isAfterLast() != true){
            // text += " "+result.getString(result.getColumnIndex("PRO_NAME"));
            EditTextSetSmsEdit.setText(result.getString(result.getColumnIndex("sms_text")));
            //products.add(ProId+ " , "+ProName);

            result.moveToNext();
            // Toast.makeText(this,pro_id+" "+etProNameEdit,Toast.LENGTH_SHORT).show();
        }

        //Toast.makeText(this,sms_id,Toast.LENGTH_SHORT).show();



    }

    @Override
    public void onClick(View Sender) {
        if(Sender.getId() == R.id.buttonSmsUpdate){
            String checksms = EditTextSetSmsEdit.getText().toString();

            if(TextUtils.isEmpty(checksms)) {
                EditTextSetSmsEdit.setError("Please type your SMS.");
                return;
            }
            else {
                db.execSQL("UPDATE tbl_setSms SET sms_text = '" + EditTextSetSmsEdit.getText() + "' WHERE sms_id = " + id2);
                Intent in = new Intent(this, SmsSetActivity.class);
                startActivity(in);
                Toast.makeText(this, "Updated Successfully.", Toast.LENGTH_SHORT).show();
            }

        }

        else if(Sender.getId() == R.id.buttonDeleteSms){
            AlertDialog.Builder msgBox = new AlertDialog.Builder(this);
            msgBox.setTitle("Delete Confirm");
            msgBox.setMessage("Are you sure to delete ?");
            msgBox.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                @Override
                public  void onClick(DialogInterface arg0, int arg1){

                }
            });
            msgBox.setPositiveButton("Delete", new DialogInterface.OnClickListener(){
                @Override
                public  void onClick(DialogInterface arg0, int arg1){
                    db.execSQL("DELETE FROM tbl_setSms WHERE sms_id = "+id2);
                    Intent in = new Intent(SmsEditActivity.this, SmsSetActivity.class);
                    startActivity(in);
                    Toast.makeText(SmsEditActivity.this,"Deleted Successfully.",Toast.LENGTH_SHORT).show();

                }
            });
            msgBox.show();
        }
    }
}
