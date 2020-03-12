package com.example.msm;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MultipleListDataAdapter extends RecyclerView.Adapter<MultipleListDataAdapter.MultipleItemHolder> {

    private ArrayList<Admin_Info1> dataList;
    private Context mContext;

    public MultipleListDataAdapter(ArrayList<Admin_Info1> dataList, Context mContext) {
        this.dataList = dataList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MultipleItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_list, null);
        MultipleItemHolder mh = new MultipleItemHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(@NonNull MultipleItemHolder multipleItemHolder, int i) {
        ArrayList singleList = dataList.get(i).arrayList;

        multipleItemHolder.tv_list_year.setText(dataList.get(i).getYear()+"");
        multipleItemHolder.tv_list_month.setText(dataList.get(i).getMonth()+"");

        SingleListDataAdapter itemListDataAdapter = new SingleListDataAdapter(singleList, mContext);

        multipleItemHolder.recyclerView.setHasFixedSize(true);
        multipleItemHolder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        multipleItemHolder.recyclerView.setAdapter(itemListDataAdapter);
        multipleItemHolder.recyclerView.setNestedScrollingEnabled(false);


    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class MultipleItemHolder extends RecyclerView.ViewHolder {
        protected TextView tv_list_year, tv_list_month;
        protected RecyclerView recyclerView;

        public MultipleItemHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_list_year = (TextView) itemView.findViewById(R.id.tv_list_year);
            this.tv_list_month = (TextView) itemView.findViewById(R.id.tv_list_month);
            this.recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerListView);

        }
    }
}
