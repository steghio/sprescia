package com.blogspot.groglogs.sprescia.ui.run;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.groglogs.sprescia.R;

import lombok.Getter;

@Getter
public class RunViewHolder extends RecyclerView.ViewHolder {
    private final ImageView kmhIconImageView;
    private final TextView kmhTextView;
    private final ImageView kmIconImageView;
    private final TextView kmTextView;
    private final ImageView stepsIconImageView;
    private final TextView stepsTextView;
    private final ImageView timeIconImageView;
    private final TextView timeTextView;
    private final TextView dateTextView;
    private final ImageButton editButton;
    private final ImageButton deleteButton;

    public RunViewHolder(@NonNull View itemView) {
        super(itemView);
        this.kmhIconImageView = itemView.findViewById(R.id.kmhIconImageView);
        this.kmhTextView = itemView.findViewById(R.id.kmhTextView);
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
