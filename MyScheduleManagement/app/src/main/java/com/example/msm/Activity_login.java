package com.example.msm;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class Activity_login extends AppCompatActivity {
    EditText input;
    Button btn_contine, btn_createAccount;
    Db_helper passDB;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        init_DB();
    }

    //초기화
    private void init() {
        //패스워드 표시
        input = (EditText) findViewById(R.id.edit_password);
        btn_contine = (Button) findViewById(R.id.btn_login);
        btn_createAccount = (Button) findViewById(R.id.btn_addPassword);

        //버튼 이벤트 활성화
        btn_contine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginIntent();
            }
        });
        btn_createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });

        //패스워드 뷰 기능추가
        passwordEdit(input);
    }

    private void init_DB(){
        passDB = new Db_helper(getApplicationContext());  //DB생성
        try {
            //읽기, 쓰기 모드오픈/
            db = passDB.getWritableDatabase();
        } catch (SQLiteException e) {
            //권한이 없거나 디스크용량 부족
            Toast.makeText(getApplicationContext(),"You do not have access to the database",Toast.LENGTH_LONG).show();
            finish();
       }
    }

    //기능 구현
    private void loginIntent(){
        if(!db_search(input.getText().toString())){
            Admin_cooki.setPassword(input.getText().toString());
            intentVeiw(Activity_Main.class);
        }else
            Toast.makeText(getApplicationContext(),"Password is wrong",Toast.LENGTH_SHORT).show();
    }

    private void createAccount(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_newacc, null);
        builder.setView(view);

        final EditText alert_edt1 = (EditText) view.findViewById(R.id.alert_passEdit1);
        final EditText alert_edt2 = (EditText) view.findViewById(R.id.alert_passEdit2);
        final Button alert_btn = (Button) view.findViewById(R.id.alert_passCreate);
        final AlertDialog dialog = builder.create();
        passwordEdit(alert_edt1);
        passwordEdit(alert_edt2);

        alert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alert_edt1.getText().toString().equals(alert_edt2.getText().toString()) && !TextUtils.isEmpty(alert_edt1.getText()))
                {
                    if(db_search(alert_edt1.getText().toString())){
                        db_Add(alert_edt1.getText().toString());
                        dialog.cancel();
                    }else
                        Toast.makeText(getApplicationContext(),"Password already exists",Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(),"Two passwords do not match or are null",Toast.LENGTH_SHORT).show();
            }
        });
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void db_Add(String value){
        String sqlInsert = String.format(Locale.US,"INSERT INTO %s VALUES (%s);",passDB.table_Pass,value);
        db.execSQL(sqlInsert);
        Toast.makeText(getApplicationContext(),"success!",Toast.LENGTH_SHORT).show();
    }

    private boolean db_search(String value){
        try{
            Cursor cursor = db.rawQuery("SELECT * FROM "+ passDB.table_Pass+";" , null);        //레코드 포인터 생성
            while(cursor.moveToNext()){
               String content =  cursor.getString(cursor.getColumnIndex("password"));
               if(content.equals(value)){
                   //이미 존재하는 패스워드
                   return false;
               }
            }
        }catch (SQLiteException e){
            //DB 안의 데이터가 존재 하지 않음.
        }
        //일치하는 패스워드가 없음.
        return true;
    }

    private void intentVeiw(Class nextContext) {
        Intent i = new Intent(getApplicationContext(),nextContext);
        startActivity(i);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        finish();
    }

    private void passwordEdit(EditText edit){
        edit.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );
        edit.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }
}
