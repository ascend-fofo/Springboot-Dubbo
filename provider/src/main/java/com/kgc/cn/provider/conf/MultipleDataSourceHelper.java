package com.kgc.cn.provider.conf;

public class MultipleDataSourceHelper {
    public static final String MASTER = "master";
    public final static String SLAVE = "slave";

    private static ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static void set(String db){
        contextHolder.set(db);
    }

    public static String get(){
        return contextHolder.get();
    }
}
