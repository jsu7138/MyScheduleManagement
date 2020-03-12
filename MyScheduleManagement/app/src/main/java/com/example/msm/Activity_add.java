package com.example.msm;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.applikeysolutions.cosmocalendar.utils.SelectionType;
import com.applikeysolutions.cosmocalendar.view.CalendarView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Activity_add extends AppCompatActivity {
    private Db_helper db_admin;
    private SQLiteDatabase db;
    private CalendarView calendarView;
    private List<Calendar> days;
    private TextView tv_time_type, tv_time_Hour, tv_time_minute;
    private EditText editText;
    private LinearLayout layout_timeBox;
    int currentHour, currentMinute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedul);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_addVeiw));  //툴바 커스텀

        initViews();
        initDb();
        initTime(-1,0);
    }

    //초기화
    private void initViews() {
        calendarView = (CalendarView) findViewById(R.id.calendar_view);
        calendarView.setCalendarOrientation(OrientationHelper.HORIZONTAL);
        calendarView.setSelectionType(SelectionType.MULTIPLE);
        tv_time_type = (TextView) findViewById(R.id.tv_time_type);
        tv_time_Hour = (TextView) findViewById(R.id.tv_time_hour);
        tv_time_minute = (TextView) findViewById(R.id.tv_time_minute);
        editText = (EditText) findViewById(R.id.edit_context);
        layout_timeBox =(LinearLayout) findViewById(R.id.layout_timeBox);

        editText.addTextChangedListener(new TextWatcher()
        {   //줄수 제한
            String previousString = "";
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                previousString= s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editText.getLineCount() > 4) {
                    editText.setText(previousString);
                    editText.setSelection(editText.length());
                }
            }
        });

        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //텍스트 스크롤 무브
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

        layout_timeBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOnTimerDialog();
            }
        });
    }

    private void initDb(){
        db_admin = new Db_helper(getApplicationContext());  //DB생성
        try {
            //읽기, 쓰기 모드오픈/
             db = db_admin.getWritableDatabase();
        } catch (SQLiteException e) {
            //권한이 없거나 디스크용량 부족
            Toast.makeText(getApplicationContext(),"You do not have access to the database",Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void initTime(int hours, int minutes){
        if(hours == -1){
            currentHour = Integer.parseInt(new SimpleDateFormat("HH").format(new Date(System.currentTimeMillis())));
            currentMinute =Integer.parseInt(new SimpleDateFormat("mm").format(new Date(System.currentTimeMillis())));
        }else{
            currentHour = hours;
            currentMinute = minutes;
        }

        if(currentHour>11){
            tv_time_type.setText("오후");
            if(currentHour == 12)
                tv_time_Hour.setText(currentHour+"");
            else
                tv_time_Hour.setText((currentHour%12)+"");
        }else {
            tv_time_type.setText("오전");
            tv_time_Hour.setText(currentHour+"");
        }
        tv_time_minute.setText(currentMinute+"");
    }

    /*기능 관리*/
    private boolean db_add(){
        try {
            for( int i=0; i<days.size(); i++)
            {
                db_admin.infoTableAdd(
                        db,
                        new SimpleDateFormat("EE").format(days.get(i).getTime()),
                        days.get(i).get(Calendar.DAY_OF_MONTH),
                        days.get(i).get(Calendar.MONTH)+1,
                        days.get(i).get(Calendar.YEAR),
                        currentHour,
                        currentMinute,
                        editText.getText().toString(),
                        0
                        );
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private boolean compareDatabaseTable(){
        Cursor cursor = db_admin.infoTableSearch(db);
        while (cursor.moveToNext()){
            for( int i=0; i<days.size(); i++)
            {
                if(cursor.getInt(cursor.getColumnIndex("year")) == days.get(i).get(Calendar.YEAR) &&
                        cursor.getInt(cursor.getColumnIndex("month")) ==  (days.get(i).get(Calendar.MONTH)+1) &&
                        cursor.getInt(cursor.getColumnIndex("day_of_month")) == days.get(i).get(Calendar.DAY_OF_MONTH) &&
                        cursor.getInt(cursor.getColumnIndex("hour")) == currentHour &&
                        cursor.getInt(cursor.getColumnIndex("minute")) == currentMinute &&
                        cursor.getString(cursor.getColumnIndex("context")).equals(editText.getText().toString()) &&
                        cursor.getString(cursor.getColumnIndex("password")).equals(Admin_cooki.getPassword())
                ){
                    return false;
                }
            }
        }
        return true;
    }

    private void clearSelectionsMenuClick() {
        initTime(-1,0);
        editText.setText(null);
        calendarView.clearSelections(); //선택 해제
    }

    private void intentVeiw() {
        Intent i = new Intent(getApplicationContext(), Activity_Main.class);
        startActivity(i);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);   //스타트를 해주고 적용을 해야한다.
        finish();
    }

    private void setOnTimerDialog(){
        final View innerView = getLayoutInflater().inflate(R.layout.dialog_timepicker,null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(innerView);
        final AlertDialog dialog = builder.create();
        Button btn_positive = (Button) innerView.findViewById(R.id.timePickerBtn_Positive);
        Button btn_negative = (Button) innerView.findViewById(R.id.timePickerBtn_Negative);
        btn_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePicker timePicker = innerView.findViewById(R.id.timePickerView);
                initTime(timePicker.getHour(),timePicker.getMinute());
                dialog.dismiss();
            }
        });
        btn_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //다이얼로그 여백처리
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intentVeiw();
    }

    /*옵션 메뉴 커스텀 관리*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_upload:
                days = calendarView.getSelectedDates(); //선택된 날짜
                if(!(days.size() == 0 || TextUtils.isEmpty(editText.getText()))){
                    if(compareDatabaseTable()){
                        if(db_add()) {
                            intentVeiw();
                        }else
                            Toast.makeText(this.getApplicationContext(),"fail",Toast.LENGTH_SHORT).show();
                    }else
                        Toast.makeText(this.getApplicationContext(),"The schedule already exists",Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(this.getApplicationContext(),"Please write the date and content",Toast.LENGTH_SHORT).show();
                return true;

            case R.id.clear_selections:
                clearSelectionsMenuClick();
                return true;

            case R.id.show_back:
                intentVeiw();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
