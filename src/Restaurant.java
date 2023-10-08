import DBConnect.DataBase;
import DBConnect.ResultsetPrinter;
import DBConnect.Tools;

import java.util.Scanner;
import java.util.Vector;

public class Restaurant {

    public Restaurant(){
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
        System.out.println("退出商家系统！");
    }

    public static void main(String[] args) {
        Restaurant restaurant = new Restaurant();
    }

    /**
     * 显示商家界面，选择登录或注册。
     * @return flag 执行的操作
     */
    private int showMenu(){
        System.out.println("欢迎进入商家系统！请选择功能：（1：登录，2：注册，3：退出）");
        Scanner sc = new Scanner(System.in);
        int flag = sc.nextInt();
        return flag;
    }

    /**
     * 登录
     */
    private void logIn(){
        //获取商家id
        System.out.println("请输入商家id：");
        Scanner sc = new Scanner(System.in);
        String rt_Id = sc.next();
        //获取商家密码
        System.out.println("请输入商家密码：");
        String rt_Password = sc.next();
        //登录验证
        String sql = "select * from restaurant where rt_Id = ? and rt_Password = ?;";
        Vector<Vector>value=new DataBase().DBQuery(sql,new String[] {rt_Id,rt_Password});
        if(value.size() > 0){
            //登录成功
            System.out.println("登录成功");
            int flag;
            while ((flag = showFunc()) != 8){
                switch (flag){
                    case 1:
                        //修改商家信息
                        changeRestaurantInfo(rt_Id);
                        break;
                    case 2:
                        //查看商品信息
                        showCommodityInfo(rt_Id);
                        break;
                    case 3:
                        //新增商品
                        String rt_Name = getRestaurantName(rt_Id);
                        addCommodity(rt_Name, rt_Id);
                        break;
                    case 4:
                        //修改商品
                        modifyCommodity(rt_Id);
                        break;
                    case 5:
                        //删除商品
                        delCommodity(rt_Id);
                        break;
                    case 6:
                        //查看订单
                        showorders(rt_Id);
                        break;
                    case 7:
                        //接受订单
                        receiveorders(rt_Id);
                        break;
                    default:
                        System.out.println("请输入数字1-8！");
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
        System.out.println("输入商家名：");
        String rt_name = sc.next();
        System.out.println("输入地址：");
        String rt_Address = sc.next();
        System.out.println("输入手机号：");
        String rt_Phone = sc.next();
        System.out.println("输入密码：");
        String rt_password = sc.next();
        String sql = "insert into restaurant values(?,?,?,?,?)";

        //新id
        String newUserId = Tools.addId("rt_Id", "restaurant");
        int count = new DataBase().insert(sql,new String[]{newUserId, rt_name, rt_Address,rt_password, rt_password });
        if (count < 1){
            System.out.println("注册失败！");
        }else {
            System.out.println("注册成功！");
            System.out.println("您的id:" + newUserId);
        }
    }

    /**显示商家功能
     * @return flag 执行的操作
     */
    private int showFunc(){
        System.out.println("请选择商家功能：(1:修改商家信息,2:查看商品, 3:新增商品,4:修改商品, 5:删除商品,6:查看订单,7:接受订单,8:退出)");
        Scanner sc = new Scanner(System.in);
        int flag = sc.nextInt();
        return flag;
    }

    /**
     * 修改商家信息todo 修改信息牵扯到其他表
     * @param rt_id 商家id
     */
    private void changeRestaurantInfo(String rt_id){
        Scanner sc = new Scanner(System.in);
        System.out.println("输入新商家名:");
        String rt_Name = sc.next();
        System.out.println("输入新地址:");
        String rt_Address = sc.next();
        System.out.println("输入新手机号:");
        String rt_Phone = sc.next();
        System.out.println("输入新密码:");
        String rt_Password = sc.next();

        //更新数据
        String sql = "update restaurant set rt_name = ?, rt_Address = ?, rt_Phone, rt_password = ? " +
                "where rt_id = ?;";
        int count = new DataBase().insert(sql,new String[]{rt_Name, rt_Address, rt_Phone, rt_Password, rt_id});
        if (count < 1){
            System.out.println("修改失败！");
        }else {
            System.out.println("修改成功！");
        }
    }

    /**
     * 查看商品信息
     * @param rt_id 商家id
     */
    private void showCommodityInfo(String rt_id){
        String sql = "select * from commodity where co_rt_id = " + rt_id + ";";
        ResultsetPrinter.sqlQuery(sql);
    }

    /**
     *增加商品
     * @param co_Rt_Name 商家名
     * @param co_Rt_Id 商家id
     */
    private void addCommodity(String co_Rt_Name, String co_Rt_Id){
        Scanner sc = new Scanner(System.in);
        System.out.println("输入商品名:");
        String co_Name = sc.next();
        System.out.println("输入价格:");
        double co_Price = sc.nextDouble();
        System.out.println("输入剩余数量:");
        int co_RemainingNumber = sc.nextInt();

        //插入数据
        String sql = "insert into commodity values(?,?,?,?,?,?);";
        int count = new DataBase().insert(sql, new String[]{Tools.addId("co_id","commodity"),
                co_Name, co_Rt_Name, co_Rt_Id, String.valueOf(co_Price), String.valueOf(co_RemainingNumber)});

        if (count < 1){
            System.out.println("添加失败！");
        }else {
            System.out.println("添加成功！");
        }
    }

    /**
     * 修改商品
     * @param rt_Id 商家id
     */
    private void modifyCommodity(String rt_Id){
        Scanner sc = new Scanner(System.in);
        showCommodityInfo(rt_Id);
        System.out.println("输入修改的商品编号:");
        String co_Id = sc.next();

        //判断是否存在
        String sql_exist = "select * from commodity where co_Id = " + co_Id + " and co_rt_Id = " + rt_Id + ";";

        Vector<Vector> vv = new DataBase().dbQuery(sql_exist);
        boolean isEmpty = vv.isEmpty();
        if (isEmpty == true){
            System.out.println("商品不存在！");
        }else {
            //商品存在，可以修改
            System.out.println("输入新商品名:");
            String co_Name = sc.next();
            System.out.println("输入新价格:");
            double co_Price = sc.nextDouble();
            System.out.println("输入新剩余数量:");
            int co_RemainingNum = sc.nextInt();
            String co_Rt_Name = getRestaurantName(rt_Id);

            //修改
            String sql_update = "update commodity set co_Name = ?, co_Rt_Name = ?, co_Rt_Id = ?, " +
                    "co_Price = ?, co_Remainingnumber = ? where co_Id = ?;";
            int count = new DataBase().insert(sql_update,new String[]{co_Name, co_Rt_Name, rt_Id,
                    String.valueOf(co_Price), String.valueOf(co_RemainingNum), co_Id});
            if (count < 1){
                System.out.println("修改失败！");
            }else {
                System.out.println("修改成功！");
            }

        }

    }

    /**
     * 删除商品
     * @param rt_Id
     */
    private void delCommodity(String rt_Id){
        showCommodityInfo(rt_Id);
        System.out.println("输入删除的商品编号:");
        Scanner sc = new Scanner(System.in);
        String co_Id = sc.next();

        //删除
        String sql = "delete from commodity where co_Id = " + co_Id + " ;";
        int count = new DataBase().insert(sql,new String[]{});
        if (count < 1){
            System.out.println("删除失败！");
        }else {
            System.out.println("删除成功！");
        }
    }
    //查看订单
    private void showorders(String rt_Id){
        String sql="select * from orders where Od_Rt_Id = "+rt_Id+" ;";
        ResultsetPrinter.sqlQuery(sql);
    }
    //接受订单
    private void receiveorders(String rt_Id){
        //展示订单
        String sql1="select * from orders where Od_Rt_Id = "+rt_Id+" and Od_State = \"up\";";
        ResultsetPrinter.sqlQuery(sql1);
        //输入要接受的订单编号
        System.out.println("输入接受的订单编号：");
        Scanner re = new Scanner(System.in);
        String od_Id = re.next();
        String sql_state = "select od_state from orders where od_id = " + od_Id + " ;";
        Vector<Vector> vv = new DataBase().dbQuery(sql_state);

        //判断结果是否为空
        boolean isEmpty = vv.isEmpty();
        if (isEmpty == true){
            System.out.println("订单编号错误！");
        }else {
            Vector v = vv.elementAt(0);
            String od_state = v.elementAt(0).toString();
            if (od_state.equals("up") ){
                String sql_update = "update orders set od_state = ? " +
                        "where od_id = ? ;";
                int count = new DataBase().insert(sql_update, new String[]{"received",od_Id});
                if (count < 1){
                    System.out.println("订单无法接受！");
                }else {
                    System.out.println("订单成功接受！");
                }
            }
            else if (od_state.equals("received")){
                System.out.println("无法接受已存在的订单！");
            }
        }


    }

    /**
     * 获取商家名
     * @param rt_Id 商家id
     * @return rt_Name 商家名
     */
    private String getRestaurantName(String rt_Id){
        String rt_Name;
        String sql = "select rt_Name from restaurant where rt_Id = " + rt_Id + " ;";
        Vector<Vector> vv = new DataBase().dbQuery(sql);
        Vector v = vv.elementAt(0);
        rt_Name = v.elementAt(0).toString();
        return rt_Name;
    }
}
