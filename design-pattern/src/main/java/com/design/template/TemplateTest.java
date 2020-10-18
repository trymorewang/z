package com.design.template;

public class TemplateTest {

    public static void main(String[] args) {
        //使用本地缓存
        AbstractSetting setting1 = new LocalSetting();
        System.out.println("test = " + setting1.getSetting("test"));
        System.out.println("test = " + setting1.getSetting("test"));

        //使用redis缓存
        AbstractSetting setting2 = new RedisSetting();
        System.out.println("autosave = " + setting2.getSetting("autosave"));
        System.out.println("autosave = " + setting2.getSetting("autosave"));
    }
}
