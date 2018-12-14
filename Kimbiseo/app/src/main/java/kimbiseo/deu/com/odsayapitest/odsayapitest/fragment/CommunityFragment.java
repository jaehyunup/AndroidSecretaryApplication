package kimbiseo.deu.com.odsayapitest.odsayapitest.fragment;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import kimbiseo.deu.com.odsayapitest.R;

import static android.content.Context.MODE_PRIVATE;
import static android.view.Gravity.BOTTOM;


public class CommunityFragment extends Fragment
{
    /*private ListView listView;                      // 리스트뷰
    private ArrayAdapter<String> lva;*/
    private Button btnInsert;
    private EditText editMSG;
    private String[] data = {};
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.activity_community, container,false);
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swipelayout);

        btnInsert = (Button) layout.findViewById(R.id.btnSend);
        editMSG = (EditText) layout.findViewById(R.id.editMSG);
        listView = (ListView) layout.findViewById(R.id.listview);

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertDB(editMSG.getText().toString());
                Toast.makeText(layout.getContext(), "등록성공! 당겨서 새로고침", Toast.LENGTH_SHORT).show();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        searchDB();

                        refresh();

                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 4000);   //4초간 새로고침 화면이 보임
            }
        });

        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );



        searchDB();

        ArrayAdapter<String> lva = new ArrayAdapter<String>(
                this.getActivity(),
                android.R.layout.simple_list_item_1,
                data
        );

        listView.setAdapter(lva);


        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(layout.getContext());

                // AlertDialog 셋팅
                alertDialogBuilder
                        .setMessage("메모를 삭제하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("삭제",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        //메모삭제
                                        deleteDB(listView.getAdapter().getItem(position).toString());
                                        refresh();
                                    }
                                })
                        .setNegativeButton("취소",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        // 다이얼로그를 취소한다
                                        dialog.cancel();
                                    }
                                });

                // 다이얼로그 생성
                AlertDialog alertDialog = alertDialogBuilder.create();

                // 다이얼로그 보여주기
                alertDialog.show();
                return false;
            }
        });


        return layout;
    }

    public void insertDB(String chat) {
        SQLiteDatabase db = this.getActivity().openOrCreateDatabase("memo_data", MODE_PRIVATE, null);
        String sql = "INSERT INTO MEMO_TABLE(CHAT) VALUES( '" + chat + "' )";
        db.execSQL(sql);
    }

    public void deleteDB(String chat){
        SQLiteDatabase db = this.getActivity().openOrCreateDatabase("memo_data", MODE_PRIVATE, null);
        String sql = "DELETE FROM MEMO_TABLE WHERE CHAT = '"  + chat + "' ";
        db.execSQL(sql);
    }

    public void searchDB() {
        SQLiteDatabase db = this.getActivity().openOrCreateDatabase("memo_data", MODE_PRIVATE, null);
        Cursor c = db.rawQuery("SELECT * FROM MEMO_TABLE ORDER BY CHAT DESC LIMIT 20", null);

        if(c.getCount() == 0){
            data = null;
            data = new String[1];
            data[0] = "메모 없음";
            return;
        }
        String[] str = null;
        str = new String[c.getCount()];
        int i = 0;
        while(c.moveToNext()){
            str[i] = c.getString(c.getColumnIndex("CHAT"));
            i = i+1;
        }
        data = null;
        data = new String[c.getCount()];
        System.arraycopy(str, 0, data, 0, str.length);
    }

    private void refresh(){
        android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.detach(this).attach(this).commit();
    }

}