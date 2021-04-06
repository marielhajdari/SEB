package testing;

import manage.History;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class HistoryTest {
    @Test
    public void addPushUpsTest(){
        History h = new History();
        String uname = "username";
        int pu = 2;
        int expTotal = 14;

        int res = h.addPushUps(uname,pu);

        Assert.assertEquals(expTotal, res);
    }

    @Test
    public void totalPushUpsTest(){
        History h = new History();
        String uname = "username";
        int expTotal = 12;

        int res = h.totalPushUps(uname);

        Assert.assertEquals(expTotal, res);
    }

    @Test
    public void activeTourTest(){
        History h = new History();
        boolean exp = true;

        boolean res = h.findActiveTour();

        Assert.assertEquals(exp, res);
    }
}
