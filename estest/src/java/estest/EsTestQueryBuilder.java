package estest;

import org.elasticsearch.action.search.MultiSearchRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.facet.Facet;
import org.elasticsearch.search.facet.FacetBuilder;
import org.elasticsearch.search.facet.FacetBuilders;
import org.elasticsearch.search.facet.terms.TermsFacet;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: gbhattarai
 * Date: 4/24/14
 * Time: 9:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class EsTestQueryBuilder {

    org.slf4j.Logger logger = LoggerFactory.getLogger(EsTestQueryBuilder.class);

     Set<String>authors= new LinkedHashSet<String>();
    //static TransportClient client;
    private boolean asc;
    public MultiSearchRequestBuilder multiSearchQuery(TransportClient client) {

        MultiSearchRequestBuilder multiSearchRequestBuilder = new MultiSearchRequestBuilder(client);
        multiSearchRequestBuilder.add(getQuery1(client));
        multiSearchRequestBuilder.add(getQuery4(client));
        multiSearchRequestBuilder.add(getAuthorWiseBook(client));
          return multiSearchRequestBuilder;

    }

    public SearchRequestBuilder getAuthorWiseBook(TransportClient client)
    {
        getSetofAuthor(client);
        System.out.println(authors);

        SearchRequestBuilder builder=getAuthorWiseBookFacet(client);
        System.out.println(builder);
        return builder;
    }

    public SearchRequestBuilder getAuthorWiseBookFacet(TransportClient client)
    {
        SearchRequestBuilder builder= client.prepareSearch("library");
        builder.setTypes("book");
        builder.setSize(0);
        for(String author:authors)
        {
            FilterBuilder filterBuilder = FilterBuilders.termFilter("author",author);
            FacetBuilder facetBuilder = FacetBuilders.termsFacet("AuthorWiseBook:"+author).field("title");
            facetBuilder.facetFilter(filterBuilder);
            builder.addFacet(facetBuilder);
        }
         return builder;
    }
    public void getSetofAuthor(TransportClient client)
    {
        SearchRequestBuilder builder = client.prepareSearch("library");
        builder.setTypes("book");
        FacetBuilder facetBuilder=FacetBuilders.termsFacet("AuthorNames").field("author");
        builder.addFacet(facetBuilder);
        builder.setSize(0);
        SearchResponse response = builder.execute().actionGet();
        processAuthorResponse(response);
    }

    public void processAuthorResponse(SearchResponse response)
    {
        for (Facet facet : response.getFacets())
        {
            if(facet instanceof TermsFacet)
            {
                TermsFacet termsFacet=(TermsFacet)facet;
                for(TermsFacet.Entry entry:termsFacet.getEntries())
                {
                    authors.add(entry.getTerm().toString());
                }
            }
        }
    }



    private SearchRequestBuilder getQuery1(TransportClient client){
        SearchRequestBuilder requestBuilder= client.prepareSearch("library");
        requestBuilder.setTypes("book");
        requestBuilder.setSize(0);
        QueryBuilder queryBuilder= QueryBuilders.matchAllQuery();
        FacetBuilder facetBuilder= FacetBuilders.termsStatsFacet("AuthorWise").keyField("author").valueField("copies");
        requestBuilder.setQuery(queryBuilder);
        requestBuilder.addFacet(facetBuilder);
        System.out.println("requestBuilder = " + requestBuilder);
        return requestBuilder;
    }

    private SearchRequestBuilder getQuery2(TransportClient client) {
        SearchRequestBuilder requestBuilder = client.prepareSearch("library");
        requestBuilder.setTypes("book");
        requestBuilder.setSize(0);
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        FacetBuilder facetBuilder = FacetBuilders.statisticalFacet("statistical_test").field("copies");
        requestBuilder.setQuery(queryBuilder);
        requestBuilder.addFacet(facetBuilder);
        System.out.println("requestBuilder = " + requestBuilder);
        return requestBuilder;
    }
    private SearchRequestBuilder getQuery4(TransportClient client){
        SearchRequestBuilder requestBuilder=client.prepareSearch("library");
        requestBuilder.setTypes("book");
        requestBuilder.setSize(0);
        QueryBuilder queryBuilder= QueryBuilders.matchAllQuery();
        FacetBuilder facetBuilder= FacetBuilders.termsStatsFacet("YearWise").keyField("year").valueField("copies");
        requestBuilder.setQuery(queryBuilder);
        requestBuilder.addFacet(facetBuilder);
        return requestBuilder;
    }



    private SearchRequestBuilder getQuery3(TransportClient client) {
        SearchRequestBuilder requestBuilder = client.prepareSearch("2000");
        requestBuilder.setTypes("MemberSearch");
        requestBuilder.setSize(0);
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        FacetBuilder facetBuilder = FacetBuilders.statisticalFacet("statistical_test").field("prospectiveTotal");
        FilterBuilder filterBuilder = FilterBuilders.termFilter("groupId", "01");
        facetBuilder.facetFilter(filterBuilder);
        requestBuilder.setQuery(queryBuilder);
        requestBuilder.addFacet(facetBuilder);
        System.out.println("requestBuilder = " + requestBuilder);
        return requestBuilder;
    }


}
