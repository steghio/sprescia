package com.blogspot.groglogs.sprescia.model.view;

import android.os.Parcel;
import android.os.Parcelable;

import com.blogspot.groglogs.sprescia.R;
import com.blogspot.groglogs.sprescia.activity.CreateDocumentActivity;
import com.blogspot.groglogs.sprescia.util.DateTimeUtils;

import java.time.LocalDate;

import lombok.Data;

@Data
public class RunViewItem extends AbstractViewItem {

    private Long id;
    private double km;
    private int hours;
    private int minutes;
    private double kmh;
    private LocalDate date;

    public RunViewItem(Long id, double km, int hours, int minutes, LocalDate date){
        this.id = id;
        this.km = km;
        this.hours = hours;
        this.minutes = minutes;
        this.date = date;
        this.kmh = calculateKmH();
    }

    private double calculateKmH(){
        return km / (hours + ((double) minutes / 60));
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
        dest.writeInt(hours);
        dest.writeInt(minutes);
        dest.writeLong(date.toEpochDay());
    }

    protected RunViewItem(Parcel in) {
        id = in.readLong();
        km = in.readDouble();
        hours = in.readInt();
        minutes = in.readInt();
        kmh = calculateKmH();
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
                DateTimeUtils.fromString(split[4])
        );
    }
}
