package com.diezsiete.lscapp.db;

import android.annotation.SuppressLint;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.util.StringUtil;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LSCAppTypeConverters {
    @TypeConverter
    public static List<String> stringToStringList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(data.split("\\s*,\\s*"));
    }

    @TypeConverter
    public static String stringListToString(List<String> strings) {
        return TextUtils.join(",", strings);
    }

    @SuppressLint("RestrictedApi")
    @TypeConverter
    public static List<Integer> stringToIntList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }
        return StringUtil.splitToIntList(data);
    }

    @SuppressLint("RestrictedApi")
    @TypeConverter
    public static String intListToString(List<Integer> ints) {
        return StringUtil.joinIntoString(ints);
    }
}