package com.blogspot.groglogs.sprescia.ui.run;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blogspot.groglogs.sprescia.R;
import com.blogspot.groglogs.sprescia.ui.viewholder.AbstractViewHolder;

import lombok.Getter;

@Getter
public class RunViewHolder extends AbstractViewHolder {
    private final ImageView kmhIconImageView;
    private final TextView kmhTextView;

    public RunViewHolder(@NonNull View itemView) {
        super(itemView);
        this.kmhIconImageView = itemView.findViewById(R.id.kmhIconImageView);
        this.kmhTextView = itemView.findViewById(R.id.kmhTextView);
    }
}
