package identityservice.service;

import identityservice.constant.PredefinedRole;
import identityservice.dto.request.UserCreationRequestDto;
import identityservice.dto.request.UserUpdateRequestDto;
import identityservice.dto.response.UserResponseDto;
import identityservice.entity.Role;
import identityservice.entity.User;
import identityservice.exception.AppException;
import identityservice.exception.ErrorCode;
import identityservice.mapper.ProfileMapper;
import identityservice.mapper.UserMapper;
import identityservice.repository.RoleRepository;
import identityservice.repository.UserRepository;
import identityservice.repository.httpclient.ProfileClient;
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

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    ProfileMapper profileMapper;
    ProfileClient profileClient;

    public UserResponseDto createUser(UserCreationRequestDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        log.info("input: {}", userDto);


        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);

        user.setRoles(roles);
        user = userRepository.save(user);

        var profileRequest = profileMapper.toProfileCreationRequest(userDto);
        profileRequest.setUserId(user.getId());

        var profile = profileClient.createProfile(profileRequest);
        log.info("profile: {}", profile);

        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponseDto> getUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDto getUser(Long id) {
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
            log.info(user.getRoles().toString());

            return userMapper.toUserResponse(user);
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponseDto getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException(ErrorCode.USER_NOT_FOUND.getMessage()));

        return userMapper.toUserResponse(user);
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponseDto updateUser(Long id, UserUpdateRequestDto userUpdateRequestDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        userMapper.userUpdate(user, userUpdateRequestDto);
        user.setPassword(passwordEncoder.encode(userUpdateRequestDto.getPassword()));

        var roles = roleRepository.findAllById(userUpdateRequestDto.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

}
