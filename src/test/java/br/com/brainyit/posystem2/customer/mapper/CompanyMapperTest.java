package br.com.brainyit.posystem2.customer.mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.brainyit.posystem2.domain.Company;
import br.com.brainyit.posystem2.domain.CustomerStatus;
import br.com.brainyit.posystem2.model.CompanyRestModel;

@ExtendWith(MockitoExtension.class)
class CompanyMapperTest {

    @Test
    void test_mapFromDomain_whenValidCompany_returnsCompanyRestModel() {
        // Arrange
        Company company = Company.builder()
            .cnpjCgc("12.345.678/0001-90")
            .inscrEstadual("123456789")
            .inscrMunicipal("987654321")
            .build();
        company.setId(1);
        company.setName("Test Company");
        company.setDetails("Company details");
        company.setStatus(CustomerStatus.ACTIVE);

        // Act
        CompanyRestModel result = CompanyMapper.builder().build().mapFromDomain(company);

        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result.getId(), is("1"));
        assertThat(result.getName(), is("Test Company"));
        assertThat(result.getDetails(), is("Company details"));
        assertThat(result.getStatus(), is(CustomerStatus.ACTIVE.toString()));
        assertThat(result.getCnpj(), is("12.345.678/0001-90"));
        assertThat(result.getInscricaoEstadual(), is("123456789"));
        assertThat(result.getInscricaoMunicipal(), is("987654321"));
    }

    @Test
    void test_mapFromDomain_whenCompanyIsNull_returnsNull() {
        // Act
        CompanyRestModel result = CompanyMapper.builder().build().mapFromDomain(null);

        // Assert
        assertThat(result, is(nullValue()));
    }

    @Test
    void test_mapFromDomain_whenEmptyCompany_returnsEmptyRestBean() {
        // Arrange
        Company company = Company.builder().build();
        company.setId(1);
        company.setStatus(CustomerStatus.ACTIVE);

        // Act
        CompanyRestModel result = CompanyMapper.builder().build().mapFromDomain(company);

        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result.getId(), is("1"));
        assertThat(result.getName(), is(nullValue()));
        assertThat(result.getDetails(), is(nullValue()));
        assertThat(result.getStatus(), is(CustomerStatus.ACTIVE.toString()));
        assertThat(result.getCnpj(), is(nullValue()));
        assertThat(result.getInscricaoEstadual(), is(nullValue()));
        assertThat(result.getInscricaoMunicipal(), is(nullValue()));
    }

    @Test
    void test_mapToDomain_whenValidRestBean_returnsCompany() {
        // Arrange
        CompanyRestModel restBean = new CompanyRestModel();
        restBean.id("1");
        restBean.name("Test Company");
        restBean.details("Company details");
        restBean.status(CustomerStatus.ACTIVE.toString());
        restBean.cnpj("12.345.678/0001-90");
        restBean.inscricaoEstadual("123456789");
        restBean.inscricaoMunicipal("987654321");

        // Act
        Company result = CompanyMapper.builder().build().mapToDomain(restBean);

        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result.getId(), is(1));
        assertThat(result.getName(), is("Test Company"));
        assertThat(result.getDetails(), is("Company details"));
        assertThat(result.getStatus(), is(CustomerStatus.ACTIVE));
        assertThat(result.getCnpjCgc(), is("12.345.678/0001-90"));
        assertThat(result.getInscrEstadual(), is("123456789"));
        assertThat(result.getInscrMunicipal(), is("987654321"));
    }

    @Test
    void test_mapToDomain_whenRestBeanIsNull_returnsNull() {
        // Act
        Company result = CompanyMapper.builder().build().mapToDomain(null);

        // Assert
        assertThat(result, is(nullValue()));
    }

    @Test
    void test_mapToDomain_whenEmptyRestBean_returnsEmptyCompany() {
        // Arrange
        CompanyRestModel restBean = new CompanyRestModel();


        // Act
        Company result = CompanyMapper.builder().build().mapToDomain(restBean);

        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result.getId(), is(nullValue()));
        assertThat(result.getName(), is(nullValue()));
        assertThat(result.getDetails(), is(nullValue()));
        assertThat(result.getStatus(), is(nullValue()));
        assertThat(result.getCnpjCgc(), is(nullValue()));
        assertThat(result.getInscrEstadual(), is(nullValue()));
        assertThat(result.getInscrMunicipal(), is(nullValue()));
    }
}
