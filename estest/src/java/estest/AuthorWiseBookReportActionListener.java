package estest;


import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.search.facet.Facet;
import org.elasticsearch.search.facet.terms.TermsFacet;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created with IntelliJ IDEA.
 * User: gannu
 * Date: 6/22/14
 * Time: 5:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class AuthorWiseBookReportActionListener extends SuperListener {
    org.slf4j.Logger logger = LoggerFactory.getLogger(AuthorWiseBookReportActionListener.class);

    public AtomicBoolean isReady=new AtomicBoolean(false);
    public XContentBuilder contentBuilder=null;

    public Map<String,HoldName> Outermap= new HashMap<String, HoldName>();

    @Override
    public void actionResponse(ActionResponse actionResponse) {

        if (actionResponse instanceof MultiSearchResponse){
            MultiSearchResponse multiSearchResponse = (MultiSearchResponse) actionResponse;
            MultiSearchResponse.Item[] responseItems = multiSearchResponse.getResponses();
            int counter =1;

            for (MultiSearchResponse.Item responseItem : responseItems) {

               // logger.error("Response {}= {}", counter++, responseItem.getResponse().toString());
                if(responseItem.isFailure()){
                    System.out.println(responseItem.getFailureMessage());
                    System.out.println("counter = " + counter);
                }
                else
                {
                    SearchResponse searchResponse = responseItem.getResponse();

                    processResponse(searchResponse);

                    counter++;
                }
            }
        }else{
            logger.error("Failed to handle response.");

        }
        try {
            writeContent();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        isReady.set(true);
    }



    private void processResponse(SearchResponse searchResponse) {
        for(Facet facet:searchResponse.getFacets())
        {
            TermsFacet termsFacet = (TermsFacet)facet;
            String author= termsFacet.getName().split(":")[1];
            HoldName existingHoldName = getHoldName(author);
            Record record = existingHoldName.getRecord(author);

            for(TermsFacet.Entry entry:termsFacet){
                String metric= termsFacet.getName().split(":")[0];
                record.map.put(metric,entry.getTerm().toString());
                System.out.println(record.map.entrySet());
            }

        }

    }

    public HoldName getHoldName(String name){
        if(!Outermap.containsKey(name))
        {
            HoldName holdName= new HoldName();
            Outermap.put(name,holdName);

        }
        return Outermap.get(name);
    }

    public class HoldName{
        public String id;
        public Map<String,Record> map = new HashMap<String, Record>();
        public Record getRecord(String name){
            if(!map.containsKey(name))
            {
                Record record= new Record();
                this.map.put(name,record);
            }
            return map.get(name);
        }
    }

    public class Record{

       public String id;
        public Map<String,String> map= new HashMap<String, String>();

    }

    private void writeContent() throws Exception{
        contentBuilder= XContentFactory.jsonBuilder().startObject().startObject("report");
        for(Map.Entry<String,HoldName> entry: Outermap.entrySet()){
            String firstlevelkey= entry.getKey();
            HoldName holdName= Outermap.get(firstlevelkey);
            contentBuilder.startObject(firstlevelkey);
            Record record=holdName.getRecord(firstlevelkey);
            for (Map.Entry<String,String>entry1:record.map.entrySet()){
                contentBuilder.field(entry1.getKey(),entry1.getValue());
            }
            contentBuilder.endObject();
        }
        contentBuilder.endObject();
    }

    @Override
    public void onFailure(Throwable throwable) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
