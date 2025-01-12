package com.blogspot.groglogs.sprescia.model.view;

import android.os.Parcel;
import android.os.Parcelable;

import com.blogspot.groglogs.sprescia.R;
import com.blogspot.groglogs.sprescia.activity.CreateDocumentActivity;
import com.blogspot.groglogs.sprescia.util.DateTimeUtils;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RunViewItem implements Parcelable {

    private Long id;
    private double km;
    private int steps;
    private int hours;
    private int minutes;
    private LocalDate date;

    public double getKmh(){
        return km / (hours + ((double) minutes / 60));
    }

    public float getTimeAsDecimal(){
        return hours + (float) minutes / 100;
    }

    public int getStepsIconResId(){
        return R.drawable.ic_footsteps_24dp;
    }

    public int getKmIconResId(){
        return R.drawable.ic_path_24dp;
    }

    public int getTimeIconResId(){
        return R.drawable.ic_time_24dp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeDouble(km);
        dest.writeInt(steps);
        dest.writeInt(hours);
        dest.writeInt(minutes);
        dest.writeLong(date.toEpochDay());
    }

    protected RunViewItem(Parcel in) {
        id = in.readLong();
        km = in.readDouble();
        steps = in.readInt();
        hours = in.readInt();
        minutes = in.readInt();
        date = LocalDate.ofEpochDay(in.readLong());
    }

    public static final Parcelable.Creator<RunViewItem> CREATOR = new Parcelable.Creator<>() {
        @Override
        public RunViewItem createFromParcel(Parcel source) {
            return new RunViewItem(source);
        }

        @Override
        public RunViewItem[] newArray(int size) {
            return new RunViewItem[size];
        }
    };

    public String toCsv(){
        return id + CreateDocumentActivity.CSV_SEPARATOR +
                km + CreateDocumentActivity.CSV_SEPARATOR +
                steps + CreateDocumentActivity.CSV_SEPARATOR +
                hours + CreateDocumentActivity.CSV_SEPARATOR +
                minutes + CreateDocumentActivity.CSV_SEPARATOR +
                date.toString() + "\n";
    }

    public static RunViewItem fromCsv(String csv){
        String[] split = csv.split(CreateDocumentActivity.CSV_SEPARATOR);

        return new RunViewItem(null,
                Double.parseDouble(split[1]),
                Integer.parseInt(split[2]),
                Integer.parseInt(split[3]),
                Integer.parseInt(split[4]),
                DateTimeUtils.fromString(split[5])
        );
    }
}
