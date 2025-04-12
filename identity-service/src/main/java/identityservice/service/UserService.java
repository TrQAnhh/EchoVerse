package identityservice.service;

import identityservice.dto.request.UserCreationRequestDto;
import identityservice.dto.request.UserUpdateRequestDto;
import identityservice.dto.response.ApiResponse;
import identityservice.dto.response.UserResponseDto;
import identityservice.entity.User;
import identityservice.exception.AppException;
import identityservice.exception.ErrorCode;
import identityservice.mapper.UserMapper;
import identityservice.repository.RoleRepository;
import identityservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    public ApiResponse<UserResponseDto> createUser(UserCreationRequestDto userCreationRequestDto) {

        if(userCreationRequestDto.getPhoneNumber() != null) {
            if (userRepository.findByPhoneNumber(userCreationRequestDto.getPhoneNumber()).isPresent()) {
                throw new AppException(ErrorCode.PHONE_NUMBER_EXISTED);
            }
        }

        if (userCreationRequestDto.getEmail() != null) {
            if (userRepository.findByEmail(userCreationRequestDto.getEmail()).isPresent()) {
                throw new AppException(ErrorCode.EMAIL_EXISTED);
            }
        }

        var existedUser = userRepository.findByUsername(userCreationRequestDto.getUsername());

        if (existedUser.isPresent()) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUser(userCreationRequestDto);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        ApiResponse<UserResponseDto> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("User registered successfully");

        List<String> roleNames = userCreationRequestDto.getRoles();
        if (roleNames == null || roleNames.isEmpty()) {
            roleNames = List.of("USER");
        }

        var roles = roleRepository.findAllByRoleName(roleNames);
        user.setRoles(new HashSet<>(roles));

        user.setRoles(new HashSet<>(roles));

        response.setResult(userMapper.toUserResponseDto(userRepository.save(user)));

        return response;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserResponseDto)
                .collect(Collectors.toList());
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponseDto getUserById(String userId) {
        return userMapper.toUserResponseDto(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponseDto getMyInfo() {
        var context = SecurityContextHolder.getContext();
        var username = context.getAuthentication().getName();
        return userMapper.toUserResponseDto(userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponseDto updateUser(String id, UserUpdateRequestDto userUpdateRequestDto) {

        User existedUser = userRepository.findById(id).
                orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userMapper.updateUser(existedUser, userUpdateRequestDto);

        existedUser.setPassword(passwordEncoder.encode(userUpdateRequestDto.getPassword()));

        var roles = roleRepository.findAllByRoleName(userUpdateRequestDto.getRoles());
        existedUser.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponseDto(userRepository.save(existedUser));
    }

}
