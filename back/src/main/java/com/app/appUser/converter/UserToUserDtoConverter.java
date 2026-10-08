package com.app.appUser.converter;

import com.app.appUser.AppUser;
import com.app.appUser.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDtoConverter implements Converter<AppUser, UserDto> {

    @Override
    public UserDto convert(AppUser source) {
        return new UserDto(
                source.getId(),
                source.getUsername(),

                source.getRole().name(),

                source.getFio(),
                source.getDate(),
                source.getEmail(),

                source.getImg(),

                source.isAccountNonLocked()
        );
    }
}
