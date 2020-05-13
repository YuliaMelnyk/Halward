package com.example.halward;

import android.app.Dialog;
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

import com.example.halward.model.Habit;
import com.example.halward.timer.TimerHabitActivity;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

public class CommonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private List<Habit> mHabits;
    private Context mContext;
    private ItemClickListener mClickListener;
    private Habit mHabit;
    private Dialog mDialog;

    public CommonAdapter(Context context, List<Habit> habits) {
        mContext = context;
        mHabits = habits;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        view = mInflater.inflate(R.layout.fragment_habit, parent, false);
        final RecyclerView.ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).cardText.setText(mHabits.get(position).getName());
            ((ViewHolder) holder).tagText.setText(String.valueOf("#" +mHabits.get(position).getTag()));
            Picasso.get().load(mHabits.get(position).getImage()).into(((ViewHolder) holder).cardImage);
            ((ViewHolder) holder).cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, TimerHabitActivity.class);
                    intent.putExtra("position", position);
                    mContext.startActivity(intent);
                }

            });
        }
    }

    @Override
    public int getItemCount() {
        return mHabits.size();
    }

    public void setRecyclerViewOnClickListenerHack(ItemClickListener r){
        mClickListener = r;
    }

    public void removeItem(int position) {
        mHabits.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Habit habit, int position) {
        mHabits.add(position, habit);
        notifyItemInserted(position);
    }

    public void setItemActive( int duration, boolean isActive){
        mHabit.setActive(isActive);
        mHabit.setStartDate(new Date());
        mHabit.setDuration(duration);
    }
    public void setItemInactive( boolean isActive){
        mHabit.setActive(isActive);
        mHabit.setEndDate(new Date(System.currentTimeMillis()-24*60*60*1000));
    }
    public List<Habit> getData() {
        return mHabits;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        //mHabitFragment = (HomeFragment) mContext;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView cardImage;
        TextView cardText, textDuration, tagText;
        CardView cv;


        public ViewHolder(View itemView) {
            super(itemView);
            cardImage = (ImageView) itemView.findViewById(R.id.habit_image);
            cardText = (TextView) itemView.findViewById(R.id.title_text);
            tagText = (TextView) itemView.findViewById(R.id.tag_text);
            cv = (CardView) itemView.findViewById(R.id.card_habits);
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
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}

