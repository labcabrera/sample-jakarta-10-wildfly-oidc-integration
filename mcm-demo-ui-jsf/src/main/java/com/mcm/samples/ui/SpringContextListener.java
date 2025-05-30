package com.mcm.samples.ui;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.ContextLoaderListener;

@WebListener
@Slf4j
public class SpringContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("Initializing Spring context");
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(SecurityConfig.class);
        sce.getServletContext().setAttribute("org.springframework.web.context.WebApplicationContext.ROOT", context);
        //sce.getServletContext().addListener(new ContextLoaderListener(context));
    }
}