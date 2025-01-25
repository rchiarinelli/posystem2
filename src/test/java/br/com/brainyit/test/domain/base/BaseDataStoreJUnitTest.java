/**
 * 
 */
package br.com.brainyit.test.domain.base;

import br.com.brainyit.posystem2.domain.Company;
import br.com.brainyit.posystem2.domain.Individual;
import org.junit.Before;

import br.com.brainyit.posystem2.domain.CustomerStatus;
import br.com.brainyit.posystem2.domain.Subscriber;

/**
 * @author Rafael Chiarinelli
 *
 */
public abstract class BaseDataStoreJUnitTest {
	
	@Before
	public void prepare() throws Exception {
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
		
		Individual individual = new Individual();
		individual.setCpf("2232312312");
		individual.setDetails("NALNALANLA");
		individual.setName("AAAAAA");
		individual.setRg("4535354354");
		individual.setStatus(CustomerStatus.ACTIVE);
		individual.setSubscriber(subscriber);
		this.saveData(individual);

		individual = new Individual();
		individual.setCpf("85678");
		individual.setDetails("NALNALANLA");
		individual.setName("CCCCC");
		individual.setRg("79798798");
		individual.setStatus(CustomerStatus.ACTIVE);
		individual.setSubscriber(subscriber);
		this.saveData(individual);

		individual = new Individual();
		individual.setCpf("345645");
		individual.setDetails("NALNALANLA");
		individual.setName("WWWWWWW");
		individual.setRg("34234234234");
		individual.setStatus(CustomerStatus.ACTIVE);
		individual.setSubscriber(subscriber);
		this.saveData(individual);

		Company company = new Company();
		company.setCnpjCgc("33423");
		company.setInscrEstadual("55555");
		company.setInscrMunicipal("333333");
		company.setDetails("NALNALANLA");
		company.setName("Usinagem XPTO");
		company.setStatus(CustomerStatus.ACTIVE);
		company.setSubscriber(subscriber);
		this.saveData(company);
	
		
		company = new Company();
		company.setCnpjCgc("22222");
		company.setInscrEstadual("33333");
		company.setInscrMunicipal("22222");
		company.setDetails("NALNALANLA");
		company.setName("ABCDC Retifica");
		company.setStatus(CustomerStatus.ACTIVE);
		company.setSubscriber(subscriber);
		this.saveData(company);
		
		company = new Company();
		company.setCnpjCgc("234234");
		company.setInscrEstadual("643563456");
		company.setInscrMunicipal("123123");
		company.setDetails("NALNALANLA");
		company.setName("XYZ LABALBAAL");
		company.setStatus(CustomerStatus.ACTIVE);
		company.setSubscriber(subscriber);
		this.saveData(company);
		
		//Clientes inativos
		individual = new Individual();
		individual.setCpf("345645");
		individual.setDetails("NALNALANLA");
		individual.setName("Jose dos Santos INATIVO");
		individual.setRg("34234234234");
		individual.setStatus(CustomerStatus.INACTIVE);
		individual.setSubscriber(subscriber);
		this.saveData(individual);

		company = new Company();
		company.setCnpjCgc("33423");
		company.setInscrEstadual("55555");
		company.setInscrMunicipal("333333");
		company.setDetails("NALNALANLA");
		company.setName("Usinagem Inativa");
		company.setStatus(CustomerStatus.INACTIVE);
		company.setSubscriber(subscriber);
		this.saveData(company);
		
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
		
		Individual individual = new Individual();
		individual.setCpf("2232312312");
		individual.setDetails("NNNNNNONO");
		individual.setName("");
		individual.setRg("77777777");
		individual.setStatus(CustomerStatus.ACTIVE);
		individual.setSubscriber(subscriber);
		this.saveData(individual);
		
		
		Company company = new Company();
		company.setCnpjCgc("33423");
		company.setInscrEstadual("55555");
		company.setInscrMunicipal("333333");
		company.setDetails("NALNALANLA");
		company.setName("BADDADTO Usi");
		company.setStatus(CustomerStatus.ACTIVE);
		company.setSubscriber(subscriber);
		this.saveData(company);
	}
	
	private void saveData(Object obj) {
	}
	
}
