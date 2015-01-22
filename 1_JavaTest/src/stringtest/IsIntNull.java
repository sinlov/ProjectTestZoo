package stringtest;


public class IsIntNull {
    public static void main(String[] args) {
        int a;
    }
    private static boolean getNumIsNull(int a){
        if (String.valueOf(a).equals("")) {
            return true;
        }else {
            return false;
        }
    }
}
