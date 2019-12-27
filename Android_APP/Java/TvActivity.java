package com.example.minch.smart_home_mate_rsapberry;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class TvActivity extends AppCompatActivity {
    private WebView mWebView;
    int progress = 0;
    SeekBar sBar;
    LinearLayout aLay;
    TextView bar_Text;
    String myCamera, mySelect, myChoice, htmlContentInStringFormat, htmlPageUrl;
    private String myUrl = "http://192.168.0.3:8091/?action=stream";// 접속 URL (내장HTML의 경우 왼쪽과 같이 쓰고 아니면 걍 URL)
    JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv);
        mWebView = (WebView) findViewById(R.id.webView);//xml 자바코드 연결
        sBar = (SeekBar) findViewById(R.id.sBar);
        bar_Text = (TextView) findViewById(R.id.bar_Text);
        aLay = (LinearLayout) findViewById(R.id.aLay);
        mySelect = "2";
        myChoice = "2";
        mWebView.getSettings().setJavaScriptEnabled(true);//자바스크립트 허용

        mWebView.loadUrl(myUrl);//웹뷰 실행
        mWebView.setWebChromeClient(new WebChromeClient());//웹뷰에 크롬 사용 허용//이 부분이 없으면 크롬에서 alert가 뜨지 않음
        mWebView.setWebViewClient(new WebViewClientClass());//새창열기 없이 웹뷰 내에서 다시 열기//페이지 이동 원활히 하기위해 사용
        htmlPageUrl = "https://capstoneteamh.000webhostapp.com/camera_read.php";
        jsoupAsyncTask.execute();

        sBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                myCamera = String.valueOf(i);
                if(i < 5 ){
                    bar_Text.setText("좌측으로 회전");
                }
                else if(i > 5){
                    bar_Text.setText("우측으로 회전");
                }
                else if(i == 5){
                    bar_Text.setText("정면");
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                new Thread() {
                    public void run() {
                        HttpPostData();//잠그기
                    }
                }.start();
            }
        });
    }



    public boolean onKeyDown(int keyCode, KeyEvent event) {//뒤로가기 버튼 이벤트
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {//웹뷰에서 뒤로가기 버튼을 누르면 뒤로가짐
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }//onKeyDown

    private class WebViewClientClass extends WebViewClient {//페이지 이동
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("check URL",url);
            view.loadUrl(url);
            return true;
        }
    }//WebViewClientClass

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
            buffer.append("value").append("=").append(myCamera).append("&");                 // php 변수에 값 대입
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
        } catch (MalformedURLException e) {
            //
        } catch (IOException e) {
            //
        } // tr

    } // HttpPostData

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
            int i = Integer.parseInt(htmlContentInStringFormat);
            sBar.setProgress(i);
            if(i < 5 ){
                bar_Text.setText("좌측으로 회전");
            }
            else if(i > 5){
                bar_Text.setText("우측으로 회전");
            }
            else if(i == 5){
                bar_Text.setText("정면");
            }
        }

    }
}
