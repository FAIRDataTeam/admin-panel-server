package nl.dtls.adminpanel.service.application;

import static java.lang.String.format;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import nl.dtls.adminpanel.api.dto.application.ApplicationChangeDTO;
import nl.dtls.adminpanel.api.dto.application.ApplicationDTO;
import nl.dtls.adminpanel.database.repository.application.ApplicationRepository;
import nl.dtls.adminpanel.database.repository.instance.InstanceRepository;
import nl.dtls.adminpanel.entity.application.Application;
import nl.dtls.adminpanel.service.instance.InstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private InstanceRepository instanceRepository;

    @Autowired
    private InstanceService instanceService;

    @Autowired
    private ApplicationMapper applicationMapper;

    public List<ApplicationDTO> getApplications() {
        List<Application> applications = applicationRepository.findAll();
        return
            applications
                .stream()
                .map(applicationMapper::toDTO)
                .collect(toList());
    }

    public Optional<ApplicationDTO> getApplicationByUuid(String uuid) {
        return
            applicationRepository
                .findByUuid(uuid)
                .map(applicationMapper::toDTO);
    }

    public ApplicationDTO createApplication(ApplicationChangeDTO reqDto) {
        String uuid = UUID.randomUUID().toString();
        Application application = applicationMapper.fromCreateDTO(reqDto, uuid);
        applicationRepository.save(application);
        return applicationMapper.toDTO(application);
    }

    public Optional<ApplicationDTO> cloneApplication(String source) {
        Optional<Application> oApplication = applicationRepository.findByUuid(source);
        if (oApplication.isEmpty()) {
            return empty();
        }
        String uuid = UUID.randomUUID().toString();
        Application application = oApplication.get();
        Application clonedApplication = application
            .toBuilder()
            .id(null)
            .uuid(uuid)
            .name(format("Copy of %s", application.getName()))
            .build();
        applicationRepository.save(clonedApplication);
        return of(applicationMapper.toDTO(clonedApplication));
    }

    public Optional<ApplicationDTO> updateApplication(String uuid, ApplicationChangeDTO reqDto) {
        Optional<Application> oApplication = applicationRepository.findByUuid(uuid);
        if (oApplication.isEmpty()) {
            return empty();
        }
        Application application = oApplication.get();
        Application updatedInstance = applicationMapper.fromChangeDTO(reqDto, application);
        applicationRepository.save(updatedInstance);
        return of(applicationMapper.toDTO(updatedInstance));
    }

    public boolean deleteApplication(String uuid) {
        Optional<Application> oApplication = applicationRepository.findByUuid(uuid);
        if (oApplication.isEmpty()) {
            return false;
        }
        Application application = oApplication.get();
        instanceRepository.findByApplication(application)
            .forEach(i -> instanceService.deleteInstance(i.getUuid()));
        applicationRepository.delete(application);
        return true;
    }
}
