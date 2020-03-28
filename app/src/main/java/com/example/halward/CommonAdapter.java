package com.example.halward;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.halward.model.Habit;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private List<Habit> mHabits;
    private Context mContext;
    private ItemClickListener mClickListener;

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
        RecyclerView.ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).cardText.setText(mHabits.get(position).getName());
            ((ViewHolder) holder).textDuration.setText(String.valueOf(mHabits.get(position).getDuration()) + " days");
            Picasso.get().load(mHabits.get(position).getImage()).into(((ViewHolder) holder).cardImage);
            //((ViewHolder) holder).cardImage.setImageURI(load);
        }
    }

    @Override
    public int getItemCount() {
        return mHabits.size();
    }

    public void removeItem(int position) {
        mHabits.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Habit habit, int position) {
        mHabits.add(position, habit);
        notifyItemInserted(position);
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
        TextView cardText;
        TextView textDuration;
        CardView cv;


        public ViewHolder(View itemView) {
            super(itemView);
            cardImage = (ImageView) itemView.findViewById(R.id.habit_image);
            cardText = (TextView) itemView.findViewById(R.id.title_text);
            textDuration = (TextView) itemView.findViewById(R.id.sub_text);
            cv = (CardView) itemView.findViewById(R.id.card_habits);
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mClickListener != null) {
                        mClickListener.onItemClick(view, getAdapterPosition());
                    }
                }
            });
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

