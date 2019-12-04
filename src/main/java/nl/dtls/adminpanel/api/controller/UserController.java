package nl.dtls.adminpanel.api.controller;

import static java.lang.String.format;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import nl.dtls.adminpanel.api.dto.user.UserChangeDTO;
import nl.dtls.adminpanel.api.dto.user.UserCreateDTO;
import nl.dtls.adminpanel.api.dto.user.UserDTO;
import nl.dtls.adminpanel.api.dto.user.UserPasswordDTO;
import nl.dtls.shared.entity.exception.ResourceNotFoundException;
import nl.dtls.adminpanel.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<UserDTO> dto = userService.getUsers();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserCreateDTO reqDto) {
        UserDTO dto = userService.createUser(reqDto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<UserDTO> getUser(@PathVariable final String uuid)
        throws ResourceNotFoundException {
        Optional<UserDTO> oDto = userService.getUserByUuid(uuid);
        if (oDto.isPresent()) {
            return new ResponseEntity<>(oDto.get(), HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException(format("User '%s' doesn't exist", uuid));
        }
    }

    @RequestMapping(value = "/{uuid}", method = RequestMethod.PUT)
    public ResponseEntity<UserDTO> putUser(@PathVariable final String uuid,
        @RequestBody @Valid UserChangeDTO reqDto) throws ResourceNotFoundException {
        Optional<UserDTO> oDto = userService.updateUser(uuid, reqDto);
        if (oDto.isPresent()) {
            return new ResponseEntity<>(oDto.get(), HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException(format("User '%s' doesn't exist", uuid));
        }
    }

    @RequestMapping(value = "/{uuid}/password", method = RequestMethod.PUT)
    public ResponseEntity<UserDTO> putUserPassword(@PathVariable final String uuid,
        @RequestBody @Valid UserPasswordDTO reqDto) throws ResourceNotFoundException {
        Optional<UserDTO> oDto = userService.updatePassword(uuid, reqDto);
        if (oDto.isPresent()) {
            return new ResponseEntity<>(oDto.get(), HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException(format("User '%s' doesn't exist", uuid));
        }
    }

    @RequestMapping(value = "/{uuid}", method = RequestMethod.DELETE)
    public HttpEntity deleteUser(@PathVariable final String uuid)
        throws ResourceNotFoundException {
        boolean result = userService.deleteUser(uuid);
        if (result) {
            return ResponseEntity.noContent().build();
        } else {
            throw new ResourceNotFoundException(format("User '%s' doesn't exist", uuid));
        }
    }

}
