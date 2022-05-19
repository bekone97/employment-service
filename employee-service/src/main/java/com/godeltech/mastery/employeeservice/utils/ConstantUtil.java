package com.godeltech.mastery.employeeservice.utils;

public class ConstantUtil {
    public static class Employee{
        public static final String EMPLOYEE_ID = "employee_id";
        public static final String FIRST_NAME = "first_name";
        public static final String LAST_NAME = "last_name";
        public static final String DEPARTMENT_ID = "department_id";
        public static final String GENDER = "gender";
        public static final String DATE_OF_BIRTH = "date_of_birth";
        public static final String JOB_TITTLE = "job_tittle";
    }
    public static class Exception{
        public final static String NO_FOUNDED_PATTERN = "%s wasn't found by %s=%s";
        public final static String NO_FOUNDED_FROM_RESOURCE_PATTERN = "%s wasn't found by %s=%s from %s with %s=%s";
        public static final String EMPLOYEE_ID_FOR_EXCEPTION = "employeeId";
        public static final String VALIDATION_ERROR = "Validation error";
        public static final String NO_PREDICATE_TYPE = "Predicate component doesn't exist for predicate type";
        public final static String NOT_UNIQUE_PATTERN = "%s is already exists by %s=%s";
    }
    public static class Response{
        public static final String MEDIA_TYPE="application/json";
        public static final String RESPONSE_CODE_OK = "200";
        public static final String RESPONSE_CODE_BAD_REQUEST = "400";
        public static final String RESPONSE_CODE_INTERNAL_SERVER_ERROR = "500";
        public static final String RESPONSE_DESCRIPTION_OK = "Success";
        public static final String RESPONSE_DESCRIPTION_BAD_REQUEST = "Bad Request";
        public static final String RESPONSE_DESCRIPTION_INTERNAL_SERVER_ERROR = "Internal Server Error";
        public static final String RESPONSE_CODE_NOT_FOUNDED = "404";
        public static final String RESPONSE_DESCRIPTION_NOT_FOUNDED = "Not Founded";
    }
    public static class Kafka{
        public static final String REPLY_TOPIC_EMPLOYEE_CREATION ="REPLY_CREATION";
        public static final String TOPIC_EMPLOYEE_CREATION ="CREATION";
    }


}
