import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Elena on 10.05.2018.
 */
public class SwissTest {
    private Swiss swiss;
    @Before
    public void init() { swiss = new Swiss(); }

    @Test
    public void statistic() {
        String str = "averagee word length";
        String[] mas = str.split(" ");
        List<Object> lst = new LinkedList<Object>();
        lst.add("averagee");
        lst.add("word");
        lst.add(str.length());
        lst.add(6.0);
        Assert.assertEquals(swiss.staticStrings(mas, str.length()), lst);
        List<Object> testString = swiss.staticStrings(mas, str.length());
        assertTrue((Integer)testString.get(2) == 20);
        Assert.assertEquals((String) testString.get(1), "word");
        Assert.assertEquals((String) testString.get(0), "averagee");
        assertTrue((Double)testString.get(3) == 6.0);
    }

}
