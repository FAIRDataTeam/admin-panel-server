package nl.dtls.adminpanel.service.instance;

import static java.lang.String.format;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import nl.dtls.adminpanel.api.dto.instance.InstanceChangeDTO;
import nl.dtls.adminpanel.api.dto.instance.InstanceDTO;
import nl.dtls.adminpanel.database.repository.application.ApplicationRepository;
import nl.dtls.adminpanel.database.repository.instance.InstanceRepository;
import nl.dtls.adminpanel.database.repository.server.ServerRepository;
import nl.dtls.adminpanel.entity.application.Application;
import nl.dtls.adminpanel.entity.exception.ValidationException;
import nl.dtls.adminpanel.entity.instance.Instance;
import nl.dtls.adminpanel.entity.instance.InstanceStatus;
import nl.dtls.adminpanel.entity.server.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class InstanceService {

    @Autowired
    private InstanceRepository instanceRepository;

    @Autowired
    private ServerRepository serverRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private InstanceMapper instanceMapper;

    @Autowired
    private RestTemplate restTemplate;

    public List<InstanceDTO> getInstances() {
        return
            instanceRepository
                .findAll()
                .stream()
                .parallel()
                .map(i -> instanceMapper.toDTO(i, computeInstanceStatus(i)))
                .collect(toList());
    }

    public Optional<InstanceDTO> getInstanceByUuid(String uuid) {
        return
            instanceRepository
                .findByUuid(uuid)
                .map(i -> instanceMapper.toDTO(i, computeInstanceStatus(i)));
    }

    public InstanceDTO createInstance(InstanceChangeDTO reqDto) {
        String uuid = UUID.randomUUID().toString();
        Server server = serverRepository.findByUuid(reqDto.getServerUuid()).get();
        Application application = applicationRepository.findByUuid(reqDto.getApplicationUuid())
            .get();
        Instance instance = instanceMapper.fromCreateDTO(reqDto, uuid, server, application);
        instanceRepository.save(instance);
        return instanceMapper.toDTO(instance, InstanceStatus.NOT_RUNNING);
    }

    public Optional<InstanceDTO> cloneInstance(String source) {
        Optional<Instance> oInstance = instanceRepository.findByUuid(source);
        if (oInstance.isEmpty()) {
            return empty();
        }
        String uuid = UUID.randomUUID().toString();
        Instance instance = oInstance.get();
        Instance clonedInstance = instance
            .toBuilder()
            .id(null)
            .uuid(uuid)
            .name(format("Copy of %s", instance.getName()))
            .build();
        instanceRepository.save(clonedInstance);
        return of(instanceMapper.toDTO(clonedInstance, InstanceStatus.NOT_RUNNING));
    }

    public Optional<InstanceDTO> updateInstance(String uuid, InstanceChangeDTO reqDto) {
        Optional<Instance> oInstance = instanceRepository.findByUuid(uuid);
        if (oInstance.isEmpty()) {
            return empty();
        }
        Optional<Server> oServer = serverRepository.findByUuid(reqDto.getServerUuid());
        if (oServer.isEmpty()) {
            throw new ValidationException("Server doesn't exist");
        }
        Optional<Application> oApplication = applicationRepository
            .findByUuid(reqDto.getApplicationUuid());
        if (oApplication.isEmpty()) {
            throw new ValidationException("Application doesn't exist");
        }

        Instance instance = oInstance.get();
        Server server = oServer.get();
        Application application = oApplication.get();
        Instance updatedInstance = instanceMapper
            .fromChangeDTO(reqDto, instance, server, application);
        instanceRepository.save(updatedInstance);
        return of(instanceMapper.toDTO(updatedInstance, computeInstanceStatus(updatedInstance)));
    }

    public boolean deleteInstance(String uuid) {
        Optional<Instance> oInstance = instanceRepository.findByUuid(uuid);
        if (oInstance.isEmpty()) {
            return false;
        }
        instanceRepository.delete(oInstance.get());
        return true;
    }

    private InstanceStatus computeInstanceStatus(Instance instance) {
        try {
            doHttpCall(instance.getUrl());
            return InstanceStatus.RUNNING;
        } catch (ResourceAccessException e) {
            return InstanceStatus.NOT_RUNNING;
        } catch (HttpClientErrorException e) {
            log.info("Instance {} (http: {}, status: {})", instance.getUrl(),
                e.getStatusCode().toString(), InstanceStatus.ERROR);
            return InstanceStatus.ERROR;
        }
    }

    private ResponseEntity<String> doHttpCall(String url) {
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        if (response.getStatusCode() == HttpStatus.FOUND
            || response.getStatusCode() == HttpStatus.MOVED_PERMANENTLY) {
            List<String> locations = response.getHeaders().get(HttpHeaders.LOCATION);
            if (locations != null && locations.size() == 1) {
                return doHttpCall(locations.get(0));
            }
        }
        return response;
    }
}
