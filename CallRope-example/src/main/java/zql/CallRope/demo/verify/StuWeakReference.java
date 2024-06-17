package zql.CallRope.demo.verify;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class StuWeakReference extends WeakReference<Student> {
    Integer sid;
    public StuWeakReference(Student referent, ReferenceQueue<? super Student> q) {
        super(referent, q);
        this.sid = referent.sid;
    }
}
