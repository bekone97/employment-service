package com.godeltech.mastery.employeeservice.controller;

import com.godeltech.mastery.employeeservice.dto.EmployeeDtoRequest;
import com.godeltech.mastery.employeeservice.dto.EmployeeDtoResponse;
import com.godeltech.mastery.employeeservice.handling.ApiErrorResponse;
import com.godeltech.mastery.employeeservice.handling.ValidationErrorResponse;
import com.godeltech.mastery.employeeservice.service.EmployeeService;
import com.godeltech.mastery.employeeservice.validator.ValidId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static com.godeltech.mastery.employeeservice.utils.ConstantUtil.Response.*;


@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@Validated
@Slf4j
public class EmployeeController {


    private final EmployeeService employeeService;

    @Operation(summary = "Respond a group of all employees, or a group of employees who consists firstName")
    @ApiResponses({
            @ApiResponse(responseCode = RESPONSE_CODE_OK, description = RESPONSE_DESCRIPTION_OK,
            content = {@Content(mediaType = MEDIA_TYPE,
            schema = @Schema(implementation = EmployeeDtoResponse.class))}),
            @ApiResponse(responseCode = RESPONSE_CODE_BAD_REQUEST, description = RESPONSE_DESCRIPTION_BAD_REQUEST,
                    content = {@Content(mediaType = MEDIA_TYPE,
                            schema = @Schema(implementation = ApiErrorResponse.class))}),
            @ApiResponse(responseCode = RESPONSE_CODE_INTERNAL_SERVER_ERROR,description = RESPONSE_DESCRIPTION_INTERNAL_SERVER_ERROR,
            content = @Content)
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeDtoResponse> getEmployees(@Parameter(description = "Persons' part of or full firstname")
                                       @RequestParam(required = false) String firstName) {
        log.info("Get all employees or part of employees by a part of firstName:{}",firstName);

        return Optional.ofNullable(firstName)
                .map(employeeService::findByFirstNameLike)
                .orElseGet(employeeService::getEmployees);

    }

    @Operation(summary = "Respond an employee by employeeId")
    @ApiResponses({
            @ApiResponse(responseCode = RESPONSE_CODE_OK, description =RESPONSE_DESCRIPTION_OK,
                    content ={@Content(mediaType = MEDIA_TYPE,
                            schema = @Schema(implementation = EmployeeDtoResponse.class))}),
            @ApiResponse(responseCode = RESPONSE_CODE_NOT_FOUNDED, description= RESPONSE_DESCRIPTION_NOT_FOUNDED,
                    content = {@Content(mediaType = MEDIA_TYPE,
                            schema = @Schema(implementation = ApiErrorResponse.class))}),
            @ApiResponse(responseCode = RESPONSE_CODE_INTERNAL_SERVER_ERROR,description = RESPONSE_DESCRIPTION_INTERNAL_SERVER_ERROR,
            content = @Content)
    }
    )
    @GetMapping("/{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeDtoResponse getEmployeeById(@Parameter(description = "Id of employee to be searched", required = true,example = "1")
                                    @PathVariable @ValidId Long employeeId) {
        log.info("Get an employee by employeeId:{}",employeeId);

        return employeeService.getEmployeeById(employeeId);

    }
    @Operation(summary = "Save a new employee")
    @ApiResponses({
            @ApiResponse(responseCode = RESPONSE_CODE_OK, description = RESPONSE_DESCRIPTION_OK,content ={@Content(mediaType = MEDIA_TYPE,
                    schema = @Schema(implementation = EmployeeDtoResponse.class))}),
            @ApiResponse(responseCode = RESPONSE_CODE_BAD_REQUEST, description=RESPONSE_DESCRIPTION_BAD_REQUEST,
                    content = {@Content(mediaType = MEDIA_TYPE,
                            schema = @Schema(implementation = ValidationErrorResponse.class))}),
            @ApiResponse(responseCode = RESPONSE_CODE_INTERNAL_SERVER_ERROR,description = RESPONSE_DESCRIPTION_INTERNAL_SERVER_ERROR,
                    content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeDtoResponse saveEmployee(@Parameter(description = "Employee information for a new employee to be created",required = true)
                                     @Valid @RequestBody EmployeeDtoRequest employeeDtoRequest) throws ExecutionException, InterruptedException, TimeoutException {

        log.info("Save a new employee:{}",employeeDtoRequest);

        return employeeService.save(employeeDtoRequest);

    }

    @Operation(summary = "Update an existing employee by id")
    @ApiResponses({
            @ApiResponse(responseCode = RESPONSE_CODE_OK, description = RESPONSE_DESCRIPTION_OK,
                    content ={@Content(mediaType = MEDIA_TYPE,
                    schema = @Schema(implementation = EmployeeDtoResponse.class))}),
            @ApiResponse(responseCode = RESPONSE_CODE_BAD_REQUEST, description=RESPONSE_DESCRIPTION_BAD_REQUEST,
                    content = {@Content(mediaType = MEDIA_TYPE,
                            schema = @Schema(implementation = ValidationErrorResponse.class))}),
            @ApiResponse(responseCode = RESPONSE_CODE_NOT_FOUNDED, description=RESPONSE_DESCRIPTION_NOT_FOUNDED,
                    content = {@Content(mediaType = MEDIA_TYPE,
                            schema = @Schema(implementation = ApiErrorResponse.class))}),
            @ApiResponse(responseCode = RESPONSE_CODE_INTERNAL_SERVER_ERROR,description = RESPONSE_DESCRIPTION_INTERNAL_SERVER_ERROR,content = @Content)
    })
    @PutMapping("/{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeDtoResponse updateEmployee(@Parameter(description = "Id of employee to be updated",required = true,example = "1")
                                       @PathVariable @ValidId Long employeeId,
                                   @Parameter(description = "Employee info")
                                   @Valid @RequestBody EmployeeDtoRequest employeeDtoRequest) {

        log.info("Update an employee by id of employee:{} and request body:{}",
               employeeId,employeeDtoRequest);

        return employeeService.update(employeeId, employeeDtoRequest);

    }
    @Operation(description = "Delete an existing employee by id")
    @ApiResponses({
            @ApiResponse(responseCode = RESPONSE_CODE_OK, description = RESPONSE_DESCRIPTION_OK),
            @ApiResponse(responseCode = RESPONSE_CODE_NOT_FOUNDED, description=RESPONSE_DESCRIPTION_NOT_FOUNDED,
                    content = {@Content(mediaType = MEDIA_TYPE,
                            schema = @Schema(implementation = ApiErrorResponse.class))}),
            @ApiResponse(responseCode = RESPONSE_CODE_INTERNAL_SERVER_ERROR, description = RESPONSE_DESCRIPTION_INTERNAL_SERVER_ERROR,content = @Content)
    })
    @DeleteMapping("/{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteEmployee(@Parameter(description = "Id of employee to be deleted",required = true,example = "1")
            @PathVariable @ValidId Long employeeId) {

        log.info("Delete an employee by id:{}",employeeId);
        employeeService.deleteById(employeeId);
    }


}
