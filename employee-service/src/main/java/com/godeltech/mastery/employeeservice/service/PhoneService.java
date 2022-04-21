package com.godeltech.mastery.employeeservice.service;

import com.godeltech.mastery.employeeservice.dto.PhoneDto;

import java.util.List;

public interface PhoneService {

    List<PhoneDto> getPhonesByEmployee(Long employeeId);

    PhoneDto save(PhoneDto phoneDto,Long employeeId);

    PhoneDto update(Long phoneId, PhoneDto phoneDto, Long employeeId);

    void deleteById(Long phoneId, Long employeeId);

}
