import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class SwissTest {
    private Swiss swiss;
    @Before
    public void init() { swiss = new Swiss(); }

    @Test
    public void statistic() {
        String strings = "averagee word length";
        String[] arrayStrings = strings.split(" ");
        ListString listString = new ListString();
        listString.setAverage(6.0);
        listString.setShortest("word");
        listString.setLength(20);
        listString.setLongest("averagee");
        List<ListString> testString1 = new ArrayList<ListString>();
        testString1.add(listString);
//        Assert.assertEquals(swiss.getListListStrings(strings),testString1);
        List<ListString> testString = swiss.getListListStrings(strings);
        for(ListString listString1 : testString) {
            assertTrue(listString1.getLength() == 20);
            Assert.assertEquals(listString1.getShortest(), "word");
            Assert.assertEquals(listString1.getLongest(), "averagee");
            assertTrue(listString1.getAverage() == 6.0);
        }
    }

}
