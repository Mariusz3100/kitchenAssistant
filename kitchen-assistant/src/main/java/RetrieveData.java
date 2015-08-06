  

import java.util.List;

import mariusz.ambroziak.kassistant.model.Produkt;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;  
import org.hibernate.SessionFactory;  
import org.hibernate.Transaction;  
import org.hibernate.cfg.Configuration;  
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class RetrieveData {  
	public static void main(String[] args) {  

		//creating configuration object  
		Configuration cfg=new Configuration();  
		cfg.configure("hibernate.cfg.xml");//populates the data of the configuration file  

		//creating seession factory object  
		SessionFactory factory=createSessionFactory();  

		//creating session object  
		Session session=factory.openSession();  

		//creating transaction object  
		Transaction t=session.beginTransaction();  

		SQLQuery  query =  session.createSQLQuery("select * from Produkt");  
		query.addEntity(Produkt.class);
		List<Produkt> results = query.list();
		t.commit();//transaction is commited  
		session.close();  

		System.out.println("successfully retrieved: "+results.get(0).getCena());  

	}  
	
	private static SessionFactory sessionFactory;
	private static ServiceRegistry serviceRegistry;

	public static SessionFactory createSessionFactory() {
	    Configuration configuration = new Configuration();
	    configuration.configure();
	    serviceRegistry = new ServiceRegistryBuilder().applySettings(
	            configuration.getProperties()).build();
	    sessionFactory = configuration.buildSessionFactory(serviceRegistry);
	    return sessionFactory;
	}
}  