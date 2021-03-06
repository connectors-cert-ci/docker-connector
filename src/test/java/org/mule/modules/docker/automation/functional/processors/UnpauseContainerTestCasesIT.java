/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.docker.automation.functional.processors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.docker.DockerConnector;
import org.mule.modules.docker.automation.util.TestsConstants;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;

public class UnpauseContainerTestCasesIT extends AbstractTestCase<DockerConnector> {

    CreateContainerResponse createContainerResponse = null;

    public UnpauseContainerTestCasesIT() {
        super(DockerConnector.class);
    }

    @Before
    public void setup() {
        createContainerResponse = getConnector().runContainer(TestsConstants.IMAGE_NAME, TestsConstants.IMAGE_TAG, TestsConstants.UNPAUSE_CONTAINER, TestsConstants.COMMAND);
    }

    @After
    public void tearDown() {
        try {
            getConnector().killContainer(createContainerResponse.getId(), TestsConstants.KILL_CONTAINER_SIGNAL);
        } catch (Exception e) {
        } finally {
            getConnector().deleteContainer(createContainerResponse.getId(), true, true);
        }
    }

    @Test
    public void verify() {
        assertNotNull(createContainerResponse.getId());
        getConnector().pauseContainer(createContainerResponse.getId());
        getConnector().unpauseContainer(createContainerResponse.getId());
        InspectContainerResponse inspect = getConnector().inspectContainer(createContainerResponse.getId(), false);
        assertFalse(inspect.getState().getPaused());
        assertTrue(inspect.getState().getRunning());
    }

}