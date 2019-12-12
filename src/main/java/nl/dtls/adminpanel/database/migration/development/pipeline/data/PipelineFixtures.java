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
package nl.dtls.adminpanel.database.migration.development.pipeline.data;

import nl.dtls.adminpanel.database.migration.development.instance.data.InstanceFixtures;
import nl.dtls.adminpanel.database.repository.instance.InstanceRepository;
import nl.dtls.adminpanel.entity.instance.Instance;
import nl.dtls.adminpanel.entity.pipeline.Pipeline;
import nl.dtls.adminpanel.entity.pipeline.PipelineStatus;
import nl.dtls.adminpanel.entity.pipeline.PipelineType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PipelineFixtures {

    @Autowired
    private InstanceFixtures instanceFixtures;

    @Autowired
    private InstanceRepository instanceRepository;

    public Pipeline pipeline() {
        Instance instance = instanceRepository.findByUuid(instanceFixtures.fdpStaging().getUuid())
            .get();
        return new Pipeline(
            "052cb344-7d02-4074-996b-681ecf1d43b1",
            PipelineType.DEPLOY,
            PipelineStatus.DONE,
            "1. Creating directory - started\n1. Creating directory - ended\n2. Copy binary files"
                + " - started\n2. Copy binary files - ended",
            instance);
    }

}
