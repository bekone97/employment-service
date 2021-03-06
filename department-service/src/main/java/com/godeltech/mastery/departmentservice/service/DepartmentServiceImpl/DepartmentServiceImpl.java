package com.godeltech.mastery.departmentservice.service.DepartmentServiceImpl;

import com.godeltech.mastery.departmentservice.dto.DepartmentDtoResponse;
import com.godeltech.mastery.departmentservice.exception.ResourceNotFoundException;
import com.godeltech.mastery.departmentservice.model.DepartmentRepository;
import com.godeltech.mastery.departmentservice.model.entity.Department;
import com.godeltech.mastery.departmentservice.service.DepartmentService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("run")
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository, ModelMapper modelMapper) {
        this.departmentRepository = departmentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public DepartmentDtoResponse findDepartmentById(Long departmentId) {
        return departmentRepository.findById(departmentId)
                .map(department -> modelMapper.map(department, DepartmentDtoResponse.class))
                .orElseThrow(() -> new ResourceNotFoundException(Department.class, "department_id", departmentId));
    }
}
