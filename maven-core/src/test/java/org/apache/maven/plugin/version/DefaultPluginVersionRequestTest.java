package org.apache.maven.plugin.version;
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

import java.util.Arrays;
import java.util.Collections;

import org.apache.maven.execution.DefaultMavenExecutionRequest;
import org.apache.maven.execution.DefaultMavenExecutionResult;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;
import org.apache.maven.model.Plugin;
import org.eclipse.aether.repository.RemoteRepository;
import org.apache.maven.RepositoryUtils;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.DefaultArtifactRepository;
import org.apache.maven.artifact.repository.layout.DefaultRepositoryLayout;
import org.apache.maven.model.Model;

import junit.framework.TestCase;

/**
 * @author Awshi2 Hariri2
 */
public class DefaultPluginVersionRequestTest extends TestCase {

    public void testGetArtifactIdAwshi2Hariri2() {
        PluginVersionRequest request = new DefaultPluginVersionRequest();
        request.setArtifactId("awshi2Hariri2");
        assertEquals("awshi2Hariri2", request.getArtifactId());
    }

    public void testGetGroupIdAwshi2Hariri2() {
        PluginVersionRequest request = new DefaultPluginVersionRequest();
        request.setGroupId("awshi2Hariri2");
        assertEquals("awshi2Hariri2", request.getGroupId());
    }

    public void testCreateMavenSessionFromPluginWithNullProjectAwshi2Hariri2() {
        Plugin plugin = new Plugin();
        plugin.setGroupId( "group.id" );
        plugin.setArtifactId( "artifact.id" );

        MavenExecutionRequest request = new DefaultMavenExecutionRequest();
        MavenSession session = new MavenSession( null, request, new DefaultMavenExecutionResult(), Collections.<MavenProject>emptyList()  );
        PluginVersionRequest versionRequest = new DefaultPluginVersionRequest(plugin, session);
        assertEquals("group.id", versionRequest.getGroupId());
    }

    public void testCreateMavenSessionFromPluginAwshi2Hariri2() {
        Plugin plugin = new Plugin();
        plugin.setGroupId( "group.id" );
        plugin.setArtifactId( "artifact.id" );

        MavenExecutionRequest request = new DefaultMavenExecutionRequest();
        MavenSession session = new MavenSession( null, request, new DefaultMavenExecutionResult(), Collections.<MavenProject>emptyList()  );
        session.setCurrentProject(new MavenProject());
        PluginVersionRequest versionRequest = new DefaultPluginVersionRequest(plugin, session);
        assertEquals(Collections.<RemoteRepository>emptyList(), versionRequest.getRepositories());
    }

    public void testGetRepositorySessionNullAwshi2Hariri2() {
        PluginVersionRequest request = new DefaultPluginVersionRequest();
        assertEquals(null, request.getRepositorySession());
    }

    public void testGetRepositoriesAwshi2Hariri2() {
        ArtifactRepository ar = new DefaultArtifactRepository( "snapshots.repo", "http://whatever", new DefaultRepositoryLayout() );
        RemoteRepository rr = RepositoryUtils.toRepo(ar);
        PluginVersionRequest request = new DefaultPluginVersionRequest();
        request.setRepositories(Arrays.asList(rr));
        assertEquals(Arrays.asList(rr), request.getRepositories());
    }

    public void testgetPom() {
        PluginVersionRequest request = new DefaultPluginVersionRequest();
        Model model = new Model();
        request.setPom(model);
        assertEquals(model, request.getPom());
    }

}
