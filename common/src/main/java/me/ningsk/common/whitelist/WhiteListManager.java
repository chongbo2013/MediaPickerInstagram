package me.ningsk.common.whitelist;

import java.util.HashMap;

import static me.ningsk.common.whitelist.DeviceModelList.HUWEI.HUAWEI_6Plus;
import static me.ningsk.common.whitelist.DeviceModelList.HUWEI.HUAWEI_H60;
import static me.ningsk.common.whitelist.DeviceModelList.HUWEI.HUAWEI_HONOR_6PLUS;
import static me.ningsk.common.whitelist.DeviceModelList.HUWEI.HUAWEI_MATE7;
import static me.ningsk.common.whitelist.DeviceModelList.HUWEI.HUAWEI_P6;
import static me.ningsk.common.whitelist.DeviceModelList.HUWEI.HUAWEI_P9;
import static me.ningsk.common.whitelist.DeviceModelList.HUWEI.HUAWEI_P9_EVA;

public class WhiteListManager
{
    private HashMap<String, WhiteItem> mWhiteListMap = new HashMap();
    private static WhiteListManager sInstance = new WhiteListManager();

    public static WhiteListManager getInstance()
    {
        return sInstance;
    }

    public WhiteItem getWhiteItem(String device) {
        return (WhiteItem)this.mWhiteListMap.get(device);
    }

    public void putWhiteItem(String device, WhiteItem item) {
        this.mWhiteListMap.put(device, item);
    }

    static
    {
        WhiteItem huaweiP6Item = new WhiteItem(HUAWEI_P6);
        huaweiP6Item.getPropertyValueExchanger()
                .addPropertyEntry(PropertyValueExchanger.KEY_ENCODER_YUV420_FORMAT,
                        Integer.valueOf(21),
                        Integer.valueOf(39));

        WhiteItem huaweiP9Item = new WhiteItem(HUAWEI_P9);
        huaweiP9Item.getPropertyValueExchanger()
                .addPropertyEntry(PropertyValueExchanger.KEY_ENCODER_YUV420_FORMAT,
                        Integer.valueOf(39),
                        Integer.valueOf(21));

        WhiteItem huawei6PlusItem = new WhiteItem(HUAWEI_6Plus);
        huawei6PlusItem.getPropertyValueExchanger()
                .addPropertyEntry(PropertyValueExchanger.KEY_ENCODER_YUV420_FORMAT,
                        Integer.valueOf(39),
                        Integer.valueOf(21));

        WhiteItem huaweiHonor6PlusItem = new WhiteItem(HUAWEI_HONOR_6PLUS);
        huaweiHonor6PlusItem.getPropertyValueExchanger()
                .addPropertyEntry(PropertyValueExchanger.KEY_ENCODER_YUV420_FORMAT,
                        Integer.valueOf(39),
                        Integer.valueOf(21));

        WhiteItem huaweiP9EVAItem = new WhiteItem(HUAWEI_P9_EVA);
        huaweiP9EVAItem.getPropertyValueExchanger()
                .addPropertyEntry(PropertyValueExchanger.KEY_ENCODER_YUV420_FORMAT,
                        Integer.valueOf(20),
                        Integer.valueOf(19));

        WhiteItem huaweiH60Item = new WhiteItem(HUAWEI_H60);
        huaweiH60Item.getPropertyValueExchanger()
                .addPropertyEntry(PropertyValueExchanger.KEY_ENCODER_YUV420_FORMAT,
                        Integer.valueOf(20),
                        Integer.valueOf(19));

        WhiteItem huaweiMate7Item = new WhiteItem(HUAWEI_MATE7);
        huaweiMate7Item.getPropertyValueExchanger()
                .addPropertyEntry(PropertyValueExchanger.KEY_ENCODER_YUV420_FORMAT,
                        Integer.valueOf(20),
                        Integer.valueOf(19));

        sInstance.putWhiteItem(huaweiP6Item.getDevice(), huaweiP6Item);
        sInstance.putWhiteItem(huaweiP9Item.getDevice(), huaweiP9Item);
        sInstance.putWhiteItem(huawei6PlusItem.getDevice(), huawei6PlusItem);
        sInstance.putWhiteItem(huaweiHonor6PlusItem.getDevice(), huaweiHonor6PlusItem);
        sInstance.putWhiteItem(huaweiP9EVAItem.getDevice(), huaweiP9EVAItem);
        sInstance.putWhiteItem(huaweiH60Item.getDevice(), huaweiH60Item);
        sInstance.putWhiteItem(huaweiMate7Item.getDevice(), huaweiMate7Item);
    }
}
