package estest;

//import org.grails.plugin.resource.AggregatedResourceMeta;

/**
 * Created with IntelliJ IDEA.
 * User: gbhattarai
 * Date: 4/1/14
 * Time: 10:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class ElasticSearchAPI {
    /*static TransportClient client;
    private boolean asc;

    static void connect(){
        Settings settings = ImmutableSettings.settingsBuilder()
                .put("cluster.name", "esone").build();
        client = new TransportClient(settings);
        client.addTransportAddress(new InetSocketTransportAddress("localhost", 9300));



    }
    static {
        connect();
    }
    void start(){
        connect();
        SearchRequestBuilder requestBuilder = getQuery2();
        //System.out.println("requestBuilder = " + requestBuilder);
        SearchResponse response=requestBuilder.execute().actionGet();
        System.out.println("response = " + response);

    }
    //nested facet
    private SearchRequestBuilder getQuery15() {
        SearchRequestBuilder requestBuilder=client.prepareSearch("games");
        requestBuilder.setTypes("football");
       // requestBuilder.setSize(0);
        NestedFilterBuilder nestedFilterBuilder = FilterBuilders.nestedFilter("player",FilterBuilders.termFilter("forward","messi"));
        FilteredQueryBuilder filteredQueryBuilder =new FilteredQueryBuilder(QueryBuilders.matchAllQuery(),nestedFilterBuilder);

//        QueryBuilder queryBuilder= QueryBuilders.matchAllQuery();
//        FacetBuilder facetBuilder= FacetBuilders.termsFacet("term_facet").field("copies");
//        requestBuilder.setQuery(queryBuilder);
//        requestBuilder.addFacet(facetBuilder);
        requestBuilder.setQuery(filteredQueryBuilder);
        return requestBuilder;

    }


        //term aggregation with avg aggregation
    private SearchRequestBuilder getQuery14() {
        SearchRequestBuilder requestBuilder=client.prepareSearch("sports");
        requestBuilder.setTypes("athlete");
        requestBuilder.setSize(0);
        TermsBuilder aggregationBuilders= AggregationBuilders.terms("term_count").field("sport").order(Terms.Order.aggregation("rating_avg", asc)).subAggregation(AggregationBuilders.avg("rating_avg").field("rating"));
        requestBuilder.addAggregation(aggregationBuilders);
        return requestBuilder;

    }

    //term aggregation
    private SearchRequestBuilder getQuery13() {
        SearchRequestBuilder requestBuilder=client.prepareSearch("sports");
        requestBuilder.setTypes("athlete");
        requestBuilder.setSize(0);
        TermsBuilder aggregationBuilders= AggregationBuilders.terms("term_count").field("sport");
        requestBuilder.addAggregation(aggregationBuilders);
        return requestBuilder;


    }
    //value_count aggregation

    private SearchRequestBuilder getQuery12() {
        SearchRequestBuilder requestBuilder=client.prepareSearch("sports");
        requestBuilder.setTypes("athlete");
        requestBuilder.setSize(0);
        ValueCountBuilder aggregationBuilders= AggregationBuilders.count("value_count").field("sport");
        requestBuilder.addAggregation(aggregationBuilders);
        return requestBuilder;

    }

    //term aggregation


    //term facet with query
    private SearchRequestBuilder getQuery11() {
        SearchRequestBuilder requestBuilder=client.prepareSearch("library");
        requestBuilder.setTypes("book");
        requestBuilder.setSize(0);
        QueryBuilder queryBuilder= QueryBuilders.matchAllQuery();
        FacetBuilder facetBuilder= FacetBuilders.termsFacet("term_facet").field("copies");
        requestBuilder.setQuery(queryBuilder);
        requestBuilder.addFacet(facetBuilder);
        return requestBuilder;

    }

    // or filter
    public SearchRequestBuilder getQuery10() {
        SearchRequestBuilder requestBuilder= client.prepareSearch("library");
        requestBuilder.setTypes("book");
       // FilterBuilder filterBuilder= FilterBuilders.notFilter(FilterBuilders.termFilter("author","ramesh"));
        FilterBuilder filterBuilder= FilterBuilders.orFilter(FilterBuilders.prefixFilter("title","java"),FilterBuilders.prefixFilter("title","hadoop"));
        requestBuilder.setPostFilter(filterBuilder);
        return requestBuilder;
    }

    // not filter

    private SearchRequestBuilder getQuery9() {
        SearchRequestBuilder requestBuilder= client.prepareSearch("library");
        requestBuilder.setTypes("book");
       // FilterBuilder filterBuilder= FilterBuilders.notFilter(FilterBuilders.rangeFilter("price").from("1").to("2"));
        FilterBuilder filterBuilder= FilterBuilders.notFilter(FilterBuilders.termFilter("author","ramesh"));
        requestBuilder.setPostFilter(filterBuilder);
        return requestBuilder;
    }
    //not filter


    //filtered query
    private SearchRequestBuilder getQuery8() {
        SearchRequestBuilder requestBuilder=client.prepareSearch("library");
        requestBuilder.setTypes("book");
        QueryBuilder queryBuilder= QueryBuilders.termQuery("copies",1000);
        FilterBuilder filterBuilder= FilterBuilders.termFilter("author","ramesh");
        FacetBuilder facetBuilder= FacetBuilders.statisticalFacet("statistical_test").field("copies");
        requestBuilder.setQuery(queryBuilder);
        requestBuilder.setPostFilter(filterBuilder);
        requestBuilder.addFacet(facetBuilder);
        return requestBuilder;
    }


    //exist filter
    private SearchRequestBuilder getQuery7() {
        SearchRequestBuilder requestBuilder=client.prepareSearch("library");
        requestBuilder.setTypes("book");
        ExistsFilterBuilder existsFilterBuilder= FilterBuilders.existsFilter("copies");
        requestBuilder.setPostFilter(existsFilterBuilder);
        return requestBuilder;

    }

    //ids filter
    private SearchRequestBuilder getQuery6() {
        SearchRequestBuilder requestBuilder=client.prepareSearch("library");
        requestBuilder.setTypes("book");
        IdsFilterBuilder idsFilterBuilder=FilterBuilders.idsFilter().addIds("12");
        requestBuilder.setPostFilter(idsFilterBuilder);
        return requestBuilder;

    }


    //missing filter
    private SearchRequestBuilder getQuery5() {
        SearchRequestBuilder requestBuilder=client.prepareSearch("library");
        requestBuilder.setTypes("book");
        MissingFilterBuilder filterBuilders=FilterBuilders.missingFilter("copies");
        requestBuilder.setPostFilter(filterBuilders);
        return requestBuilder;

    }

     //Range facet
    private SearchRequestBuilder getQuery4() {
        SearchRequestBuilder requestBuilder=client.prepareSearch("library");
        requestBuilder.setTypes("book");
        requestBuilder.setSize(0);
        QueryBuilder queryBuilder= QueryBuilders.matchAllQuery();
        //FacetBuilder facetBuilder= FacetBuilders.statisticalFacet("statistical_test").field("copies");
        FacetBuilder facetBuilder= FacetBuilders.rangeFacet("range_facet").field("year").addRange(2011, 2015);
        requestBuilder.setQuery(queryBuilder);
        requestBuilder.addFacet(facetBuilder);
        return requestBuilder;

    }
      //term facet
    private SearchRequestBuilder getQuery3() {
        SearchRequestBuilder requestBuilder=client.prepareSearch("library");
        requestBuilder.setTypes("book");
        requestBuilder.setSize(0);
        QueryBuilder queryBuilder= QueryBuilders.matchAllQuery();
        //FacetBuilder facetBuilder= FacetBuilders.statisticalFacet("statistical_test").field("copies");
        FacetBuilder facetBuilder= FacetBuilders.termsFacet("term_facet").field("copiess");
        requestBuilder.setQuery(queryBuilder);
        requestBuilder.addFacet(facetBuilder);
        return requestBuilder;
    }

      //statistical facet
    private SearchRequestBuilder getQuery2() {
        SearchRequestBuilder requestBuilder=client.prepareSearch("library");
        requestBuilder.setTypes("book");
        requestBuilder.setSize(0);
        QueryBuilder queryBuilder= QueryBuilders.matchAllQuery();
        FacetBuilder facetBuilder= FacetBuilders.statisticalFacet("statistical_test").field("copies");
        requestBuilder.setQuery(queryBuilder);
        requestBuilder.addFacet(facetBuilder);
        return requestBuilder;
    }

    private SearchRequestBuilder getQuery1() {
        SearchRequestBuilder requestBuilder=client.prepareSearch("library");
        requestBuilder.setTypes("book");
        requestBuilder.setSize(0);
        QueryBuilder queryBuilder= QueryBuilders.matchAllQuery();
        FacetBuilder facetBuilder= FacetBuilders.statisticalFacet("statistical_test").field("copies");
        requestBuilder.setQuery(queryBuilder);
        requestBuilder.addFacet(facetBuilder);
        return requestBuilder;
    }

    public static void main(String[] args) {
        new ElasticSearchAPI().start();


    }*/
}
