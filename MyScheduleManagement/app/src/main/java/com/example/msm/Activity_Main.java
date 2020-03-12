package com.example.msm;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Point;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class Activity_Main extends AppCompatActivity {
    Context mainContext;
    RecyclerView my_recycler_view;
    MultipleListDataAdapter adapter;
    ArrayList<Admin_Info1> dataList;
    Db_helper db_admin;
    SQLiteDatabase db;
    private Animation fab_open, fab_close;
    public static FloatingActionButton fab_main, fab_item_delete;
    public static boolean fab_switch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));  //툴바 커스텀
        init();
        initDB();
        initList();
        fabViewOn();
        recyclerViewOn();

    }
    //초기화
    private void init(){
        mainContext = getApplicationContext();
        fab_main = (FloatingActionButton) findViewById(R.id.fab_main);
        fab_item_delete = (FloatingActionButton) findViewById(R.id.fab_item_delet);
        dataList = new ArrayList<>();
        Admin_cooki.setArrayList(new ArrayList<Admin_Info2>());
        windowSizeSetting();

    }

    private void initDB(){
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

    private void initList(){
        try{
            //레코드 포인터 생성
            Cursor cursor = db_admin.infoTableSearch(db);
            while(cursor.moveToNext()){
                Admin_Info1 admin_info = new Admin_Info1();
                ArrayList<Admin_Info2> list = new ArrayList<>();
                int years = cursor.getInt(cursor.getColumnIndex("year"));
                int months = cursor.getInt(cursor.getColumnIndex("month"));
                int day_of_month = cursor.getInt(cursor.getColumnIndex("day_of_month"));
                int dayColor = getColorsFind();
                do{
                    if(years == cursor.getInt(cursor.getColumnIndex("year")) && months == cursor.getInt(cursor.getColumnIndex("month"))){
                        if(day_of_month != cursor.getInt(cursor.getColumnIndex("day_of_month"))){
                            day_of_month = cursor.getInt(cursor.getColumnIndex("day_of_month"));
                            dayColor = getColorsFind();
                        }
                        list.add(new Admin_Info2(
                                cursor.getInt(cursor.getColumnIndex("year")),
                                cursor.getInt(cursor.getColumnIndex("month")),
                                cursor.getString(cursor.getColumnIndex("week")),
                                cursor.getInt(cursor.getColumnIndex("day_of_month")),
                                cursor.getInt(cursor.getColumnIndex("hour")),
                                cursor.getInt(cursor.getColumnIndex("minute")),
                                cursor.getString(cursor.getColumnIndex("context")),
                                dayColor,
                                cursor.getInt(cursor.getColumnIndex("checking"))
                        ));
                    }else {
                        cursor.moveToPrevious();
                        break;
                    }
                }while (cursor.moveToNext());

                admin_info.setYear(years);
                admin_info.setMonth(months);
                admin_info.setArrayList(list);
                dataList.add(admin_info);
            }
        }catch (SQLiteException e){
        }

        if(dataList.isEmpty()){
            //리스트가 비어있다면
        }
    }

    /*기능 관리*/
    private int getColorsFind(){
        Random random = new Random();
        int []a = this.getResources().getIntArray(R.array.mycolors);

        return a[random.nextInt(a.length)];
    }

    private void windowSizeSetting(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Admin_cooki.setWindow_size_x(size.x);
        Admin_cooki.setWindow_size_y(size.y);
    }

    private void fabViewOn(){
        fab_switch = true;
        final AlertDialog.Builder builder = fabDialogView();
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        fab_main.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                if(fab_main.getVisibility() == VISIBLE && fab_switch){
                    setOnfabDoVisibleManager(VISIBLE ,fab_open,true,false);
                }else {
                    setOnfabDoVisibleManager(INVISIBLE,fab_close,false,false);
                }
            }
        });
        fab_item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.show();
            }
        });
    }

    @SuppressLint("RestrictedApi")
    private void setOnfabDoVisibleManager(int doVisible,Animation anime, Boolean clickBool,Boolean mainExtend){
        // fab_main.setImageResource(R.drawable.ic_add);
        fab_item_delete.startAnimation(anime);
        fab_item_delete.setClickable(clickBool);
        fab_item_delete.setVisibility(doVisible);
        fab_switch = !fab_switch;
        if(mainExtend){
            fab_main.setVisibility(INVISIBLE);
            fab_main.setAnimation(fab_close);
            fab_main.setClickable(false);
            fab_switch = true;
        }
    }

    private AlertDialog.Builder fabDialogView(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("정말 삭제합니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dataBaseDeleteTable();
                setOnfabDoVisibleManager(INVISIBLE,fab_close,false,true);
                return;
            }
        });
        builder.setNegativeButton("취소",null);

        return builder;
    }

    private void intentVeiw(Class nextContext) {
        Intent i = new Intent(getApplicationContext(),nextContext);
        startActivityForResult(i, 0);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        finish();
    }

    private void recyclerViewOn(){
        my_recycler_view = (RecyclerView) findViewById(R.id.recyclerMainView);
        my_recycler_view.setHasFixedSize(true);
        adapter = new MultipleListDataAdapter(dataList, this);
        my_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        my_recycler_view.setAdapter(adapter);

        recyclerViewPositionFind(my_recycler_view);
    }

    private void recyclerViewPositionFind(RecyclerView recyclerView){
        //초기 뷰의 위치를 선정하는 메소드
        int month = Integer.parseInt(new SimpleDateFormat("MM", Locale.getDefault()).format(Calendar.getInstance().getTime()));
        int year = Integer.parseInt(new SimpleDateFormat("YYYY", Locale.getDefault()).format(Calendar.getInstance().getTime()));
        int positionX = dataList.indexOf(new Admin_Info1(year,month));  //현재 날짜가 등록되어 있는지 우선 판별하여 포지션을 리턴받는다.
        if(dataList.size() != 0) {
            if (positionX > -1)
                //같은 날짜가 존재한다면 해당 날짜의 뷰를 먼저 보여준다.
                recyclerViewSetScrollToPosition(positionX, recyclerView);
            else
                //존재하지 않는다면 가장 최근 등록한 날짜의 뷰를 보여준다.
                recyclerViewSetScrollToPosition(dataList.size()-1, recyclerView);
        }
    }

    private void recyclerViewSetScrollToPosition(int value, RecyclerView recyclerView){
        //뷰의 포지션을 이동
        recyclerView.scrollToPosition(value);
    }

    private void dataBaseDeleteTable(){
        int year, month, tmpArrayPosition, tmpArrayPosition2;
        ArrayList<Admin_Info2> tmpArrayList;
        Admin_Info2 tmpList;
        for(int i = 0; i < Admin_cooki.getArrayList().size() ; i++) {
            //내부 카드뷰 찾기
            year = Admin_cooki.getArrayList().get(i).year;
            month = Admin_cooki.getArrayList().get(i).month;
            tmpArrayPosition = dataList.indexOf(new Admin_Info1(year,month));
            tmpArrayList = dataList.get(tmpArrayPosition).arrayList;
            tmpArrayPosition2 = tmpArrayList.indexOf(Admin_cooki.getArrayList().get(i));

            //DB 테이블 삭제
            tmpList = dataList.get(tmpArrayPosition).arrayList.get(tmpArrayPosition2);
            db_admin.infoTableRemove(db,tmpList.day,tmpList.month,tmpList.year,tmpList.hour,tmpList.minute,tmpList.context,tmpList.checking);

            //내부 카드뷰 삭제(상세)
            dataList.get(tmpArrayPosition).arrayList.remove(tmpArrayPosition2);
            if(dataList.get(tmpArrayPosition).arrayList.isEmpty()){
                //외부 카드뷰 삭제(년,월)
                dataList.remove(tmpArrayPosition);
            }
        }
        Admin_cooki.setArrayList(new ArrayList<Admin_Info2>());
        adapter.notifyDataSetChanged();
    }

    //앱 내장 버튼 관리
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intentVeiw(Activity_login.class);
    }

    /*옵션 메뉴 커스텀 관리*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_add:
                Admin_cooki.setArrayList(new ArrayList<Admin_Info2>());
                intentVeiw(Activity_add.class);
                return true;
            case R.id.show_logout:
                Admin_cooki.clearCookie();
                intentVeiw(Activity_login.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}