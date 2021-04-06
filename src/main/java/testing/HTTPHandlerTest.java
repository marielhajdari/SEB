package testing;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import server.CURLY;
import server.ReadRequest;

public class HTTPHandlerTest {
    @Test
    public void testHTTPVerb(){
        ReadRequest rreq = new ReadRequest();
        String myVerb = "GET";
        CURLY expected = CURLY.GET;

        CURLY res = rreq.setMyCurl(myVerb);

        Assert.assertEquals(expected, res);
    }

}
