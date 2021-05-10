package yildiz.edu.tr.onur.aydemir;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;


public class UsersDBConnection extends SQLiteOpenHelper {

    public UsersDBConnection(@Nullable Context context) {
        super(context, "users.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE USERS(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "FIRSTNAME TEXT," +
                "LASTNAME TEXT," +
                "PHONE TEXT," +
                "EMAIL TEXT," +
                "PASSWORD TEXT);";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addNewUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("FIRSTNAME", user.getFirstName());
        cv.put("LASTNAME", user.getLastName());
        cv.put("PHONE", user.getPhoneNumber());
        cv.put("EMAIL", user.geteMail());
        cv.put("PASSWORD", user.getPassword());

        long insert = db.insert("USERS", null, cv);
        db.close();

        if(insert == -1)
            return false;
        return true;
    }

    public ArrayList<User> getAllUsers(){
        ArrayList<User> users = new ArrayList<>();
        String query = "SELECT * FROM USERS;";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                String firstname = cursor.getString(1);
                String lastname = cursor.getString(2);
                String phone = cursor.getString(3);
                String email = cursor.getString(4);
                String password = cursor.getString(5);
                User user = new User(firstname,lastname,phone,email,password, id);
                users.add(user);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return users;
    }

    public User getAUserWEMail(String email){
        User user;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM USERS WHERE EMAIL = "  + "'"  + email+ "'" + " ;";
        Cursor cursor = db.rawQuery(query,  null);
        if(cursor != null)
            cursor.moveToFirst();
        int id = cursor.getInt(0);
        String firstname = cursor.getString(1);
        String lastname = cursor.getString(2);
        String phone = cursor.getString(3);
        String mail = cursor.getString(4);
        String password = cursor.getString(5);
        user = new User(firstname,lastname,phone,mail,password,id);
        return user;
    }


}
