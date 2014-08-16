package estest;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.search.facet.Facet;
import org.elasticsearch.search.facet.termsstats.TermsStatsFacet;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Created with IntelliJ IDEA.
 * User: gbhattarai
 * Date: 4/24/14
 * Time: 9:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class EsTestActionListener implements ActionListener<ActionResponse> {


    org.slf4j.Logger logger = LoggerFactory.getLogger(EsTestActionListener.class);

    public AtomicBoolean isReady=new AtomicBoolean(false);
     public XContentBuilder contentBuilder=null;
    private Map<String,Map<String,Double>> map= new HashMap<String, Map<String, Double>>();
      @Override
    public void onResponse(ActionResponse actionResponse) {

          System.out.println("+++++++++++++++++++++++++++test+++++++++++++++++");

          if (actionResponse instanceof MultiSearchResponse){
              MultiSearchResponse multiSearchResponse = (MultiSearchResponse) actionResponse;
              MultiSearchResponse.Item[] responseItems = multiSearchResponse.getResponses();
              int counter =1;

              for (MultiSearchResponse.Item responseItem : responseItems) {

                  logger.error("Response {}= {}", counter++, responseItem.getResponse().toString());
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
    private void processResponse(SearchResponse response)  {


                for (Facet facet : response.getFacets().facets()) {
                    TermsStatsFacet termsStatsFacet = (TermsStatsFacet) facet;
                    for (TermsStatsFacet.Entry stringEntry : termsStatsFacet.getEntries()) {
                        initRecord(facet.getName(),stringEntry);
                    }


                }



    }

    private void initRecord(String facetName, TermsStatsFacet.Entry stringEntry){
        Map<String,Double> inMap= new HashMap<String, Double>();
        map.put(facetName+"_"+stringEntry.getTerm(),inMap);
        inMap.put("No_of_Book",Double.parseDouble(stringEntry.getCount()+""));
        inMap.put("mean",Double.parseDouble(stringEntry.getMean()+""));
    }


    private void writeContent() throws Exception {


        contentBuilder=XContentFactory.jsonBuilder().startObject().startObject("report");

        for (Map.Entry<String,Map<String,Double>> e : map.entrySet()) {
            contentBuilder.startObject(e.getKey());

            Map<String,Double>Inmap = e.getValue();

            for(Map.Entry<String,Double> inMap : Inmap.entrySet()) {

                contentBuilder.field(inMap.getKey(),inMap.getValue());

            }
            contentBuilder.endObject();

        }
        contentBuilder.endObject();

    }


    @Override
    public void onFailure(Throwable throwable) {
        isReady.set(true);
    }


}
