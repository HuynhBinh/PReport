package pl.surecase.eu;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class MyDaoGenerator
{

    public static Schema schema;

    public static void main(String args[]) throws Exception
    {
        schema = new Schema(3, "greendao");
        genDB();
        new DaoGenerator().generateAll(schema, args[0]);
    }

    public static void genDB()
    {
        Entity location = schema.addEntity("PLocation");
        location.addIdProperty().autoincrement();
        location.addLongProperty("svId").unique();
        location.addDoubleProperty("latitude");
        location.addDoubleProperty("longitude");
        location.addBooleanProperty("isDelete");
        location.addStringProperty("timeStamp");
        location.addStringProperty("CSGTType");
        location.addStringProperty("type");
        location.addIntProperty("count");
        location.addIntProperty("status");
        location.addBooleanProperty("isReported");
        location.addStringProperty("note");
        location.addBooleanProperty("isEnter").notNull();


        Entity myLastLocation = schema.addEntity("MyLocation");
        myLastLocation.addIdProperty();
        myLastLocation.addDoubleProperty("latitude");
        myLastLocation.addDoubleProperty("longitude");

        Entity sharePref = schema.addEntity("SharePrefs");
        sharePref.addStringProperty("key").primaryKey();
        sharePref.addStringProperty("value");


    }

    public static void gen1box_manyitems()
    {

        Entity box = schema.addEntity("Box");
        box.addIdProperty();
        box.addStringProperty("name");
        box.addIntProperty("slots");
        box.addStringProperty("description");

        Entity item = schema.addEntity("Item");
        Property itemId = item.addIdProperty().getProperty();
        item.addStringProperty("name");
        item.addIntProperty("quantity");

        Property boxId = item.addLongProperty("boxId").getProperty();
        ToMany boxToItem = box.addToMany(item, boxId);
        boxToItem.orderDesc(itemId);

    }


    public static void gen1box1item()
    {

        Entity box = schema.addEntity("Box");
        box.addIdProperty();
        box.addStringProperty("name");
        box.addIntProperty("slots");
        box.addStringProperty("description");

        Entity item = schema.addEntity("Item");
        item.addIdProperty();
        item.addStringProperty("name");
        item.addIntProperty("quantity");

        Property itemId = box.addLongProperty("itemId").getProperty();
        box.addToOne(item, itemId);

    }
}
