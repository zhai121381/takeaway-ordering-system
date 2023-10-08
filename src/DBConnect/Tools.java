package DBConnect;

import DBConnect.DataBase;

import java.util.Vector;

public class Tools {
    public Tools(){

    }

    /**
     * 创建新id
     * @param id_type id类型，如us_id
     * @param table 数据表名, 如user
     * @return id 新id
     *
     */
    public static  String addId(String id_type, String table){
        //char不能用max比较，要加0转换成数字
        String sql = "select max( " + id_type + " + 0) from " + table + ";";
        Vector<Vector> vv = new DataBase().dbQuery(sql);
        Vector v = vv.elementAt(0);
        double o = Double.parseDouble(v.elementAt(0).toString());
        int id = (int)o + 1;

        return String.valueOf(id);
    }
}
