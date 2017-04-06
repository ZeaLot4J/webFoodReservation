<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import = "org.jfree.chart.ChartFactory,
              org.jfree.chart.JFreeChart,
              org.jfree.chart.servlet.ServletUtilities,
              org.jfree.chart.title.TextTitle,
              org.jfree.data.time.TimeSeries,
              org.jfree.data.time.Month,
              org.jfree.data.time.TimeSeriesCollection,
              java.awt.Font"%>
<%
//访问量统计时间线
TimeSeries timeSeries = new TimeSeries("阿蜜果blog访问量统计", Month.class);
//时间曲线数据集合
TimeSeriesCollection lineDataset = new TimeSeriesCollection();
//构造数据集合
timeSeries.add(new Month(1, 2007), 11200);
timeSeries.add(new Month(2, 2007), 9000);
timeSeries.add(new Month(3, 2007), 6200);
timeSeries.add(new Month(4, 2007), 8200);
timeSeries.add(new Month(5, 2007), 8200);
timeSeries.add(new Month(6, 2007), 12200);
timeSeries.add(new Month(7, 2007), 13200);
timeSeries.add(new Month(8, 2007), 8300);
timeSeries.add(new Month(9, 2007), 12400);
timeSeries.add(new Month(10, 2007), 12500);
timeSeries.add(new Month(11, 2007), 13600);
timeSeries.add(new Month(12, 2007), 12500);
lineDataset.addSeries(timeSeries);
JFreeChart chart = ChartFactory.createTimeSeriesChart("访问量统计时间线", "month", "visit amount", lineDataset, true, true, true);
//设置子标题
TextTitle subtitle = new TextTitle("2007年度", new Font("黑体", Font.BOLD, 12));
chart.addSubtitle(subtitle);
//设置主标题
chart.setTitle(new TextTitle("阿蜜果blog访问量统计", new Font("隶书", Font.ITALIC, 15)));
chart.setAntiAlias(true);
String filename = ServletUtilities.saveChartAsPNG(chart, 500, 300, null, session);
String graphURL = request.getContextPath() + "/DisplayChart?filename=" + filename;
%>
<img src="<%= graphURL %>"width=500 height=300 border=0 usemap="#<%= filename %>"> 