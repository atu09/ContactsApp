package com.atirek.alm.contactsapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public ArrayList<Person> contactList = new ArrayList<>();

    Uri contactUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    String[] PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone._ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
    };
    String SELECTION = ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER + "='1'";

    TextView tv_contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_contacts = (TextView) findViewById(R.id.tv_contacts);

        requestPermission();

    }


    public void getContactList(View view) {

        Cursor contacts = getContentResolver().query(contactUri, PROJECTION, SELECTION, null, null);

        String contactsList = "";
        for (int i = 0; i < contacts.getCount(); i++) {
            contacts.moveToPosition(i);

            String contactId = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
            String contactName = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String contactNumber = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            Log.d("Contact" + i + ">>>", contactId + " - " + contactName + " - " + contactNumber);

            contactsList = contactsList + "\n" + contactName + " - " + contactNumber;

            Person person = new Person(contactName, contactNumber);
            contactList.add(person);
        }

        tv_contacts.setText(contactsList);

    }

    public void requestPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MainActivity.this, "Contacts Permission Granted", Toast.LENGTH_SHORT).show();
        }

    }

}

class Person {
    String myName = "";
    String myNumber = "";

    public Person(String myName, String myNumber) {
        this.myName = myName;
        this.myNumber = myNumber;
    }

    public String getName() {
        return myName;
    }

    public void setName(String name) {
        myName = name;
    }

    public String getPhoneNum() {
        return myNumber;
    }

    public void setPhoneNum(String number) {
        myNumber = number;
    }
}
