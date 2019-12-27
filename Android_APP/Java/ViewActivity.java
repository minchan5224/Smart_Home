package com.example.minch.smart_home_mate_rsapberry;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ViewActivity extends AppCompatActivity {

    TextView R_inCenText, resultText;
    Button btnCtrl, btnBack;
    ImageView imageValue;
    int select, tryFlg;
    String myValue, mySelect, myChoice, htmlPageUrl, htmlContentInStringFormat;
    JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        Intent intent = getIntent();
        select = intent.getExtras().getInt("Num");
        mySelect = "3";
        if(select == 1){
            htmlPageUrl = "https://capstoneteamh.000webhostapp.com/gas_read.php";
            myChoice = "1";

        }
        else if(select == 2){
            //htmlPageUrl = "https://capstoneteamh.000webhostapp.com/fish_read.php";
            //myChoice = "2";
        }
        jsoupAsyncTask.execute();

        R_inCenText = (TextView) findViewById(R.id.R_inCenText); //imageView 안쪽 text
        resultText = (TextView) findViewById(R.id.resultText); //imageView 아래쪽 text
        btnCtrl = (Button) findViewById(R.id.btnCtrl); // 동작 버튼
        btnBack = (Button) findViewById(R.id.btnBack); // 뒤로가기
        imageValue = (ImageView) findViewById(R.id.imageValue); //이미지 뷰

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnCtrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                tryFlg=0;
                Intent intent = getIntent();
                if(tryFlg == 0) {
                    if (htmlContentInStringFormat.equals("1") && select == 1) {//가스밸브 잠김상태
                        tryFlg++;
                        myValue = "2";
                        new Thread() {
                            public void run() {
                                HttpPostData();//열기
                            }
                        }.start();
                        try{
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        finish();
                        startActivity(intent);

                    } else if (htmlContentInStringFormat.equals("2") && select == 1) {//가스밸브 열림상태
                        tryFlg++;
                        myValue = "1";
                        new Thread() {
                            public void run() {
                                HttpPostData();//잠그기
                            }
                        }.start();
                        try{
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        finish();
                        startActivity(intent);
                    }
                }

            }
        });


    }
    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {

                Document doc = Jsoup.connect(htmlPageUrl).get();
                //htmlContentInStringFormat = doc.getAllElements().toString();


                //테스트1
                Elements titles= doc.select("div.read");
                if(titles.equals("null")){
                    titles= doc.select("div.read");
                }
                for(Element e: titles){
                    htmlContentInStringFormat = e.text().trim();

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(Void result) {
            if(htmlContentInStringFormat.equals("1") && select == 1){
                R_inCenText.setText("가스밸브 잠김");
                imageValue.setImageResource(R.drawable.ctrl_green);
                btnCtrl.setText("열기");
                btnCtrl.setBackgroundResource(R.drawable.btn_bg);
            }
            else if(htmlContentInStringFormat.equals("2") && select == 1){
                R_inCenText.setText("가스밸브 열림");
                imageValue.setImageResource(R.drawable.ctrl_red);
                btnCtrl.setText("잠금");
                btnCtrl.setBackgroundResource(R.drawable.btn_bg);
            }
        }

    }

    public void HttpPostData() {
        try {
            //URL 설정, 접속
            URL url = new URL("https://capstoneteamh.000webhostapp.com/ctrl.php");       // URL 설정
            HttpURLConnection http = (HttpURLConnection) url.openConnection();   // 접속
            //전송 모드 설정 기본 설정
            http.setDefaultUseCaches(false);
            http.setDoInput(true);                         // 서버에서 읽기 모드 지정
            http.setDoOutput(true);                       // 서버로 쓰기 모드 지정
            http.setRequestMethod("POST");         // 전송 방식은 POST

            // 서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다
            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            //서버로 값 전송
            StringBuffer buffer = new StringBuffer();
            buffer.append("value").append("=").append(myValue).append("&");                 // php 변수에 값 대입
            buffer.append("choice").append("=").append(myChoice).append("&");                 // php 변수에 값 대입
            buffer.append("select").append("=").append(mySelect);


            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "EUC-KR");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(buffer.toString());
            writer.flush();
            //서버 응답
            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "EUC-KR");
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {       // 서버에서 라인단위로 보내줄 것이므로 라인단위로 읽는다
                builder.append(str + "\n");                     // View에 표시하기 위해 라인 구분자 추가
            }
//            myResult = builder.toString();                       // 전송결과를 전역 변수에 저장
//            resultText.setText(myResult);
//            Toast.makeText(ViewActivity.this, "전송 후 결과 받음", Toast.LENGTH_SHORT).show();
        } catch (MalformedURLException e) {
            //
        } catch (IOException e) {
            //
        } // tr

    } // HttpPostData
}
