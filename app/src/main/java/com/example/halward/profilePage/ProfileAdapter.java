package com.example.halward.profilePage;

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

import com.example.halward.CommonAdapter;
import com.example.halward.R;
import com.example.halward.addActivity.AddHabitActivity;
import com.example.halward.calendarPage.CalendarAdapter;
import com.example.halward.model.Habit;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @author yuliiamelnyk on 25/04/2020
 * @project Halward
 */
public class ProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Habit> mHabits;
    private Context mContext;
    private int resourse;
    private CommonAdapter.ItemClickListener mClickListener;

    public ProfileAdapter(Context mContext, List<Habit> mhabits, int resourse) {
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
        ViewHolder holder = new ProfileAdapter.ViewHolder(view);



        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if ( holder instanceof ProfileAdapter.ViewHolder) {
             ((ViewHolder) holder).cardText.setText(mHabits.get(position).getName());
            ((ViewHolder) holder).textDuration.setText(String.valueOf(mHabits.get(position).getDuration())+" days");
            Picasso.get().load(mHabits.get(position).getImage()).into(((ViewHolder) holder).cardImage);
            ((ViewHolder) holder).cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mClickListener != null) {
                        mClickListener.onItemClick(view, holder.getAdapterPosition());
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mHabits.size();
    }
/*
    public void removeItem(int position){
        mHabits.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Habit habit, int position){
        mHabits.add(position, habit);
        notifyItemInserted(position);
    }*/

    public List<Habit> getData(){
        return mHabits;
    }
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView cardImage;
        TextView cardText;
        TextView textDuration;
        CardView cv;


        public ViewHolder(View itemView) {
            super(itemView);
            cardImage = (ImageView) itemView.findViewById(R.id.habit_image_cv);
            cardText = (TextView) itemView.findViewById(R.id.title_text_cv);
            textDuration = (TextView) itemView.findViewById(R.id.sub_text_cv);
            cv = (CardView) itemView.findViewById(R.id.cards_profile);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
    // convenience method for getting data at click position
    public Habit getItem(int id) {
        return mHabits.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(CommonAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}