<%--
  Created by IntelliJ IDEA.
  User: gannu
  Date: 7/6/14
  Time: 9:13 PM
  To change this template use File | Settings | File Templates.
--%>

<%--
  Created by IntelliJ IDEA.
  User: gannu
  Date: 7/6/14
  Time: 11:02 PM
--%>

<!DOCTYPE html>
<html>
<head>
    <g:javascript library="jquery"/>
    <script src="http://code.highcharts.com/adapters/standalone-framework.js"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
    <script src="http://code.highcharts.com/maps/highmaps.js"></script>
    <meta name="layout" content="main"/>
    <title>Simple Ajax</title>




</head>
<body>

<script>

    $(document).ready(function() {
        var chart1 = new Highcharts.Chart({
            chart: {
                renderTo: 'graphDiv',
                defaultSeriesType: 'bar'
            },
            title: {
                text: 'Fruit Consumption'
            },
            xAxis: {
                categories: ['Apples', 'Bananas', 'Oranges']
            },
            yAxis: {
                title: {
                    text: 'Fruit eaten'
                }
            },
            series: [{
                name: 'Anne',
                data: [1, 0, 4]
            }, {
                name: 'Martin',
                data: [5, 7, 3]
            }]
        });
    });



</script>

<div style='float: left; width: 20%; min-height: 500px; background-color: #c0c0c0;'>
    <g:set var="theID" value="${999}"/>
    <g:set var="firstNumber" value="${5}"/>
    <g:set var="secondNumber" value="${7}"/>

    <g:remoteLink controller="esTest" action="renderString"
                  update="mainContent" params="[report:'author']">Render String</g:remoteLink><br/>

    <g:remoteLink controller="esTest" action="renderString"
                  update="mainContent" params="[report:'copies',theID:'10']">Render Page</g:remoteLink><br/>

    <g:remoteLink controller="simple" action="withCustomParam"
                  params="${[a:firstNumber, b:secondNumber]}"
                  update="mainContent">With Custom Parameter</g:remoteLink><br/>
</div>
<div id="mainContent" style='float: right; width: 80%; min-height: 500px; background-color: #c0ffc0;'>

    <div id="container" style="width:100%; height:400px;"></div>
    <p>This is the main content</p>
</div>
</body>
</html>