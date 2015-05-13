/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.maven.graph;

import junit.framework.TestCase;
import org.apache.maven.execution.ProjectDependencyGraph;
import org.apache.maven.model.Dependency;
import org.apache.maven.project.DuplicateProjectException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.dag.CycleDetectedException;

import java.util.Arrays;
import java.util.List;

/**
 * @author Awshi2 Hariri2
 */
public class FilteredProjectDependencyGraphTest
    extends TestCase
{

    private final MavenProject aProject = createA();

    private final MavenProject depender1 = createProject( Arrays.asList( toDependency( aProject ) ), "depender1" );

    private final MavenProject depender2 = createProject( Arrays.asList( toDependency( aProject ) ), "depender2" );

    private final MavenProject depender3 = createProject( Arrays.asList( toDependency( aProject ) ), "depender3" );

    private final MavenProject depender4 =
        createProject( Arrays.asList( toDependency( aProject ), toDependency( depender3 ) ), "depender4" );

    private final MavenProject transitiveOnly =
        createProject( Arrays.asList( toDependency( depender3 ) ), "depender5" );

    public void testGetSortedProjectsAwshi2Hariri2()
        throws DuplicateProjectException, CycleDetectedException
    {
        ProjectDependencyGraph graph = new DefaultProjectDependencyGraph( Arrays.asList( depender1, depender2, aProject ) );
        ProjectDependencyGraph filteredGraph = new FilteredProjectDependencyGraph( graph, Arrays.asList( depender1, aProject ) );
        final List<MavenProject> sortedProjects = filteredGraph.getSortedProjects();
        assertEquals( aProject, sortedProjects.get( 0 ) );
        assertEquals( depender1, sortedProjects.get( 1 ) );
    }

    public void testConstructorWithNullArgAwshi2Hariri2()
        throws DuplicateProjectException, CycleDetectedException
    {
        boolean exceptionThrown = false;
        try {
        ProjectDependencyGraph filteredGraph = new FilteredProjectDependencyGraph( null, Arrays.asList( depender1, aProject ) );
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    public void testToStringAwshi2Hariri2()
        throws DuplicateProjectException, CycleDetectedException
    {
        ProjectDependencyGraph graph = new DefaultProjectDependencyGraph( Arrays.asList( depender1, depender2, aProject ) );
        ProjectDependencyGraph filteredGraph = new FilteredProjectDependencyGraph( graph, Arrays.asList( depender1, aProject ) );

        assertEquals( "[MavenProject: org.apache:A:1.2 @ , MavenProject: org.apache:depender1:1.2 @ ]", filteredGraph.toString() );
    }

    public void testVerifyExpectedParentStructureAwshi2Hariri2()
        throws CycleDetectedException, DuplicateProjectException
    {
        // This test verifies the baseline structure used in susequent tests. If this fails, the rest will fail.
        ProjectDependencyGraph graph = threeProjectsDependingOnASingle();
        final List<MavenProject> sortedProjects = graph.getSortedProjects();
        assertEquals( aProject, sortedProjects.get( 0 ) );
        assertEquals( depender1, sortedProjects.get( 1 ) );
        assertEquals( depender2, sortedProjects.get( 2 ) );
        assertEquals( depender3, sortedProjects.get( 3 ) );
    }

    public void testVerifyThatDownsteamProjectsComeInSortedOrderAwshi2Hariri2()
        throws CycleDetectedException, DuplicateProjectException
    {
        final List<MavenProject> downstreamProjects =
            threeProjectsDependingOnASingle().getDownstreamProjects( aProject, true );
        assertEquals( depender1, downstreamProjects.get( 0 ) );
        assertEquals( depender2, downstreamProjects.get( 1 ) );
        assertEquals( depender3, downstreamProjects.get( 2 ) );
    }

    public void testGetDownstreamProjectsWithNullInputAwshi2Hariri2()
        throws CycleDetectedException, DuplicateProjectException
    {
        boolean exceptionThrown = false;
        try {
            List<MavenProject> downstreamProjects =
                threeProjectsDependingOnASingle().getDownstreamProjects( null, true );
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    public void testGetUpstreamProjectsWithNullInputAwshi2Hariri2()
        throws CycleDetectedException, DuplicateProjectException
    {
        boolean exceptionThrown = false;
        try {
            List<MavenProject> downstreamProjects =
                threeProjectsDependingOnASingle().getUpstreamProjects( null, true );
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    public void testTransitivesInOrderAwshi2Hariri2()
        throws CycleDetectedException, DuplicateProjectException
    {
        final ProjectDependencyGraph dgraph =
            new DefaultProjectDependencyGraph( Arrays.asList( depender1, depender4, depender2, depender3, aProject ) );
        final ProjectDependencyGraph graph =
            new FilteredProjectDependencyGraph( dgraph, Arrays.asList( depender1, depender4, depender2, depender3, aProject ) );

        final List<MavenProject> downstreamProjects = graph.getDownstreamProjects( aProject, true );
        assertEquals( depender1, downstreamProjects.get( 0 ) );
        assertEquals( depender3, downstreamProjects.get( 1 ) );
        assertEquals( depender4, downstreamProjects.get( 2 ) );
        assertEquals( depender2, downstreamProjects.get( 3 ) );
    }

    public void testNonTransitivesInOrderAwshi2Hariri2()
        throws CycleDetectedException, DuplicateProjectException
    {
        final ProjectDependencyGraph dgraph =
            new DefaultProjectDependencyGraph( Arrays.asList( depender1, depender4, depender2, depender3, aProject ) );
        final ProjectDependencyGraph graph =
            new FilteredProjectDependencyGraph( dgraph, Arrays.asList( depender1, depender4, depender2, depender3, aProject ) );

        final List<MavenProject> downstreamProjects = graph.getDownstreamProjects( aProject, false );
        assertEquals( depender1, downstreamProjects.get( 0 ) );
        assertEquals( depender3, downstreamProjects.get( 1 ) );
        assertEquals( depender4, downstreamProjects.get( 2 ) );
        assertEquals( depender2, downstreamProjects.get( 3 ) );
    }

    public void testWithTranistiveOnlyAwshi2Hariri2()
        throws CycleDetectedException, DuplicateProjectException
    {
        final ProjectDependencyGraph dgraph = new DefaultProjectDependencyGraph(
            Arrays.asList( depender1, transitiveOnly, depender2, depender3, aProject ) );
        final ProjectDependencyGraph graph = new FilteredProjectDependencyGraph(
                                                                                dgraph, Arrays.asList( depender1, transitiveOnly, depender2, depender3, aProject ) );

        final List<MavenProject> downstreamProjects = graph.getDownstreamProjects( aProject, true );
        assertEquals( depender1, downstreamProjects.get( 0 ) );
        assertEquals( depender3, downstreamProjects.get( 1 ) );
        assertEquals( transitiveOnly, downstreamProjects.get( 2 ) );
        assertEquals( depender2, downstreamProjects.get( 3 ) );
    }

    public void testWithMissingTranistiveOnlyAwshi2Hariri2()
        throws CycleDetectedException, DuplicateProjectException
    {
        final ProjectDependencyGraph dgraph = new DefaultProjectDependencyGraph(
            Arrays.asList( depender1, transitiveOnly, depender2, depender3, aProject ) );
        final ProjectDependencyGraph graph = new FilteredProjectDependencyGraph(
                                                                                dgraph, Arrays.asList( depender1, transitiveOnly, depender2, depender3, aProject ) );

        final List<MavenProject> downstreamProjects = graph.getDownstreamProjects( aProject, false );
        assertEquals( depender1, downstreamProjects.get( 0 ) );
        assertEquals( depender3, downstreamProjects.get( 1 ) );
        assertEquals( depender2, downstreamProjects.get( 2 ) );
    }

    public void testGetUpstreamProjectsAwshi2Hariri2()
        throws CycleDetectedException, DuplicateProjectException
    {
        ProjectDependencyGraph graph = threeProjectsDependingOnASingle();
        final List<MavenProject> downstreamProjects = graph.getUpstreamProjects( depender1, true );
        assertEquals( aProject, downstreamProjects.get( 0 ) );
    }

    private ProjectDependencyGraph threeProjectsDependingOnASingle()
        throws CycleDetectedException, DuplicateProjectException
    {
        ProjectDependencyGraph graph = new DefaultProjectDependencyGraph( Arrays.asList( depender1, depender2, depender3, depender4, aProject ) );
                                                                           return new FilteredProjectDependencyGraph( graph, Arrays.asList( depender1, depender2, depender3, aProject ) );
    }

    private static MavenProject createA()
    {
        MavenProject result = new MavenProject();
        result.setGroupId( "org.apache" );
        result.setArtifactId( "A" );
        result.setVersion( "1.2" );
        return result;
    }

    static Dependency toDependency( MavenProject mavenProject )
    {
        final Dependency dependency = new Dependency();
        dependency.setArtifactId( mavenProject.getArtifactId() );
        dependency.setGroupId( mavenProject.getGroupId() );
        dependency.setVersion( mavenProject.getVersion() );
        return dependency;
    }

    private static MavenProject createProject( List<Dependency> dependencies, String artifactId )
    {
        MavenProject result = new MavenProject();
        result.setGroupId( "org.apache" );
        result.setArtifactId( artifactId );
        result.setVersion( "1.2" );
        result.setDependencies( dependencies );
        return result;
    }

}
