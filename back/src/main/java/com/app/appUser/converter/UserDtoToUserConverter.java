package com.app.appUser.converter;

import com.app.appUser.AppUser;
import com.app.appUser.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToUserConverter implements Converter<UserDto, AppUser> {

    @Override
    public AppUser convert(UserDto source) {
        return new AppUser(
                source.fio(),
                source.date(),
                source.email()
        );
    }
}
