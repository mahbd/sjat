package com.sajjadsjat.model;

import com.sajjadsjat.enums.Goods;
import com.sajjadsjat.enums.Units;

public class UserRecordData {
    public static ClientRecord instance1 = new ClientRecord(new Record(Goods.CEMENT, 1, Units.BAG, 560, 0, "Kausar"));
    public static ClientRecord instance2 = new ClientRecord(new Record(Goods.SAND, 1, Units.CFT, 30, 0, "Rofiqul"));
    public static ClientRecord instance3 = new ClientRecord(new Record(Goods.ROD, 1, Units.KG, 80, 0, "Kausar"));
    public static ClientRecord instance4 = new ClientRecord(new Record(Goods.PILLAR, 1, Units.CFT, 30, 0, "Dollar"));
    public static ClientRecord instance5 = new ClientRecord(new Record(Goods.BRICK, 1, Units.PCS, 10, 0, "Kausar"));
    public static ClientRecord instance6 = new ClientRecord(new Record(Goods.KHUA, 1, Units.CFT, 30, 0, "Rofiqul"));
}
