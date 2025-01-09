package com.blogspot.groglogs.sprescia.model.view;

import android.os.Parcelable;

public abstract class AbstractViewItem implements Parcelable {
    public abstract String toCsv();
}
