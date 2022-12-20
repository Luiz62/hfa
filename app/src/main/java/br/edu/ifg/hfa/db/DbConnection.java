package br.edu.ifg.hfa.db;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public abstract class DbConnection {

    private static FirebaseAuth mAuth;
    private static DatabaseReference databaseReference;
    private static FirebaseDatabase instance;

    public static DatabaseReference getDatabaseReference() {
        if (databaseReference == null) {
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }
        return databaseReference;
    }

    public static FirebaseDatabase getInstance() {
        if (instance == null) {
            instance = FirebaseDatabase.getInstance();
        }
        return instance;
    }

    public static FirebaseAuth getAuth(){

        if(mAuth == null ) {
            mAuth = FirebaseAuth.getInstance();
        }

        return mAuth;
    }
}
