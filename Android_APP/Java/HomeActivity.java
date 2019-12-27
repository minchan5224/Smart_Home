package com.example.minch.smart_home_mate_rsapberry;

        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.text.Editable;
        import android.text.TextWatcher;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.EditText;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements ListViewBtnAdapter.ListBtnClickListener {
    static final String[] LIST_MENU = {"LIST1", "LIST2", "LIST3"};
    TextView topText;




    public boolean loadItemsFromDB(ArrayList<ListViewBtnItem> list) {
        setContentView(R.layout.activity_home);
        topText = (TextView) findViewById(R.id.topText);
        Intent intent = getIntent();
        String name = intent.getExtras().getString("name");
        topText.setText(name+" 님 안녕하세요!");

        ListViewBtnItem item;


        if (list == null) {
            list = new ArrayList<ListViewBtnItem>();
        }

        // 순서를 위한 i 값을 1로 초기화.


        // 아이템 생성.
        item = new ListViewBtnItem(); //0번
//        item.setIcon(ContextCompat.getDrawable(this, R.drawable.sample));
        item.setTitle("가스");
        item.setDesc("가스 제어");
        list.add(item);


        item = new ListViewBtnItem(); //1번
//        item.setIcon(ContextCompat.getDrawable(this, R.drawable.sample3));
        item.setTitle("Web Cam");
        item.setDesc("Web Cam 확인");
        list.add(item);


//        item = new ListViewBtnItem();
//        item.setIcon(ContextCompat.getDrawable(this, R.drawable.sample4));
//        item.setTitle("전등 2");
//        item.setDesc("전등 제어");
//        list.add(item);

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final ListView listview;
        ListViewBtnAdapter adapter;

        ArrayList<ListViewBtnItem> items = new ArrayList<ListViewBtnItem>();
        adapter = new ListViewBtnAdapter(this, R.layout.listview_btn_item, items, this);

        // items 로드.
        loadItemsFromDB(items);

        listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // TODO : item click
            }
        });


    }

    @Override
    public void onListBtnClick(int position) {
        //가스_0, 사료_1, 카메라_2
        if (position == 0) //0 부터 시작
        {
            Toast.makeText(this, "가스 제어", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), ViewActivity.class);
            intent.putExtra("Num", position+1);
            startActivity(intent);
        }
        if (position == 1)
        {
            Toast.makeText(this, "Web Cam 확인", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), TvActivity.class);
            intent.putExtra("Num", position);
            startActivity(intent);
        }
//        else {
//            Intent intent = new Intent(getApplicationContext(), ScrollingActivity.class);
//            intent.putExtra("Num", position);
//            startActivity(intent);
//        }
    }
}