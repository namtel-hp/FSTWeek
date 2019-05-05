package com.qerat.fstweek;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUtilClass {
    private static DatabaseReference databaseReference;
    private static FirebaseDatabase database;


    public static DatabaseReference getDatabaseReference(){
        if(databaseReference==null){
            getDatabase();
            databaseReference=database.getReference();
        }
        return databaseReference;
    }
    public static FirebaseDatabase getDatabase(){
        if (database==null){
            database= FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
        }
        return database;
    }

}
