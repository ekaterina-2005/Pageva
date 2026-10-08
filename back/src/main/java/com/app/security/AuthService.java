package com.app.security;

import com.app.appUser.AppUser;
import com.app.appUser.MyUserPrincipal;
import com.app.appUser.UserDto;
import com.app.appUser.converter.UserToUserDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final UserToUserDtoConverter userToUserDtoConverter;

    public Map<String, Object> createLoginInfo(Authentication authentication) {
        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();

        AppUser user = principal.user();
        UserDto userDto = userToUserDtoConverter.convert(user);

        String token = jwtProvider.createToken(authentication);

        Map<String, Object> loginResultMap = new HashMap<>();

        loginResultMap.put("user", userDto);
        loginResultMap.put("token", token);

        return loginResultMap;
    }
}
