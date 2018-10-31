package me.ningsk.mediascanlibrary.utils;

import java.util.ArrayList;
import java.util.List;

import me.ningsk.mediascanlibrary.entity.LocalMedia;


public class DataTransferStation {
    private ArrayList<LocalMedia> items;
    private ArrayList<LocalMedia> selectedItems;

    private DataTransferStation() {
    }

    public static DataTransferStation getInstance() {
        return DataTransferStationHolder.STATION;
    }

    public void putItems(List<LocalMedia> items) {
        this.items = new ArrayList<>(items);
    }

    public ArrayList<LocalMedia> getItems() {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        return this.items;
    }

    public ArrayList<LocalMedia> getSelectedItems() {
        if (this.selectedItems == null) {
            this.selectedItems = new ArrayList<>();
        }
        return this.selectedItems;
    }

    public void putSelectedItem(LocalMedia item) {
        if (this.selectedItems == null) {
            this.selectedItems = new ArrayList<>();
        }
        this.selectedItems.add(item);
    }

    public void putSelectedItems(List<LocalMedia> items) {
        if (items != null && !items.isEmpty()) {
            if (this.selectedItems == null) {
                this.selectedItems = new ArrayList<>();
            }
            this.selectedItems.addAll(items);
        }
    }

    public void removeFromSelectedItems(LocalMedia item) {
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
