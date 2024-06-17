package zql.CallRope.demo.verify;

import java.lang.ref.ReferenceQueue;

public class demo {
    public static void main(String[] args) throws InterruptedException {
        ReferenceQueue<Student> queue = new ReferenceQueue<>();
        Student student = new Student(1, "zql");
        StuWeakReference studentWeakReference = new StuWeakReference(student, queue);
        clear(student);
        System.gc();
        StuWeakReference stu = (StuWeakReference) queue.remove();
        System.out.println(stu.sid);
    }

    public static void clear(Student student){
        student = null;
    }
}
