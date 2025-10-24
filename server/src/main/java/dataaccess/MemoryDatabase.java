package dataaccess;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import model.UserData;

public class MemoryDatabase {

    // use concurrent in case multiple things try to edit at once
    // static because it is the same accross whole program
    public static Map<String, UserData> userMap = new ConcurrentHashMap();
}
