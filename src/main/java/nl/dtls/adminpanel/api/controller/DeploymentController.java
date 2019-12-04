package nl.dtls.adminpanel.api.controller;

import java.io.IOException;
import nl.dtls.adminpanel.service.deployment.DeploymentService;
import nl.dtls.shared.entity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/instances/{uuid}/deployments")
public class DeploymentController {

    @Autowired
    private DeploymentService deploymentService;

    @RequestMapping(method = RequestMethod.POST)
    public HttpEntity postDeployments(@PathVariable final String uuid)
        throws IOException, ResourceNotFoundException {
        deploymentService.deploy(uuid);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public HttpEntity deleteDeployments(@PathVariable final String uuid)
        throws IOException, ResourceNotFoundException {
        deploymentService.dispose(uuid);
        return ResponseEntity.noContent().build();
    }

}
