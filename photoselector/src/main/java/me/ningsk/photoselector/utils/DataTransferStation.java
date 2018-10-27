package me.ningsk.photoselector.utils;

import java.util.ArrayList;

import me.ningsk.photoselector.bean.MediaBean;

public class DataTransferStation {
    private ArrayList<MediaBean> items;
    private ArrayList<MediaBean> selectedItems;

    private DataTransferStation() {
    }

    public static DataTransferStation getInstance() {
        return DataTransferStationHolder.STATION;
    }

    public void putItems(ArrayList<MediaBean> items) {
        this.items = new ArrayList<>(items);
    }

    public ArrayList<MediaBean> getItems() {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        return this.items;
    }

    public ArrayList<MediaBean> getSelectedItems() {
        if (this.selectedItems == null) {
            this.selectedItems = new ArrayList<>();
        }
        return this.selectedItems;
    }

    public void putSelectedItem(MediaBean item) {
        if (this.selectedItems == null) {
            this.selectedItems = new ArrayList<>();
        }
        this.selectedItems.add(item);
    }

    public void putSelectedItems(ArrayList<MediaBean> items) {
        if (items != null && !items.isEmpty()) {
            if (this.selectedItems == null) {
                this.selectedItems = new ArrayList<>();
            }
            this.selectedItems.addAll(items);
        }
    }

    public void removeFromSelectedItems(MediaBean item) {
        if (this.selectedItems != null) {
            this.selectedItems.remove(item);
        }
    }

    public void onDestroy() {
        if (this.items != null) {
            items.clear();
            items = null;
        }
        if (this.selectedItems != null) {
            selectedItems.clear();
            selectedItems = null;
        }
    }

    private static class DataTransferStationHolder {
        private static final DataTransferStation STATION = new DataTransferStation();
    }
}

