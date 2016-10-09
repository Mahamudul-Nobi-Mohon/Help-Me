package example.mohon.com.helpme;

import android.Manifest;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;


public class SmsSend extends AppCompatActivity implements AdapterView.OnItemClickListener {

    Button buttonHappy;
    TextView textViewLocation;
    Button btnShowLocation;
    private ListView listMessages ;
    SQLiteDatabase db;
    String id2 = "0";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_send);

        checkGPSStatus();
        db = openOrCreateDatabase("helpme1",MODE_PRIVATE,null);
//        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_setSms(sms_id INTEGER PRIMARY KEY AUTOINCREMENT, sms_text TEXT)");

        textViewLocation = (TextView) findViewById(R.id.textViewLocation);// text to show addresses
        btnShowLocation = (Button) findViewById(R.id.btnShowLocation); // button
        listMessages = (ListView) findViewById(R.id.listMessages);

        ArrayList<String> Mymessages = new ArrayList<String>();

        Cursor result = db.rawQuery("SELECT * from tbl_setSms order by sms_id desc",null);
        result.moveToFirst();
        String text = "";
        String smsid = "";
        String smstext = "";
        while (result.isAfterLast() != true){
            // text += " "+result.getString(result.getColumnIndex("PRO_NAME"));
            smsid = result.getString(result.getColumnIndex("sms_id"));
            smstext = result.getString(result.getColumnIndex("sms_text"));
            Mymessages.add(smsid+ " , "+smstext);
            result.moveToNext();
        }
        //Toast.makeText(this,text,Toast.LENGTH_SHORT).show();




        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,Mymessages);
        listMessages.setAdapter(adapter1);
        listMessages.setOnItemClickListener(this);




