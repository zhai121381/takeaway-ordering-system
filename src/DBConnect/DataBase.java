package DBConnect;

import java.sql.*;
import java.util.Vector;

public class DataBase {
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    private Statement st;

    public Vector<Vector> dbQuery(String sql){
        Vector<Vector> v=new Vector<Vector>();
        con= JDBC.connection();
        try {
            st=con.createStatement();
            rs=st.executeQuery(sql);
            int c=rs.getMetaData().getColumnCount();//返回结果集对象中的字段数
            while(rs.next()) {
                Vector vt=new Vector();
                for(int i=1;i<=c;i++) {
                    vt.add(rs.getObject(i));
                }
                v.add(vt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                st.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return v;
    }

    //动态查询
    public Vector<Vector> DBQuery(String sql, Object[] params) {
        Vector<Vector> v = new Vector<Vector>();
        con= JDBC.connection();
        ResultSet rs = null;

        try {
            // st = con.createStatement();
            ps = con.prepareStatement(sql);
            if (params != null && params.length > 0) {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
            }

            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int i = rsmd.getColumnCount();
            while (rs.next()) {
                Vector vx = new Vector();
                for (int m = 1; m <= i; m++) {
                    try {
                        vx.add(rs.getObject(m));
                    } catch (SQLException e) {
                        if (e.getMessage().startsWith("Cannot convert value '0000-00-00 00:00:00' from"))
                        {vx.add(java.sql.Timestamp.valueOf("0001-01-01 00:00:00"));
                            // System.out.println(vx.elementAt(vx.size()-1).toString());
                        }
                    }
                }
                v.add(vx);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ps.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return v;
    }



    //插入，更新，删除
    public int insert(String sql,Object[] params) {
        int count=0;
        con= JDBC.connection();
        try {
            con.setAutoCommit(false);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        try {
            ps=con.prepareStatement(sql);
            if(params!=null&&params.length>0) {
                for(int i=0;i<params.length;i++) {
                    ps.setObject(i+1, params[i]);
                }
            }
            count=ps.executeUpdate();
            if(count>0) {
                con.commit();
            }
            con.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                ps.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return count;
    }

//    public static void main(String[] args) {
//        DataBase n = new DataBase();
//        Object[] a= new Object[1];
//        a[0]=1;
//        n.DBQuery("select * from user where Us_id=?",a);
//    }
}
