package com.bitsolution.pln.Helpers;

import org.json.JSONObject;

/**
 * Created by GILBERT on 27/08/2017.
 */

public class SQLiteDBHelper {
    static String DatabaseName = "rainbow";
    static int DatabaseVersion = 1;

    public static String DT_REQ = "MOBILE";
    public static String Table_Menu = "menu";
    public static String Table_Label = "mbl_label";
    public static String Table_Rbm = "rute_baca";

    static String CTable_Menu = "CREATE TABLE `" + Table_Menu + "` ( `block` TEXT, `url` TEXT, `icons` TEXT, `label` TEXT, `urut` INTEGER)";
    static String CTable_Label = "CREATE TABLE `" + Table_Label + "` ( `msg_code` TEXT, `msg_text` TEXT)";
    static String CTable_Rbm = "CREATE TABLE `" + Table_Rbm + "` ( `rute` TEXT )";

    public static void BatchSaveDataToSQLite(String table, JSONObject json) {
    }

}
