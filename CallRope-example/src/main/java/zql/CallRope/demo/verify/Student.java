package zql.CallRope.demo.verify;

public class Student {
    Integer sid;
    String name;
    public Student(Integer sid, String name) {
        this.sid = sid;
        this.name = name;
    }
    @Override
    public String toString() {
        return "Student{" +
                "sid=" + sid +
                ", name='" + name + '\'' +
                '}';
    }
}
