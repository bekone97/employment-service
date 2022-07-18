package com.godeltech.mastery.departmentservice.service.DepartmentServiceImpl;

import com.godeltech.mastery.departmentservice.dto.DepartmentDtoResponse;
import com.godeltech.mastery.departmentservice.model.DepartmentRepository;
import com.godeltech.mastery.departmentservice.service.DepartmentService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("test")
public class AnotherDepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;

    public AnotherDepartmentServiceImpl(DepartmentRepository departmentRepository, ModelMapper modelMapper) {
        this.departmentRepository = departmentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public DepartmentDtoResponse findDepartmentById(Long departmentId) {
        System.out.println("Сработал друго департмент сервис");
        return departmentRepository.findById(departmentId)
                .map(dep -> modelMapper.map(dep, DepartmentDtoResponse.class))
                .orElseThrow(() -> new RuntimeException("lsadasld"));
    }
}