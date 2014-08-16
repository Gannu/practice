package estest

import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject

class EsTestController {
  EsTestService esTestService
    def report;
    def index() {
           }
    def result(){
        esTestService.getResult(report)

    }
    def renderString() {
        configureParam();
        JSONObject data = result() ? JSON.parse(result()) : new JSONObject()
        render  data  as String
    }
    def configureParam(){
        report=params.get("report");
        println "report = $report"
    };

}
