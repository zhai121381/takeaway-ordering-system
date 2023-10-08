package DBConnect;

import DBConnect.JDBC;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;



// 打印sql查询
public class ResultsetPrinter {
    public static void printResultset(ResultSet rs) throws SQLException {
        if(rs.isBeforeFirst() == false){
            System.out.println("查询结果为空！");
            return ;
        }
        ResultSetMetaData resultsetMetaData = rs.getMetaData();

        int columnCount = resultsetMetaData.getColumnCount();

        int[] columnMaxLengths = new int[columnCount];
        ArrayList<String[]> results = new ArrayList<>();
        while (rs.next()) {
            String[] columnStr =new String[columnCount];
            for (int i = 0; i<columnCount; i++){
                columnStr[i] = rs.getString(i+1);
                columnMaxLengths[i] = Math.max(columnMaxLengths[i],(columnStr[i]==null) ? 0: columnStr[i].length());
                columnMaxLengths[i] = Math.max(columnMaxLengths[i],resultsetMetaData.getColumnName(i+1).length());
            }

            results.add(columnStr);
        }
        printseparator(columnMaxLengths);
        printcolumnName(resultsetMetaData, columnMaxLengths);
        printseparator(columnMaxLengths);
        Iterator<String[]> iterator = results.iterator();
        String[] columnstr;
        while (iterator.hasNext()) {
            columnstr = iterator.next();
            for (int i = 0; i < columnCount; i++) {

                System.out.printf("|%" + columnMaxLengths[i] + "s", columnstr[i]);
            }
            System.out.println("I");
        }
        printseparator(columnMaxLengths);
    }

    private static void printcolumnName(ResultSetMetaData resultSetMetaData, int[] columnMaxLengths) throws SQLException{

        int columnCount = resultSetMetaData.getColumnCount();
        for(int i = 0;i<columnCount;i++){
            System.out.printf("|%" + columnMaxLengths[i] + "s", resultSetMetaData.getColumnName(i+1));
        }
        System.out.println("|");
    }

    private static void printseparator(int[]columnMaxLengths) {
        for(int i=0;i<columnMaxLengths.length;i++){
            System.out.print("+");
            for (int j = 0; j < columnMaxLengths[i]; j++){
                System.out.print("-");
            }
        }
        System.out.println("+");
    }

    public static void sqlQuery(String sql){
        Connection con = JDBC.connection();
        Statement st = null;
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            printResultset(rs);
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                st.close();
                con.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }


    }
}



