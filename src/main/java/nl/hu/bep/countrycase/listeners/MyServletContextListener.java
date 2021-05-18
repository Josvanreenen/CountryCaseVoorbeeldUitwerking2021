package nl.hu.bep.countrycase.listeners;

import nl.hu.bep.countrycase.persistence.PersistenceManager;
import reactor.core.scheduler.Schedulers;
import reactor.netty.http.HttpResources;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.time.Duration;

import static java.lang.System.*;

@WebListener
public class MyServletContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            PersistenceManager.loadWorldFromAzure();
            out.println("World loaded from Azure...");
        } catch (Exception e) {
            out.println("Error loading world: " + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            PersistenceManager.saveWorldToAzure();
            out.println("World saved to Azure...");
        } catch (Exception e) {
            out.println("Error saving world: " + e.getMessage());
        }

        Schedulers.shutdownNow();
        HttpResources.disposeLoopsAndConnectionsLater(Duration.ZERO, Duration.ZERO).block();
    }
}
