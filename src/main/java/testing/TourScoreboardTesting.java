package testing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import manage.History;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class TourScoreboardTesting {

    @Test
    public void tstesting(){
        History h = new History();
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode arrayNode = mapper.createArrayNode();
        String expected = null;

        ObjectNode on = mapper.createObjectNode();
        on.put("participant ID :",1);
        //on.put("Elo:",rs.getInt(2));
        on.put("Name:","username");
        on.put("Total PushUps:",12);
        on.put("Tour ID :",1);
        arrayNode.add(on);
        try {
            expected = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayNode);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String res = h.tourScoreboard();

        Assert.assertEquals(expected, res);


    }
}
