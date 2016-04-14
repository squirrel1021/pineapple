package com.generator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * 这个dao生成工具，编辑的时候不要删别人写的实体，直接自己增加方法，然后执行，就会在
 * android db包下生成。
 * 建议在方法注释上加上自己的英文名
 */
public class PineappleGenerator {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1000, "pineapple.bd.com.pineapple.db");

        //注释内容为范例，生成的时候别打开！
        // addNote(schema);
        //addCustomerOrder(schema);

        addUser(schema);
        addUserAuth(schema);
        addMusic(schema);

        //用绝对路径找！
        new DaoGenerator().generateAll(schema, "app/src/main/java");
    }

    private static void addMusic(Schema schema) {
        Entity music = schema.addEntity("Music");
        music.addIdProperty();
        music.addIntProperty("mediaType");//eg: mp3
        music.addIntProperty("musicType");//eg:儿歌，英文
        music.addStringProperty("url");
        music.addStringProperty("singer");
        music.addStringProperty("author");
        music.addStringProperty("poster_url");//海报url
        music.addStringProperty("lyrics_url");//歌词url
        music.addStringProperty("name");
        music.addLongProperty("size");
        music.addStringProperty("albums");//专辑
        music.addIntProperty("quality");
        music.setSuperclass("cn.bmob.v3.BmobObject");
    }

    /**
     * @author:keivn
     * @param schema
     * one -- many  user -- userAuth
     */
    private static void addUser(Schema schema){
        Entity user = schema.addEntity("User");
        user.addIdProperty();
        user.addStringProperty("user_id");
        user.addStringProperty("nickname");
        user.addStringProperty("avatar");
        user.addIntProperty("gender");
        user.addStringProperty("hobbies");
        user.addStringProperty("jobs");
        user.setSuperclass("cn.bmob.v3.BmobObject");
    }
    /**
     * @author:keivn
     * @param schema
     */
    private static void addUserAuth(Schema schema){
        Entity userAuth = schema.addEntity("UserAuth");
        userAuth.addIdProperty();
        userAuth.addStringProperty("user_id"); //对应user的objectId
        userAuth.addIntProperty("onLineType");
        userAuth.addIntProperty("identity_type");
        userAuth.addStringProperty("identify_unique_id ");//（手机号 邮箱 用户名或第三方应用的唯一标识）
        userAuth.addStringProperty("credential"); //credential 密码凭证（站内的保存密码，站外的不保存或保存token）
        userAuth.addBooleanProperty("verified"); //是否验证，三方登录默认认证了
        userAuth.addLongProperty("update_time");
        userAuth.setSuperclass("cn.bmob.v3.BmobObject");//别忘了加这句
    }

    /**
     * @author:keivn
     * @param schema
     */
    private static void addNote(Schema schema) {
        Entity note = schema.addEntity("Note");
        note.addIdProperty();
        note.addStringProperty("text").notNull();
        note.addStringProperty("comment");
        note.addDateProperty("date");
    }
    /**
     * @author:keivn
     * @param schema
     */
    private static void addCustomerOrder(Schema schema) {
        Entity customer = schema.addEntity("Customer");
        customer.addIdProperty();
        customer.addStringProperty("name").notNull();

        Entity order = schema.addEntity("Order");
        order.setTableName("ORDERS"); // "ORDER" is a reserved keyword
        order.addIdProperty();
        Property orderDate = order.addDateProperty("date").getProperty();
        Property customerId = order.addLongProperty("customerId").notNull().getProperty();
        order.addToOne(customer, customerId);

        ToMany customerToOrders = customer.addToMany(order, customerId);
        customerToOrders.setName("orders");
        customerToOrders.orderAsc(orderDate);
    }

}


