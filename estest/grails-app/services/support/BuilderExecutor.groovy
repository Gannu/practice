package support

import estest.authorwisebook.AuthorWiseBookReportActionListener
import org.elasticsearch.action.ActionRequestBuilder

/**
 * Created with IntelliJ IDEA.
 * User: gbhattarai
 * Date: 4/30/14
 * Time: 6:03 PM
 * To change this template use File | Settings | File Templates.
 */
public  class BuilderExecutor implements Runnable {
    ActionRequestBuilder builder;
    AuthorWiseBookReportActionListener Listener;

    public BuilderExecutor(ActionRequestBuilder builder, AuthorWiseBookReportActionListener listener) {
        this.builder = builder;
        this.Listener = listener;
    }

    @Override
    public void run() {
        try {
            builder.execute(Listener);
        } catch (Exception e) {
            Listener.isReady(true)
            e.printStackTrace();
        }
    }
}

