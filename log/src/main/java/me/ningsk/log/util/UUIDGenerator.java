package me.ningsk.log.util;

import java.util.UUID;

public class UUIDGenerator
{
    public static final String generateUUID()
    {
        return UUID.randomUUID().toString().toUpperCase();
    }
}
