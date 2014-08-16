package estest

import org.elasticsearch.action.search.MultiSearchRequestBuilder
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.common.xcontent.XContentBuilder
import support.BuilderExecutor
import support.LibraryBuilder
import support.LibraryListener

class EsTestService {
    static TransportClient client

    def getResult(String report) {
            handleRequest(report)
    }
    def handleRequest(String report){

        Settings settings = ImmutableSettings.settingsBuilder()
                .put("cluster.name", "esone").build()
        client = new TransportClient(settings)
        client.addTransportAddress(new InetSocketTransportAddress("localhost", 9300)) //10.0.4.70

        XContentBuilder content = handleInternal(client,report)

        return content ? content.bytes().toUtf8(): ""
    }
    protected XContentBuilder handleInternal(TransportClient client,String report) throws Exception {
        LibraryBuilder lb=new LibraryBuilder();
        LibraryListener ls=new LibraryListener();
        SuperQueryBuilder superQueryBuilder=lb.getBuilder(report);
        SuperListener top=ls.getListener(report);
        MultiSearchRequestBuilder builder=superQueryBuilder.multiSearchQuery(client);
        try {
            Thread thread = new Thread(new BuilderExecutor(builder, top));
            thread.start();
            while (!top.isReady.get()) {
                Thread.currentThread().sleep(100);
            }
        } catch (Exception e) {
            top.isReady.set(true);
            e.printStackTrace();
        }
        return top.contentBuilder
    }
}
