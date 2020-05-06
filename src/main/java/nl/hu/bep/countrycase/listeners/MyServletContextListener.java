package nl.hu.bep.countrycase.listeners;

import nl.hu.bep.countrycase.persistence.PersistenceManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class MyServletContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            PersistenceManager.loadWorldFromAzure();
            System.out.println("World loaded from Azure...");
        } catch (Exception e) {
            System.out.println("Error loading world: " + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            PersistenceManager.saveWorldToAzure();
            System.out.println("World saved to Azure...");
        } catch (Exception e) {
            System.out.println("Error saving world: " + e.getMessage());
        }
    }
}
