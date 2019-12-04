package nl.dtls.adminpanel.service.user;

import nl.dtls.adminpanel.api.dto.user.UserChangeDTO;
import nl.dtls.adminpanel.api.dto.user.UserCreateDTO;
import nl.dtls.adminpanel.api.dto.user.UserDTO;
import nl.dtls.adminpanel.api.dto.user.UserPasswordDTO;
import nl.dtls.adminpanel.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDTO toDTO(User user) {
        return
            new UserDTO(
                user.getUuid(),
                user.getName(),
                user.getEmail());
    }

    public User fromCreateDTO(UserCreateDTO dto, String uuid) {
        return
            new User(
                uuid,
                dto.getName(),
                dto.getEmail(),
                passwordEncoder.encode(dto.getPassword()));
    }

    public User fromChangeDTO(UserChangeDTO dto, User user) {
        return
            user
                .toBuilder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }

    public User fromPasswordDTO(UserPasswordDTO reqDto, User user) {
        return
            user
                .toBuilder()
                .passwordHash(passwordEncoder.encode(reqDto.getPassword()))
                .build();
    }
}
