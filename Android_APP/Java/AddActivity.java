package com.example.minch.smart_home_mate_rsapberry;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {
    Button btnAdd, btnBack, btnTest;
    EditText inputId, inputPw, inputCode, inputName, inputRePw;
    myDBHelper myHelper;//그냥 쓸수없음 객체생성하고 사용, 객체선언
    SQLiteDatabase sqlDB, sqlDB_t;//쿼리문 수행용 객체선언   .execSQL();사용용
    String pass1=null;
    String pass2=null;
    Cursor s;
    int Reflag=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);


        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnTest = (Button) findViewById(R.id.btnTest);
        inputId = (EditText) findViewById(R.id.inputId);
        inputPw = (EditText) findViewById(R.id.inputPw);
        inputCode = (EditText) findViewById(R.id.inputCode);
        inputName = (EditText) findViewById(R.id.inputName);
        inputRePw = (EditText) findViewById(R.id.inputRePw);
        myHelper = new myDBHelper(this);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
//                sqlDB.close();//데이터베이스를 열었으면 닫아야 한다
//                sqlDB_t.close();//데이터베이스를 열었으면 닫아야 한다
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String code = inputCode.getText().toString();
                String code = "AADF34";
                if(Reflag==2 && code.equals(inputCode.getText().toString())) {
                    pass1 = inputPw.getText().toString();
                    pass2 = inputRePw.getText().toString();

                    if (pass1.equals(pass2)) {
                        sqlDB = myHelper.getWritableDatabase();//쓰기전용으로 DB연다
                        sqlDB.execSQL("INSERT INTO userTBL VALUES ('"+inputId.getText().toString()+"','"+inputName.getText().toString()+"','"+inputPw.getText().toString()+"');");
                        //sqlDB.close();//레코드 추가작업 끝나면 DB닫기
                        //사용자는 레코드가 추가되면 잘 입역되었다는 안내맨트 친절하게 전달
                        Toast.makeText(getApplicationContext(), "회원가입 완료", Toast.LENGTH_LONG).show();
                        finish();

                    } else {
                        Toast.makeText(getApplicationContext(), "비밀번호가 동일하지 않습니다.\n다시 입력해주세요.", Toast.LENGTH_LONG).show();
                    }
                }
                else
                    if(Reflag == 1) {
                        if (code.equals(inputCode.getText().toString())){
                            Toast.makeText(getApplicationContext(), "ID 중복확인을 해주세요.", Toast.LENGTH_LONG).show();
                        }

                        else{
                            Toast.makeText(getApplicationContext(), "ID 중복확인 버튼과 Code를 확인해 주세요", Toast.LENGTH_LONG).show();
                        }

                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Code를 확인 해주세요.", Toast.LENGTH_LONG).show();
                    }

            }
        });

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlDB_t = myHelper.getReadableDatabase();
                Cursor cursor;
                cursor = sqlDB_t.rawQuery("SELECT * FROM userTBL;", null); // 개수 2차원 배열 형태로 저장됨
                String edt1 = null;
                String uId1 = null;

//                    Toast.makeText(getApplicationContext(), "중복확인 되었습니다.", Toast.LENGTH_SHORT).show();
//                    Reflag = 1;
                int result = Reflag;


                while (cursor.moveToNext()) {
                    edt1 = cursor.getString(0);
                    uId1 = inputId.getText().toString();
                    if (edt1.equals(uId1)) {
                        Toast.makeText(getApplicationContext(), "동일한 ID가 존재합니다.", Toast.LENGTH_SHORT).show();
                        result ++;
                        Reflag = 1;
                        break;
                    }
                }
                if (result == Reflag) {
                    Toast.makeText(getApplicationContext(), "중복확인 되었습니다.", Toast.LENGTH_SHORT).show();
                    Reflag = 2;
                }

                cursor.close();//cursor를 사용했으면 종료해줘야함
                //sqlDB_t.close();//데이터베이스를 열었으면 닫아야 한다
            }
        });
    }//onCreate

    public static class myDBHelper extends SQLiteOpenHelper {
        //2개의 필수 메소드 onCreate onUpgrave()
        //myBDhelper클래스 내부에 커서를 놓고 메소드 override해서 씀
        //myBDhelper는 생성자가 데이터베이스를 만듦 SQLiteopenHelper();
        public myDBHelper(Context context){//클래스 추가 Context 프로젝트에 DB생성
            super(context,"userDB",null,1);//실질적인 DB생성위치 SQLiteOpenHelper(); 생성자
        }
        @Override//code override 에서 추가
        public void onCreate(SQLiteDatabase db) {//데이터베이스생성
            db.execSQL("CREATE TABLE userTBL ( uId CHAR(30) PRIMARY KEY,uName CHAR(10),uPw CHAR(32));");//SQL문을 수행하는 메소드

            //db.close();//레코드 추가작업 끝나면 DB닫기
        }
        //추가 메소드가 필요함 onUpgrade()필수메소드 추가되면 에러메시지 사라짐
        @Override//code override 에서 추가,
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {//테이블이 있으면 삭제하고 테이블을 다시 생성
            //데이터베이스의 테이블을 신버전으로 바꿔라
            db.execSQL("DROP TABLE IF EXISTS userTBL");//테이블이 있으면 삭제하는 쿼리
            //테이블 없으면 테이블 생성 위에서 썻던것 호출
            onCreate(db);
        }


    }//public static class myDBHelper extends SQLiteOpenHelper

}//public class AddActivity extends AppCompatActivity
