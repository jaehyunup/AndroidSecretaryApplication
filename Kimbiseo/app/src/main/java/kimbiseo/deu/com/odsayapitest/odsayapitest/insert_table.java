package kimbiseo.deu.com.odsayapitest.odsayapitest;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import kimbiseo.deu.com.odsayapitest.R;
import kimbiseo.deu.com.odsayapitest.odsayapitest.mainPackage.MainPage;

import java.util.Random;

public class insert_table extends AppCompatActivity
{
    private DBHelper dbHelper;
    private CheckBox[] mCheckBox = new CheckBox[40];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_table);

        this.setResult(0);

        Button btnCreateSqlite = (Button) findViewById(R.id.MakeDB);
        Button btnInsertData = (Button) findViewById(R.id.btnInsertData);
        Button btnDeleteData = (Button) findViewById(R.id.btnDeleteData);
        mCheckBox[0] = (CheckBox) findViewById(R.id.mon_1);
        mCheckBox[1] = (CheckBox) findViewById(R.id.mon_2);
        mCheckBox[2] = (CheckBox) findViewById(R.id.mon_3);
        mCheckBox[3] = (CheckBox) findViewById(R.id.mon_4);
        mCheckBox[4] = (CheckBox) findViewById(R.id.mon_5);
        mCheckBox[5] = (CheckBox) findViewById(R.id.mon_6);
        mCheckBox[6] = (CheckBox) findViewById(R.id.mon_7);
        mCheckBox[7] = (CheckBox) findViewById(R.id.mon_8);

        mCheckBox[8] = (CheckBox) findViewById(R.id.tue_1);
        mCheckBox[9] = (CheckBox) findViewById(R.id.tue_2);
        mCheckBox[10] = (CheckBox) findViewById(R.id.tue_3);
        mCheckBox[11] = (CheckBox) findViewById(R.id.tue_4);
        mCheckBox[12] = (CheckBox) findViewById(R.id.tue_5);
        mCheckBox[13] = (CheckBox) findViewById(R.id.tue_6);
        mCheckBox[14] = (CheckBox) findViewById(R.id.tue_7);
        mCheckBox[15] = (CheckBox) findViewById(R.id.tue_8);

        mCheckBox[16] = (CheckBox) findViewById(R.id.wed_1);
        mCheckBox[17] = (CheckBox) findViewById(R.id.wed_2);
        mCheckBox[18] = (CheckBox) findViewById(R.id.wed_3);
        mCheckBox[19] = (CheckBox) findViewById(R.id.wed_4);
        mCheckBox[20] = (CheckBox) findViewById(R.id.wed_5);
        mCheckBox[21] = (CheckBox) findViewById(R.id.wed_6);
        mCheckBox[22] = (CheckBox) findViewById(R.id.wed_7);
        mCheckBox[23] = (CheckBox) findViewById(R.id.wed_8);

        mCheckBox[24] = (CheckBox) findViewById(R.id.thu_1);
        mCheckBox[25] = (CheckBox) findViewById(R.id.thu_2);
        mCheckBox[26] = (CheckBox) findViewById(R.id.thu_3);
        mCheckBox[27] = (CheckBox) findViewById(R.id.thu_4);
        mCheckBox[28] = (CheckBox) findViewById(R.id.thu_5);
        mCheckBox[29] = (CheckBox) findViewById(R.id.thu_6);
        mCheckBox[30] = (CheckBox) findViewById(R.id.thu_7);
        mCheckBox[31] = (CheckBox) findViewById(R.id.thu_8);

        mCheckBox[32] = (CheckBox) findViewById(R.id.fri_1);
        mCheckBox[33] = (CheckBox) findViewById(R.id.fri_2);
        mCheckBox[34] = (CheckBox) findViewById(R.id.fri_3);
        mCheckBox[35] = (CheckBox) findViewById(R.id.fri_4);
        mCheckBox[36] = (CheckBox) findViewById(R.id.fri_5);
        mCheckBox[37] = (CheckBox) findViewById(R.id.fri_6);
        mCheckBox[38] = (CheckBox) findViewById(R.id.fri_7);
        mCheckBox[39] = (CheckBox) findViewById(R.id.fri_8);
        final View[] mV = new View[40];
        final int[] colorRGB = new int[40];
        final ColorDrawable[] color = new ColorDrawable[40];

        //checkbox 선택시 배경 변경
        for(int i=0; i<40; i++){
            final int index;
            index = i;
            mCheckBox[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mCheckBox[index].isChecked()){
                        if(mCheckBox[index].getText().equals(" ")){
                            mV[index] = v;
                        }
                        else{
                            color[index] = (ColorDrawable) mCheckBox[index].getBackground();
                            colorRGB[index] = color[index].getColor();
                        }
                        v.setBackgroundColor(Color.rgb(142,204,234));
                    }
                    else{
                        if(mCheckBox[index].getText().equals(" ")){
                            v.setBackgroundResource(R.drawable.checkbox_stroke);
                        }
                        else{
                            v.setBackgroundColor(colorRGB[index]);
                        }
                    }
                }
            });
        }

        //SQLite 생성 및 삽입
        btnCreateSqlite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dbHelper = new DBHelper(insert_table.this,"time_table", null, 1);
                dbHelper.readDB();
                dbHelper.createData();

                String str;
                String name;
                String day = "";
                int time;
                String num;
                int target_num;
                for(int i=0 ; i<40 ; i++){
                    final int no;
                    no = i;
                    if(!mCheckBox[no].getText().equals(" ")){
                        str = (String) mCheckBox[no].getText();
                        target_num = str.indexOf("(");
                        name = str.substring(0, target_num - 1);
                        num = str.substring(target_num + 1, str.length() - 1);
                        int day_time = Range(no);
                        switch (day_time){
                            case 1:
                                day = "mon";
                                break;
                            case 2:
                                day = "tue";
                                break;
                            case 3:
                                day = "wed";
                                break;
                            case 4:
                                day = "thu";
                                break;
                            case 5:
                                day = "fri";
                                break;
                        }
                        time = (no % 8) + 1;
                        String strTIME = String.valueOf(time);
                        ColorDrawable cd;
                        cd = (ColorDrawable) mCheckBox[no].getBackground();

                        dbHelper.addData(name, day, strTIME, num, cd.getColor());

                        insert_table.this.setResult(1);

                        finish();
                    }
                }
            }
        });

        //시간표 정보 입력
        btnInsertData.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(insert_table.this);
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.insert_dialog, null);
                builder.setView(view);
                final Button submit = (Button) view.findViewById(R.id.buttonSubmit);
                final EditText ClassName = (EditText) view.findViewById(R.id.editClassName);
                final EditText ClassNum = (EditText) view.findViewById(R.id.editClassNum);

                final AlertDialog dialog = builder.create();
                submit.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Random random = new Random();
                        int r = random.nextInt(256);
                        int g = random.nextInt(256);
                        int b = random.nextInt(256);
                        for(int i=0; i<40; i++){
                            final int number;
                            number = i;
                            if(mCheckBox[number].isChecked()){
                                mV[number].setBackgroundColor(Color.rgb(r,g,b));
                                mCheckBox[number].setText((CharSequence) ClassName.getText() + "\n(" + ClassNum.getText() + ")");
                                mCheckBox[number].setChecked(false);
                            }
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        //시간표 정보 삭제
        btnDeleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0; i<40; i++){
                    if(mCheckBox[i].isChecked()){
                        mCheckBox[i].setText(" ");
                        mCheckBox[i].setBackgroundResource(R.drawable.checkbox_stroke);
                        mCheckBox[i].setChecked(false);
                    }
                }
            }
        });

    }

    public int Range(int a){
        if(a >= 0 && a <= 7){
            return 1;
        }
        if(a >= 8 && a <= 15){
            return 2;
        }
        if(a >= 16 && a <= 23){
            return 3;
        }
        if(a >= 24 && a <= 31){
            return 4;
        }
        if(a >= 32 && a <= 39){
            return 5;
        }
        return 0;
    }
}
