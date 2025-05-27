package br.com.brainyit.posystem2.customer.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import br.com.brainyit.posystem2.api.CustomerApiDelegate;
import br.com.brainyit.posystem2.customer.facade.CustomerFacade;
import br.com.brainyit.posystem2.customer.mapper.CompanyMapper;
import br.com.brainyit.posystem2.customer.mapper.IndividualMapper;
import br.com.brainyit.posystem2.model.CompanyRestModel;
import br.com.brainyit.posystem2.model.CustomerRestModel;
import br.com.brainyit.posystem2.model.IndividualRestModel;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class CustomerApiDelegateImpl implements CustomerApiDelegate {

    private final CustomerFacade customerFacade;

    @Override
    public ResponseEntity<CompanyRestModel> subscriberCustomersCompaniesCustomerIdPut(String subscriber, String customerId, CompanyRestModel companyRestModel) {
        if (companyRestModel == null) {
            return ResponseEntity.badRequest().build();
        }

        if (!customerId.equals(companyRestModel.getId())) {
            return ResponseEntity.badRequest().build();
        }

        try {
            final var companyMapper = CompanyMapper.builder().build();
            var company = companyMapper.mapToDomain(companyRestModel);
            var updatedCompany = customerFacade.updateCompany(Long.parseLong(subscriber), company);
            return ResponseEntity.ok(companyMapper.mapFromDomain(updatedCompany));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<CompanyRestModel> subscriberCustomersCompaniesPost(String subscriber, CompanyRestModel companyRestModel) {
        if (companyRestModel == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            final var companyMapper = CompanyMapper.builder().build();
            final var company = companyMapper.mapToDomain(companyRestModel);
            final var createdCompany = customerFacade.createCompany(Long.parseLong(subscriber), company);
            return ResponseEntity.ok(companyMapper.mapFromDomain(createdCompany));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<List<CustomerRestModel>> subscriberCustomersGet(String subscriber) {
        return ResponseEntity.ok(customerFacade.list(Long.parseLong(subscriber)));
    }

    @Override
    public ResponseEntity<CustomerRestModel> subscriberCustomersIdGet(String subscriber, String id) {
        if (id == null || !id.matches("\\d+")) {
            return ResponseEntity.badRequest().build();
        }

        try {
            var customer = customerFacade.get(Long.parseLong(subscriber), Integer.parseInt(id));
            if (customer == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(customer);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @Override
    public ResponseEntity<IndividualRestModel> subscriberCustomersIndividualsCustomerIdPut(String subscriber,
            String customerId, IndividualRestModel individualRestModel) {
        if (individualRestModel == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            final var individualMapper = IndividualMapper.builder().build();
            var individual = individualMapper.mapToDomain(individualRestModel);
            return ResponseEntity.ok(customerFacade.updateIndividual(Long.parseLong(subscriber), individual));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<IndividualRestModel> subscriberCustomersIndividualsPost(String subscriber,
            IndividualRestModel individualRestModel) {
        if (individualRestModel == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            final var individualMapper = IndividualMapper.builder().build();
            final var individual = individualMapper.mapToDomain(individualRestModel);
            return ResponseEntity.ok(customerFacade.createIndividual(Long.parseLong(subscriber), individual));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
