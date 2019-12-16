package com.greentea.locker.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.greentea.locker.AppInfoActivity;
import com.greentea.locker.PlaceDatabase.PickedPlace;
import com.greentea.locker.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

    private List<PickedPlace> listData = new ArrayList<>();

    private final LayoutInflater layoutInflater;

    private OnListItemSelectedInterface mListener;
    private OnItemClickListener itemListener = null;

    public RecyclerAdapter(Context context, OnListItemSelectedInterface onListItemSelectedInterface){
        layoutInflater = LayoutInflater.from(context);
        mListener = onListItemSelectedInterface;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = layoutInflater.inflate(R.layout.item, parent, false);
        return new ItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, final int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수
        holder.onBind(listData.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Context context = v.getContext();
                Intent intent = new Intent(context, AppInfoActivity.class);

                intent.putExtra("pickedPlace", listData.get(position));
                ((Activity) context).startActivityForResult(intent, 123);
            }
        });
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.itemListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ViewHolder(View itemView){
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){

                        if(itemListener != null){
                            itemListener.onItemClick(v, pos);
                        }
                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수
        return listData.size();
    }

    public void setPlaces(List<PickedPlace> places){
        listData = places;
        notifyDataSetChanged();
    }

    public void addItem(PickedPlace pickedPlace) {
        // 외부에서 item을 추가시킬 함수
        listData.add(pickedPlace);
    }

    // ViewHolder
    // 여기서 subView를 setting
    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView textView1;

        public ItemViewHolder(View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.textView);
        }

        void onBind(PickedPlace pickedPlace) {
            textView1.setText("  " + pickedPlace.getPlaceName());
            if(pickedPlace.getVersion() == true) {
//                textView1.setBackgroundColor(0xFFC71585);
//                textView1.setTextColor(0xFFFFFFFF);
                textView1.setTextColor(0xFFC71585);
            }
            else {
//                textView1.setBackgroundColor(0xFFFFFFFF);
//                textView1.setTextColor(0xFF000000);
                textView1.setTextColor(0xFFFFFFFF);
            }
        }
    }
}
