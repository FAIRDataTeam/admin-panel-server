/**
 * The MIT License
 * Copyright © ${project.inceptionYear} DTL
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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
import nl.dtls.adminpanel.database.repository.user.UserRepository;
import nl.dtls.adminpanel.entity.exception.ValidationException;
import nl.dtls.adminpanel.entity.user.User;
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