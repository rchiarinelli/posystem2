/**
 * 
 */
package br.com.brainyit.test.domain.base;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Before;

import br.com.brainyit.posystem2.domain.CustomerStatus;
import br.com.brainyit.posystem2.domain.PFCustomer;
import br.com.brainyit.posystem2.domain.PJCustomer;
import br.com.brainyit.posystem2.domain.Subscriber;
import br.com.brainyit.posystem2.util.HibernateUtils;

/**
 * @author Rafael Chiarinelli
 *
 */
public abstract class BaseDataStoreJUnitTest {
	
	@Before
	public void prepare() throws Exception {
		HibernateUtils.initSessionFactory("hibernate_test.cfg.xml");
		this.loadData();
	}
	
	public void loadData() {
		loadSubscriber1();
		loadSubscriber2();
	}
	
	
	private void loadSubscriber1() {
		//Data for subscriber 1
		Subscriber subscriber = new Subscriber();
		subscriber.setCity("Nova Odessa");
		subscriber.setComplement("Jd. São Jorge");
		subscriber.setDocument("222222");
		subscriber.setEmail("contato@rianusinagem.com.br");
		subscriber.setName("Rian Usinagem");
		subscriber.setNumber("111");
		subscriber.setStreet("Rua X");
		subscriber.setZipCode("13460-000");
		this.saveData(subscriber);
		
		PFCustomer pfCustomer = new PFCustomer();
		pfCustomer.setCpf("2232312312");
		pfCustomer.setDetails("NALNALANLA");
		pfCustomer.setName("AAAAAA");
		pfCustomer.setRg("4535354354");
		pfCustomer.setStatus(CustomerStatus.ACTIVE);
		pfCustomer.setSubscriber(subscriber);
		this.saveData(pfCustomer);

		pfCustomer = new PFCustomer();
		pfCustomer.setCpf("85678");
		pfCustomer.setDetails("NALNALANLA");
		pfCustomer.setName("CCCCC");
		pfCustomer.setRg("79798798");
		pfCustomer.setStatus(CustomerStatus.ACTIVE);
		pfCustomer.setSubscriber(subscriber);
		this.saveData(pfCustomer);

		pfCustomer = new PFCustomer();
		pfCustomer.setCpf("345645");
		pfCustomer.setDetails("NALNALANLA");
		pfCustomer.setName("WWWWWWW");
		pfCustomer.setRg("34234234234");
		pfCustomer.setStatus(CustomerStatus.ACTIVE);
		pfCustomer.setSubscriber(subscriber);
		this.saveData(pfCustomer);

		PJCustomer pjCustomer = new PJCustomer();
		pjCustomer.setCnpjCgc("33423");
		pjCustomer.setInscrEstadual("55555");
		pjCustomer.setInscrMunicipal("333333");
		pjCustomer.setDetails("NALNALANLA");
		pjCustomer.setName("Usinagem XPTO");
		pjCustomer.setStatus(CustomerStatus.ACTIVE);
		pjCustomer.setSubscriber(subscriber);
		this.saveData(pjCustomer);
	
		
		pjCustomer = new PJCustomer();
		pjCustomer.setCnpjCgc("22222");
		pjCustomer.setInscrEstadual("33333");
		pjCustomer.setInscrMunicipal("22222");
		pjCustomer.setDetails("NALNALANLA");
		pjCustomer.setName("ABCDC Retifica");
		pjCustomer.setStatus(CustomerStatus.ACTIVE);
		pjCustomer.setSubscriber(subscriber);
		this.saveData(pjCustomer);
		
		pjCustomer = new PJCustomer();
		pjCustomer.setCnpjCgc("234234");
		pjCustomer.setInscrEstadual("643563456");
		pjCustomer.setInscrMunicipal("123123");
		pjCustomer.setDetails("NALNALANLA");
		pjCustomer.setName("XYZ LABALBAAL");
		pjCustomer.setStatus(CustomerStatus.ACTIVE);
		pjCustomer.setSubscriber(subscriber);
		this.saveData(pjCustomer);
		
		//Clientes inativos
		pfCustomer = new PFCustomer();
		pfCustomer.setCpf("345645");
		pfCustomer.setDetails("NALNALANLA");
		pfCustomer.setName("Jose dos Santos INATIVO");
		pfCustomer.setRg("34234234234");
		pfCustomer.setStatus(CustomerStatus.INACTIVE);
		pfCustomer.setSubscriber(subscriber);
		this.saveData(pfCustomer);

		pjCustomer = new PJCustomer();
		pjCustomer.setCnpjCgc("33423");
		pjCustomer.setInscrEstadual("55555");
		pjCustomer.setInscrMunicipal("333333");
		pjCustomer.setDetails("NALNALANLA");
		pjCustomer.setName("Usinagem Inativa");
		pjCustomer.setStatus(CustomerStatus.INACTIVE);
		pjCustomer.setSubscriber(subscriber);
		this.saveData(pjCustomer);		
		
	}
	
	
	
	private void loadSubscriber2() {
		Subscriber subscriber = new Subscriber();
		subscriber.setCity("Nova Odessa");
		subscriber.setComplement("Jd. São Jorge");
		subscriber.setDocument("222222");
		subscriber.setEmail("contato@rianusinagem.com.br");
		subscriber.setName("Empresa X");
		subscriber.setNumber("111");
		subscriber.setStreet("Rua X");
		subscriber.setZipCode("13460-000");
		this.saveData(subscriber);
		
		PFCustomer pfCustomer = new PFCustomer();
		pfCustomer.setCpf("2232312312");
		pfCustomer.setDetails("NNNNNNONO");
		pfCustomer.setName("");
		pfCustomer.setRg("77777777");
		pfCustomer.setStatus(CustomerStatus.ACTIVE);
		pfCustomer.setSubscriber(subscriber);
		this.saveData(pfCustomer);
		
		
		PJCustomer pjCustomer = new PJCustomer();
		pjCustomer.setCnpjCgc("33423");
		pjCustomer.setInscrEstadual("55555");
		pjCustomer.setInscrMunicipal("333333");
		pjCustomer.setDetails("NALNALANLA");
		pjCustomer.setName("BADDADTO Usi");
		pjCustomer.setStatus(CustomerStatus.ACTIVE);
		pjCustomer.setSubscriber(subscriber);
		this.saveData(pjCustomer);
	}
	
	private void saveData(Object obj) {
		Session session = HibernateUtils.getSessionFactory().getCurrentSession();
		Transaction tx = HibernateUtils.getSessionFactory().getCurrentSession().beginTransaction();
		session.save(obj);
		tx.commit();
	}
	
}
