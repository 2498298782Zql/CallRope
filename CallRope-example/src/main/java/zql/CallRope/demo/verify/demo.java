package zql.CallRope.demo.verify;

import java.lang.ref.ReferenceQueue;

public class demo {
    public static void main(String[] args) throws InterruptedException {
        ReferenceQueue<Student> queue = new ReferenceQueue<>();
        StuWeakReference studentWeakReference = new StuWeakReference(new Student(1, "zql"), queue);
        clear(studentWeakReference);
        StuWeakReference stu = (StuWeakReference) queue.remove();
        System.out.println(stu.sid);
    }

    public static void clear(StuWeakReference student){
        System.out.println(student.get().name);
        System.gc();
    }
}
