package org.apache.maven.toolchain;


/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.execution.DefaultMavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.project.MavenProject;
import org.apache.maven.toolchain.model.ToolchainModel;
import org.codehaus.plexus.logging.Logger;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class Awshi2Hariri2DefaultToolchainManagerTest {

    @Mock
    private Logger logger;

    @InjectMocks
    private DefaultToolchainManager toolchainManager;

    @Mock
    private ToolchainFactory toolchainFactory_basicType;

    @Mock
    private ToolchainFactory toolchainFactory_rareType;

    @Before
    public void onSetup() throws Exception
    {
        toolchainManager = new DefaultToolchainManager();

        MockitoAnnotations.initMocks( this );

        toolchainManager.factories = new HashMap<String, ToolchainFactory>();
        toolchainManager.factories.put("basic", toolchainFactory_basicType);
        toolchainManager.factories.put("rare", toolchainFactory_rareType);
    }

    @Test
    public void testT1S1R3() {
        String type = null;
        MavenSession session = null;

        Toolchain result = toolchainManager.getToolchainFromBuildContext(type, session);
        assertNull(result);
    }

    @Test
    public void testT1S2R2() {
        String type = null;
        MavenSession session = mock(MavenSession.class);

        // Set up session such that type is not in its context
        Map<String, Object> context = new HashMap<String, Object>();

        // Setting up mock
        MavenProject project = mock(MavenProject.class);
        PluginDescriptor desc = new PluginDescriptor();
        desc.setGroupId(PluginDescriptor.getDefaultPluginGroupId());
        desc.setArtifactId(PluginDescriptor.getDefaultPluginArtifactId("toolchains"));
        when(session.getCurrentProject()).thenReturn(project);
        when(session.getPluginContext(desc, project)).thenReturn(context);

        Toolchain result = toolchainManager.getToolchainFromBuildContext(type, session);
        assertNull(result);
    }

    @Test
    public void testT2S1R3() {
        String type = "";
        MavenSession session = null;

        Toolchain result = toolchainManager.getToolchainFromBuildContext(type, session);
        assertNull(result);
    }

    @Test
    public void testT2S2R1() {
        String type = "";
        MavenSession session = mock(MavenSession.class);

        // Set up session such that type is in its context
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("toolchain-", new ToolchainModel());

        // Setting up mock
        MavenProject project = mock(MavenProject.class);
        PluginDescriptor desc = new PluginDescriptor();
        desc.setGroupId(PluginDescriptor.getDefaultPluginGroupId());
        desc.setArtifactId(PluginDescriptor.getDefaultPluginArtifactId("toolchains"));
        when(session.getCurrentProject()).thenReturn(project);
        when(session.getPluginContext(desc, project)).thenReturn(context);

        Toolchain result = toolchainManager.getToolchainFromBuildContext(type, session);
        verify(logger).error("Missing toolchain factory for type: . Possibly caused by misconfigured project.");
    }

    @Test
    public void testT2S2R2() {
        String type = "";
        MavenSession session = mock(MavenSession.class);

        // Set up session such that type is not in its context
        Map<String, Object> context = new HashMap<String, Object>();

        // Setting up mock
        MavenProject project = mock(MavenProject.class);
        PluginDescriptor desc = new PluginDescriptor();
        desc.setGroupId(PluginDescriptor.getDefaultPluginGroupId());
        desc.setArtifactId(PluginDescriptor.getDefaultPluginArtifactId("toolchains"));
        when(session.getCurrentProject()).thenReturn(project);
        when(session.getPluginContext(desc, project)).thenReturn(context);

        Toolchain result = toolchainManager.getToolchainFromBuildContext(type, session);
        assertNull(result);
    }

    @Test
    public void testT3S1R3() {
        String type = "basic";
        MavenSession session = null;

        Toolchain result = toolchainManager.getToolchainFromBuildContext(type, session);
        assertNull(result);
    }

    @Test
    public void testT3S2R1() {
        String type = "basic";
        MavenSession session = mock(MavenSession.class);

        // Set up session such that type is in its context
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("toolchain-basic", new ToolchainModel());

        // Setting up mock
        MavenProject project = mock(MavenProject.class);
        PluginDescriptor desc = new PluginDescriptor();
        desc.setGroupId(PluginDescriptor.getDefaultPluginGroupId());
        desc.setArtifactId(PluginDescriptor.getDefaultPluginArtifactId("toolchains"));
        when(session.getCurrentProject()).thenReturn(project);
        when(session.getPluginContext(desc, project)).thenReturn(context);

        Toolchain result = toolchainManager.getToolchainFromBuildContext(type, session);
        assertNull(result);
    }

    @Test
    public void testT3S2R2() {
        String type = "basic";
        MavenSession session = mock(MavenSession.class);

        // Set up session such that type is not in its context
        Map<String, Object> context = new HashMap<String, Object>();

        // Setting up mock
        MavenProject project = mock(MavenProject.class);
        PluginDescriptor desc = new PluginDescriptor();
        desc.setGroupId(PluginDescriptor.getDefaultPluginGroupId());
        desc.setArtifactId(PluginDescriptor.getDefaultPluginArtifactId("toolchains"));
        when(session.getCurrentProject()).thenReturn(project);
        when(session.getPluginContext(desc, project)).thenReturn(context);

        Toolchain result = toolchainManager.getToolchainFromBuildContext(type, session);
        assertNull(result);
    }
}
