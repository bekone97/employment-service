package com.godeltech.mastery.employeeservice.service.impl;

import com.godeltech.mastery.employeeservice.dao.PhoneRepository;
import com.godeltech.mastery.employeeservice.dao.entity.Phone;
import com.godeltech.mastery.employeeservice.dto.PhoneDto;
import com.godeltech.mastery.employeeservice.exception.NotUniqueResourceException;
import com.godeltech.mastery.employeeservice.exception.ResourceNotFoundException;
import com.godeltech.mastery.employeeservice.mapping.PhoneMapper;
import com.godeltech.mastery.employeeservice.service.EmployeeService;
import com.godeltech.mastery.employeeservice.service.PhoneService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PhoneServiceImpl implements PhoneService {


    private final PhoneRepository phoneRepository;
    private final EmployeeService employeeService;
    private final PhoneMapper phoneMapper;


    @Override
    public List<PhoneDto> getPhonesByEmployee(Long employeeId) {
        log.debug("Get all phones of employee:{}", employeeId);
        return employeeService.getEmployeeById(employeeId)
                .getPhones();
    }

    @Override
    @Transactional
    public PhoneDto save(PhoneDto phoneDto, Long employeeId) {
        log.debug("Save new phone:{} of employee:{}", phoneDto, employeeId);
        if (phoneRepository.existsPhoneByNumber(phoneDto.getNumber())) {
            throw new NotUniqueResourceException(Phone.class,"number",phoneDto.getNumber());
        }
        employeeService.getEmployeeById(employeeId);
        var phone = phoneRepository.save(phoneMapper.mapToPhone(phoneDto, employeeId));
        return phoneMapper.mapToPhoneDto(phone);
    }

    @Override
    @Transactional
    public PhoneDto update(Long phoneId, PhoneDto phoneDto, Long employeeId) {
        log.debug("Update phone:{} with phoneId:{} of employee:{}", phoneDto, phoneId, employeeId);
        if (phoneRepository.existsPhoneByNumber(phoneDto.getNumber())) {
            throw new NotUniqueResourceException(Phone.class,"number",phoneDto.getPhoneId());
        }
        return phoneRepository.findPhoneByEmployeeEmployeeIdAndPhoneId(employeeId, phoneId)
                .map(phoneEntity -> phoneMapper.mergePhone(phoneEntity,phoneDto))
                .map(phoneRepository::save)
                .map(phoneMapper::mapToPhoneDto)
                .orElseThrow(() -> new ResourceNotFoundException(Phone.class, "phoneId", phoneId));
    }

    @Override
    @Transactional
    public void deleteById(Long phoneId, Long employeeId) {
        log.debug("Deleting phone by phoneId:{} and employeeId:{}", phoneId, employeeId);
        var phone = phoneRepository.findPhoneByEmployeeEmployeeIdAndPhoneId(employeeId, phoneId)
                .orElseThrow(() -> new ResourceNotFoundException(Phone.class, "phoneId", phoneId));
        phoneRepository.deleteById(phone.getPhoneId());
    }
}

