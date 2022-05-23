package com.godeltech.mastery.employeeservice.mapping;

import com.godeltech.mastery.employeeservice.dao.entity.Phone;
import com.godeltech.mastery.employeeservice.dto.PhoneDto;

public interface PhoneMapper {
    PhoneDto mapToPhoneDto(Phone phone);

    Phone mapToPhone(PhoneDto phoneDto, Long employeeId);

    Phone mergePhone(Phone phone, PhoneDto phoneDto);
}
