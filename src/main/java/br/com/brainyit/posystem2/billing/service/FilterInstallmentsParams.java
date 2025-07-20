package br.com.brainyit.posystem2.billing.service;

import java.time.LocalDate;
import java.util.List;

import br.com.brainyit.posystem2.domain.InstallmentStatus;

public record FilterInstallmentsParams(
                LocalDate from,
                LocalDate to,
                List<InstallmentStatus> statuses) {

}
