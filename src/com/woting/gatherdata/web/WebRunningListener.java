package com.woting.gatherdata.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woting.gatherdata.ApiGatherListener;

public class WebRunningListener implements ServletContextListener {
    private Logger logger=LoggerFactory.getLogger(this.getClass());

    @Override
    //初始化
    public void contextInitialized(ServletContextEvent arg0) {
        try {
            ApiGatherListener.begin();
        } catch(Exception e) {
            logger.error("Web运行时监听启动异常：",e);
        }
    }

    @Override
    //销毁
    public void contextDestroyed(ServletContextEvent arg0) {
    }
}