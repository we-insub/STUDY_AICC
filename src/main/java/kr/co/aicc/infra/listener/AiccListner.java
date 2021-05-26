package kr.co.aicc.infra.listener;

import io.netty.util.internal.InternalThreadLocalMap;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AiccListner implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
    }
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        InternalThreadLocalMap.destroy();
    }
}
