package com.godeltech.mastery.departmentservice.service.DepartmentServiceImpl;

import com.godeltech.mastery.departmentservice.dto.DepartmentDtoResponse;
import com.godeltech.mastery.departmentservice.exception.ResourceNotFoundException;
import com.godeltech.mastery.departmentservice.model.DepartmentRepository;
import com.godeltech.mastery.departmentservice.model.entity.Department;
import com.godeltech.mastery.departmentservice.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;

    @Override
    public DepartmentDtoResponse findDepartmentById(Long departmentId) {
        return departmentRepository.findById(departmentId)
                .map(department -> modelMapper.map(department, DepartmentDtoResponse.class))
                .orElseThrow(() -> new ResourceNotFoundException(Department.class, "department_id", departmentId));
    }
}
