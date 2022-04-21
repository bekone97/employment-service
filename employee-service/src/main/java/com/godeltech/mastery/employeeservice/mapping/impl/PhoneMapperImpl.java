package com.godeltech.mastery.employeeservice.mapping.impl;

import com.godeltech.mastery.employeeservice.dao.entity.Employee;
import com.godeltech.mastery.employeeservice.dao.entity.Phone;
import com.godeltech.mastery.employeeservice.dto.PhoneDto;
import com.godeltech.mastery.employeeservice.mapping.EmployeeMapper;
import com.godeltech.mastery.employeeservice.mapping.PhoneMapper;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PhoneMapperImpl implements PhoneMapper {

    private final EmployeeMapper employeeMapper;
    private final ModelMapper modelMapper;

    @Override
    public PhoneDto mapToPhoneDto(Phone phone) {
        return modelMapper.map(phone,PhoneDto.class);
    }

    @Override
    public Phone mapToPhone(PhoneDto phoneDto, Long employeeId) {
       var phone= modelMapper.map(phoneDto,Phone.class);
       phone.setEmployee(employeeMapper.initEmployeeWithId(employeeId));
       return phone;
    }

    @Override
    public Phone mergePhone(Phone phone, PhoneDto phoneDto) {
        phone.setNumber(phoneDto.getNumber());
        return phone;
    }

}