// click the below to get the current location and address.
        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                //for (int i = 1; i <= 2; i++) {
                    turnGPSOn(); // method to turn on the GPS if its in off state.
                    getMyCurrentLocation();
               // }

            }
        });
        // buttonHappy = (Button)findViewById(R.id.buttonHappy);
        //for (int i = 1; i <= 1; i++) {
            turnGPSOn(); // method to turn on the GPS if its in off state.
            getMyCurrentLocation();
      //  }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


       // turnOnLocation();



    }

    @Override
    public void onItemClick(AdapterView<?> parent, final View clickedView, int pos, long id) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Warning !!");
        builder.setMessage("Are you sure to send SMS ??");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
           public void onClick(DialogInterface dialog, int which) {

                TextView tv1 = (TextView)clickedView;

                int commaIndex = tv1.getText().toString().indexOf(",");
                String sms_id = tv1.getText().toString().substring(0, commaIndex);
                Intent in = new Intent(SmsSend.this, SelectToSendSms.class);
                in.putExtra("sms_id", sms_id);
                //Toast.makeText(this,sms_id,Toast.LENGTH_SHORT).show();
                startActivity(in);


            }
       });
        builder.setNegativeButton("No", null);
        builder.create();
        builder.show();


    }


    public void readData(String sms_id){
        // Toast.makeText(this,"id= "+pro_id,Toast.LENGTH_SHORT).show();
        // db = openOrCreateDatabase("ProductMGT2",MODE_PRIVATE,null);

        Cursor result = db.rawQuery("SELECT sms_text FROM tbl_setSms where sms_id = "+sms_id,null);
        result.moveToFirst();
        while (result.isAfterLast() != true){
            // text += " "+result.getString(result.getColumnIndex("PRO_NAME"));
            String message = result.getString(result.getColumnIndex("sms_text"));
            //products.add(ProId+ " , "+ProName);

            result.moveToNext();
            // Toast.makeText(this,pro_id+" "+etProNameEdit,Toast.LENGTH_SHORT).show();
        }

        //Toast.makeText(this,sms_id,Toast.LENGTH_SHORT).show();



    }

    public void gotomainpage(){
        Intent toy = new Intent(SmsSend.this, MainActivity.class);
        toy.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toy);
    }

    @Override
    public void onBackPressed(){
           finish();
                gotomainpage();


    }



    public void sendMessage(View view) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Warning !!");
        builder.setMessage("The money will be deducted from your Account. Are you sure to send your current Address All of your contact ?? ");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                String message_phone = "";
                String message = "";

                Cursor result2 = db.rawQuery("SELECT phonenumber FROM contactNumberSaver2 ", null);
                result2.moveToFirst();
                while (result2.isAfterLast() != true) {
                    message_phone = result2.getString(result2.getColumnIndex("phonenumber"));
                     result2.moveToNext();
                    Toast.makeText(SmsSend.this, message_phone, Toast.LENGTH_LONG).show();
                    if ((StatePosition == null || StatePosition == "") && (CityName == "" || CityName ==null) && (CountryName == "" || CountryName== null) ) {
                        message = "Now , I am in Danger. Please help me. My Latitude : " + MyLat + ",and Longitude: " + MyLong + " . please search in internet by that Latitude & Longitude values. ";
                    }
//                    if (StatePosition == null || StatePosition == "") {
//                        message = "Now , I am in Danger. Please help me. My Latitude : " + MyLat + ",and Longitude: " + MyLong + " . please search in internet by that Latitude & Longitude values. ";
//                    }
                    else {
                        message = "Now , I am in Danger. Please help me. I have no time for call you. Call me now.Now My location is  " + StatePosition + ", " + CityName + " ," + CountryName + ".";
                    }
                    if (message_phone.length() > 0 && message.length() > 0 && MyLat > 0 ) {
                        // call the sms manager
                        PendingIntent pi = PendingIntent.getActivity(SmsSend.this, 0,
                                new Intent(SmsSend.this, SmsSend.class), 0);
                        SmsManager sms = SmsManager.getDefault();
                        // this is the function that does all the magic
                        sms.sendTextMessage( message_phone, null, message, pi, null);
                        Toast.makeText(getBaseContext(), "SMS Send successfully.", Toast.LENGTH_SHORT).show();

                    } else {
                       Toast.makeText(getBaseContext(), "SMS Not Send. Because without location you can't send SMS.", Toast.LENGTH_SHORT).show();

                    }
                }

            }
        });
        builder.setNegativeButton("No", null);
        builder.create();
        builder.show();

    }

    /*public void sendMessageForSorry(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Warning !!");
        builder.setMessage("Are you sure You don't need help ??");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                Cursor database = my.display();
                if (database.getCount() == 0) {
                    Toast.makeText(SmsSend.this, "No Data Found!", Toast.LENGTH_LONG).show();
                    return;
                }

                database.moveToFirst();
                String details[] = new String[database.getCount()];
                String phoneNumber[] = new String[details.length];
                String msg[] = new String[details.length];
                int n = 0;
                int perfect_send = 0;
                //t1.setText(data);

                do {

                    phoneNumber[n] = database.getString(0).toString();

                    msg[n] = "Sorry, Now I am not in danger . The SMS was mistake by me. Again sorry, don't need to call me for confirm. Thanks.. ";

                    Toast.makeText(SmsSend.this, details[n], Toast.LENGTH_LONG).show();

                    if (phoneNumber[n].length() > 0 && msg[n].length() > 0) {
                        // call the sms manager
                        PendingIntent pi = PendingIntent.getActivity(SmsSend.this, 0,
                                new Intent(SmsSend.this, SmsSend.class), 0);
                        SmsManager sms = SmsManager.getDefault();
                        // this is the function that does all the magic
                        sms.sendTextMessage("0" + phoneNumber[n], null, msg[n], pi, null);
                        Toast.makeText(getBaseContext(), "SMS Send successfully.", Toast.LENGTH_SHORT).show();
                        perfect_send = perfect_send + 1;
                    } else {
                        // display message if text fields are empty
                        Toast.makeText(getBaseContext(), "SMS Not send.", Toast.LENGTH_SHORT).show();
                    }

                    n++;

                } while (database.moveToNext());

                //Toast.makeText(getBaseContext(),"Total Sms Send: "+perfect_send,Toast.LENGTH_SHORT).show();

                // ListAdapter listAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,details);
                // l1.setAdapter(listAdapter);

            }
        });
        builder.setNegativeButton("No", null);
        builder.create();
        builder.show();
    }
*/
    public void mainPage(View v) {
        Intent toy = new Intent(SmsSend.this, MainActivity.class);
        toy.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toy);
    }

    //for location show

    public void turnGPSOn() {
        try {

            String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

            if (!provider.contains("gps")) { //if gps is disabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                sendBroadcast(poke);
            }
        } catch (Exception e) {

        }
    }

    // Method to turn off the GPS
    public void turnGPSOff() {
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (provider.contains("gps")) { //if gps is enabled

            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }

    // turning off the GPS if its in on state. to avoid the battery drain.

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        turnGPSOff();
    }


    /**
     * Check the type of GPS Provider available at that instance and  collect the location informations
     *
     * @Output Latitude and Longitude
     */
    void getMyCurrentLocation() {

//        LocationManager locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        LocationListener locListener = new MyLocationListener();
//            gps_enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//            network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

           // Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();



        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locListener = new MyLocationListener();
        try {
            gps_enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }
        //don't start listeners if no provider is enabled

        //if(!gps_enabled && !network_enabled)

        //return false;

        if (gps_enabled) {
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);

        }


        if (gps_enabled) {
            location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        }

        if (network_enabled && location == null) {

            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);

        }
        if (network_enabled && location == null) {
            location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        }

        if (location != null) {

            MyLat = location.getLatitude();
            MyLong = location.getLongitude();
        } else {
            Location loc = getLastKnownLocation(this);
            if (loc != null) {
                MyLat = loc.getLatitude();
                MyLong = loc.getLongitude();
            }
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locManager.removeUpdates(locListener); // removes the periodic updates from location listener to avoid battery drainage. If you want to get location at the periodic intervals call this method using pending intent.

        try {
// Getting address from found locations.
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());
            addresses = geocoder.getFromLocation(MyLat, MyLong, 1);
            StatePosition = addresses.get(0).getAddressLine(0);

            StateName = addresses.get(0).getAdminArea();
            CityName = addresses.get(0).getLocality();
            CountryName = addresses.get(0).getCountryName();
            // you can get more details other than this . like country code, state code, etc.
            System.out.println(" StateName " + StateName);
            System.out.println(" CityName " + CityName);
            System.out.println(" CountryName " + CountryName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //textView2.setText("" + MyLat);
        // textView3.setText("" + MyLong);
        if (StatePosition == "" || StatePosition == null) {
            textViewLocation.setText("Latitude: " + MyLat + ", Longitude: " + MyLong);
        } else {
            textViewLocation.setText(StatePosition + ", " + CityName + " ," + CountryName);
        }


    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "SmsSend Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://example.mohon.com.helpme/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "SmsSend Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://example.mohon.com.helpme/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }



    // Location listener class. to get location.
    public class MyLocationListener implements LocationListener {
        public void onLocationChanged(Location location) {
            if (location != null) {
            }
        }

        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
        }
    }


    private boolean gps_enabled = false;
    private boolean network_enabled = false;
    Location location;
    Double MyLat, MyLong;
    String CityName = "";
    String StateName = "";
    String CountryName = "";
    String StatePosition = "";


// below method to get the last remembered location. because we don't get locations all the times .At some instances we are unable to get the location from GPS. so at that moment it will show us the last stored location.

    public Location getLastKnownLocation(Context context)

    {
        Location location = null;
        LocationManager locationmanager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        List list = locationmanager.getAllProviders();
        boolean i = false;
        Iterator iterator = list.iterator();
        do {
            //System.out.println("---------------------------------------------------------------------");
            if (!iterator.hasNext())
                break;
            String s = (String) iterator.next();
            //if(i != 0 && !locationmanager.isProviderEnabled(s))
            if (i != false && !locationmanager.isProviderEnabled(s))
                continue;
            // System.out.println("provider ===> "+s);
//            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//              //  return TODO;
//            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                // return TODO;
            }
            Location location1 = locationmanager.getLastKnownLocation(s);
            if (location1 == null)
                continue;
            if (location != null) {
                //System.out.println("location ===> "+location);
                //System.out.println("location1 ===> "+location);
                float f = location.getAccuracy();
                float f1 = location1.getAccuracy();
                if (f >= f1) {
                    long l = location1.getTime();
                    long l1 = location.getTime();
                    if (l - l1 <= 600000L)
                        continue;
                }
            }
            location = location1;
            // System.out.println("location  out ===> "+location);
            //System.out.println("location1 out===> "+location);
            i = locationmanager.isProviderEnabled(s);
            // System.out.println("---------------------------------------------------------------------");
        } while (true);
        return location;
    }
    //end for location show

    //for turn on location

//    public void turnOnLocation(){
//        // Get Location Manager and check for GPS & Network location services
//        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
//        if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
//                !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
//            // Build the alert dialog
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("Location Services Not Active");
//            builder.setMessage("Please enable Location Services and GPS");
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    // Show location settings when the user acknowledges the alert dialog
//                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                    startActivity(intent);
//                }
//            });
//            Dialog alertDialog = builder.create();
//            alertDialog.setCanceledOnTouchOutside(false);
//            alertDialog.show();
//        }
//        else{
//            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            startActivity(intent);
//        }
//    }
    //end for turn on location


    private void checkGPSStatus() {
        LocationManager locationManager = null;
        boolean gps_enabled = false;
        boolean network_enabled = false;
        if ( locationManager == null ) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex){}
        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex){}
        if ( !gps_enabled && !network_enabled ){
            AlertDialog.Builder dialog = new AlertDialog.Builder(SmsSend.this);
            dialog.setMessage("GPS not enabled");
            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //this will navigate user to the device location settings screen
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            AlertDialog alert = dialog.create();
            alert.show();
        }
    }
}

