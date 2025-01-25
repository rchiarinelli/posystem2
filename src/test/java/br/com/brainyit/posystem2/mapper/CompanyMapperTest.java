package br.com.brainyit.posystem2.mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.brainyit.posystem2.domain.Company;
import br.com.brainyit.posystem2.domain.CustomerStatus;
import br.com.brainyit.posystem2.restbean.customer.CompanyRestBean;

@ExtendWith(MockitoExtension.class)
class CompanyMapperTest {

    @Test
    void test_mapFromDomain_whenValidCompany_returnsCompanyRestBean() {
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
        CompanyRestBean result = CompanyMapper.builder().build().mapFromDomain(company);

        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result.getId(), is("1"));
        assertThat(result.getName(), is("Test Company"));
        assertThat(result.getDetails(), is("Company details"));
        assertThat(result.getStatus(), is(CustomerStatus.ACTIVE.toString()));
        assertThat(result.getCnpj(), is("12.345.678/0001-90"));
        assertThat(result.getIe(), is("123456789"));
        assertThat(result.getIm(), is("987654321"));
    }

    @Test
    void test_mapFromDomain_whenCompanyIsNull_returnsNull() {
        // Act
        CompanyRestBean result = CompanyMapper.builder().build().mapFromDomain(null);

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
        CompanyRestBean result = CompanyMapper.builder().build().mapFromDomain(company);

        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result.getId(), is("1"));
        assertThat(result.getName(), is(nullValue()));
        assertThat(result.getDetails(), is(nullValue()));
        assertThat(result.getStatus(), is(CustomerStatus.ACTIVE.toString()));
        assertThat(result.getCnpj(), is(nullValue()));
        assertThat(result.getIe(), is(nullValue()));
        assertThat(result.getIm(), is(nullValue()));
    }

    @Test
    void test_mapToDomain_whenValidRestBean_returnsCompany() {
        // Arrange
        CompanyRestBean restBean = CompanyRestBean.builder()
                .id("1")
                .name("Test Company")
                .details("Company details")
                .status(CustomerStatus.ACTIVE.toString())
                .cnpj("12.345.678/0001-90")
                .ie("123456789")
                .im("987654321")
                .build();

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
        CompanyRestBean restBean = CompanyRestBean.builder()
                .id("1")
                .status(CustomerStatus.ACTIVE.toString())
                .build();

        // Act
        Company result = CompanyMapper.builder().build().mapToDomain(restBean);

        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result.getId(), is(1));
        assertThat(result.getName(), is(nullValue()));
        assertThat(result.getDetails(), is(nullValue()));
        assertThat(result.getStatus(), is(CustomerStatus.ACTIVE));
        assertThat(result.getCnpjCgc(), is(nullValue()));
        assertThat(result.getInscrEstadual(), is(nullValue()));
        assertThat(result.getInscrMunicipal(), is(nullValue()));
    }
}
