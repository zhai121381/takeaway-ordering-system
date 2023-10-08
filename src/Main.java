import java.util.Scanner;

public class Main {

    public Main(){

        int id ;//标记用户身份
        while ((id = showMenu()) != 4){
            if (id == 1){
                User user = new User();
            }else if (id == 2){
                Restaurant restaurant = new Restaurant();
            }else if(id == 3){
                Rider rider = new Rider();
            }else {
                System.out.println("请输入数字1-4！");
            }
        }
        System.out.println("再见！");
    }

    public static void main(String[] args) {
        try {
            Main login = new Main();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private int showMenu(){
        System.out.println("欢迎登陆外卖系统！请选择您的身份：（1：用户，2：商家，3：骑手，4：退出）");
        Scanner sc = new Scanner(System.in);
        int id = sc.nextInt();
        return id;
    }
}
