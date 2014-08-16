package support;

import estest.AuthorWiseBookReportActionListener;
import estest.SuperListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gannu
 * Date: 7/9/14
 * Time: 8:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class LibraryListener {

    Map<String, SuperListener> listeners= new HashMap<String, SuperListener>();
    LibraryListener(){
        listeners.put("author",new AuthorWiseBookReportActionListener());
    }
    public SuperListener getListener(String report){
              return listeners.get(report);
    }
}
