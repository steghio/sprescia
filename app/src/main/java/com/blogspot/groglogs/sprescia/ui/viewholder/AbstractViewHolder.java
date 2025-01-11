package com.blogspot.groglogs.sprescia.ui.viewholder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.groglogs.sprescia.R;

import lombok.Getter;

@Getter
public class AbstractViewHolder extends RecyclerView.ViewHolder {
    protected final ImageView kmIconImageView;
    protected final TextView kmTextView;
    protected final ImageView stepsIconImageView;
    protected final TextView stepsTextView;
    protected final ImageView timeIconImageView;
    protected final TextView timeTextView;
    protected final TextView dateTextView;
    protected final ImageButton editButton;
    protected final ImageButton deleteButton;

    public AbstractViewHolder(@NonNull View itemView) {
        super(itemView);
        this.stepsIconImageView = itemView.findViewById(R.id.stepsIconImageView);
        this.stepsTextView = itemView.findViewById(R.id.stepsTextView);
        this.kmIconImageView = itemView.findViewById(R.id.kmIconImageView);
        this.kmTextView = itemView.findViewById(R.id.kmTextView);
        this.timeIconImageView = itemView.findViewById(R.id.timeIconImageView);
        this.timeTextView = itemView.findViewById(R.id.timeTextView);
        this.dateTextView = itemView.findViewById(R.id.dateTextView);
        this.editButton = itemView.findViewById(R.id.editButton);
        this.deleteButton = itemView.findViewById(R.id.deleteButton);
    }
}
