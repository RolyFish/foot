package roily.demo02;

public class UserServiceProxy implements UserService{
    private UserServiceImpl userService;

    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }

    public void add() {
        log("add");
        userService.add();
    }

    public void del() {
        log("del");
        userService.del();
    }

    public void update() {
        log("update");
        userService.update();
    }

    public void get() {
        userService.get();
    }

    public void log(String arg){
        System.out.println("使用了"+arg+"方法");
    }
}
