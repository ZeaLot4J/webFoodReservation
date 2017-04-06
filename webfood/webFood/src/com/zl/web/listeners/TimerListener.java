package com.zl.web.listeners;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.zl.utils.UpAndDownUtil;
import com.zl.utils.UserRedis;
import com.zl.utils.ZLConstants;

import redis.clients.jedis.Jedis;

public class TimerListener implements ServletContextListener{

	private Jedis jedis = new Jedis(ZLConstants.REDIS_URL);
	private Timer t = new Timer();
	@Override
	public void contextInitialized(ServletContextEvent event) {
		int hour = 2;//晚上两点处理
		int minute = 0;
		ServletContext application=event.getServletContext();
		if(application.getAttribute("hour")!=null){
			hour = Integer.parseInt(application.getAttribute("hour").toString());
		}
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, hour);
		cal.set(Calendar.MINUTE, minute);
		
		t.schedule(new TimerTask(){
			@Override
			public void run() {
				UserRedis.keepNDaysRecord(jedis, ZLConstants.KEEP_NDAYS_FOR_ONLINE_USER);
			}
			
		}, cal.getTime(), 24*60*60*1000);
		
		t.schedule(new TimerTask(){

			@Override
			public void run() {
				UpAndDownUtil.keepNDaysRecord(jedis, ZLConstants.KEEP_NDAYS_FOR_ONLINE_USER);
			}
			
		}, 24*60*60*1000);
	}
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		if(t!=null){
			t.cancel();
		}
	}


}
