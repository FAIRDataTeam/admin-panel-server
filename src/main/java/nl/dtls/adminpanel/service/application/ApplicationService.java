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
