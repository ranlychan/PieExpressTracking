package com.ranlychen.pieexpresstracking.dbflow;

import com.dbflow5.annotation.Database;
import com.dbflow5.config.DBFlowDatabase;

@Database(version = AppDataBase.VERSION)
public abstract class AppDataBase extends DBFlowDatabase {

    public static final String NAME = "AppDatabase";

    public static final int VERSION = 1;

}