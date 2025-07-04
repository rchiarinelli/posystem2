package br.com.brainyit.posystem2.po.service;

import br.com.brainyit.posystem2.domain.POStatus;

public record FilterProductionOrdersParams(String poNumber, String customerName, POStatus... statuses) {

}
