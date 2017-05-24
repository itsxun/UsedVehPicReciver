package xin.fallen.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import xin.fallen.config.StaticConfig;
import xin.fallen.util.ConfigLoader;
import xin.fallen.util.FileFinder;

public class SpringReadyLis implements ApplicationListener<ApplicationPreparedEvent> {
    private static Logger log = LoggerFactory.getLogger("logger");

    @Override
    public void onApplicationEvent(ApplicationPreparedEvent event) {
        ConfigLoader.load(FileFinder.find("config.xml"), StaticConfig.class);
        log.info("config load complete");
    }
}