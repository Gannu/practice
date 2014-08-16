package estest

import org.codehaus.groovy.grails.web.json.JSONObject
import org.elasticsearch.action.search.SearchRequestBuilder
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.search.facet.Facet
import org.elasticsearch.search.facet.statistical.StatisticalFacet
import org.elasticsearch.search.facet.terms.TermsFacet

class ApiController {

    def index() {
        ElasticSearchAPI elasticSearchAPI= new ElasticSearchAPI()
        SearchRequestBuilder requestBuilder= elasticSearchAPI.getQuery2()
        JSONObject jsonObject=new JSONObject()
        JSONObject jsonObject1=new JSONObject()
        jsonObject.put("abc",jsonObject1)



        SearchResponse response=requestBuilder.execute().actionGet();
        for (Facet facet : response.facets.facets()) {
            if(facet instanceof StatisticalFacet){
                StatisticalFacet sfacet=(StatisticalFacet)facet
                //double mean=sfacet.getMean()

                jsonObject1.put("mean",sfacet.mean)
                jsonObject1.put("total",sfacet.total)
                jsonObject1.put("variance",sfacet.variance)

              }
            else if(facet instanceof TermsFacet)
            {
               // TermsFacet termsFacet = (TermsFacet) response.getFacets().facetsAsMap().get("termsFacet");


                //TermsFacet termsFacet= TermsFacet(facet)
                for(TermsFacet.Entry entry:facet.getEntries()){
                    jsonObject1.put(entry.getTerm().string(),entry.getCount());
                }

                jsonObject1.put("other",facet.otherCount)
                jsonObject1.put("total",facet.totalCount)
                jsonObject1.put("variance",facet.missingCount)



            }

        }

        //System.out.println("response = " + response);
                 //render StatisticalFacet
        render new JSONObject(jsonObject.toString())


    }
}
