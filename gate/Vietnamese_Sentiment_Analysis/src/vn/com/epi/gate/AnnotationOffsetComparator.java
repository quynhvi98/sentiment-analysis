package vn.com.epi.gate;

import gate.Annotation;
import org.junit.Assert;

import java.util.Comparator;

/**
 * Posted from Jun 09, 2018 9:57 AM
 *
 * @author viquy (vi.quynh.31598@gmail.com)
 */
public class AnnotationOffsetComparator implements Comparator<Annotation> {
    public int compare(Annotation a1, Annotation a2){
        Assert.assertNotNull(a1);
        Assert.assertNotNull(a2);

        int offset1 = a1.getStartNode().getOffset().intValue();
        int offset2 = a2.getStartNode().getOffset().intValue();

        return offset1 - offset2;
    }
}
