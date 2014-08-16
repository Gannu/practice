package com.softech.estestcommon;

import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;

/**
 * Created with IntelliJ IDEA.
 * User: gannu
 * Date: 8/7/14
 * Time: 8:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class FilterUtils {
    public FilterUtils(){}
    public static  FilterBuilder getTermFilter(String field, String value){
        FilterBuilder filterBuilder= FilterBuilders.termFilter(field,value);
        return  filterBuilder;
    }
}
