package kimbiseo.deu.com.odsayapitest.odsayapitest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DBHelper extends SQLiteOpenHelper {
    private Context context;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Toast.makeText(context, "업그레이드 완료", Toast.LENGTH_SHORT).show();
    }

    public void readDB(){
        SQLiteDatabase db = getReadableDatabase();
    }

    public void createData(){
        SQLiteDatabase db = getWritableDatabase();

        StringBuffer sb = new StringBuffer();
        sb.append(" CREATE TABLE TIME_TABLE ( ");
        sb.append(" CLASS_NAME, ");
        sb.append(" DAY, ");
        sb.append(" TIME, ");
        sb.append(" CLASS_NUM, ");
        sb.append(" COLOR ) ");

        db.execSQL(sb.toString());

    }

    public void addData(String name, String day, String time, String num, int color){

        SQLiteDatabase db = getWritableDatabase();

        StringBuffer sb = new StringBuffer();
        sb.append(" INSERT INTO TIME_TABLE ( ");
        sb.append(" CLASS_NAME, DAY, TIME, CLASS_NUM, COLOR ) ");
        sb.append(" VALUES ( ?, ?, ?, ?, ? ) ");
        db.execSQL(sb.toString(),
                new Object[]{ name, day, time, num, color });
        Toast.makeText(context, "시간표 설정 완료", Toast.LENGTH_SHORT).show();
    }

    public void createData2(){
        SQLiteDatabase db = getWritableDatabase();

        StringBuffer sb = new StringBuffer();
        sb.append(" CREATE TABLE DATA_TABLE ( ");
        sb.append(" UNI_NAME, ");
        sb.append(" READY_TIME ) ");

        db.execSQL(sb.toString());
    }

    public void addData2(String name, String time){

        SQLiteDatabase db = getWritableDatabase();

        StringBuffer sb = new StringBuffer();
        sb.append(" INSERT INTO DATA_TABLE ( ");
        sb.append(" UNI_NAME, READY_TIME ) ");
        sb.append(" VALUES ( ?, ? ) ");
        db.execSQL(sb.toString(),
                new Object[]{ name, time });
        Toast.makeText(context, "정보 설정 완료", Toast.LENGTH_SHORT).show();
    }

    public void createGPS(){
        SQLiteDatabase db = getWritableDatabase();

        StringBuffer sb = new StringBuffer();
        sb.append(" CREATE TABLE GPS_TABLE ( ");
        sb.append(" LATITUDE, ");
        sb.append(" LONGITUDE ) ");

        db.execSQL(sb.toString());

        Toast.makeText(context, "GPS Table 생성완료", Toast.LENGTH_SHORT).show();
    }

    public void addGPS(){

        SQLiteDatabase db = getWritableDatabase();

        StringBuffer sb = new StringBuffer();
        sb.append(" INSERT INTO GPS_TABLE ( ");
        sb.append(" LATITUDE, LONGITUDE ) ");
        sb.append(" VALUES ( 1, 1 ) ");
        db.execSQL(sb.toString());
        Toast.makeText(context, "gps 삽입 완료", Toast.LENGTH_SHORT).show();
    }

    public void updateGPS(double lati, double longi) {
        SQLiteDatabase db = getWritableDatabase();

        StringBuffer sb = new StringBuffer();

        sb.append(" UPDATE GPS_TABLE ");
        sb.append(" SET LATITUDE = ?, LONGITUDE = ?  ");
        db.execSQL(sb.toString(),
                new Object[]{ lati, longi });
        Toast.makeText(context, "gps 업데이트 완료", Toast.LENGTH_SHORT).show();
    }

    public void createMemo(){
        SQLiteDatabase db = getWritableDatabase();

        StringBuffer sb = new StringBuffer();
        sb.append(" CREATE TABLE MEMO_TABLE ( ");
        sb.append(" CHAT ) ");

        db.execSQL(sb.toString());

        Toast.makeText(context, "Memo Table 생성완료", Toast.LENGTH_SHORT).show();
    }

    public void addMemo(String str){

        SQLiteDatabase db = getWritableDatabase();

        StringBuffer sb = new StringBuffer();
        sb.append(" INSERT INTO MEMO_TABLE ( ");
        sb.append(" CHAT ) ");
        sb.append(" VALUES ( ? ) ");
        db.execSQL(sb.toString(),
                new Object[]{ str });
        Toast.makeText(context, "메모 삽입 완료", Toast.LENGTH_SHORT).show();
    }
}