package com.godeltech.mastery.employeeservice.controller;

import com.godeltech.mastery.employeeservice.dto.PhoneDto;
import com.godeltech.mastery.employeeservice.service.PhoneService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("employees/{employeeId}/phones")
@RequiredArgsConstructor
@Slf4j
public class PhoneController {
    private final PhoneService phoneService;


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PhoneDto> getPhones(@PathVariable Long employeeId){
        log.info("Get all phones of employee with id:{}",employeeId);
        return phoneService.getPhonesByEmployee(employeeId);
    }

    @PostMapping
    public PhoneDto savePhone(@PathVariable Long employeeId,
                              @RequestBody PhoneDto phoneDto){
        log.info("Save new phone:{} for employee with id:{}",phoneDto,employeeId);
        return phoneService.save(phoneDto,employeeId);
    }

    @PutMapping("/{phoneId}")
    public PhoneDto updatePhone(@PathVariable Long employeeId,
                                @PathVariable Long phoneId,
                                @RequestBody PhoneDto phoneDto){
        log.info("Update phone:{} with id :{} for employee with id:{}",phoneDto,phoneId,employeeId);
        return phoneService.update(phoneId,phoneDto,employeeId);
    }

    @DeleteMapping("/{phoneId}")
    public void deletePhone(@PathVariable Long phoneId,
                            @PathVariable Long employeeId){
        log.info("Delete phone with id:{}",phoneId);
        phoneService.deleteById(phoneId,employeeId);
    }
}
