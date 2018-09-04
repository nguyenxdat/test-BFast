package vn.datnx.todolist.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemTodo implements Parcelable {

    private String title;
    private boolean isComplete;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeByte(this.isComplete ? (byte) 1 : (byte) 0);
    }

    public ItemTodo() {
    }

    protected ItemTodo(Parcel in) {
        this.title = in.readString();
        this.isComplete = in.readByte() != 0;
    }

    public static final Parcelable.Creator<ItemTodo> CREATOR = new Parcelable.Creator<ItemTodo>() {
        @Override
        public ItemTodo createFromParcel(Parcel source) {
            return new ItemTodo(source);
        }

        @Override
        public ItemTodo[] newArray(int size) {
            return new ItemTodo[size];
        }
    };
}
