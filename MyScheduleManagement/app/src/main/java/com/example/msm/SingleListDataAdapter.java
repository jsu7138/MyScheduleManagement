package com.example.msm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.zip.Inflater;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static android.view.View.inflate;

public class SingleListDataAdapter extends RecyclerView.Adapter<SingleListDataAdapter.SingleItemHolder>{
    private ArrayList<Admin_Info2> list;
    private Context mContext;

    public SingleListDataAdapter(ArrayList<Admin_Info2> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public SingleItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_singlecard, null);
        SingleItemHolder mh = new SingleItemHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(@NonNull final SingleItemHolder singleItemHolder, final int i) {
        singleItemHolder.tv_list_week.setText(list.get(i).getWeek());
        singleItemHolder.tv_list_day_of_month.setText(list.get(i).getDay()+"");
        singleItemHolder.tv_list_hour.setText(list.get(i).getHour()+"");
        singleItemHolder.tv_list_min.setText(list.get(i).getMinute()+"");
        singleItemHolder.tv_list_context.setText(list.get(i).getContext()+"");
        // 원래값 -> this.getResources().getIntArray(R.array.mycolors) or mContext.getResources().getColor(R.color.color1);
        singleItemHolder.cardView_inner.setCardBackgroundColor(list.get(i).getColors());

        if(list.get(i).select){
            singleItemHolder.cardView.setBackgroundColor(mContext.getResources().getColor(R.color.colorGray));
        }else {
            singleItemHolder.cardView.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
        }

        if(list.get(i).checking == 1){
            singleItemHolder.cardCheckImg.setVisibility(VISIBLE);
        }else
            singleItemHolder.cardCheckImg.setVisibility(INVISIBLE);


               /* Glide.with(mContext)
                .load(feedItem.getImageURL())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.bg)
                .into(feedListRowHolder.thumbView);*/
    }


    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

    //객체의 기능
    public class SingleItemHolder extends RecyclerView.ViewHolder  implements View.OnCreateContextMenuListener {

        protected TextView tv_list_week, tv_list_day_of_month, tv_list_hour, tv_list_min, tv_list_context;
        protected CardView cardView,cardView_inner;
        protected ImageView cardCheckImg;
        protected SQLiteDatabase db;
        protected Db_helper db_admin;

