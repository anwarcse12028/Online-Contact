package com.wadud.anwarhussen.onlinecontacts;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Phone;

public class AfterLogInActivity extends AppCompatActivity  implements View.OnClickListener{

    Button btnRetriveContactlist, btnSave,btnDelete;
    TextView contactList;
    String urlSave = "http://mahwadud.net16.net/contact/savecontact.php";
    String urlDelete = "http://mahwadud.net16.net/contact/deletecontact.php";
    String urlRetrive = "http://mahwadud.net16.net/contact/retrivecontact.php";
    String tname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_log_in);
        btnRetriveContactlist = (Button)findViewById(R.id.buttongetContact);
        btnSave = (Button)findViewById(R.id.buttonSave);
        contactList = (TextView)findViewById(R.id.textViewContact);
        btnRetriveContactlist.setOnClickListener(this);
        btnDelete = (Button)findViewById(R.id.buttonDelete);
        btnDelete.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        tname = bundle.getString("tname");
    }

    @Override
    public void onClick(View v) {
        if(v == btnRetriveContactlist){

            /*
            ContentResolver cr = getContentResolver();
            StringBuffer output = new StringBuffer();
            Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
            if(cursor.getCount()>0){
                while (cursor.moveToNext()){
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String  name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                   if(Integer.parseInt( cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)))>0){
                       output.append("\n Name : "+name);
                       Cursor phoneCursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" = ?", new String[] { id }, null);
                       while (phoneCursor.moveToNext()){
                           String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                           output.append("\n Phone Number"+phoneNumber);
                       }
                       phoneCursor.close();

                   }
                    output.append("\n");
                }
            }*/
            try {
                retriveContact();
            } catch (JSONException e) {
                e.printStackTrace();
            }

          //  contactList.setText(output);
        }
        if(v == btnSave){
            try {
                uploadContact();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(v == btnDelete){
            try {
                deleteContactList();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void retriveContact() throws JSONException {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlRetrive,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(AfterLogInActivity.this, response, Toast.LENGTH_LONG).show();
                        String name, phone,id="12";
                        StringBuffer output = new StringBuffer();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray result = jsonObject.getJSONArray("contactlist");
                            for(int i = 0; i<result.length();i++) {
                                JSONObject contactData = result.getJSONObject(i);
                                name = contactData.getString("name");
                                phone = contactData.getString("phone");
                                id = contactData.getString("id");
                               //saveContact(name ,id, phone);
                                output.append(id+"\n"+name+"\n"+phone+"\n\n");
                            }

                            contactList.setText(output);
                            saveContact("anwar Hussen" ,"100","987654321");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AfterLogInActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        contactList.setText(error.toString());
                        Log.e("error", error.getMessage() + "  " + error.toString());

                    }
                }
        ) {

            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("tname",tname);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void saveContact(String name,String id, String phone) {
        ContentValues values = new ContentValues();
        values.put(Contacts.People.NUMBER, phone);
        values.put(Contacts.People.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM);
        values.put(Contacts.People.LABEL, name);
        values.put(Contacts.People.NAME, name);
        Uri dataUri = getContentResolver().insert(Contacts.People.CONTENT_URI, values);
        Uri updateUri = Uri.withAppendedPath(dataUri, Contacts.People.Phones.CONTENT_DIRECTORY);
        values.clear();
        values.put(Contacts.People.Phones.TYPE, Contacts.People.TYPE_MOBILE);
        values.put(Contacts.People.NUMBER, phone);
        updateUri = getContentResolver().insert(updateUri, values);
        Log.d("CONTACT", ""+updateUri);

    }

    private void deleteContactList() throws JSONException {

       StringRequest stringRequest = new StringRequest(Request.Method.POST, urlDelete,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // StringTokenizer token = new StringTokenizer(response);
                       // Toast.makeText(AfterLogInActivity.this, response, Toast.LENGTH_LONG).show();


                       contactList.setText(response);

                       // if (token.nextToken().equals("success")) {
                            //goToAfterLogInActivity();
                       // }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AfterLogInActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        contactList.setText(error.toString());
                        Log.e("error", error.getMessage() + "  " + error.toString());

                    }
                }
        ) {

            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("tname",tname);
               return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void uploadContact() throws JSONException {

         JSONObject jsonObject; //= new JSONObject();
        final JSONArray  jsonArray = new JSONArray();
        StringBuffer list = new StringBuffer();
        ContentResolver cr = getContentResolver();
        final ArrayList<ContactList> arrayList = new ArrayList<ContactList>();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
        if(cursor.getCount()>0){
            while (cursor.moveToNext()){

               jsonObject = new JSONObject();
              ContactList contactListObject = new ContactList();
                String phoneNumber = null;
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String  name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if(Integer.parseInt( cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)))>0){
                   contactListObject.setName(name);
                    contactListObject.setId(id);
                    jsonObject.put("id",id);
                    jsonObject.put("name",name);

                    Cursor phoneCursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" = ?", new String[] { id }, null);
                    while (phoneCursor.moveToNext()){
                         phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                       contactListObject.setPhoneNUmber(phoneNumber);
                       jsonObject.put("phone",phoneNumber);
                    }
                    phoneCursor.close();

                }
                list.append(id+" \n"+name+"\n"+phoneNumber+"\n\n");

               arrayList.add(contactListObject);
                // arrayList.add(new ContactList(id,name,phoneNumber));
              jsonArray.put(jsonObject);
            }
            contactList.setText(list);
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlSave,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        StringTokenizer token = new StringTokenizer(response);
                        Toast.makeText(AfterLogInActivity.this, response, Toast.LENGTH_LONG).show();
                        contactList.setText(response);
                        if (token.nextToken().equals("success")) {
                            //goToAfterLogInActivity();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AfterLogInActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        contactList.setText(error.toString());
                        Log.e("error", error.getMessage() + "  " + error.toString());

                    }
                }
        ) {

            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("tname",tname);
                params.put("num",arrayList.size()+"");
               // StringBuffer listb = new StringBuffer();
                for(int i = 0;i < arrayList.size();i++) {
                    params.put("id"+i,arrayList.get(i).getId());
                    params.put("name"+i,arrayList.get(i).getName());
                    params.put("phone"+i,arrayList.get(i).getPhoneNUmber());
                   // listb.append(arrayList.get(i).getId()+" \n"+name+"\n"+phoneNumber+"\n\n");
                }
               return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    }

