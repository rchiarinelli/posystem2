package br.com.brainyit.posystem2.domain;

/**
 * 
 */
public enum ServiceType {

    MACHINE("Máquina"), HUMAN("Serviço"), MATERIAL("Material");

    private final String type;

    private ServiceType(String t) {
        this.type = t;
    }
}
