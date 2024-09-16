package SwitchAndKeys.ServletContextListener;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.logging.Logger;

@WebListener
public class MyServletContextListener implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(MyServletContextListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("ServletContext initialized.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("ServletContext destroyed. Attempting to clean up MySQL connections.");
        try {
            AbandonedConnectionCleanupThread.checkedShutdown();
            LOGGER.info("MySQL connections cleaned up successfully.");
        } catch (Exception e) {
            LOGGER.severe("Unexpected error during MySQL cleanup: " + e.getMessage());
        }
    }
}
