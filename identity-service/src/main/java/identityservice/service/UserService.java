package identityservice.service;

import identityservice.constant.PredefinedRole;
import identityservice.dto.request.ImterestRequestDto;
import identityservice.dto.request.StreamerRequestDto;
import identityservice.dto.request.UserCreationRequestDto;
import identityservice.dto.request.UserUpdateRequestDto;
import identityservice.dto.response.*;
import identityservice.entity.Role;
import identityservice.entity.User;
import identityservice.exception.AppException;
import identityservice.exception.ErrorCode;
import identityservice.mapper.ProfileMapper;
import identityservice.mapper.UserMapper;
import identityservice.repository.RoleRepository;
import identityservice.repository.UserRepository;
import identityservice.repository.httpclient.ImterestClient;
import identityservice.repository.httpclient.LivekitClient;
import identityservice.repository.httpclient.ProfileClient;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
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
    ProfileMapper profileMapper;
    ProfileClient profileClient;
    ImterestClient imterestClient;
    LivekitClient livekitClient;

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

        ImterestRequestDto imterestRequest = new ImterestRequestDto();
        imterestRequest.setName(userDto.getFirstName() +" " + userDto.getLastName());
        imterestRequest.setEmail(userDto.getEmail());
        imterestRequest.setPassword(user.getPassword());

        var imterestAccount = imterestClient.createUserImterest(imterestRequest);
        log.info("imterest: {}", imterestAccount);

        StreamerRequestDto streamerRequestDto = new StreamerRequestDto();
        streamerRequestDto.setName(userDto.getFirstName() +" " + userDto.getLastName());
        streamerRequestDto.setEmail(userDto.getEmail());

        var streamerData = livekitClient.StreamerRegister(streamerRequestDto);
        log.info("streamer: {}", streamerData);

        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponseDto> getUsers() {
        List<User> users = userRepository.findAll();
        List<UserProfileResponseDto> profiles = profileClient.getAllProfiles().getResult();

        Map<Long, UserProfileResponseDto> profileMap = profiles.stream()
                .collect(Collectors.toMap(UserProfileResponseDto::getUserId, Function.identity()));

        List<UserResponseDto> result = new ArrayList<>();

        for (User user : users) {
            UserResponseDto dto = userMapper.toUserResponse(user);
            dto.setUsername(user.getUsername());

            UserProfileResponseDto profileDto = profileMap.get(user.getId());
            dto.setProfile(profileDto);

            if (user.getRoles() != null && !user.getRoles().isEmpty()) {
                Set<RoleResponseDto> roleDtos = user.getRoles().stream().map(role -> {
                    RoleResponseDto roleDto = RoleResponseDto.builder()
                            .name(role.getName())
                            .description(role.getDescription())
                            .permissions(role.getPermissions() != null
                                    ? role.getPermissions().stream()
                                    .map(perm -> new PermissionResponseDto(perm.getName(), perm.getDescription()))
                                    .collect(Collectors.toSet())
                                    : null)
                            .build();
                    return roleDto;
                }).collect(Collectors.toSet());

                dto.setRoles(roleDtos);
            } else {
                dto.setRoles(null);
            }

            result.add(dto);
        }
        return result;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDto getUser(Long id) {
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
            log.info(user.getRoles().toString());

            return userMapper.toUserResponse(user);
    }

    @PostAuthorize("returnObject.id.toString() == authentication.name")
    public UserResponseDto getMyInfo() {
        var context = SecurityContextHolder.getContext();
        long userId = Long.parseLong(context.getAuthentication().getName());

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException(ErrorCode.USER_NOT_FOUND.getMessage()));
        var userProfile = profileClient.getProfileByUserId(userId);

        log.info("user: {}", userProfile);

        StreamerResponseDto streamerData = null;
        try {
            streamerData = livekitClient.getStreamerByEmail(userProfile.getResult().getEmail());
        } catch (Exception e) {
            log.warn("Email not found: {}", userProfile.getResult().getEmail());
        }

        return userMapper.toUserResponse(user)
                .toBuilder()
                .profile(userProfile.getResult())
                .streamer(streamerData)
                .build();
    }

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.name")
    public UserResponseDto updateUser(Long id, UserUpdateRequestDto userUpdateRequestDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        userMapper.userUpdate(user, userUpdateRequestDto);
        if (userUpdateRequestDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userUpdateRequestDto.getPassword()));
        }

        var roles = roleRepository.findAllById(userUpdateRequestDto.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        user.setDeleted(true);
        userRepository.save(user);
    }
}
