package com.pm.patientservice.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.model.Patient;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class PatientMapperTest {

  @Test
  void toDtoMapsAllFields() {
    Patient patient = new Patient();
    patient.setId(UUID.randomUUID());
    patient.setName("Jane Smith");
    patient.setEmail("jane.smith@example.com");
    patient.setAddress("456 Elm St");
    patient.setDateOfBirth(LocalDate.of(1985, 6, 15));
    patient.setRegisteredDate(LocalDate.of(2024, 2, 2));

    PatientResponseDTO dto = PatientMapper.toDTO(patient);

    assertThat(dto.getId()).isEqualTo(patient.getId().toString());
    assertThat(dto.getName()).isEqualTo("Jane Smith");
    assertThat(dto.getEmail()).isEqualTo("jane.smith@example.com");
    assertThat(dto.getAddress()).isEqualTo("456 Elm St");
    assertThat(dto.getDateOfBirth()).isEqualTo("1985-06-15");
  }

  @Test
  void toModelParsesDates() {
    PatientRequestDTO request = new PatientRequestDTO();
    request.setName("Jane Smith");
    request.setEmail("jane.smith@example.com");
    request.setAddress("456 Elm St");
    request.setDateOfBirth("1985-06-15");
    request.setRegisteredDate("2024-02-02");

    Patient patient = PatientMapper.toModel(request);

    assertThat(patient.getName()).isEqualTo("Jane Smith");
    assertThat(patient.getDateOfBirth()).isEqualTo(LocalDate.of(1985, 6, 15));
    assertThat(patient.getRegisteredDate()).isEqualTo(LocalDate.of(2024, 2, 2));
  }
}
