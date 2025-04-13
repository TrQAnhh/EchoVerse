package identityservice.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import identityservice.dto.request.AuthenticationRequestDto;
import identityservice.dto.request.IntrospectRequestDto;
import identityservice.dto.request.LogoutRequestDto;
import identityservice.dto.request.RefreshTokenRequestDto;
import identityservice.dto.response.AuthenticationResponseDto;
import identityservice.dto.response.IntrospectResponseDto;
import identityservice.dto.response.RefreshTokenResponseDto;
import identityservice.entity.InvalidatedToken;
import identityservice.entity.User;
import identityservice.exception.AppException;
import identityservice.exception.ErrorCode;
import identityservice.repository.InvalidatedTokenRepository;
import identityservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class AuthenticationService {

    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${jwt.signer-key}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected String VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected String REFRESHABLE_DURATION;

    public AuthenticationResponseDto login(AuthenticationRequestDto authenticationRequestDto) {
        var existedUser = userRepository.findByUsername(authenticationRequestDto.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(authenticationRequestDto.getPassword(), existedUser.getPassword());

        if(!authenticated){
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }

        try {
            var token = tokenGenerator(existedUser);
            return AuthenticationResponseDto.builder()
                    .is_authenticated(true)
                    .token(token)
                    .build();
        } catch (JOSEException e) {
            throw new AppException(ErrorCode.FAIL_TO_GENERATE_TOKEN);
        }
    }

    private String tokenGenerator(User user) throws JOSEException {

        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("echoverse.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(Long.parseLong(VALID_DURATION), ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope",buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try{
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new AppException(ErrorCode.FAIL_TO_GENERATE_TOKEN);
        }
    }

    public IntrospectResponseDto introspect(IntrospectRequestDto introspectRequestDto)  throws ParseException, JOSEException {
        var token = introspectRequestDto.getToken();
        boolean isValid = true;
        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
        }
        return IntrospectResponseDto.builder()
                .valid(isValid)
                .build();
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions().forEach(permission -> {
                        stringJoiner.add(permission.getName());
                    });
                }
            });
        }

        return stringJoiner.toString();
    }

    public void logout(LogoutRequestDto logoutRequestDto) throws ParseException, JOSEException {
        var token = verifyToken(logoutRequestDto.getToken(),true);

        String jwtId = token.getJWTClaimsSet().getJWTID();
        Date expirationTime = token.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jwtId)
                .expirationTime(expirationTime)
                .build();

        invalidatedTokenRepository.save(invalidatedToken);
    }

    private SignedJWT verifyToken (String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expirationTime = (isRefresh) ? new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(Long.parseLong(REFRESHABLE_DURATION), ChronoUnit.SECONDS).toEpochMilli()): signedJWT.getJWTClaimsSet().getExpirationTime();

        // Verify token
        boolean verified = signedJWT.verify(verifier);

        if(invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if(!(verified && expirationTime.after(new Date()))){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return signedJWT;

    }

    public RefreshTokenResponseDto refreshToken(RefreshTokenRequestDto refreshTokenRequestDto) throws ParseException, JOSEException {

        var signJWT = verifyToken(refreshTokenRequestDto.getToken(), true);

        var jit = signJWT.getJWTClaimsSet().getJWTID();

        var expirationTime = signJWT.getJWTClaimsSet().getExpirationTime();
        InvalidatedToken invalidatedToken = InvalidatedToken.builder().id(jit).expirationTime(expirationTime).build();

        invalidatedTokenRepository.save(invalidatedToken);

        var username = signJWT.getJWTClaimsSet().getSubject();
        var user = userRepository.findByUsername(username).orElseThrow(
                () -> new AppException(ErrorCode.UNAUTHENTICATED)
        );

        var token = tokenGenerator(user);
        return RefreshTokenResponseDto.builder()
                .is_authenticated(true)
                .token(token)
                .build();

    }
}
