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
        //用绝对路径找！
        new DaoGenerator().generateAll(schema, "app/src/main/java");
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
