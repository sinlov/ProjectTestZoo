package stringtest;

public class NumberTest {
    public static void main(String[] args) {
        int a = Integer.MAX_VALUE >> 2;
        int b = Integer.MAX_VALUE;
        System.out.println(a);
        System.out.println(b);
        System.out.println(b);
        System.out.println(b);
        System.out.println(a);
        System.out.println(a);
        for(int i = 0; i < 100; i++){
            System.out.println(1 << i);
        }
            
    }  
}
