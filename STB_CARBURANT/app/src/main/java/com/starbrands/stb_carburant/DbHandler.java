package com.starbrands.stb_carburant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Editable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class DbHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 3;
    private static final String DB_NAME = "stbcarburant";

    public DbHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE IF NOT EXISTS DEPARTEMENT (" +
                "dep_id integer NOT NULL PRIMARY KEY," +
                "dep_description nvarchar COLLATE NOCASE);");
        db.execSQL("CREATE TABLE IF NOT EXISTS USERS (" +
                "usr_id integer NOT NULL PRIMARY KEY," +
                "usr_name nvarchar COLLATE NOCASE," +
                "usr_first_name nvarchar COLLATE NOCASE," +
                "usr_dep_id integer," +
                "usr_login nvarchar COLLATE NOCASE," +
                "usr_password nvarchar COLLATE NOCASE," +
                "usr_current bit," +
                "usr_active bit," +
                "FOREIGN KEY(usr_dep_id) REFERENCES DEPARTEMENT(dep_id) );");
        db.execSQL("CREATE TABLE IF NOT EXISTS BRAND (" +
                "br_id	integer NOT NULL PRIMARY KEY," +
                "br_description	nvarchar COLLATE NOCASE);");
        db.execSQL("CREATE TABLE IF NOT EXISTS MODEL (" +
                "md_id	integer NOT NULL PRIMARY KEY," +
                "md_description	nvarchar COLLATE NOCASE," +
                "md_br_id	integer," +
                "FOREIGN KEY(md_br_id) REFERENCES BRAND(br_id));");
        db.execSQL("CREATE TABLE IF NOT EXISTS FUEL (" +
                "fl_id	integer NOT NULL PRIMARY KEY," +
                "fl_description	nvarchar COLLATE NOCASE);");
        db.execSQL("CREATE TABLE IF NOT EXISTS COST (" +
                "ct_id	integer NOT NULL PRIMARY KEY," +
                "ct_fl_id	integer," +
                "ct_date	nvarchar," +
                "ct_price	numeric," +
                "ct_active bit," +
                "FOREIGN KEY(ct_fl_id) REFERENCES FUEL(fl_id));");
        db.execSQL("CREATE TABLE IF NOT EXISTS VEHICLE (" +
                "vh_id	integer NOT NULL PRIMARY KEY," +
                "vh_md_id	integer," +
                "vh_fl_id	integer," +
                "vh_code	varchar COLLATE NOCASE," +
                "vh_immatriculaton	nvarchar COLLATE NOCASE," +
                "vh_creation_date	nvarchar," +
                "vh_active	bit," +
                "FOREIGN KEY(vh_fl_id) REFERENCES FUEL(fl_id)," +
                "FOREIGN KEY(vh_md_id) REFERENCES MODEL(md_id));");
        db.execSQL("CREATE TABLE IF NOT EXISTS AFFECTATION (" +
                "af_id	integer NOT NULL PRIMARY KEY," +
                "af_usr_id	integer," +
                "af_vh_id	integer," +
                "af_date	nvarchar," +
                "af_active	bit," +
                "FOREIGN KEY(af_usr_id) REFERENCES USERS(usr_id)," +
                "FOREIGN KEY(af_vh_id) REFERENCES VEHICLE(vh_id));");
        db.execSQL("CREATE TABLE IF NOT EXISTS PAYMENT_TYPE (" +
                "pt_id	integer NOT NULL PRIMARY KEY," +
                "pt_description	nvarchar COLLATE NOCASE);");

        db.execSQL("CREATE TABLE IF NOT EXISTS PAYMENT (" +
                "pa_id	integer NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "pa_usr_id	integer," +
                "pa_date	nvarchar," +
                "pa_distance	numeric," +
                "pa_unit_price	numeric," +
                "pa_cost	numeric," +
                "pa_latitude	numeric," +
                "pa_longitude	numeric," +
                "pa_pt_id	integer," +
                "pa_sync_id	integer," +
                "pa_sync_date	nvarchar," +
                "pa_comment	nvarchar COLLATE NOCASE," +
                "pa_file_path	nvarchar COLLATE NOCASE," +
                "pa_file_name	nvarchar COLLATE NOCASE," +
                "pa_km_counter_file_path	nvarchar COLLATE NOCASE," +
                "pa_km_counter_file_name	nvarchar COLLATE NOCASE," +
                "pa_updated bit," +
                "FOREIGN KEY(pa_usr_id) REFERENCES USERS(usr_id)," +
                "FOREIGN KEY(pa_pt_id) REFERENCES PAYMENT_TYPE(pt_id));");

        db.execSQL("CREATE TABLE IF NOT EXISTS KILOMETRAGE (" +
                "km_id	integer NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "km_usr_id	integer," +
                "km_date	nvarchar," +
                "km_kilometrage	numeric," +
                "km_latitude	numeric," +
                "km_longitude	numeric," +
                "km_sync_id	integer," +
                "km_sync_date	nvarchar," +
                "km_updated bit," +
                "km_file_path	nvarchar COLLATE NOCASE," +
                "km_file_name	nvarchar COLLATE NOCASE," +
                "accepted bit," +
                "montant_recharge	numeric," +
                "motif_refus	nvarchar," +
                "FOREIGN KEY(km_usr_id) REFERENCES USERS(usr_id));");

        db.execSQL("CREATE TABLE IF NOT EXISTS TRAJET (" +
                "tr_id	integer NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "tr_usr_id	integer," +
                "tr_motif	nvarchar," +
                "tr_date_depart	nvarchar," +
                "tr_kilometrage_depart	numeric," +
                "tr_lieu_depart	nvarchar," +
                "tr_date_arrivee	nvarchar," +
                "tr_kilometrage_arrivee	numeric," +
                "tr_lieu_arrivee	nvarchar," +
                "tr_note	nvarchar," +
                "tr_sync_id	integer," +
                "tr_sync_date	nvarchar," +
                "tr_updated bit," +
                "tr_type bit," +
                "FOREIGN KEY(tr_usr_id) REFERENCES USERS(usr_id));");

        db.execSQL("CREATE TABLE IF NOT EXISTS GPS (" +
                "gps_id	integer NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "gps_usr_id	integer," +
                "gps_latitude	numeric," +
                "gps_longitude	numeric," +
                "gps_date	nvarchar," +
                "gps_sync_id	integer," +
                "gps_sync_date	nvarchar," +
                "FOREIGN KEY(gps_usr_id) REFERENCES USERS(usr_id));");

        db.execSQL("CREATE TABLE IF NOT EXISTS PARAMETRE (" +
                "prm_id	integer NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "prm_key	nvarchar," +
                "prm_value	nvarchar);");

        // department test
        ContentValues cValues = new ContentValues();
//        cValues.put("dep_id", 1000);
//        cValues.put("dep_description", "GUEST");

        // Insert the new row, retucrning the primary key value of the new row
//        db.insert("DEPARTEMENT", null, cValues);

        // user test
        cValues = new ContentValues();
        cValues.put("usr_id", 1);
        cValues.put("usr_name", "GUEST");
        cValues.put("usr_first_name", "GUEST");
        cValues.put("usr_dep_id", 1);
        cValues.put("usr_login", "GUEST");
        cValues.put("usr_password", "0");
        cValues.put("usr_current", 1);
        cValues.put("usr_active", 1);
        db.insert("USERS", null, cValues);

        // vehicle test
//        cValues = new ContentValues();
//        cValues.put("vh_id", 1);
//        cValues.put("vh_md_id", 1000);
//        cValues.put("vh_fl_id", 1005);
//        cValues.put("vh_code", "vehicle guest code");
//        cValues.put("vh_immatriculaton", "8977897hu8989");
//        cValues.put("vh_creation_date", "2021-02-15 13:44:54.287");
//        cValues.put("vh_active", 1);
//        db.insert("VEHICLE", null, cValues);

        // affectation test
