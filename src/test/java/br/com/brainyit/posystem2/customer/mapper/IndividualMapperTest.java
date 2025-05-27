package br.com.brainyit.posystem2.customer.mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.brainyit.posystem2.domain.CustomerStatus;
import br.com.brainyit.posystem2.domain.Individual;
import br.com.brainyit.posystem2.model.IndividualRestModel;

@ExtendWith(MockitoExtension.class)
class IndividualMapperTest {

    @Test
    void test_mapFromDomain_whenValidIndividual_returnsIndividualRestModel() {
        // Arrange
        Individual individual = Individual.builder()
                .cpf("123.456.789-00")
                .rg("RG12345")
                .build();
        individual.setId(1);
        individual.setName("John Doe");
        individual.setDetails("Test details");
        individual.setStatus(CustomerStatus.ACTIVE);

        // Act
        IndividualRestModel result = IndividualMapper.builder().build().mapFromDomain(individual);

        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result.getId(), is("1"));
        assertThat(result.getName(), is("John Doe"));
        assertThat(result.getDetails(), is("Test details"));
        assertThat(result.getStatus(), is(CustomerStatus.ACTIVE.toString()));
        assertThat(result.getCpf(), is("123.456.789-00"));
        assertThat(result.getRg(), is("RG12345"));
    }

    @Test
    void test_mapFromDomain_whenIndividualIsNull_returnsNull() {
        // Act
        IndividualRestModel result = IndividualMapper.builder().build().mapFromDomain(null);

        // Assert
        assertThat(result, is(nullValue()));
    }

    @Test
    void test_mapFromDomain_whenEmptyIndividual_returnsEmptyRestBean() {
        // Arrange
        Individual individual = Individual.builder().build();
        individual.setId(1);
        individual.setStatus(CustomerStatus.ACTIVE);

        // Act
        IndividualRestModel result = IndividualMapper.builder().build().mapFromDomain(individual);

        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result.getId(), is("1"));
        assertThat(result.getName(), is(nullValue()));
        assertThat(result.getDetails(), is(nullValue()));
        assertThat(result.getStatus(), is(CustomerStatus.ACTIVE.toString()));
        assertThat(result.getCpf(), is(nullValue()));
        assertThat(result.getRg(), is(nullValue()));
    }

    @Test
    void test_mapToDomain_whenValidRestBean_returnsIndividual() {
        // Arrange
        IndividualRestModel restBean = new IndividualRestModel();
        restBean.id("1");
        restBean.name("John Doe");
        restBean.details("Test details");
        restBean.status(CustomerStatus.ACTIVE.toString());
        restBean.cpf("123.456.789-00");
        restBean.rg("RG12345");

        // Act
        Individual result = IndividualMapper.builder().build().mapToDomain(restBean);

        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result.getId(), is(1));
        assertThat(result.getName(), is("John Doe"));
        assertThat(result.getDetails(), is("Test details"));
        assertThat(result.getStatus(), is(CustomerStatus.ACTIVE));
        assertThat(result.getCpf(), is("123.456.789-00"));
        assertThat(result.getRg(), is("RG12345"));
    }

    @Test
    void test_mapToDomain_whenRestBeanIsNull_returnsNull() {
        // Act
        Individual result = IndividualMapper.builder().build().mapToDomain(null);

        // Assert
        assertThat(result, is(nullValue()));
    }

    @Test
    void test_mapToDomain_whenEmptyRestBean_returnsEmptyIndividual() {
        // Arrange
        IndividualRestModel restBean = new IndividualRestModel();

        // Act
        Individual result = IndividualMapper.builder().build().mapToDomain(restBean);

        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result.getId(), is(nullValue()));
        assertThat(result.getName(), is(nullValue()));
        assertThat(result.getDetails(), is(nullValue()));
        assertThat(result.getStatus(), is(nullValue()));
        assertThat(result.getCpf(), is(nullValue()));
        assertThat(result.getRg(), is(nullValue()));
    }

}
