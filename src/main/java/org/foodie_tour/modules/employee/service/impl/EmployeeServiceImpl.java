package org.foodie_tour.modules.employee.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.foodie_tour.common.exception.DuplicateResourceException;
import org.foodie_tour.common.exception.ResourceNotFoundException;
import org.foodie_tour.common.utils.MailSampleText;
import org.foodie_tour.common.utils.RandomCode;
import org.foodie_tour.common.exception.InvalidateDataException;
import org.foodie_tour.modules.auth.service.AuthService;
import org.foodie_tour.modules.employee.dto.request.EmployeeCreateRequest;
import org.foodie_tour.modules.employee.dto.request.EmployeeUpdateRequest;
import org.foodie_tour.modules.employee.dto.request.SetPasswordRequest;
import org.foodie_tour.modules.employee.dto.response.EmployeeResponse;
import org.foodie_tour.modules.employee.entity.Employee;
import org.foodie_tour.modules.auth.entity.Role;
import org.foodie_tour.modules.employee.enums.EmployeeStatus;
import org.foodie_tour.modules.employee.mapper.EmployeeMapper;
import org.foodie_tour.modules.employee.repository.EmployeeRepository;
import org.foodie_tour.modules.auth.repository.RoleRepository;
import org.foodie_tour.modules.employee.service.EmployeeService;
import org.foodie_tour.modules.mail.dto.request.SendMailRequest;
import org.foodie_tour.modules.mail.service.MailService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployeeServiceImpl implements EmployeeService {
    EmployeeRepository employeeRepository;
    RoleRepository roleRepository;
    EmployeeMapper employeeMapper;
    PasswordEncoder passwordEncoder;
    AuthService authService;
    MailService mailService;


    @Transactional
    public EmployeeResponse create(EmployeeCreateRequest request) {
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email '" + request.getEmail() + "' đã được sử dụng");
        }

        if (employeeRepository.existsByPhone(request.getPhone())) {
            throw new DuplicateResourceException("Số điện thoại '" + request.getPhone() + "' đã được sử dụng");
        }

        Employee employee = employeeMapper.toEntity(request);

        String rawPassword = RandomCode.generateRandomCodeByKey(request.getEmail(), 10);
        employee.setPassword(passwordEncoder.encode(rawPassword));

        employee.setStatus(EmployeeStatus.ACTIVE);

        if (roleRepository.existsById(request.getRoleId())) {
            Role role = roleRepository.getReferenceById(request.getRoleId());
            employee.setRole(role);
        } else {
            throw new ResourceNotFoundException("Vai trò không tồn tại");
        }

        employeeRepository.save(employee);

        String mailTitle = MailSampleText.CREATE_EMPLOYEE_TITLE;
        String mailContent = String.format(MailSampleText.CREATE_EMPLOYEE_CONTENT, request.getEmail(), rawPassword);
        String[] receiver = {request.getEmail()};
        SendMailRequest sendMailRequest = new SendMailRequest(receiver, mailTitle, mailContent);
        mailService.sendMail(sendMailRequest);

        return employeeMapper.toResponse(employee);
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponse> getAll(EmployeeStatus status) {
        if (status == null) {
            return employeeRepository.findAll()
                    .stream()
                    .map(employeeMapper::toResponse)
                    .toList();
        } else {
            return employeeRepository.findByStatus(status)
                    .stream()
                    .map(employeeMapper::toResponse)
                    .toList();
        }
    }

    @Transactional(readOnly = true)
    public EmployeeResponse getById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nhân viên không tồn tại"));
        return employeeMapper.toResponse(employee);
    }

    @Transactional
    public EmployeeResponse update(long id, EmployeeUpdateRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nhân viên không tồn tại"));

        if (!request.getEmail().equals(employee.getEmail())) {
            if (employeeRepository.existsByEmail(request.getEmail())) {
                throw new DuplicateResourceException("Email '" + request.getEmail() + "' đã được sử dụng");
            }
            employee.setEmail(request.getEmail());
        }

        if (!request.getPhone().equals(employee.getPhone())) {
            if (employeeRepository.existsByPhone(request.getPhone())) {
                throw new DuplicateResourceException("Số điện thoại '" + request.getPhone() + "' đã được sử dụng");
            }
            employee.setPhone(request.getPhone());
        }

        employee.setEmployeeName(request.getEmployeeName());
        employeeRepository.save(employee);
        return employeeMapper.toResponse(employee);
    }
    
    @Transactional
    public EmployeeResponse changeStatus(Long id, EmployeeStatus status) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nhân viên không tồn tại"));
        employee.setStatus(status);
        employeeRepository.save(employee);
        return employeeMapper.toResponse(employee);
    }

    @Transactional
    public EmployeeResponse updateRole(Long id, Long roleId) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nhân viên không tồn tại"));

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vai trò không tồn tại"));

        employee.setRole(role);
        employeeRepository.save(employee);
        return employeeMapper.toResponse(employee);
    }

    @Override
    @Transactional
    public void updatePassword(SetPasswordRequest request, String accessToken) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new InvalidateDataException("Mật khẩu xác nhận không khớp");
        }

        String subjectFromToken = authService.getSubjectFromToken(accessToken);

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentEmployeeId = jwt.getSubject();

        if (!subjectFromToken.equals(currentEmployeeId)) {
            throw new InvalidateDataException("Không có quyền thực hiện thao tác này");
        }

        Employee employee = employeeRepository.findById(Long.parseLong(currentEmployeeId))
                .orElseThrow(() -> new ResourceNotFoundException("Nhân viên không tồn tại"));

        employee.setPassword(passwordEncoder.encode(request.getNewPassword()));
        employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên"));

        employee.setStatus(EmployeeStatus.DELETED);
        employeeRepository.save(employee);
    }
}
