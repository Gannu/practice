package support;

import estest.AuthorWiseBookReportQueryBuilder;
import estest.PriceWiseBookBuilder;
import estest.SuperQueryBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gannu
 * Date: 7/9/14
 * Time: 8:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class LibraryBuilder {
    Map<String, SuperQueryBuilder> builder= new HashMap<String, SuperQueryBuilder>();
    LibraryBuilder(){

       builder.put("author", new AuthorWiseBookReportQueryBuilder());
        builder.put("price", new PriceWiseBookBuilder());

    }
    public SuperQueryBuilder getBuilder(String report){
        return builder.get(report);
    }
}
