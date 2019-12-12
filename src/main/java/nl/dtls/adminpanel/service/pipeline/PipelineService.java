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
package nl.dtls.adminpanel.service.pipeline;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import nl.dtls.adminpanel.api.dto.pipeline.PipelineCreateDTO;
import nl.dtls.adminpanel.api.dto.pipeline.PipelineDTO;
import nl.dtls.adminpanel.database.repository.instance.InstanceRepository;
import nl.dtls.adminpanel.database.repository.pipeline.PipelineRepository;
import nl.dtls.adminpanel.entity.exception.ValidationException;
import nl.dtls.adminpanel.entity.instance.Instance;
import nl.dtls.adminpanel.entity.pipeline.Pipeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PipelineService {

    @Autowired
    private InstanceRepository instanceRepository;

    @Autowired
    private PipelineRepository pipelineRepository;

    @Autowired
    private PipelineMapper pipelineMapper;

    public List<PipelineDTO> getPipelines() {
        return
            pipelineRepository
                .findAll()
                .stream()
                .map(pipelineMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<PipelineDTO> getPipelineByUuid(String uuid) {
        return
            pipelineRepository
                .findByUuid(uuid)
                .map(pipelineMapper::toDTO);
    }

    public PipelineDTO createPipeline(PipelineCreateDTO reqDto) {
        // Get Instance
        Optional<Instance> oInstance = instanceRepository.findByUuid(reqDto.getInstanceUuid());
        if (oInstance.isEmpty()) {
            throw new ValidationException("Instance doesn't exist");
        }
        Instance instance = oInstance.get();

        // Run pipeline
        String uuid = UUID.randomUUID().toString();
        Pipeline pipeline = pipelineMapper.fromCreateDTO(reqDto, uuid, instance);
        pipelineRepository.save(pipeline);
        return pipelineMapper.toDTO(pipeline);
    }

    public void deletePipelines() {
        pipelineRepository.deleteAll();
    }

    public boolean deletePipeline(String uuid) {
        Optional<Pipeline> oPipeline = pipelineRepository.findByUuid(uuid);
        if (oPipeline.isEmpty()) {
            return false;
        }
        pipelineRepository.delete(oPipeline.get());
        return true;
    }
}
