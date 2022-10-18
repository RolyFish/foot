package roily.demo04;

public class me {

    public static void main(String[] args) {
        Marry marry = new MarryImpl();
        System.out.println(marry.getClass().getName());

        Class<?>[] interfaces = marry.getClass().getInterfaces();
        for (Class<?> anInterface : interfaces) {
            System.out.println(anInterface.getName());
        }

        System.out.println(marry.getClass().getPackage().getName());
    }

}
