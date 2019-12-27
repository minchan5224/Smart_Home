package com.example.minch.smart_home_mate_rsapberry;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewBtnAdapter extends ArrayAdapter implements View.OnClickListener, Filterable  {
    // 버튼 클릭 이벤트를 위한 Listener 인터페이스 정의.
    private ArrayList<ListViewBtnItem> listViewBtnItemList = new ArrayList<ListViewBtnItem>();

    private ArrayList<ListViewBtnItem> filteredItemList = listViewBtnItemList;
    Filter listFilter;

    public interface ListBtnClickListener {
        void onListBtnClick(int position) ;
    }

    // 생성자로부터 전달된 resource id 값을 저장.
    int resourceId ;
    // 생성자로부터 전달된 ListBtnClickListener  저장.
    private ListBtnClickListener listBtnClickListener ;


    // ListViewBtnAdapter 생성자. 마지막에 ListBtnClickListener 추가.
    ListViewBtnAdapter(Context context, int resource, ArrayList<ListViewBtnItem> list, ListBtnClickListener clickListener) {
        super(context, resource, list) ;

        // resource id 값 복사. (super로 전달된 resource를 참조할 방법이 없음.)
        this.resourceId = resource ;

        this.listBtnClickListener = clickListener ;
    }

//    public int getCount(){
//        return filteredItemList.size();
//    }


    // 새롭게 만든 Layout을 위한 View를 생성하는 코드

    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position ;
        final Context context = parent.getContext();
        // 생성자로부터 저장된 resourceId(listview_btn_item)에 해당하는 Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.resourceId/*R.layout.listview_btn_item*/, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)로부터 위젯에 대한 참조 획득
        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.imageView1);
        TextView titleTextView = (TextView) convertView.findViewById(R.id.textView1);
        TextView descTextView = (TextView) convertView.findViewById(R.id.textView2);
        Button button1 = (Button) convertView.findViewById(R.id.button1);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewBtnItem listViewItem = (ListViewBtnItem) getItem(position);
//        ListViewBtnItem listViewItem = filteredItemList.get(position);



        // 아이템 내 각 위젯에 데이터 반영
        iconImageView.setImageDrawable(listViewItem.getIcon());
        titleTextView.setText(listViewItem.getTitle());
        descTextView.setText(listViewItem.getDesc());

        button1.setTag(position);
        button1.setOnClickListener(this);

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

//    @Override
//    public Object getItem(int position) {
//        return filteredItemList.get(position);
//     }

    public void addItem(Drawable icon, String text){
        ListViewBtnItem item = new ListViewBtnItem();
        item.setIcon(icon);
        item.setTitle(text);
        item.setDesc(text);

        listViewBtnItemList.add(item);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if(listFilter == null)
            listFilter = new ListFilter();

        return listFilter;
    }

    private class ListFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                results.values =  listViewBtnItemList;
                results.count =  listViewBtnItemList.size();
            } else {
                ArrayList<ListViewBtnItem> itemList = new ArrayList<ListViewBtnItem>() ;

                for (ListViewBtnItem item :  listViewBtnItemList) {
                    if (item.getTitle().toUpperCase().contains(constraint.toString().toUpperCase()) || item.getDesc().toUpperCase().contains(constraint.toString().toUpperCase()))

                    {
                        itemList.add(item);
                    }
                }

                results.values = itemList;
                results.count = itemList.size() ;
            }
            return results;
        }
//
//        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
//
//            // update listview by filtered data list.
//            filteredItemList = (ArrayList<ListViewBtnItem>) results.values ;
//
//            // notify
//            if (results.count > 0) {
//                notifyDataSetChanged() ;
//            } else {
//                notifyDataSetInvalidated() ;
//            }
        }
    }


    // button1가 눌려졌을 때 실행되는 onClick함수.
    public void onClick(View v) {
        // ListBtnClickListener(MainActivity)의 onListBtnClick() 함수 호출.

        if (this.listBtnClickListener != null) {
            this.listBtnClickListener.onListBtnClick((int)v.getTag()) ;

        }
    }


}