package com.example.halward;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.halward.R;
import com.example.halward.addActivity.AddHabitActivity;
import com.example.halward.model.Habit;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @author yuliiamelnyk on 2020-02-18
 * @project Halward
 */
public class CommonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Habit> mHabits;
    private Context mContext;
    private int resourse;

    public CommonAdapter(Context mContext,List<Habit> mhabits,  int resourse) {
        this.mContext = mContext;
        this.mHabits = mhabits;
        this.resourse = resourse;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        view = mInflater.inflate(resourse, parent, false);
        RecyclerView.ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).cardText.setText(mHabits.get(position).getName());
            ((ViewHolder) holder).textDuration.setText(String.valueOf(mHabits.get(position).getDuration())+" days");
            Picasso.get().load(mHabits.get(position).getImage()).into(((ViewHolder) holder).cardImage);
            //((ViewHolder) holder).cardImage.setImageURI(load);
            ((ViewHolder) holder).cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.getContext().startActivity(new Intent(view.getContext(), AddHabitActivity.class));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mHabits.size();
    }

    public void removeItem(int position){
        mHabits.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Habit habit, int position){
        mHabits.add(position, habit);
        notifyItemInserted(position);
    }

    public List<Habit> getData(){
        return mHabits;
    }
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        //mHabitFragment = (HomeFragment) mContext;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView cardImage;
        TextView cardText;
        TextView textDuration;
        CardView cv;


        public ViewHolder(View itemView) {
            super(itemView);
            cardImage = (ImageView) itemView.findViewById(R.id.habit_image_cal);
            cardText = (TextView) itemView.findViewById(R.id.title_text_cal);
            textDuration = (TextView) itemView.findViewById(R.id.sub_text_cal);
            cv = (CardView) itemView.findViewById(R.id.card_habits_calendar);

        }
    }
}
