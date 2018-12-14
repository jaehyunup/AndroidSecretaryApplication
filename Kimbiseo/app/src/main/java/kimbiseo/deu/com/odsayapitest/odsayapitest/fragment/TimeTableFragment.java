package kimbiseo.deu.com.odsayapitest.odsayapitest.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import com.nhn.android.data.DBAdapter;
import com.odsay.odsayandroidsdk.ODsayService;

import org.json.JSONException;

import kimbiseo.deu.com.odsayapitest.R;
import kimbiseo.deu.com.odsayapitest.odsayapitest.DBHelper;
import kimbiseo.deu.com.odsayapitest.odsayapitest.GpsInfo;

import static android.content.Context.MODE_PRIVATE;
import static android.view.Gravity.BOTTOM;


public class TimeTableFragment extends Fragment {
    TextView txt;
    ODsayService odsayService;

    private TextView[] mTextView = new TextView[40];
    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private boolean isPermission = false;
    private GpsInfo gps;
    public SharedPreferences prefs;


    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        TableLayout layout = (TableLayout) inflater.inflate(R.layout.activity_table_main, container, false);
        TableLayout tableLayout = (TableLayout) layout.findViewById(R.id.time_table); // 테이블 레이아웃

        mTextView[0] = (TextView) layout.findViewById(R.id.mon_1);
        mTextView[1] = (TextView) layout.findViewById(R.id.mon_2);
        mTextView[2] = (TextView) layout.findViewById(R.id.mon_3);
        mTextView[3] = (TextView) layout.findViewById(R.id.mon_4);
        mTextView[4] = (TextView) layout.findViewById(R.id.mon_5);
        mTextView[5] = (TextView) layout.findViewById(R.id.mon_6);
        mTextView[6] = (TextView) layout.findViewById(R.id.mon_7);
        mTextView[7] = (TextView) layout.findViewById(R.id.mon_8);

        mTextView[8] = (TextView) layout.findViewById(R.id.tue_1);
        mTextView[9] = (TextView) layout.findViewById(R.id.tue_2);
        mTextView[10] = (TextView) layout.findViewById(R.id.tue_3);
        mTextView[11] = (TextView) layout.findViewById(R.id.tue_4);
        mTextView[12] = (TextView) layout.findViewById(R.id.tue_5);
        mTextView[13] = (TextView) layout.findViewById(R.id.tue_6);
        mTextView[14] = (TextView) layout.findViewById(R.id.tue_7);
        mTextView[15] = (TextView) layout.findViewById(R.id.tue_8);

        mTextView[16] = (TextView) layout.findViewById(R.id.wed_1);
        mTextView[17] = (TextView) layout.findViewById(R.id.wed_2);
        mTextView[18] = (TextView) layout.findViewById(R.id.wed_3);
        mTextView[19] = (TextView) layout.findViewById(R.id.wed_4);
        mTextView[20] = (TextView) layout.findViewById(R.id.wed_5);
        mTextView[21] = (TextView) layout.findViewById(R.id.wed_6);
        mTextView[22] = (TextView) layout.findViewById(R.id.wed_7);
        mTextView[23] = (TextView) layout.findViewById(R.id.wed_8);

        mTextView[24] = (TextView) layout.findViewById(R.id.thu_1);
        mTextView[25] = (TextView) layout.findViewById(R.id.thu_2);
        mTextView[26] = (TextView) layout.findViewById(R.id.thu_3);
        mTextView[27] = (TextView) layout.findViewById(R.id.thu_4);
        mTextView[28] = (TextView) layout.findViewById(R.id.thu_5);
        mTextView[29] = (TextView) layout.findViewById(R.id.thu_6);
        mTextView[30] = (TextView) layout.findViewById(R.id.thu_7);
        mTextView[31] = (TextView) layout.findViewById(R.id.thu_8);

        mTextView[32] = (TextView) layout.findViewById(R.id.fri_1);
        mTextView[33] = (TextView) layout.findViewById(R.id.fri_2);
        mTextView[34] = (TextView) layout.findViewById(R.id.fri_3);
        mTextView[35] = (TextView) layout.findViewById(R.id.fri_4);
        mTextView[36] = (TextView) layout.findViewById(R.id.fri_5);
        mTextView[37] = (TextView) layout.findViewById(R.id.fri_6);
        mTextView[38] = (TextView) layout.findViewById(R.id.fri_7);
        mTextView[39] = (TextView) layout.findViewById(R.id.fri_8);


        selectDB();

        return layout;


        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard


    }

    public void selectDB() {
        SQLiteDatabase db = this.getActivity().openOrCreateDatabase("time_table", MODE_PRIVATE, null);
        Cursor c = db.rawQuery("SELECT * FROM TIME_TABLE", null);

        int itime = 0;

        while (c.moveToNext()) {
            String class_name = c.getString(c.getColumnIndex("CLASS_NAME"));
            String day = c.getString(c.getColumnIndex("DAY"));
            String time = c.getString(c.getColumnIndex("TIME"));
            String class_num = c.getString(c.getColumnIndex("CLASS_NUM"));
            int color = c.getInt(c.getColumnIndex("COLOR"));

            itime = Integer.parseInt(time);

            switch (day) {
                case "mon":
                    itime = itime - 1;
                    break;
                case "tue":
                    itime = itime + 7;
                    break;
                case "wed":
                    itime = itime + 15;
                    break;
                case "thu":
                    itime = itime + 23;
                    break;
                case "fri":
                    itime = itime + 31;
                    break;
            }

            String chk = class_name + "\n(" + class_num + ")";

            if ((itime % 8 != 0) && (chk.equals(mTextView[itime - 1].getText()))) {
                mTextView[itime - 1].setGravity(Gravity.CENTER | BOTTOM);
                ColorDrawable cd;
                cd = (ColorDrawable) mTextView[itime - 1].getBackground();
                mTextView[itime].setBackgroundColor(cd.getColor());
            } else {
                mTextView[itime].setText(class_name + "\n(" + class_num + ")");
                mTextView[itime].setBackgroundColor(color);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_ACCESS_FINE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            isAccessFineLocation = true;
        } else if (requestCode == PERMISSIONS_ACCESS_COARSE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            isAccessCoarseLocation = true;
        }

        if (isAccessFineLocation && isAccessCoarseLocation) {
            isPermission = true;
        }
    }

    private void callPermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && this.getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_ACCESS_FINE_LOCATION);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && this.getContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_ACCESS_COARSE_LOCATION);
        } else {
            isPermission = true;
        }
    }
}