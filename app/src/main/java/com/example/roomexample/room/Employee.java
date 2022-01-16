package com.example.roomexample.room;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import kotlinx.android.parcel.Parcelize;

@Entity
@Parcelize
public class Employee implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;

    public double salary;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeDouble(this.salary);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readLong();
        this.name = source.readString();
        this.salary = source.readDouble();
    }

    public Employee() {
    }

    protected Employee(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.salary = in.readDouble();
    }

    public static final Parcelable.Creator<Employee> CREATOR = new Parcelable.Creator<Employee>() {
        @Override
        public Employee createFromParcel(Parcel source) {
            return new Employee(source);
        }

        @Override
        public Employee[] newArray(int size) {
            return new Employee[size];
        }
    };
}
