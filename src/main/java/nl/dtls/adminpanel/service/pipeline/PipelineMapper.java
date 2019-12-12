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

import nl.dtls.adminpanel.api.dto.pipeline.PipelineCreateDTO;
import nl.dtls.adminpanel.api.dto.pipeline.PipelineDTO;
import nl.dtls.adminpanel.entity.instance.Instance;
import nl.dtls.adminpanel.entity.pipeline.Pipeline;
import nl.dtls.adminpanel.entity.pipeline.PipelineStatus;
import nl.dtls.adminpanel.service.instance.InstanceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PipelineMapper {

    @Autowired
    private InstanceMapper instanceMapper;

    public PipelineDTO toDTO(Pipeline pipeline) {
        return new PipelineDTO(pipeline.getUuid(), pipeline.getType(), pipeline.getStatus(),
            pipeline.getLog(), pipeline.getCreated(), pipeline.getDuration(),
            instanceMapper.toSimpleDTO(pipeline.getInstance()));
    }

    public Pipeline fromCreateDTO(PipelineCreateDTO reqDto, String uuid, Instance instance) {
        return new Pipeline(uuid, reqDto.getType(), PipelineStatus.QUEUED, "", instance);
    }

}
