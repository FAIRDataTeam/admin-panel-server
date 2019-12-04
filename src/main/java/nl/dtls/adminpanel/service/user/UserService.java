package nl.dtls.adminpanel.service.user;

import static java.lang.String.format;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import nl.dtls.adminpanel.api.dto.user.UserChangeDTO;
import nl.dtls.adminpanel.api.dto.user.UserCreateDTO;
import nl.dtls.adminpanel.api.dto.user.UserDTO;
import nl.dtls.adminpanel.api.dto.user.UserPasswordDTO;
import nl.dtls.adminpanel.database.repository.UserRepository;
import nl.dtls.adminpanel.entity.user.User;
import nl.dtls.shared.entity.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public List<UserDTO> getUsers() {
        List<User> users = userRepository.findAll();
        return
            users
                .stream()
                .map(userMapper::toDTO)
                .collect(toList());
    }

    public Optional<UserDTO> getUserByUuid(String uuid) {
        return
            userRepository
                .findByUuid(uuid)
                .map(userMapper::toDTO);
    }

    public UserDTO createUser(UserCreateDTO reqDto) {
        Optional<User> oUser = userRepository.findByEmail(reqDto.getEmail());
        if (oUser.isPresent()) {
            throw new ValidationException(format("Email '%s' is already taken", reqDto.getEmail()));
        }
        String uuid = UUID.randomUUID().toString();
        User user = userMapper.fromCreateDTO(reqDto, uuid);
        userRepository.save(user);
        return userMapper.toDTO(user);
    }

    public Optional<UserDTO> updateUser(String uuid, UserChangeDTO reqDto) {
        Optional<User> oUserEmail = userRepository.findByEmail(reqDto.getEmail());
        if (oUserEmail.isPresent() && !uuid.equals(oUserEmail.get().getUuid())) {
            throw new ValidationException(format("Email '%s' is already taken", reqDto.getEmail()));
        }
        Optional<User> oUser = userRepository.findByUuid(uuid);
        if (oUser.isEmpty()) {
            return empty();
        }
        User user = oUser.get();
        User updatedUser = userMapper.fromChangeDTO(reqDto, user);
        userRepository.save(updatedUser);
        return of(userMapper.toDTO(updatedUser));
    }

    public Optional<UserDTO> updatePassword(String uuid, UserPasswordDTO reqDto) {
        Optional<User> oUser = userRepository.findByUuid(uuid);
        if (oUser.isEmpty()) {
            return empty();
        }
        User user = oUser.get();
        User updatedUser = userMapper.fromPasswordDTO(reqDto, user);
        userRepository.save(updatedUser);
        return of(userMapper.toDTO(updatedUser));
    }

    public boolean deleteUser(String uuid) {
        Optional<User> oUser = userRepository.findByUuid(uuid);
        if (oUser.isEmpty()) {
            return false;
        }
        userRepository.delete(oUser.get());
        return true;
    }
}