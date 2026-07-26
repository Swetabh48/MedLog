package com.pm.patientservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.exception.EmailAlreadyExistsException;
import com.pm.patientservice.exception.PatientNotFoundException;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

  @Mock
  private PatientRepository patientRepository;

  private PatientService patientService;

  private Patient patient;

  @BeforeEach
  void setUp() {
    patientService = new PatientService(patientRepository);

    patient = new Patient();
    patient.setId(UUID.randomUUID());
    patient.setName("John Doe");
    patient.setEmail("john.doe@example.com");
    patient.setAddress("123 Main St");
    patient.setDateOfBirth(LocalDate.of(1990, 1, 1));
    patient.setRegisteredDate(LocalDate.of(2024, 1, 1));
  }

  private PatientRequestDTO buildRequest() {
    PatientRequestDTO request = new PatientRequestDTO();
    request.setName("John Doe");
    request.setEmail("john.doe@example.com");
    request.setAddress("123 Main St");
    request.setDateOfBirth("1990-01-01");
    request.setRegisteredDate("2024-01-01");
    return request;
  }

  @Test
  void getPatientsReturnsMappedDtos() {
    when(patientRepository.findAll()).thenReturn(List.of(patient));

    List<PatientResponseDTO> result = patientService.getPatients();

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getEmail()).isEqualTo("john.doe@example.com");
    assertThat(result.get(0).getId()).isEqualTo(patient.getId().toString());
  }

  @Test
  void createPatientSavesAndReturnsDto() {
    when(patientRepository.existsByEmail("john.doe@example.com")).thenReturn(false);
    when(patientRepository.save(any(Patient.class))).thenReturn(patient);

    PatientResponseDTO result = patientService.createPatient(buildRequest());

    assertThat(result.getName()).isEqualTo("John Doe");
    verify(patientRepository).save(any(Patient.class));
  }

  @Test
  void createPatientRejectsDuplicateEmail() {
    when(patientRepository.existsByEmail("john.doe@example.com")).thenReturn(true);

    assertThatThrownBy(() -> patientService.createPatient(buildRequest()))
        .isInstanceOf(EmailAlreadyExistsException.class);
  }

  @Test
  void updatePatientUpdatesFields() {
    UUID id = patient.getId();
    when(patientRepository.findById(id)).thenReturn(Optional.of(patient));
    when(patientRepository.existsByEmailAndIdNot("john.doe@example.com", id))
        .thenReturn(false);
    when(patientRepository.save(any(Patient.class))).thenReturn(patient);

    PatientResponseDTO result = patientService.updatePatient(id, buildRequest());

    assertThat(result.getEmail()).isEqualTo("john.doe@example.com");
    verify(patientRepository).save(patient);
  }

  @Test
  void updatePatientThrowsWhenMissing() {
    UUID id = UUID.randomUUID();
    when(patientRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> patientService.updatePatient(id, buildRequest()))
        .isInstanceOf(PatientNotFoundException.class);
  }

  @Test
  void deletePatientDelegatesToRepository() {
    UUID id = UUID.randomUUID();

    patientService.deletePatient(id);

    verify(patientRepository).deleteById(id);
  }
}
