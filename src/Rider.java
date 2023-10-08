import DBConnect.DataBase;
import DBConnect.ResultsetPrinter;
import DBConnect.Tools;

import java.util.Scanner;
import java.util.Vector;

public class Rider {

    public Rider(){
        int flag;
        while ((flag = showMenu()) != 3){
            switch (flag){
                //登录
                case 1:
                    logIn();
                    break;
                //注册
                case 2:
                    register();
                    break;
                default:
                    System.out.println("请输入数字1-3！");
            }
        }
        System.out.println("退出骑手系统！");
    }

    public static void main(String[] args) {
        Rider rider = new Rider();
    }

    /**
     * 显示骑手界面，选择登录或注册。
     * @return flag 执行的操作
     */
    private int showMenu(){
        System.out.println("欢迎进入骑手系统！请选择功能：（1：登录，2：注册，3：退出）");
        Scanner sc = new Scanner(System.in);
        int flag = sc.nextInt();
        return flag;
    }

    /**
     * 登录
     */
    private void logIn(){
        //获取骑手id
        System.out.println("请输入骑手id：");
        Scanner sc = new Scanner(System.in);
        String rd_Id = sc.next();
        //获取骑手密码
        System.out.println("请输入骑手密码：");
        String rd_Password = sc.next();
        //登录验证
        String sql = "select * from rider where rd_Id = ? and rd_Password = ?;";
        Vector<Vector>value=new DataBase().DBQuery(sql,new String[] {rd_Id,rd_Password});
        if(value.size() > 0){
            //登录成功
            System.out.println("登录成功");
            int flag;
            while ((flag = showFunc()) != 4){
                switch (flag){
                    case 1:
                        //修改骑手信息
                        changeRiderInfo(rd_Id);
                        break;
                    case 2:
                        //显示订单信息
                        showOrderInfo();
                        break;
                    case 3:
                        //派送订单
                        String rd_name = getRiderName(rd_Id);
                        transportOrder(rd_Id, rd_name);
                        break;
                    default:
                        System.out.println("请输入数字1-4！");
                        break;
                }
            }
        }else{
            //登录失败
            System.out.println("用户名或密码错误");
        }
    }

    /**注册
     *
     */
    private void register(){
        Scanner sc = new Scanner(System.in);
        System.out.println("输入用户名：");
        String rd_name = sc.next();
        System.out.println("输入性别：");
        String rd_sex = sc.next();
        System.out.println("输入密码：");
        String rd_password = sc.next();
        String sql = "insert into rider values(?,?,?,?)";

        //新id
        String newUserId = Tools.addId("rd_id", "rider");
        int count = new DataBase().insert(sql,new String[]{newUserId, rd_name, rd_sex, rd_password });
        if (count < 1){
            System.out.println("注册失败！");
        }else {
            System.out.println("注册成功！");
            System.out.println("您的id:" + newUserId);
        }
    }

    /**显示骑手功能
     * @return flag 执行的操作
     */
    private int showFunc(){
        System.out.println("请选择骑手功能：(1:修改个人信息,2:查看订单信息,3:接受订单,4:退出)");
        Scanner sc = new Scanner(System.in);
        int flag = sc.nextInt();
        return flag;
    }

    /**
     * 修改骑手信息
     * @param rd_id 骑手id
     */
    private void changeRiderInfo(String rd_id){
        Scanner sc = new Scanner(System.in);
        System.out.println("输入新用户名：");
        String rd_name = sc.next();
        System.out.println("输入新性别：");
        String rd_sex = sc.next();
        System.out.println("输入新密码：");
        String rd_password = sc.next();

        String sql = "update rider set rd_name = ?, rd_sex = ?,  rd_password = ? " +
                "where rd_id = ?;";
        int count = new DataBase().insert(sql,new String[]{rd_name, rd_sex,  rd_password, rd_id});
        if (count < 1){
            System.out.println("修改失败！");
        }else {
            System.out.println("修改成功！");
        }


    }

    /**
     * 查看订单信息
     */
    private void showOrderInfo(){
        String sql = "select * from orders where od_State = 'received' or od_state = 'transporting';";
        ResultsetPrinter.sqlQuery(sql);
    }

    /**
     * 派送订单
     *
     * @param rd_id
     * @param rd_name
     */
    private void transportOrder(String rd_id, String rd_name){
        //打印订单信息
        showOrderInfo();

        //获取订单编号
        System.out.println("输入要接受的订单编号：");
        Scanner sc = new Scanner(System.in);
        String od_Id = sc.next();

        //获取订单状态
        String sql_state = "select od_state from orders where od_id = " + od_Id + ";";
        Vector<Vector> vv = new DataBase().dbQuery(sql_state);
        //判断结果是否为空
        boolean isEmpty = vv.isEmpty();
        if (isEmpty == true){
            System.out.println("订单编号错误！");
        }else {
            Vector v = vv.elementAt(0);
            String od_state = v.elementAt(0).toString();
            if (od_state.equals("received") ){
                String sql_update = "update orders set od_state = ? , od_rd_id = ?, od_rd_name = ? " +
                        "where od_id = ? ;";
                int count = new DataBase().insert(sql_update, new String[]{"transporting", rd_id, rd_name, od_Id});
                if (count < 1){
                    System.out.println("订单无法接收！");
                }else {
                    System.out.println("订单接收！");
                }
            }else {
                System.out.println("订单编号不可接收！");
            }
        }


    }

    /**
     * 接受骑手id，返回骑手名
     * @param rd_id 骑手id
     * @return rd_name 骑手名
     */
    private String getRiderName(String rd_id){
        String rd_name;
        String sql_RiderName = "select rd_name from rider where rd_id = " + rd_id + " ;";
        Vector<Vector> vv = new DataBase().dbQuery(sql_RiderName);
        Vector v = vv.elementAt(0);
        rd_name = v.elementAt(0).toString();

        return rd_name;
    }
}
