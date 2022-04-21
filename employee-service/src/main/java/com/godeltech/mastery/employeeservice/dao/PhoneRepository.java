package com.godeltech.mastery.employeeservice.dao;

import com.godeltech.mastery.employeeservice.dao.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhoneRepository extends JpaRepository<Phone,Long> {
    Optional<Phone> findPhoneByEmployeeEmployeeIdAndPhoneId(Long employeeId, Long phoneId);
    boolean existsPhoneByNumber(int number);
}
