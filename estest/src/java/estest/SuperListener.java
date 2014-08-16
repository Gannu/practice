package estest;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.ActionResponse;

/**
 * Created with IntelliJ IDEA.
 * User: gannu
 * Date: 7/9/14
 * Time: 9:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class SuperListener implements ActionListener<ActionResponse> {
    @Override
    public void onResponse(ActionResponse actionResponse) {
        actionResponse(actionResponse);
    }

    public void actionResponse(ActionResponse actionResponse) {
    }

    @Override
    public void onFailure(Throwable throwable) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
