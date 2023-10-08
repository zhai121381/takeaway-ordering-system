import DBConnect.DataBase;
import DBConnect.ResultsetPrinter;
import DBConnect.Tools;
import java.util.Scanner;
import java.util.Vector;

public class User {

    public User(){
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
                    System.out.println("请输入数字: 1-3！");
            }
        }
        System.out.println("退出用户系统! ");
    }

    public static void main(String[] args) {
        new User();
    }

    /**
     * 显示用户界面，选择登录或注册。
     * @return flag 执行的操作
     */
    private int showMenu(){
        System.out.println("欢迎进入用户系统，请选择功能: (1: 登陆, 2: 注册, 3: 退出)");
        Scanner sc = new Scanner(System.in);
        int flag = sc.nextInt();
        return flag;
    }

    //登录
    private void logIn(){
        //获取用户id
        System.out.println("请输入用户id:");
        Scanner sc = new Scanner(System.in);
        String us_Id = sc.next();
        //获取用户密码
        System.out.println("请输入用户密码:");
        String us_Password = sc.next();
        //登录验证
        String sql = "select * from user where us_Id = ? and Us_Password = ?;";
        Vector<Vector>value=new DataBase().DBQuery(sql,new String[] {us_Id,us_Password});
        if(value.size() > 0){
            //登录成功
            System.out.println("登录成功");
            int flag;
            while ((flag = showFunc()) != 9){
                switch (flag){
                    case 1:
                        //修改用户信息
                        changeUserInfo(us_Id);
                        break;
                    case 2:
                        //显示用户信息
                        showUserInfo(us_Id);
                        break;
                    case 3:
                        //显示商家信息
                        showRestInfo();
                        break;
                    case 4:
                        //显示商品信息
                        showCommodityInfo();
                        break;
                    case 5:
                        //显示骑手信息
                        showRiderInfo();
                        break;
                    case 6:
                        //显示订单信息
                        showOrderInfo(us_Id);
                        break;
                    case 7:
                        //提交订单
                        sendOrder(us_Id );
                        break;
                    case 8:
                        //完成订单
                        finishOrder(us_Id);
                        break;
                    default:
                        System.out.println("请输入数字1-9！");
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
        String us_name = sc.next();
        System.out.println("输入地址：");
        String us_address = sc.next();
        System.out.println("输入号码：");
        String us_phone = sc.next();
        System.out.println("输入密码：");
        String us_password = sc.next();
        String sql = "insert into user values(?,?,?,?,?)";

        //新id
        String newUserId = Tools.addId("us_id", "user");
        int count = new DataBase().insert(sql,new String[]{newUserId, us_name, us_address, us_phone, us_password, });
        if (count < 1){
            System.out.println("注册失败！");
        }else {
            System.out.println("注册成功！");
            System.out.println("您的id:" + newUserId);
        }
    }

    /**显示用户功能
     * @return flag 执行的操作
     */
    private int showFunc(){
        System.out.println("请选择用户功能：(1:修改个人信息,2:查看用户信息, 3:查看商家信息,4:查看商品信息,5:查看骑手信息,6:查看订单信息,7:发起订单,8:确认收货,9:退出)");
        Scanner sc = new Scanner(System.in);
        int flag = sc.nextInt();
        return flag;
    }

    /**
     * 修改用户信息
     * @param us_id 用户id
     */
    private void changeUserInfo(String us_id){
        Scanner sc = new Scanner(System.in);
        System.out.println("输入新用户名：");
        String us_name = sc.next();
        System.out.println("输入新地址：");
        String us_address = sc.next();
        System.out.println("输入新号码：");
        String us_phone = sc.next();
        System.out.println("输入新密码：");
        String us_password = sc.next();
        String sql = "update user set us_name = ?, us_address = ?, us_phone = ?, us_password = ? " +
                "where us_id = ?;";
        int count = new DataBase().insert(sql,new String[]{us_name, us_address, us_phone, us_password, us_id});
        if (count < 1){
            System.out.println("修改失败！");
        }else {
            System.out.println("修改成功！");
        }


    }

    /**
     * 查看用户信息
     * @param us_id 用户id
     */
    private void showUserInfo(String us_id){
        String sql = "select * from user where us_id = " + us_id + ";";
        ResultsetPrinter.sqlQuery(sql);
    }

    /**
     * 查看商家信息
     */
    private void showRestInfo(){
        String sql = "select rt_id, rt_name, rt_address, rt_phone from restaurant;";
        ResultsetPrinter.sqlQuery(sql);
    }

    /**
     * 查看商品信息
     */
    private void showCommodityInfo(){
        String sql = "select * from commodity;";
        ResultsetPrinter.sqlQuery(sql);
    }
    private void showCommodityInfo(String rt_id){
        String sql = "select * from commodity where Co_Rt_Id="+rt_id+";";
        ResultsetPrinter.sqlQuery(sql);
    }
    /**
     * 查看骑手信息
     */
    private void showRiderInfo(){
        String sql = "select rd_id, rd_name, rd_sex  from rider;";
        ResultsetPrinter.sqlQuery(sql);
    }

    /**
     * 查看订单信息
     * @param us_id
     */
    private void showOrderInfo(String us_id){
        String sql = "select * from orders where Od_Us_Id = " + us_id + ";";
        ResultsetPrinter.sqlQuery(sql);
    }

    /**
     * 发起订单
     * @param us_id 用户id
     */
    private void sendOrder(String us_id){

        //展示商家
        showRestInfo();
        Scanner rd = new Scanner(System.in);
        String rtid=rd.next();
        //展示商品信息
        showCommodityInfo(rtid);
        System.out.println("选择商品编号：");
        //获取商品id
        Scanner sc = new Scanner(System.in);
        String co_Id = sc.next();
        //生成新订单id
        String od_Id = Tools.addId("od_id", "orders");
        String od_Us_Name = getUserName(us_id);
        String od_Rt_Id = getRestaurantId(co_Id);
        String od_Rt_Name = getRestaurantName(co_Id);
        String od_Us_Address = getUserAddress(us_id);
        String od_Rt_Address = getRestaurantAddress(od_Rt_Id);
        String od_Co_price=getCommodityPrice(co_Id);
        String od_Co_Name=getCommodityName(co_Id);
        //插入数据库
        String sql_insert = "insert into orders values (?,?,?,?,now(),null,?,?,?,null,null,?,?,?,\"up\");";
        int count = new DataBase().insert(sql_insert, new String[]{od_Id,us_id,od_Us_Name,od_Us_Address,od_Rt_Id,od_Rt_Name,od_Rt_Address,co_Id,od_Co_Name,od_Co_price});
        if (count < 1){
            System.out.println("订单失败！");
        }else {
            System.out.println("订单成功！");
        }

    }

    /**
     * 确认收货
     * @param us_id 用户id
     */
    private void finishOrder(String us_id){
        //查找与用户匹配的order
        String sql_order = "select * from orders where od_us_id = " + us_id + "  ;";
        ResultsetPrinter.sqlQuery(sql_order);

        Scanner sc = new Scanner(System.in);
        System.out.println("输入订单编号:");
        String od_Id = sc.next();

        //获取订单状态
        String sql_state = "select od_state from orders where od_id = " + od_Id + " ;";
        Vector<Vector> vv = new DataBase().dbQuery(sql_state);
        Vector v = vv.elementAt(0);
        String od_state = v.elementAt(0).toString();
        if (od_state.equals("transporting") ){
            String sql_update = "update orders set od_finish_date = now(), od_state = ? " +
                    "where od_id = ? ;";
            int count = new DataBase().insert(sql_update, new String[]{"finish",od_Id});
            if (count < 1){
                System.out.println("订单无法完成！");
            }else {
                System.out.println("订单完成！");
            }
        }else if (od_state.equals("received")){
            System.out.println("订单未在运输！");
        }else if (od_state.equals("finish")){
            System.out.println("无法结束已完成的订单！");
        }

    }
    //获取菜品价格
    private String getCommodityPrice(String co_id){
        String co_pr ;
        String sql_UserName = "select Co_Price from commodity where co_id = " + co_id + " ;";
        Vector<Vector> vv = new DataBase().dbQuery(sql_UserName);
        Vector v = vv.elementAt(0);
        co_pr = v.elementAt(0).toString();
        return co_pr;
    }
//获取菜品名称
    private String getCommodityName(String co_id){
        String co_na ;
        String sql_UserName = "select Co_Name from commodity where co_id = " + co_id + " ;";
        Vector<Vector> vv = new DataBase().dbQuery(sql_UserName);
        Vector v = vv.elementAt(0);
        co_na = v.elementAt(0).toString();
        return co_na;
    }
    /**
     * 输入用户id，获取用户名
     * @param us_id 用户id
     * @return us_name 用户名
     *
     */
    private String getUserName(String us_id){
        String us_name ;
        String sql_UserName = "select us_name from user where us_id = " + us_id + " ;";
        Vector<Vector> vv = new DataBase().dbQuery(sql_UserName);
        Vector v = vv.elementAt(0);
        us_name = v.elementAt(0).toString();

        return us_name;
    }

    /**
     * 输入用户id，获取用户地址
     * @param us_id 用户id
     * @return us_address 用户地址
     *
     */
    private String getUserAddress(String us_id){
        String us_address ;
        String sql_UserName = "select us_address from user where us_id = " + us_id + " ;";
        Vector<Vector> vv = new DataBase().dbQuery(sql_UserName);
        Vector v = vv.elementAt(0);
        us_address = v.elementAt(0).toString();

        return us_address;
    }


    /**
     * 输入商品id，获取商家id
     * @param co_Id 商品id
     * @return rt_Id 商家id
     */
    private String getRestaurantId(String co_Id){
        String rt_Id ;
        String sql_UserName = "select co_rt_id from commodity where co_id = " + co_Id + " ;";
        Vector<Vector> vv = new DataBase().dbQuery(sql_UserName);
        Vector v = vv.elementAt(0);
        rt_Id = v.elementAt(0).toString();
        return rt_Id;
    }

    /**
     *
     * @param co_Id 商品id
     * @return rt_Name 商家名
     */
    private String getRestaurantName(String co_Id){
        String rt_Name ;
        String sql_UserName = "select co_rt_name from commodity where co_id = " + co_Id + " ;";
        Vector<Vector> vv = new DataBase().dbQuery(sql_UserName);
        Vector v = vv.elementAt(0);
        rt_Name = v.elementAt(0).toString();
        return rt_Name;
    }

    /**
     * 输入商家id，获取商家地址
     * @param rt_Id 商品id
     * @return rt_Address 商家地址
     */
    private String getRestaurantAddress(String rt_Id){
        String rt_Address ;
        String sql_UserName = "select rt_address from restaurant where rt_id = " + rt_Id + " ;";
        Vector<Vector> vv = new DataBase().dbQuery(sql_UserName);
        Vector v = vv.elementAt(0);
        rt_Address = v.elementAt(0).toString();
        return rt_Address;
    }
}
