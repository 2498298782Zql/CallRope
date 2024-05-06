package zql.CallRope.demo;

public class test3 {
    public static void main(String[] args) {
        try {
            try {
                System.out.println("1");
            }finally {
                System.out.println("2");
                return;
            }
        }finally {
            System.out.println("over");
        }
    }
}