        public SingleItemHolder(@NonNull final View itemView) {
            super(itemView);
            initDb();
            this.cardCheckImg = (ImageView) itemView.findViewById(R.id.imgView_check);
            this.tv_list_week = (TextView) itemView.findViewById(R.id.tv_list_week);
            this.tv_list_day_of_month = (TextView) itemView.findViewById(R.id.tv_list_day_of_month);
            this.tv_list_hour = (TextView) itemView.findViewById(R.id.tv_list_hour);
            this.tv_list_min = (TextView) itemView.findViewById(R.id.tv_list_min);
            this.tv_list_context = (TextView) itemView.findViewById(R.id.tv_list_context);
            this.cardView = (CardView) itemView.findViewById(R.id.cardView_single);
            this.cardView_inner = (CardView) itemView.findViewById(R.id.cardView_single_inner);

            this.cardView.setOnCreateContextMenuListener(this);
            //카드뷰의 크기를 화면 크기 맞추기 ( onBindViewHolder() -> singleItemHolder.cardView 로 사용)
            ViewGroup.LayoutParams layoutParams = this.cardView.getLayoutParams();
            layoutParams.width = Admin_cooki.getWindow_size_x()- (Admin_cooki.getWindow_size_x()/20);
            this.cardView.setLayoutParams(layoutParams);

            this.cardView.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("RestrictedApi")
                @Override
                public void onClick(View v) {
                    //체크 확인
                    if(!list.get(getAdapterPosition()).select){
                        cardView.setBackgroundColor(mContext.getResources().getColor(R.color.colorGray));
                        Admin_Info2 tmpInfo = list.get(getAdapterPosition());
                        Admin_cooki.getArrayList().add(tmpInfo);
                    }else {
                        cardView.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
                        Admin_Info2 tmpInfo = list.get(getAdapterPosition());
                        Admin_cooki.getArrayList().remove(Admin_cooki.getArrayList().indexOf(tmpInfo));
                    }
                    list.get(getAdapterPosition()).select = !list.get(getAdapterPosition()).select;

                    //쿠키 확인
                    if(Admin_cooki.getArrayList().isEmpty()) {
                        Activity_Main.fab_main.setVisibility(INVISIBLE);
                        Activity_Main.fab_main.setClickable(false);
                        Activity_Main.fab_main.startAnimation(AnimationUtils.loadAnimation((Activity) mContext, R.anim.fab_close));
                        Activity_Main.fab_item_delete.clearAnimation();
                        Activity_Main.fab_switch = true;

                        if(Activity_Main.fab_item_delete.getVisibility() != INVISIBLE){
                            Activity_Main.fab_item_delete.setVisibility(INVISIBLE);
                            Activity_Main.fab_item_delete.setClickable(false);
                            Activity_Main.fab_item_delete.startAnimation(AnimationUtils.loadAnimation((Activity) mContext, R.anim.fab_close));
                        }
                    }
                    else {
                        if(Activity_Main.fab_main.getVisibility() == INVISIBLE) {
                            Activity_Main.fab_main.startAnimation(AnimationUtils.loadAnimation((Activity) mContext, R.anim.fab_open));
                            Activity_Main.fab_main.setVisibility(View.VISIBLE);
                            Activity_Main.fab_main.setClickable(true);
                        }
                    }

                    notifyDataSetChanged();
                    /*Intent intent = new Intent(mContext, Activity_Main.class);
                    ((Activity) mContext).startActivityForResult(intent, 0);*/
                }
            });

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem checkMenu = menu.add(Menu.NONE, 1001, 1, "체크");
            MenuItem editMenu = menu.add(Menu.NONE, 1002, 2, "편집");
            checkMenu.setOnMenuItemClickListener(onEditMenu);
            editMenu.setOnMenuItemClickListener(onEditMenu);
        }

        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case 1001:
                        cardViewImgOnChange();
                        return true;
                    case 1002:
                        setOnEditContextModifier();
                        return true;
                }
                return false;
            }
        };
        private void cardViewImgOnChange(){
            if(list.get(getAdapterPosition()).checking == 0){
                db_admin.infoTableUpdate(
                        db,
                        list.get(getAdapterPosition()).day,
                        list.get(getAdapterPosition()).month,
                        list.get(getAdapterPosition()).year,
                        list.get(getAdapterPosition()).hour,
                        list.get(getAdapterPosition()).minute,
                        list.get(getAdapterPosition()).context,
                        list.get(getAdapterPosition()).checking,
                        1
                );
            }else{
                db_admin.infoTableUpdate(
                        db,
                        list.get(getAdapterPosition()).day,
                        list.get(getAdapterPosition()).month,
                        list.get(getAdapterPosition()).year,
                        list.get(getAdapterPosition()).hour,
                        list.get(getAdapterPosition()).minute,
                        list.get(getAdapterPosition()).context,
                        list.get(getAdapterPosition()).checking,
                        0
                );
            }
            list.get(getAdapterPosition()).checking = list.get(getAdapterPosition()).checking == 0 ? 1 : 0;
            cardCheckImg.setVisibility(cardCheckImg.getVisibility() == VISIBLE? INVISIBLE : VISIBLE);
            notifyDataSetChanged();
        }

        private void setOnEditContextModifier(){
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_edit,null,false);
            builder.setView(view);

            final EditText editText = (EditText) view.findViewById(R.id.edit_dialog_text);
            final Button button = (Button) view.findViewById(R.id.edit_dialog_btn);

            editText.setText(list.get(getAdapterPosition()).getContext());

            final AlertDialog dialog = builder.create();

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(editText.getText().toString().equals("")){
                        Toast.makeText(mContext,"The content is empty!",Toast.LENGTH_SHORT).show();
                    }else {
                        //SQLiteDatabase db, int day_of_month, int month, int year, int hour, int minute, String context, int checking, String afterContext
                        db_admin.infoTableContextUpdate(db,
                                list.get(getAdapterPosition()).day,
                                list.get(getAdapterPosition()).month,
                                list.get(getAdapterPosition()).year,
                                list.get(getAdapterPosition()).hour,
                                list.get(getAdapterPosition()).minute,
                                list.get(getAdapterPosition()).context,
                                list.get(getAdapterPosition()).checking,
                                editText.getText().toString()
                        );
                        list.get(getAdapterPosition()).setContext(editText.getText().toString());
                        notifyItemChanged(getAdapterPosition());
                        dialog.dismiss();
                    }
                }
            });
            //배경 모서리 투명화
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }

        private void initDb(){
            db_admin = new Db_helper((Activity) mContext);  //DB생성
            try {
                //읽기, 쓰기 모드오픈/
                db = db_admin.getWritableDatabase();
            } catch (SQLiteException e) {
                //권한이 없거나 디스크용량 부족
                Toast.makeText((Activity) mContext,"You do not have access to the database",Toast.LENGTH_LONG).show();
            }
        }
    }
}
