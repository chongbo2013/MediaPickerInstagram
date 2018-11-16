package me.ningsk.common.whitelist;

import java.util.HashMap;

public class PropertyValueExchanger
{
    public static String KEY_ENCODER_YUV420_FORMAT = "encoder-yuv420-format";
    private HashMap<String, Entry> mMap;

    public PropertyValueExchanger()
    {
        this.mMap = new HashMap();
    }

    public <T> void addPropertyEntry(String propertyName, T originalValue, T deviceRightValue)
    {
        Entry body = new Entry(originalValue, deviceRightValue);
        this.mMap.put(propertyName, body);
    }

    public <T> Entry<T> getPropertyEntry(String propertyName) {
        if (this.mMap.containsKey(propertyName)) {
            Entry body = (Entry)this.mMap.get(propertyName);
            return body;
        }
        return null;
    }

    public boolean hasProperty(String propertyName)
    {
        return this.mMap.containsKey(propertyName);
    }

    public class Entry<T>
    {
        T mOriginalValue;
        T mDeviceRightValue;

        public Entry(T originalValue, T deviceRightValue )
        {
            this.mOriginalValue = originalValue;
            this.mDeviceRightValue = deviceRightValue;
        }

        public T replaceValue(T originalValue) {
            if (this.mOriginalValue.equals(originalValue)) {
                return this.mDeviceRightValue;
            }
            return originalValue;
        }
    }
}
