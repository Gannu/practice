package estest;

import com.softech.estestcommon.FilterUtils;
import org.elasticsearch.action.search.MultiSearchRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.search.facet.Facet;
import org.elasticsearch.search.facet.FacetBuilder;
import org.elasticsearch.search.facet.FacetBuilders;
import org.elasticsearch.search.facet.terms.TermsFacet;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: gannu
 * Date: 6/22/14
 * Time: 5:25 PM
 * To change this template use File | Settings | File Templates.
 */
public  class AuthorWiseBookReportQueryBuilder extends SuperQueryBuilder{

    org.slf4j.Logger logger = LoggerFactory.getLogger(AuthorWiseBookReportQueryBuilder.class);

    Set<String> authors= new LinkedHashSet<String>();
    //static TransportClient client;
    private boolean asc;
    public MultiSearchRequestBuilder multiSearchQuery(TransportClient client) {
        MultiSearchRequestBuilder multiSearchRequestBuilder = prepareQueryBuilders(client);
        return multiSearchRequestBuilder;

    }

    private MultiSearchRequestBuilder prepareQueryBuilders(TransportClient client) {
        MultiSearchRequestBuilder multiSearchRequestBuilder= new MultiSearchRequestBuilder(client);
        getSetofAuthor(client);
        for(String author:authors)
        {
            multiSearchRequestBuilder.add(getAuthorWiseBookName(client, author));
            multiSearchRequestBuilder.add(getAuthorWiseBookCopies(client, author));
            multiSearchRequestBuilder.add(getAuthorWisePublishedYear(client, author));
        }
        return multiSearchRequestBuilder;


    }

    private SearchRequestBuilder getAuthorWisePublishedYear(TransportClient client, String author) {
        SearchRequestBuilder builder= client.prepareSearch("library");
        builder.setTypes("book");
        builder.setSize(0);
        FilterBuilder filterBuilder = FilterBuilders.termFilter("author", author);
        FacetBuilder facetBuilder = FacetBuilders.termsFacet("publishedYear:" + author).field("year");
        facetBuilder.facetFilter(filterBuilder);
        builder.addFacet(facetBuilder);
       // logger.error("publishedYear:{}",builder);
        return builder;
    }

    private SearchRequestBuilder getAuthorWiseBookCopies(TransportClient client, String author) {
        SearchRequestBuilder builder= client.prepareSearch("library");
        builder.setTypes("book");
        builder.setSize(0);
        FilterBuilder filterBuilder = FilterBuilders.termFilter("author", author);
        FacetBuilder facetBuilder = FacetBuilders.termsFacet("bookCoipes:" + author).field("copies");
        facetBuilder.facetFilter(filterBuilder);
        builder.addFacet(facetBuilder);
       // logger.error("bookCopies:{}",builder);
        return builder;

    }

    public SearchRequestBuilder getAuthorWiseBookName(TransportClient client, String author)
    {
        SearchRequestBuilder builder= client.prepareSearch("library");
        builder.setTypes("book");
        builder.setSize(0);
//        FilterBuilder filterBuilder = FilterBuilders.termFilter("author", author);
        FilterBuilder filterBuilder= FilterUtils.getTermFilter("author", author);
        FacetBuilder facetBuilder = FacetBuilders.termsFacet("bookName:" + author).field("title");
        facetBuilder.facetFilter(filterBuilder);
        builder.addFacet(facetBuilder);
        //logger.error("bookName:{}",builder);
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

}
