package me.ningsk.photoselector.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class FolderBean implements Parcelable {
    private String name;
    private String path;
    private String firstImagePath;
    private int imageNum;
    private int checkedNum;
    private boolean isChecked;
    private List<MediaBean> medias = new ArrayList<MediaBean>();


    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = firstImagePath;
    }

    public int getImageNum() {
        return imageNum;
    }

    public void setImageNum(int imageNum) {
        this.imageNum = imageNum;
    }

    public List<MediaBean> getMedias() {
        if (medias == null) {
            medias = new ArrayList<>();
        }
        return medias;
    }

    public void setMedias(List<MediaBean> medias) {
        this.medias = medias;
    }

    public int getCheckedNum() {
        return checkedNum;
    }

    public void setCheckedNum(int checkedNum) {
        this.checkedNum = checkedNum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.path);
        dest.writeString(this.firstImagePath);
        dest.writeInt(this.imageNum);
        dest.writeInt(this.checkedNum);
        dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.medias);
    }

    public FolderBean() {
    }

    protected FolderBean(Parcel in) {
        this.name = in.readString();
        this.path = in.readString();
        this.firstImagePath = in.readString();
        this.imageNum = in.readInt();
        this.checkedNum = in.readInt();
        this.isChecked = in.readByte() != 0;
        this.medias = in.createTypedArrayList(MediaBean.CREATOR);
    }

    public static final Parcelable.Creator<FolderBean> CREATOR = new Parcelable.Creator<FolderBean>() {
        @Override
        public FolderBean createFromParcel(Parcel source) {
            return new FolderBean(source);
        }

        @Override
        public FolderBean[] newArray(int size) {
            return new FolderBean[size];
        }
    };
}