//        cValues = new ContentValues();
//        cValues.put("af_id", 1);
//        cValues.put("af_usr_id", 1);
//        cValues.put("af_vh_id", 1);
//        cValues.put("af_date", "2021-02-15 13:44:54.287");
//        cValues.put("af_active", 1);
//        db.insert("AFFECTATION", null, cValues);

        cValues = new ContentValues();
        cValues.put("prm_id", 1);
        cValues.put("prm_key", "address");
        cValues.put("prm_value", "41.111.138.18:7019");
        db.insert("PARAMETRE", null, cValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE AFFECTATION ADD chosen_aff bit");

        // add aff_id column
        db.execSQL("ALTER TABLE KILOMETRAGE ADD km_aff_id  integer");
        db.execSQL("ALTER TABLE PAYMENT ADD pa_aff_id  integer");
        db.execSQL("ALTER TABLE TRAJET ADD tr_aff_id  integer");
        db.execSQL("ALTER TABLE GPS ADD gps_aff_id  integer");

        onCreate(db);
    }

    public String getAddress(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM PARAMETRE where prm_key='address'";
        Cursor cursor = db.rawQuery(query, null);
        String adrress="";
        if(cursor != null) {
            cursor.moveToNext();
            adrress=cursor.getString(cursor.getColumnIndex("prm_value"));
        }
        return adrress;
    }
    public void setAddress(String uri){
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put("prm_value", uri);
        // Insert the new row, returning the primary key value of the new row
        db.update("PARAMETRE", cValues, "prm_key=?", new String[]{String.valueOf("address")});
    }

    public boolean checkExistingActiveAffectation( ){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM AFFECTATION where af_active='" + 1 + "' and af_usr_id='" + this.GetLastUser_id()+ "'";
        Cursor cursor = db.rawQuery(query, null);
        if (null != cursor && cursor.getCount() > 0) {
                return true;
        }else {
            return false;
        }
    }

    public boolean checkExistChosenAffectation( ){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM AFFECTATION where chosen_aff=1";
        Cursor cursor = db.rawQuery(query, null);
        if (null != cursor && cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void chooseDefaultAffectation(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> activeAffectationsList=this.GetCurrentActiveAffectations();
        String defaultAff=activeAffectationsList.get(0);
        ContentValues cValues = new ContentValues();
        cValues.put("chosen_aff", 1);
        db.update("AFFECTATION", cValues, "af_id=?", new String[]{defaultAff});
    }

    public  boolean checkUsername(String user){
        String connected_user=this.GetLastUser();
        if(connected_user.equals(user)) {
            return true;
        }else{
            return false;
        }
    }

    public int CheckUserConnexion(Editable login, Editable password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT usr_id FROM USERS where usr_login='" + login + "' and usr_password='" + password + "'";
        int id = 0;
        Cursor cursor = db.rawQuery(query, null);
        if (null != cursor && cursor.getCount() > 0) {
            cursor.moveToFirst();
            id = cursor.getInt(cursor.getColumnIndex("usr_id"));
            cursor.close();
        }
        return id;
    }

    public boolean checkInitialisation() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT count(*) FROM USERS ";
        int count = 0;
        Cursor cursor = db.rawQuery(query, null);
        if (null != cursor && cursor.getCount() > 0) {
            cursor.moveToFirst();
            count = cursor.getInt(0);
            cursor.close();
        }
        return count == 1;
    }

    public boolean updatepassword(String username,String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cVals = new ContentValues();
        cVals.put("usr_password", password);
        long result=db.update("USERS", cVals, "usr_current = ?", new String[]{"1"});
        if(result==-1) {
            return false;
        }else{
            return true;
        }
    }

    // **** CRUD (insert, Read, Update, Delete) Operations **** //
    // ajouter un nouveau departement
    public void insertDepartement(int id, String designation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put("dep_id", id);
        cValues.put("dep_description", designation);
        long newRowId = db.insert("DEPARTEMENT", null, cValues);
    }

    //Get all departements
    public ArrayList<HashMap<String, String>> GetDepartements() {
        //Get the Data Repository in read mode
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HashMap<String, String>> depList = new ArrayList<>();
        String query = "SELECT dep_id, dep_description FROM DEPARTEMENT";
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            HashMap<String, String> dep = new HashMap<>();
            dep.put("dep_id", String.valueOf(cursor.getInt(cursor.getColumnIndex("dep_id"))));
            dep.put("dep_description", cursor.getString(cursor.getColumnIndex("dep_description")));
            depList.add(dep);
        }
        cursor.close();

        return depList;
    }

    //update departement
    public void UpdateDepartement(int id, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cVals = new ContentValues();
        cVals.put("dep_description", description);
        db.update("DEPARTEMENT", cVals, "dep_id = ?", new String[]{String.valueOf(id)});

    }

    //check departement exists
    public boolean DepartementExists(int dep_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select dep_id, dep_description from departement where dep_id=" + dep_id;
        Cursor cursor = db.rawQuery(query, null);
        if (null != cursor && cursor.getCount() > 0){
            cursor.close();

            return true;
        }else{

            return false;
        }
    }

    // insert new user
    public void insertUser(int id, String name, String first_name, int dep_id, String login, String password, int current, int active) {
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put("usr_id", id);
        cValues.put("usr_name", name);
        cValues.put("usr_first_name", first_name);
        cValues.put("usr_dep_id", dep_id);
        cValues.put("usr_login", login);
        cValues.put("usr_password", password);
        cValues.put("usr_current", current);
        cValues.put("usr_active", active);
        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert("USERS", null, cValues);

    }

    //Get all users
    public ArrayList<HashMap<String, String>> GetUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();
        String query = "select * from USERS";
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            HashMap<String, String> user = new HashMap<>();
            user.put("usr_id", String.valueOf(cursor.getInt(cursor.getColumnIndex("usr_id"))));
            user.put("usr_name", cursor.getString(cursor.getColumnIndex("usr_name")));
            user.put("usr_first_name", cursor.getString(cursor.getColumnIndex("usr_first_name")));
            user.put("usr_dep_id", String.valueOf(cursor.getInt(cursor.getColumnIndex("usr_dep_id"))));
            user.put("usr_login", cursor.getString(cursor.getColumnIndex("usr_login")));
            user.put("usr_password", cursor.getString(cursor.getColumnIndex("usr_password")));
            user.put("usr_current", String.valueOf(cursor.getInt(cursor.getColumnIndex("usr_current"))));
            user.put("usr_active", String.valueOf(cursor.getInt(cursor.getColumnIndex("usr_active"))));
            userList.add(user);
        }

        return userList;
    }

    // get last connected user name (login)
    public String GetLastUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT usr_login FROM USERS where usr_current=1";
        Cursor cursor = db.rawQuery(query, null);
        HashMap<String, String> user = new HashMap<>();
        if (null != cursor && cursor.getCount() > 0) {
            cursor.moveToFirst();
            user.put("usr_login", cursor.getString(cursor.getColumnIndex("usr_login")));
            cursor.close();
        }

        return user.get("usr_login");
    }

    // get user firstname & lastname
    public String getUserFirstNameLastName() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT usr_first_name,usr_name FROM USERS where usr_current=1";
        Cursor cursor = db.rawQuery(query, null);
        HashMap<String, String> user = new HashMap<>();
        if (cursor!=null && cursor.getCount()>0) {
            cursor.moveToFirst();
            user.put("usr_firstname_lastname", cursor.getString(cursor.getColumnIndex("usr_first_name"))+ " "+cursor.getString(cursor.getColumnIndex("usr_name")));
            cursor.close();
        }


        return user.get("usr_firstname_lastname");
    }
    // get last connected user id
    public Integer GetLastUser_id() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT usr_id FROM USERS where usr_current=1";
        int userId=0;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor!=null && cursor.getCount()>0) {
            cursor.moveToFirst();
            userId=cursor.getInt(cursor.getColumnIndex("usr_id"));
            cursor.close();
        }

        return  userId;
    }

    public void setChosenAffectationId(int af_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cVals = new ContentValues();
        cVals.put("chosen_aff", 0);
        db.update("AFFECTATION", cVals, null,null);

        cVals = new ContentValues();
        cVals.put("chosen_aff", 1);
        db.update("AFFECTATION", cVals, "af_id = ?", new String[]{String.valueOf(af_id)});

    }

    public Integer getChosenAffectationId() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT af_id FROM AFFECTATION where chosen_aff=1";
        int affId=0;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor!=null && cursor.getCount()>0) {
            cursor.moveToFirst();
            affId=cursor.getInt(cursor.getColumnIndex("af_id"));
            cursor.close();
        }

        return  affId;
    }

    public Integer getChosenVehicleId() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT af_vh_id FROM AFFECTATION where chosen_aff=1";
        int vhId=0;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor!=null && cursor.getCount()>0) {
            cursor.moveToFirst();
            vhId=cursor.getInt(cursor.getColumnIndex("af_vh_id"));
            cursor.close();
        }

        return  vhId;
    }

    //update new connected user
    public void UpdateConnectedUser(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cVals = new ContentValues();
        cVals.put("usr_current", 0);
        db.update("USERS", cVals, null, null);
        cVals = new ContentValues();
        cVals.put("usr_current", 1);
        db.update("USERS", cVals, "usr_id = ?", new String[]{String.valueOf(id)});
    }

    //check user exists
    public boolean UserExists(int usr_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from USERS WHERE usr_id=" + usr_id;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor!=null){
            if(cursor.getCount() > 0){
                cursor.close();

                return true;
            }   else{
                cursor.close();

                return false;
            }
        }else{

            return false;
        }
    }

    // update user
    public void updateUser(int id, String name, String first_name, int dep_id, String login, String password, int active) {
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put("usr_name", name);
        cValues.put("usr_first_name", first_name);
        cValues.put("usr_dep_id", dep_id);
        cValues.put("usr_login", login);
        cValues.put("usr_password", password);
        cValues.put("usr_active", active);
        // Insert the new row, returning the primary key value of the new row
        db.update("USERS", cValues, "usr_id = ?", new String[]{String.valueOf(id)});

    }

    // ajouter un nouveau brands
    public void insertBrand(int id, String designation) {
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put("br_id", id);
        cValues.put("br_description", designation);
        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert("BRAND", null, cValues);

    }

    //update brand
    public void UpdateBrand(int id, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cVals = new ContentValues();
        cVals.put("br_description", description);
        db.update("BRAND", cVals, "br_id = ?", new String[]{String.valueOf(id)});

    }

    //check brands exits
    public boolean brandsExists(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from BRAND WHERE br_id=" + id;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor!=null){
            if(cursor.getCount() > 0){
                cursor.close();

                return true;
            }   else{
                cursor.close();

                return false;
            }
        }else{

            return false;
        }
    }

    // ajouter un nouveau MODEL
    public void insertModel(int id, String designation, int br_id) {
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put("md_id", id);
        cValues.put("md_description", designation);
        cValues.put("md_br_id", br_id);
        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert("MODEL", null, cValues);

    }

    //update MODEL
    public void Updatemodel(int id, String designation, int br_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cVals = new ContentValues();
        cVals.put("md_description", designation);
        cVals.put("md_br_id", br_id);
        db.update("MODEL", cVals, "md_id = ?", new String[]{String.valueOf(id)});

    }

    //check MODEL exits
    public boolean modelExists(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from MODEL WHERE md_id=" + id;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor!=null){
            if(cursor.getCount() > 0){
                cursor.close();

                return true;
            }   else{
                cursor.close();

                return false;
            }
        }else{

            return false;
        }
    }

    // ajouter un nouveau FUEL
    public void insertFUEL(int id, String designation) {
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put("fl_id", id);
        cValues.put("fl_description", designation);
        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert("FUEL", null, cValues);

    }

    //update FUEL
    public void UpdateFUEL(int id, String designation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cVals = new ContentValues();
        cVals.put("fl_description", designation);
        db.update("FUEL", cVals, "fl_id = ?", new String[]{String.valueOf(id)});

    }

    //check FUEL exits
    public boolean FUELExists(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from FUEL where fl_id=" + id;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor!=null){
            if(cursor.getCount() > 0){
                cursor.close();

                return true;
            }   else{
                cursor.close();

                return false;
            }

        }else{

            return false;
        }
    }

    // ajouter un nouveau COST
    public void insertCOST(int id, int fl_id, String date, double price, int act) {
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put("ct_id", id);
        cValues.put("ct_fl_id", fl_id);
        cValues.put("ct_date", date);
        cValues.put("ct_price", price);
        cValues.put("ct_active", act);
        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert("COST", null, cValues);

    }

    //update COST
    public void UpdateCOST(int id, int fl_id, String date, double price, int act) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cVals = new ContentValues();
        cVals.put("ct_fl_id", fl_id);
        cVals.put("ct_date", date);
        cVals.put("ct_price", price);
        cVals.put("ct_active", act);
        db.update("COST", cVals, "ct_id = ?", new String[]{String.valueOf(id)});

    }

    //check COST exits
    public boolean COSTExists(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from COST where ct_id=" + id;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor!=null){
            if(cursor.getCount() > 0){
                cursor.close();

                return true;
            }   else{
                cursor.close();

                return false;
            }
        }else{

            return false;
        }
    }

    // get current fuel cost for connected user
    public double GetFuelCost(int id) {
        double price = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select ct_price from COST \n" +
                "inner join FUEL on fl_id=ct_fl_id \n" +
                "inner join VEHICLE on vh_fl_id=fl_id \n" +
                "inner join AFFECTATION on af_vh_id=vh_id \n" +
                "where af_active=1 and vh_active=1 and ct_active=1 and af_usr_id=" + id;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor!=null && cursor.getCount() > 0 ){
            cursor.moveToFirst();
            price = cursor.getDouble(cursor.getColumnIndex("ct_price"));
            cursor.close();
        }


        return price;
    }

    // ajouter un nouveau PAYMENT_TYPE
    public void insertTypePayment(int id, String description) {
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put("pt_id", id);
        cValues.put("pt_description", description);
        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert("PAYMENT_TYPE", null, cValues);

    }

    //update PAYMENT_TYPE
    public void UpdateTypePayment(int id, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cVals = new ContentValues();
        cVals.put("pt_description", description);
        db.update("PAYMENT_TYPE", cVals, "pt_id = ?", new String[]{String.valueOf(id)});

    }

    //check PAYMENT_TYPE exits
    public boolean TypePaymentExists(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from PAYMENT_TYPE where pt_id=" + id;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor!=null){
            if(cursor.getCount() > 0){
                cursor.close();
                 return true;
            }   else{
                cursor.close();
                 return false;
            }
        }else{
             return false;
        }
    }

    // get all PAYMENT_TYPE
    public ArrayList<HashMap<String, String>> GetPAYMENT_TYPE() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HashMap<String, String>> ptList = new ArrayList<>();
        String query = "select pt_id,pt_description from PAYMENT_TYPE";
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            HashMap<String, String> pt = new HashMap<>();
            pt.put("pt_id", String.valueOf(cursor.getInt(cursor.getColumnIndex("pt_id"))));
            pt.put("pt_description", cursor.getString(cursor.getColumnIndex("pt_description")));
            ptList.add(pt);
        }
        cursor.close();

         return ptList;
    }

    // get PAYMENT_TYPE id
    public int GetAPAYMENT_TYPE_id(String description) {
        int pt_id = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select pt_id from PAYMENT_TYPE where pt_description = '" + description + "'";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor!=null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            pt_id = cursor.getInt(cursor.getColumnIndex("pt_id"));
            cursor.close();
        }

         return pt_id;
    }

    // ajouter un nouveau VEHICLE
    public void insertVEHICLE(int id, int md_id, int fl_id, String code, String mat, String date, int act) {
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put("vh_id", id);
        cValues.put("vh_md_id", md_id);
        cValues.put("vh_fl_id", fl_id);
        cValues.put("vh_code", code);
        cValues.put("vh_immatriculaton", mat);
        cValues.put("vh_creation_date", date);
        cValues.put("vh_active", act);
        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert("VEHICLE", null, cValues);

    }

    //update vehicle
    public void UpdateVEHICLE(int id, int md_id, int fl_id, String code, String mat, String date, int act) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cVals = new ContentValues();
        cVals.put("vh_md_id", md_id);
        cVals.put("vh_fl_id", fl_id);
        cVals.put("vh_code", code);
        cVals.put("vh_immatriculaton", mat);
        cVals.put("vh_creation_date", date);
        cVals.put("vh_active", act);
        db.update("VEHICLE", cVals, "vh_id = ?", new String[]{String.valueOf(id)});

    }

    //check vehicle exits
    public boolean VEHICLEExists(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from VEHICLE where vh_id=" + id;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor!=null){
            if(cursor.getCount() > 0){
                cursor.close();
                 return true;
            }   else{
                cursor.close();
                 return false;
            }
        }else{
             return false;
        }
    }

    // ajouter un nouveau AFFECTATION
    public void insertAFFECTATION(int id, int usr_id, int vh_id, String date, int act) {
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put("af_id", id);
        cValues.put("af_usr_id", usr_id);
        cValues.put("af_vh_id", vh_id);
        cValues.put("af_date", date);
        cValues.put("af_active", act);
        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert("AFFECTATION", null, cValues);

    }

    //update AFFECTATION
    public void UpdateAFFECTATION(int id, int usr_id, int vh_id, String date, int act) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cVals = new ContentValues();
        cVals.put("af_usr_id", usr_id);
        cVals.put("af_vh_id", vh_id);
        cVals.put("af_date", date);
        cVals.put("af_active", act);
        db.update("AFFECTATION", cVals, "af_id = ?", new String[]{String.valueOf(id)});
    }

    //check AFFECTATION exits
    public boolean AFFECTATIONExists(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from AFFECTATION where af_id=" + id;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor!=null){
            if(cursor.getCount() > 0){
                cursor.close();
                 return true;
            }   else{
                cursor.close();
                 return false;
            }
        }else{
             return false;
        }
    }

    // get affected vehicle
    public int GetAffectedVehicle(int id) {
        int af_vh_id = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select af_vh_id from AFFECTATION \n" +
                "where af_active=1 and af_id=" + id;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor!=null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            af_vh_id = cursor.getInt(cursor.getColumnIndex("af_vh_id"));
            cursor.close();
        }
         return af_vh_id;
    }

    //get immatriculation number of vehicle
    public String  getImmatriculationVehicle(int id) {
        String immatriculation = null;
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select vh_immatriculaton from VEHICLE where vh_id=" + id;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor!=null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            immatriculation = cursor.getString(cursor.getColumnIndex("vh_immatriculaton"));
            cursor.close();
        }

         return immatriculation;
    }

    // get vehicle model
    public String  getModelDescriptionVehicle(int id) {
        String model = null;
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select md_description from VEHICLE INNER JOIN MODEL ON vh_md_id=md_id  \n" +
                "where vh_id=" + id;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor!=null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            model = cursor.getString(cursor.getColumnIndex("md_description"));
            cursor.close();
        }

         return model;
    }

    //get vehicle brand
    public String  getBrandVehicle(int id) {
        String brand = null;
        int model_id=0;
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select md_id from VEHICLE INNER JOIN MODEL ON vh_md_id=md_id  \n" +
                "where vh_id=" + id;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor!=null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            model_id = cursor.getInt(cursor.getColumnIndex("md_id"));
            cursor.close();
        }

        query = "select br_description from BRAND INNER JOIN MODEL ON br_id=md_br_id  \n" +
                "where md_id=" + model_id;

        cursor = db.rawQuery(query, null);
        if(cursor!=null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            brand = cursor.getString(cursor.getColumnIndex("br_description"));
            cursor.close();
        }

         return brand;
    }

    //insert payment
    public long insertPayment(int pa_usr_id,
                              String pa_date,
                              double pa_distance,
                              double pa_unit_price,
                              double pa_cost,
                              double pa_latitude,
                              double pa_longitude,
                              int pa_pt_id,
                              int pa_sync_id,
                              String pa_sync_date,
                              String pa_comment,
                              String pa_file_path,
                              String pa_file_name,
                              String pa_km_counter_file_path,
                              String pa_km_counter_file_name,
                              int pa_aff_id) {
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put("pa_usr_id", pa_usr_id);
        cValues.put("pa_date", pa_date);
        cValues.put("pa_distance", pa_distance);
        cValues.put("pa_unit_price", pa_unit_price);
        cValues.put("pa_cost", pa_cost);
        cValues.put("pa_latitude", pa_latitude);
        cValues.put("pa_longitude", pa_longitude);
        cValues.put("pa_pt_id", pa_pt_id);
        cValues.put("pa_sync_id", pa_sync_id);
        cValues.put("pa_sync_date", pa_sync_date);
        cValues.put("pa_comment", pa_comment);
        cValues.put("pa_file_path", pa_file_path);
        cValues.put("pa_file_name", pa_file_name);
        cValues.put("pa_km_counter_file_path", pa_km_counter_file_path);
        cValues.put("pa_km_counter_file_name", pa_km_counter_file_name);
        cValues.put("pa_updated", 0);
        cValues.put("pa_aff_id", pa_aff_id);
        long newRowId = db.insert("PAYMENT", null, cValues);

         return newRowId;
    }

    //update payment
    public long updatePayment(int pa_id,
                              int pa_usr_id,
                              String pa_date,
                              double pa_distance,
                              double pa_unit_price,
                              double pa_cost,
                              int pa_pt_id,
                              int pa_sync_id,
                              String pa_sync_date,
                              String pa_comment,
                              String pa_file_path,
                              String pa_file_name,
                              String pa_km_counter_file_path,
                              String pa_km_counter_file_name) {
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put("pa_usr_id", pa_usr_id);
        cValues.put("pa_date", pa_date);
        cValues.put("pa_distance", pa_distance);
        cValues.put("pa_unit_price", pa_unit_price);
        cValues.put("pa_cost", pa_cost);
        cValues.put("pa_pt_id", pa_pt_id);
        cValues.put("pa_sync_id", pa_sync_id);
        cValues.put("pa_sync_date", pa_sync_date);
        cValues.put("pa_comment", pa_comment);
        cValues.put("pa_file_path", pa_file_path);
        cValues.put("pa_file_name", pa_file_name);
        cValues.put("pa_km_counter_file_path", pa_km_counter_file_path);
        cValues.put("pa_km_counter_file_name", pa_km_counter_file_name);
        cValues.put("pa_updated", 1);
        long newRowId = db.update("PAYMENT", cValues,"pa_id =" + pa_id, null);

         return newRowId;
    }

    //delete payment
    public long deletePayment(int pa_id) {
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        long newRowId = db.delete("PAYMENT","pa_id =" + pa_id, null);

         return newRowId;
    }

    //Get all payments
    public ArrayList<HashMap<String, String>> GetPayment(int usr_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HashMap<String, String>> paylist = new ArrayList<>();
        String query = "SELECT pa_id \n" +
                "      ,pa_usr_id \n" +
                "      ,pa_date\n" +
                "      ,pa_distance \n" +
                "      ,pa_unit_price \n" +
                "      ,pa_cost \n" +
                "      ,pa_latitude \n" +
                "      ,pa_longitude \n" +
                "      ,pa_pt_id \n" +
                "      ,pa_sync_id \n" +
                "      ,pa_sync_date \n" +
                "      ,pa_comment \n" +
                "      ,pa_file_path \n" +
                "      ,pa_file_name \n" +
                "      ,pa_km_counter_file_path \n" +
                "      ,pa_km_counter_file_name \n" +
                "      ,pa_aff_id \n" +
                "  FROM PAYMENT \n" +
                "  where pa_usr_id=" + usr_id + " and (pa_sync_id = 0 or (pa_sync_id != 0 and pa_updated=1) )";

        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            HashMap<String, String> pay = new HashMap<>();
            pay.put("pa_id", String.valueOf(cursor.getInt(cursor.getColumnIndex("pa_id"))));
            pay.put("pa_usr_id", String.valueOf(cursor.getInt(cursor.getColumnIndex("pa_usr_id"))));
            pay.put("pa_date", String.valueOf(cursor.getString(cursor.getColumnIndex("pa_date"))));
            pay.put("pa_distance", String.valueOf(cursor.getDouble(cursor.getColumnIndex("pa_distance"))));
            pay.put("pa_unit_price", String.valueOf(cursor.getDouble(cursor.getColumnIndex("pa_unit_price"))));
            pay.put("pa_cost", String.valueOf(cursor.getDouble(cursor.getColumnIndex("pa_cost"))));
            pay.put("pa_latitude", String.valueOf(cursor.getDouble(cursor.getColumnIndex("pa_latitude"))));
            pay.put("pa_longitude", String.valueOf(cursor.getDouble(cursor.getColumnIndex("pa_longitude"))));
            pay.put("pa_pt_id", String.valueOf(cursor.getInt(cursor.getColumnIndex("pa_pt_id"))));
            pay.put("pa_sync_id", String.valueOf(cursor.getInt(cursor.getColumnIndex("pa_sync_id"))));
            pay.put("pa_sync_date", String.valueOf(cursor.getString(cursor.getColumnIndex("pa_sync_date"))));
            pay.put("pa_comment", String.valueOf(cursor.getString(cursor.getColumnIndex("pa_comment"))));
            pay.put("pa_file_path", String.valueOf(cursor.getString(cursor.getColumnIndex("pa_file_path"))));
            pay.put("pa_file_name", String.valueOf(cursor.getString(cursor.getColumnIndex("pa_file_name"))));
            pay.put("pa_km_counter_file_path", String.valueOf(cursor.getString(cursor.getColumnIndex("pa_km_counter_file_path"))));
            pay.put("pa_km_counter_file_name", String.valueOf(cursor.getString(cursor.getColumnIndex("pa_km_counter_file_name"))));
            pay.put("pa_aff_id", String.valueOf(cursor.getInt(cursor.getColumnIndex("pa_aff_id"))));
            paylist.add(pay);
        }
        cursor.close();

         return paylist;
    }

    //update PAYMENT FLAG
    public void UpdatePayment_flag(int pa_id, int pa_sync_id, String pa_sync_date, String pa_file_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cVals = new ContentValues();
        cVals.put("pa_sync_id", pa_id);
        cVals.put("pa_sync_date", pa_sync_date);
        cVals.put("pa_updated", 0);
        db.update("PAYMENT", cVals, "pa_id = " + pa_sync_id + " and pa_file_name='" + pa_file_name + "'", null);

    }

    //check payment exists
    public boolean PAYMENTxists(int id, String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM PAYMENT where pa_sync_id=" + id + " and pa_file_name='" + name + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor!=null){
            if(cursor.getCount() > 0){
                cursor.close();
                 return true;
            }   else{
                cursor.close();
                 return false;
            }
        }else{
             return false;
        }
    }

    //update payment
    public void upadtePaymentFromServer(int pa_server_id,
                              int pa_usr_id,
                              String pa_date,
                              double pa_distance,
                              double pa_unit_price,
                              double pa_cost,
                              double pa_latitude,
                              double pa_longitude,
                              int pa_pt_id,
                              String pa_sync_date,
                              String pa_comment,
                              String pa_file_path,
                              String pa_file_name,
                                String pa_km_counter_file_path,
                                String pa_km_counter_file_name,
                                        int pa_aff_id) {
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put("pa_usr_id", pa_usr_id);
        cValues.put("pa_date", pa_date);
        cValues.put("pa_distance", pa_distance);
        cValues.put("pa_unit_price", pa_unit_price);
        cValues.put("pa_cost", pa_cost);
        cValues.put("pa_latitude", pa_latitude);
        cValues.put("pa_longitude", pa_longitude);
        cValues.put("pa_pt_id", pa_pt_id);
        cValues.put("pa_sync_date", pa_sync_date);
        cValues.put("pa_comment", pa_comment);
        cValues.put("pa_file_path", pa_file_path);
        cValues.put("pa_file_name", pa_file_name);
        cValues.put("pa_km_counter_file_path", pa_km_counter_file_path);
        cValues.put("pa_km_counter_file_name", pa_km_counter_file_name);
        cValues.put("pa_aff_id", pa_aff_id);
        db.update("PAYMENT", cValues, "pa_sync_id = " + pa_server_id + " and pa_file_name='" + pa_file_name + "'", null);

    }

    //insert kilometrage
    public long insertKilometrage(int km_usr_id,
                                  String km_date,
                                  double km_kilometrage,
                                  double km_latitude,
                                  double km_longitude,
                                  int km_sync_id,
                                  String km_sync_date,
                                  String km_file_path,
                                  String km_file_name,
                                  String accepted,
                                  String montant_recharge,
                                  String motif_refus,
                                  int km_aff_id
                                  ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put("km_usr_id", km_usr_id);
        cValues.put("km_date", km_date);
        cValues.put("km_kilometrage", km_kilometrage);
        cValues.put("km_latitude", km_latitude);
        cValues.put("km_longitude", km_longitude);
        cValues.put("km_sync_id", km_sync_id);
        cValues.put("km_sync_date", km_sync_date);
        cValues.put("km_updated", 0);
        cValues.put("km_file_path", km_file_path);
        cValues.put("km_file_name", km_file_name);
        cValues.put("accepted", accepted);
        cValues.put("montant_recharge", montant_recharge);
        cValues.put("motif_refus", motif_refus);
        cValues.put("km_aff_id", km_aff_id);
        long longId=db.insert("KILOMETRAGE", null, cValues);

         return longId;
    }

    //update kilometrage
    public long updateKilometrageFromServer(int km_usr_id,
                                  double km_kilometrage,
                                  String km_date,
                                  int km_sync_id,
                                  String km_sync_date,
                                    String km_file_path,
                                    String km_file_name,
                                            String accepted,
                                            String montant_recharge,
                                            String motif_refus,
                                            int km_aff_id) {
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put("km_usr_id", km_usr_id);
        cValues.put("km_kilometrage", km_kilometrage);
        cValues.put("km_date", km_date);
        cValues.put("km_sync_date", km_sync_date);
        cValues.put("km_file_path", km_file_path);
        cValues.put("km_file_name", km_file_name);
        cValues.put("accepted", accepted);
        cValues.put("montant_recharge", montant_recharge);
        cValues.put("motif_refus", motif_refus);
        cValues.put("km_aff_id", km_aff_id);
        long newRowId = db.update("KILOMETRAGE", cValues,"km_sync_id =" + km_sync_id, null);
         return newRowId;
    }

    public long updateKilometrage(int km_id,int km_usr_id,
                             double km_kilometrage,
                             String km_date,
                                  int km_sync_id,
                                  String km_sync_date,
                                  String km_file_path,
                                  String km_file_name
                                  ) {
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put("km_usr_id", km_usr_id);
        cValues.put("km_kilometrage", km_kilometrage);
        cValues.put("km_date", km_date);
        cValues.put("km_sync_id", km_sync_id);
        cValues.put("km_sync_date", km_sync_date);
        cValues.put("km_updated", 1);
        cValues.put("km_file_path", km_file_path);
        cValues.put("km_file_name", km_file_name);
        long newRowId = db.update("KILOMETRAGE", cValues,"km_id =" + km_id, null);

         return newRowId;
    }

    //delete kilometrage
    public long deleteKilometrage(int km_id) {
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        long newRowId = db.delete("KILOMETRAGE","km_id =" + km_id, null);

         return newRowId;
    }

    //update kilometrage FLAG
    public void UpdateKilometrage_flag(int km_id, int km_sync_id, String km_sync_date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cVals = new ContentValues();
        cVals.put("km_sync_id", km_id);
        cVals.put("km_sync_date", km_sync_date);
        cVals.put("km_updated", 0);
        db.update("KILOMETRAGE", cVals, "km_id = " + km_sync_id , null);

    }

    //check kilometrage exists
    public boolean KILOMETRAGExists(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM KILOMETRAGE where km_sync_id=" + id;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor!=null){
            if(cursor.getCount() > 0){
                cursor.close();
                 return true;
            }   else{
                cursor.close();
                 return false;
            }
        }else{
             return false;
        }
    }

    //insert trajet
    public long insertTrajet(int tr_usr_id,
                             String tr_motif,
                             String tr_date_depart,
                             double tr_kilometrage_depart,
                             String tr_lieu_depart,
                             String tr_date_arrivee,
                             double tr_kilometrage_arrivee,
                             String tr_lieu_arrivee,
                             String tr_note,
                             int tr_sync_id,
                             String tr_sync_date,
                             int tr_type,
                             int tr_aff_id) {
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put("tr_usr_id", tr_usr_id);
        cValues.put("tr_motif", tr_motif);
        cValues.put("tr_date_depart", tr_date_depart);
        cValues.put("tr_kilometrage_depart", tr_kilometrage_depart);
        cValues.put("tr_lieu_depart", tr_lieu_depart);
        cValues.put("tr_date_arrivee", tr_date_arrivee);
        cValues.put("tr_kilometrage_arrivee", tr_kilometrage_arrivee);
        cValues.put("tr_lieu_arrivee", tr_lieu_arrivee);
        cValues.put("tr_note", tr_note);
        cValues.put("tr_sync_id", tr_sync_id);
        cValues.put("tr_sync_date", tr_sync_date);
        cValues.put("tr_updated", 0);
        cValues.put("tr_type", tr_type);
        cValues.put("tr_aff_id", tr_aff_id);
        long newRowId = db.insert("TRAJET", null, cValues);

         return newRowId;
    }

    //update trajet
    public long updateTrajetFromServer(int tr_usr_id,
                             String tr_motif,
                             String tr_date_depart,
                             double tr_kilometrage_depart,
                             String tr_lieu_depart,
                             String tr_date_arrivee,
                             double tr_kilometrage_arrivee,
                             String tr_lieu_arrivee,
                             String tr_note,
                             int tr_sync_id,
                             String tr_sync_date,
                                       int tr_type,
                                       int tr_aff_id
    ) {
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put("tr_usr_id", tr_usr_id);
        cValues.put("tr_motif", tr_motif);
        cValues.put("tr_date_depart", tr_date_depart);
        cValues.put("tr_kilometrage_depart", tr_kilometrage_depart);
        cValues.put("tr_lieu_depart", tr_lieu_depart);
        cValues.put("tr_date_arrivee", tr_date_arrivee);
        cValues.put("tr_kilometrage_arrivee", tr_kilometrage_arrivee);
        cValues.put("tr_lieu_arrivee", tr_lieu_arrivee);
        cValues.put("tr_note", tr_note);
        cValues.put("tr_sync_date", tr_sync_date);
        cValues.put("tr_type", tr_type);
        cValues.put("tr_aff_id", tr_aff_id);
        long newRowId = db.update("TRAJET", cValues,"tr_sync_id =" + tr_sync_id, null);

         return newRowId;
    }

    public long updateTrajet(int tr_id,
                                       int tr_usr_id,
                                       String tr_motif,
                                       String tr_date_depart,
                                       double tr_kilometrage_depart,
                                       String tr_lieu_depart,
                                       String tr_date_arrivee,
                                       double tr_kilometrage_arrivee,
                                       String tr_lieu_arrivee,
                                       String tr_note,
                                       int tr_sync_id,
                                       String tr_sync_date
    ) {
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put("tr_usr_id", tr_usr_id);
        cValues.put("tr_motif", tr_motif);
        cValues.put("tr_date_depart", tr_date_depart);
        cValues.put("tr_kilometrage_depart", tr_kilometrage_depart);
        cValues.put("tr_lieu_depart", tr_lieu_depart);
        cValues.put("tr_date_arrivee", tr_date_arrivee);
        cValues.put("tr_kilometrage_arrivee", tr_kilometrage_arrivee);
        cValues.put("tr_lieu_arrivee", tr_lieu_arrivee);
        cValues.put("tr_note", tr_note);
        cValues.put("tr_sync_id", tr_sync_id);
        cValues.put("tr_sync_date", tr_sync_date);
        cValues.put("tr_updated", 1);
        long newRowId = db.update("TRAJET", cValues,"tr_id =" + tr_id, null);

         return newRowId;
    }

    //delete trajet
    public long deleteTrajet(int tr_id) {
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        long newRowId = db.delete("TRAJET","tr_id =" + tr_id, null);

         return newRowId;
    }

    //update trajet FLAG
    public void UpdateTrajet_flag(int tr_id, int tr_sync_id, String tr_sync_date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cVals = new ContentValues();
        cVals.put("tr_sync_id", tr_id);
        cVals.put("tr_sync_date", tr_sync_date);
        cVals.put("tr_updated", 0);
        db.update("TRAJET", cVals, "tr_id = " + tr_sync_id , null);

    }

    //check trajet exists
    public boolean TRAJETExists(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM TRAJET where tr_sync_id=" + id;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor!=null){
            if(cursor.getCount() > 0){
                cursor.close();
                 return true;
            }   else{
                cursor.close();
                 return false;
            }
        }else{
             return false;
        }
    }

    //Get all trajets
    public ArrayList<HashMap<String, String>> GetTrajet(int usr_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HashMap<String, String>> trajetlist = new ArrayList<>();
        String query = "SELECT * FROM TRAJET where  tr_usr_id=" + usr_id+ " and (tr_sync_id = 0 or (tr_sync_id != 0 and tr_updated=1) )";

        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            HashMap<String, String> trajet = new HashMap<>();
            trajet.put("tr_id", String.valueOf(cursor.getInt(cursor.getColumnIndex("tr_id"))));
            trajet.put("tr_usr_id", String.valueOf(cursor.getInt(cursor.getColumnIndex("tr_usr_id"))));
            trajet.put("tr_motif", String.valueOf(cursor.getString(cursor.getColumnIndex("tr_motif"))));
            trajet.put("tr_date_depart", String.valueOf(cursor.getString(cursor.getColumnIndex("tr_date_depart"))));
            trajet.put("tr_kilometrage_depart", String.valueOf(cursor.getDouble(cursor.getColumnIndex("tr_kilometrage_depart"))));
            trajet.put("tr_lieu_depart", String.valueOf(cursor.getString(cursor.getColumnIndex("tr_lieu_depart"))));
            trajet.put("tr_date_arrivee", String.valueOf(cursor.getString(cursor.getColumnIndex("tr_date_arrivee"))));
            trajet.put("tr_kilometrage_arrivee", String.valueOf(cursor.getDouble(cursor.getColumnIndex("tr_kilometrage_arrivee"))));
            trajet.put("tr_lieu_arrivee", String.valueOf(cursor.getString(cursor.getColumnIndex("tr_lieu_arrivee"))));
            trajet.put("tr_note", String.valueOf(cursor.getString(cursor.getColumnIndex("tr_note"))));
            trajet.put("tr_sync_id", String.valueOf(cursor.getInt(cursor.getColumnIndex("tr_sync_id"))));
            trajet.put("tr_sync_date", String.valueOf(cursor.getString(cursor.getColumnIndex("tr_sync_date"))));
            trajet.put("tr_type", String.valueOf(cursor.getString(cursor.getColumnIndex("tr_type"))));
            trajet.put("tr_aff_id", String.valueOf(cursor.getInt(cursor.getColumnIndex("tr_aff_id"))));
            trajetlist.add(trajet);
        }
        cursor.close();

         return trajetlist;
    }

    //Get all kilometrages
    public ArrayList<HashMap<String, String>> GetKilometrage(int usr_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HashMap<String, String>> kilometragelist = new ArrayList<>();
        String query = "SELECT * FROM KILOMETRAGE where km_usr_id=" + usr_id + " and (km_sync_id = 0 or (km_sync_id != 0 and km_updated=1) )";

        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            HashMap<String, String> kilometrage = new HashMap<>();
            kilometrage.put("km_id", String.valueOf(cursor.getInt(cursor.getColumnIndex("km_id"))));
            kilometrage.put("km_usr_id", String.valueOf(cursor.getInt(cursor.getColumnIndex("km_usr_id"))));
            kilometrage.put("km_date", String.valueOf(cursor.getString(cursor.getColumnIndex("km_date"))));
            kilometrage.put("km_kilometrage", String.valueOf(cursor.getDouble(cursor.getColumnIndex("km_kilometrage"))));
            kilometrage.put("km_latitude", String.valueOf(cursor.getDouble(cursor.getColumnIndex("km_latitude"))));
            kilometrage.put("km_longitude", String.valueOf(cursor.getDouble(cursor.getColumnIndex("km_longitude"))));
            kilometrage.put("km_sync_id", String.valueOf(cursor.getInt(cursor.getColumnIndex("km_sync_id"))));
            kilometrage.put("km_sync_date", String.valueOf(cursor.getString(cursor.getColumnIndex("km_sync_date"))));
            kilometrage.put("km_file_path", String.valueOf(cursor.getString(cursor.getColumnIndex("km_file_path"))));
            kilometrage.put("km_file_name", String.valueOf(cursor.getString(cursor.getColumnIndex("km_file_name"))));
            kilometrage.put("km_aff_id", String.valueOf(cursor.getInt(cursor.getColumnIndex("km_aff_id"))));
            kilometragelist.add(kilometrage);
        }
        cursor.close();

         return kilometragelist;
    }

    //Get all gps
    public ArrayList<HashMap<String, String>> GetGps(int usr_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HashMap<String, String>> gpslist = new ArrayList<>();
        String query = "SELECT * FROM GPS where gps_usr_id=" + usr_id+" and gps_sync_id = 0";

        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            HashMap<String, String> gps = new HashMap<>();
            gps.put("gps_id", String.valueOf(cursor.getInt(cursor.getColumnIndex("gps_id"))));
            gps.put("gps_usr_id", String.valueOf(cursor.getInt(cursor.getColumnIndex("gps_usr_id"))));
            gps.put("gps_latitude", String.valueOf(cursor.getDouble(cursor.getColumnIndex("gps_latitude"))));
            gps.put("gps_longitude", String.valueOf(cursor.getDouble(cursor.getColumnIndex("gps_longitude"))));
            gps.put("gps_date", String.valueOf(cursor.getString(cursor.getColumnIndex("gps_date"))));
            gps.put("gps_aff_id", String.valueOf(cursor.getInt(cursor.getColumnIndex("gps_aff_id"))));
            gpslist.add(gps);
        }
        cursor.close();

         return gpslist;
    }

    //Get all gps
    public ArrayList<HashMap<String, String>> GetCurrentAffectations() {
        SQLiteDatabase db = this.getReadableDatabase();
        int usr_id=this.GetLastUser_id();
        ArrayList<HashMap<String, String>> afflist = new ArrayList<>();
        String query = "SELECT af_id, chosen_aff, vh_md_id,vh_code,vh_immatriculaton FROM AFFECTATION inner join VEHICLE on af_vh_id=vh_id where af_usr_id=" + usr_id;
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            HashMap<String, String> aff = new HashMap<>();
            aff.put("af_id", String.valueOf(cursor.getInt(cursor.getColumnIndex("af_id"))));
            aff.put("chosen_aff", String.valueOf(cursor.getInt(cursor.getColumnIndex("chosen_aff"))));
            aff.put("vh_md_id", String.valueOf(cursor.getInt(cursor.getColumnIndex("vh_md_id"))));
            aff.put("vh_code", String.valueOf(cursor.getString(cursor.getColumnIndex("vh_code"))));
            aff.put("vh_immatriculaton", String.valueOf(cursor.getString(cursor.getColumnIndex("vh_immatriculaton"))));
            afflist.add(aff);
        }
        cursor.close();
        return afflist;
    }

    public ArrayList<String> GetCurrentActiveAffectations() {
        SQLiteDatabase db = this.getReadableDatabase();
        int usr_id=this.GetLastUser_id();
        ArrayList<String> afflist = new ArrayList<>();
        String query = "SELECT af_id FROM AFFECTATION where af_active=1 and af_usr_id=" + usr_id;
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            afflist.add(String.valueOf(cursor.getInt(cursor.getColumnIndex("af_id"))));
        }
        cursor.close();
        return afflist;
    }

    // get nb total de trajets
    public int nbTrajets() {
        int af_id=this.getChosenAffectationId();
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor cursor= db.rawQuery("select count(*) from TRAJET where tr_aff_id=" + af_id,null);
        int count = 0;
        if(cursor!=null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            count = cursor.getInt(0);
            cursor.close();
        }

         return count;
    }

    // get nb total de pleins
    public int nbPleins() {
        int af_id=this.getChosenAffectationId();
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor cursor= db.rawQuery("select count(*) from PAYMENT where pa_aff_id=" + af_id,null);
        int count = 0;
        if(null != cursor && cursor.getCount() > 0) {
            cursor.moveToFirst();
            count = cursor.getInt(0);
            cursor.close();
        }

         return count;
    }

    // get kilometrage initial
    public double kilometrageInitial(){
        int af_id=this.getChosenAffectationId();
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor cursor= db.rawQuery(" select date, kilometrage from " +
                "(select km_date as date,km_kilometrage as kilometrage from KILOMETRAGE where km_aff_id=" + af_id +
                " UNION select pa_date as date,pa_distance as kilometrage from PAYMENT where pa_aff_id=" + af_id+
                " UNION select tr_date_depart as date,tr_kilometrage_depart as kilometrage from TRAJET where tr_aff_id=" + af_id +
                "  ) order by date asc",null);

        double premier_kilometrage = 0;
        if(null != cursor && cursor.getCount() > 0){
            cursor.moveToFirst();
            premier_kilometrage = cursor.getDouble(cursor.getColumnIndex("kilometrage"));
            cursor.close();
        }
         return premier_kilometrage;
    }

    // Dernier relevé kilométrique
    public double dernierKilometrage() {
        int af_id=this.getChosenAffectationId();
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor cursor= db.rawQuery(" select date, kilometrage from " +
                "(select km_date as date,km_kilometrage as kilometrage from KILOMETRAGE where km_aff_id=" + af_id +
                " UNION select pa_date as date,pa_distance as kilometrage from PAYMENT where pa_aff_id=" + af_id+
                    " UNION select tr_date_depart as date,tr_kilometrage_depart as kilometrage from TRAJET where tr_aff_id=" + af_id +
                " UNION select tr_date_arrivee as date,tr_kilometrage_arrivee as kilometrage from TRAJET where tr_aff_id=" + af_id + " and tr_date_arrivee is not null and tr_kilometrage_arrivee!=0 "+
                "  ) order by date desc ,kilometrage desc",null);

        double dernier_kilometrage = 0;
        if(null != cursor && cursor.getCount() > 0){
            cursor.moveToFirst();
            dernier_kilometrage =cursor.getDouble(cursor.getColumnIndex("kilometrage"));
            cursor.close();
        }
         return dernier_kilometrage;
    }

    // get nb paiments naftal
    public int nbPaimentsNaftal() {
        int af_id=this.getChosenAffectationId();
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor cursor= db.rawQuery("select count(*) from PAYMENT inner join PAYMENT_TYPE on pa_pt_id=pt_id where pa_aff_id=" + af_id + " and pt_description='Carte Naftal'",null);
        int count = 0;
        if(null != cursor && cursor.getCount() > 0) {
            cursor.moveToFirst();
            count = cursor.getInt(0);
            cursor.close();
        }

         return count;
    }

    // get nb paiments Bon carburant
    public int nbPaimentsBonCarburant() {
        int af_id=this.getChosenAffectationId();
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor cursor= db.rawQuery("select count(*) from PAYMENT inner join PAYMENT_TYPE on pa_pt_id=pt_id where pa_aff_id=" + af_id + " and pt_description='Bon de Carburant'",null);
        int count = 0;
        if(null != cursor && cursor.getCount() > 0) {
            cursor.moveToFirst();
            count = cursor.getInt(0);
            cursor.close();
        }

         return count;
    }

    // get nb paiments espèce
    public int nbPaimentsEspece() {
        int af_id=this.getChosenAffectationId();
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor cursor= db.rawQuery("select count(*) from PAYMENT inner join PAYMENT_TYPE on pa_pt_id=pt_id where pa_aff_id=" + af_id + " and pt_description='Espèces'",null);
        int count = 0;
        if(null != cursor && cursor.getCount() > 0) {
            cursor.moveToFirst();
            count = cursor.getInt(0);
            cursor.close();
        }

         return count;
    }

    // distance totale de trajet parcourue trajet
    public double distanceTotaleTrajet(){
        int af_id=this.getChosenAffectationId();
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor cursor= db.rawQuery(" select tr_date_depart ,tr_kilometrage_depart , tr_date_arrivee, tr_kilometrage_arrivee from TRAJET where tr_aff_id=" + af_id + " and tr_date_arrivee is not null and tr_kilometrage_arrivee!=0 "+
                " order by tr_date_depart asc",null);

        double distance_totale = 0;

        if(null != cursor && cursor.getCount() > 0){
            while (cursor.moveToNext()) {
                distance_totale+= (cursor.getDouble(cursor.getColumnIndex("tr_kilometrage_arrivee")) -cursor.getDouble(cursor.getColumnIndex("tr_kilometrage_depart")));
            }
            cursor.close();
        }
         return distance_totale;
    }

    // distance totale de trajet parcourue trajet motif travail
    public double distanceTrajetTravail(){
        int af_id=this.getChosenAffectationId();
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor cursor= db.rawQuery(" select tr_date_depart ,tr_kilometrage_depart , tr_date_arrivee, tr_kilometrage_arrivee from TRAJET  where tr_motif='TRAVAIL' and tr_aff_id=" + af_id + " and tr_date_arrivee is not null and tr_kilometrage_arrivee!=0 "+
                " order by tr_date_depart asc",null);

        double distance_travail = 0;

        if(null != cursor && cursor.getCount() > 0){
            while (cursor.moveToNext()) {
                distance_travail+= (cursor.getDouble(cursor.getColumnIndex("tr_kilometrage_arrivee")) -cursor.getDouble(cursor.getColumnIndex("tr_kilometrage_depart")));
            }
            cursor.close();
        }

         return distance_travail;
    }

    // distance totale de trajet parcourue trajet motif mission
    public double distanceTrajetMission(){
        int af_id=this.getChosenAffectationId();
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor cursor= db.rawQuery(" select tr_date_depart ,tr_kilometrage_depart , tr_date_arrivee, tr_kilometrage_arrivee from TRAJET  where tr_motif='MISSION' and tr_aff_id=" + af_id + " and tr_date_arrivee is not null and tr_kilometrage_arrivee!=0 "+
                " order by tr_date_depart asc",null);

        double distance_mission= 0;

        if(null != cursor && cursor.getCount() > 0){
            while (cursor.moveToNext()) {
                distance_mission+= (cursor.getDouble(cursor.getColumnIndex("tr_kilometrage_arrivee")) -cursor.getDouble(cursor.getColumnIndex("tr_kilometrage_depart")));
            }
            cursor.close();
        }

         return distance_mission;
    }

    // distance totale de trajet parcourue trajet motif personnel
    public double distanceTrajetPersonnel(){
        int af_id=this.getChosenAffectationId();
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor cursor= db.rawQuery(" select tr_date_depart ,tr_kilometrage_depart , tr_date_arrivee, tr_kilometrage_arrivee from TRAJET  where tr_motif='PERSONNEL' and tr_aff_id=" + af_id + " and tr_date_arrivee is not null and tr_kilometrage_arrivee!=0 "+
                " order by tr_date_depart asc",null);

        double distance_personnel= 0;

        if(null != cursor && cursor.getCount() > 0){
            while (cursor.moveToNext()) {
                distance_personnel+= (cursor.getDouble(cursor.getColumnIndex("tr_kilometrage_arrivee")) -cursor.getDouble(cursor.getColumnIndex("tr_kilometrage_depart")));
            }
            cursor.close();
        }

         return (distance_personnel);
    }

    // cout total des pleins
    public double coutTotalPleins(){
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor cursor= db.rawQuery(" select sum(pa_cost)  from PAYMENT where pa_aff_id=" + this.getChosenAffectationId() ,null);
        double coutTotalPleins = 0;
        if(null != cursor && cursor.getCount() > 0){
            cursor.moveToFirst();
            coutTotalPleins =cursor.getDouble(0);
            cursor.close();
        }

         return change(coutTotalPleins,4);
    }

    // get fuel type
    public int getFuelType(int vehicle_id) {
        int fuel_id = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select vh_fl_id from VEHICLE \n" +
                "where vh_active=1 and vh_id=" + vehicle_id;

        Cursor cursor = db.rawQuery(query, null);
       if(cursor!=null && cursor.getCount()>0){
            cursor.moveToFirst();
            fuel_id = cursor.getInt(cursor.getColumnIndex("vh_fl_id"));
            cursor.close();
        }

         return fuel_id;
    }

    // get fuel unit price
    public double getFuelUnitPrice(int fuel_id, String date) {
        double ct_price = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select ct_price from COST where ct_date< "+"'"+date+"'"+" and ct_fl_id=" + fuel_id+" order by ct_date desc";

        Cursor cursor = db.rawQuery(query, null);
        if(cursor!=null && cursor.getCount()>0){
            cursor.moveToFirst();
            ct_price = cursor.getDouble(cursor.getColumnIndex("ct_price"));
            cursor.close();
        }

         return ct_price;
    }

    // get qtee totale chargée de carburant
    public double qteeCarburant() {
        int af_id=this.getChosenAffectationId();
        int vehicle_id=this.GetAffectedVehicle(af_id);
        int fuel_id=this.getFuelType(vehicle_id);
        SQLiteDatabase db= this.getReadableDatabase();
        double qteePleins=0;

        Cursor cursor= db.rawQuery(" select *  from PAYMENT where pa_aff_id=" + af_id,null);
        if(null != cursor && cursor.getCount() > 0){
                while (cursor.moveToNext()) {
                    String datePaiment=cursor.getString(cursor.getColumnIndex("pa_date"));
                    double cost=cursor.getDouble(cursor.getColumnIndex("pa_cost"));
                    double unit_price=getFuelUnitPrice(fuel_id,datePaiment);
                    double qteeChargee=cost/unit_price;
                    qteePleins+=qteeChargee;
                }
            cursor.close();
        }

         return  change(qteePleins,4);
    }

    // get qtee totale chargée de carburant
    public double qteeCarburantParPaiement(int paiment_id) {
        int usr_id =this.GetLastUser_id();
        int af_id=this.getChosenAffectationId();
        int vehicle_id=this.GetAffectedVehicle(af_id);
        int fuel_id=this.getFuelType(vehicle_id);
        SQLiteDatabase db= this.getReadableDatabase();
        double qteeChargee=0;
        Cursor cursor= db.rawQuery(" select *  from PAYMENT where pa_aff_id=" + af_id+" and pa_id= "+paiment_id,null);

        if(null != cursor && cursor.getCount() > 0){
              cursor.moveToFirst();
              String datePaiment=cursor.getString(cursor.getColumnIndex("pa_date"));
              double cost=cursor.getDouble(cursor.getColumnIndex("pa_cost"));
              double unit_price=getFuelUnitPrice(fuel_id,datePaiment);
              qteeChargee=cost/unit_price;
              cursor.close();
        }

         return change(qteeChargee,4);
    }

    public double prixMoyLitre() {
        return  change(coutTotalPleins()/qteeCarburant(),4);
    }

    public Double consommationMoyenne() {
        int af_id=this.getChosenAffectationId();
        SQLiteDatabase db= this.getReadableDatabase();
        double avantDernierKilometrage=0;
        double dernierKilometrage=0;
        double avantDernierLitrage=0;
        Cursor cursor= db.rawQuery(" select *  from PAYMENT where pa_aff_id=" + af_id +" order by pa_date desc",null);
        if(null != cursor && cursor.getCount() > 0){
            cursor.moveToFirst();
            dernierKilometrage=cursor.getDouble(cursor.getColumnIndex("pa_distance"));
            cursor.moveToNext();
            avantDernierKilometrage=cursor.getDouble(cursor.getColumnIndex("pa_distance"));
            avantDernierLitrage=qteeCarburantParPaiement(cursor.getInt(cursor.getColumnIndex("pa_id")));
            cursor.close();
        }
        double number= (avantDernierLitrage/(dernierKilometrage-avantDernierKilometrage));
        return change(number,4);
    }

    // get cout carburant par jour
    public double coutCarburantJour() throws ParseException {
        int af_id=this.getChosenAffectationId();
        SQLiteDatabase db= this.getReadableDatabase();
        long nbDays=0;

        Cursor cursor= db.rawQuery(" select date, kilometrage from " +
                "(select km_date as date,km_kilometrage as kilometrage from KILOMETRAGE where km_aff_id=" + af_id +
                " UNION select pa_date as date,pa_distance as kilometrage from PAYMENT where pa_aff_id=" + af_id+
                " UNION select tr_date_depart as date,tr_kilometrage_depart as kilometrage from TRAJET where tr_aff_id=" + af_id +
                " UNION select tr_date_arrivee as date,tr_kilometrage_arrivee as kilometrage from TRAJET where tr_aff_id=" + af_id + " and tr_date_arrivee is not null and tr_kilometrage_arrivee!=0 "+
                "  ) order by date asc",null);

        if(null != cursor && cursor.getCount() > 0){
            cursor.moveToFirst();
            String firstDate=cursor.getString(cursor.getColumnIndex("date"));
            cursor.moveToLast();
            String lastDate=cursor.getString(cursor.getColumnIndex("date"));
            SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
            nbDays=getDifferenceDays(sdformat.parse(firstDate),sdformat.parse(lastDate));
            cursor.close();
        }

         return change(coutTotalPleins()/nbDays,4) ;
    }

    public long getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public ArrayList<HashMap<String, String>> getJournal(){
        int usr_id =this.GetLastUser_id();
        int af_id=this.getChosenAffectationId();
        SQLiteDatabase db= this.getReadableDatabase();
        ArrayList<HashMap<String, String>> journal = new ArrayList<>();
        Cursor cursor= db.rawQuery(" select id, date, kilometrage, type from " +
                "(select km_id as id, km_date as date,km_kilometrage as kilometrage, 'km' as type from KILOMETRAGE inner join AFFECTATION  on km_aff_id=af_id where af_active=1  and km_aff_id=" + af_id +
                " UNION select pa_id as id, pa_date as date,pa_distance as kilometrage, 'pa' as type from PAYMENT inner join AFFECTATION  on pa_aff_id=af_id where af_active=1  and  pa_aff_id=" + af_id+
                " UNION select tr_id as id, tr_date_depart as date,tr_kilometrage_depart as kilometrage, 'tr' as type from TRAJET inner join AFFECTATION  on tr_aff_id=af_id where af_active=1  and  tr_aff_id=" + af_id +
                "  ) order by date desc,kilometrage desc ",null);
        while (cursor.moveToNext()){
            HashMap<String, String> element = new HashMap<>();
            element.put("id", String.valueOf(cursor.getInt(cursor.getColumnIndex("id"))));
            element.put("date", cursor.getString(cursor.getColumnIndex("date")).substring(0,11));
            element.put("kilometrage", String.valueOf(cursor.getDouble(cursor.getColumnIndex("kilometrage"))));
            element.put("type", cursor.getString(cursor.getColumnIndex("type")));
            journal.add(element);
        }
        cursor.close();
         return  journal;
    }

    public boolean existTrajetsEnCours(){
        int af_id=this.getChosenAffectationId();
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor cursor= db.rawQuery("select * from TRAJET where tr_aff_id=" + af_id + " and  tr_kilometrage_arrivee=0 ",null);
        if(null != cursor && cursor.getCount() > 0){
            cursor.close();
             return  true;
        }else{
             return false;
        }
    }

    public ArrayList<HashMap<String, String>> trajets(){
        int af_id=this.getChosenAffectationId();
        SQLiteDatabase db= this.getReadableDatabase();
        ArrayList<HashMap<String, String>> journal = new ArrayList<>();

        Cursor cursor= db.rawQuery(" select * from TRAJET where tr_aff_id=" + af_id +" order by tr_date_depart desc",null);
        while (cursor.moveToNext()){
            HashMap<String, String> element = new HashMap<>();
            element.put("id", String.valueOf(cursor.getInt(cursor.getColumnIndex("tr_id"))));

            element.put("date_depart", cursor.getString(cursor.getColumnIndex("tr_date_depart")).substring(0,11));
            element.put("kilometrage_depart", cursor.getString(cursor.getColumnIndex("tr_kilometrage_depart"))+" Km");

            if( cursor.getDouble(cursor.getColumnIndex("tr_kilometrage_arrivee"))!=0){
                element.put("date_arrivee", cursor.getString(cursor.getColumnIndex("tr_date_arrivee")).substring(0,11));
                element.put("kilometrage_arrivee", cursor.getString(cursor.getColumnIndex("tr_kilometrage_arrivee"))+" Km");
            }else{
                element.put("date_arrivee", "--");
                element.put("kilometrage_arrivee", "--");
            }
            journal.add(element);
        }
        cursor.close();
         return  journal;
    }

    //insert GPS
    public long insertGPS(int gps_usr_id,
                              double gps_latitude,
                              double gps_longitude,
                                String gps_date,
                          int gps_sync_id,
                          String gps_sync_date,
                          int af_id) {
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put("gps_usr_id", gps_usr_id);
        cValues.put("gps_latitude", gps_latitude);
        cValues.put("gps_longitude", gps_longitude);
        cValues.put("gps_date", gps_date);
        cValues.put("gps_sync_id", gps_sync_id);
        cValues.put("gps_sync_date", gps_sync_date);
        cValues.put("gps_aff_id", af_id);
        long newRowId = db.insert("GPS", null, cValues);

         return newRowId;
    }

    //update gps FLAG
    public void UpdateGps_flag(int gps_id, int gps_sync_id, String gps_sync_date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cVals = new ContentValues();
        cVals.put("gps_sync_id", gps_id);
        cVals.put("gps_sync_date", gps_sync_date);
        db.update("GPS", cVals, "gps_id = " + gps_sync_id , null);

    }

    //check gps exists
    public boolean GPSExists(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM GPS where gps_sync_id=" + id;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor!=null){
            if(cursor.getCount() > 0){
                cursor.close();
                 return true;
            }   else{
                cursor.close();
                 return false;
            }
        }else{
             return false;
        }
    }

    //update Gps
    public long updateGpsFromServer(
                                  int gps_usr_id,
                                  double gps_latitude,
                                double gps_longitude,
                                  String gps_date,
                          int gps_sync_id,
                          String gps_sync_date) {
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put("gps_usr_id", gps_usr_id);
        cValues.put("gps_latitude", gps_latitude);
        cValues.put("gps_longitude", gps_longitude);
        cValues.put("gps_date", gps_date);
        cValues.put("gps_sync_date", gps_sync_date);
        long newRowId = db.update("GPS", cValues,"gps_id =" + gps_sync_id, null);
        
         return newRowId;
    }

    static double change(double value, int decimalpoint)
    {
        value = value * Math.pow(10, decimalpoint);
        value = Math.floor(value);
        value = value / Math.pow(10, decimalpoint);
        return value;
    }

    public String kmStatus(int id) {
        SQLiteDatabase readableDb = getReadableDatabase();
        Cursor cursor = readableDb.rawQuery(" select * from  KILOMETRAGE where km_id=" + id, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex("accepted"));
        }else{
            return null;
        }
    }

    public String kmCharge(int id) {
        SQLiteDatabase readableDb = getReadableDatabase();
        Cursor cursor = readableDb.rawQuery(" select * from  KILOMETRAGE where km_id=" + id, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex("montant_recharge"));
        }else{
            return null;
        }
    }

    public String trType(int id) {
        SQLiteDatabase readableDb = getReadableDatabase();
        Cursor cursor= readableDb.rawQuery(" select * from  TRAJET where tr_id=" +id,null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex("tr_type"));
        }else{
            return null;
        }
    }

    public boolean trTermine(int id) {
        SQLiteDatabase readableDb = getReadableDatabase();
        Cursor cursor= readableDb.rawQuery(" select * from  TRAJET where tr_id=" +id,null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            if(cursor.getDouble(cursor.getColumnIndex("tr_kilometrage_arrivee"))>0){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
}