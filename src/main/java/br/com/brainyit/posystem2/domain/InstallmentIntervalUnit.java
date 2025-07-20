package br.com.brainyit.posystem2.domain;

public enum InstallmentIntervalUnit {

    MONTH("Mês"), WEEK("Semana"), DAY("Dia");

    private final String unit;

    private InstallmentIntervalUnit(String u) {
        this.unit = u;
    }

    public String getUnitValue() {
        return this.unit;
    }

}
