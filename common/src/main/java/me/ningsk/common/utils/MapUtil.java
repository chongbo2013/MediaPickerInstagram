package me.ningsk.common.utils;


import java.util.Map;

public class MapUtil
{
    public static <T, K> T parseMapValue(Map<K, T> map, K key, T defValue)
    {
        if ((map != null) && (map.containsKey(key))) {
            return map.get(key);
        }
        return defValue;
    }
}