package org.apache.maven.project.artifact;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.metadata.ResolutionGroup;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.ArtifactRepositoryPolicy;
import org.apache.maven.artifact.resolver.filter.ExcludesArtifactFilter;
import org.apache.maven.project.artifact.DefaultMavenMetadataCache.CacheKey;
import org.apache.maven.repository.DelegatingLocalArtifactRepository;
import org.apache.maven.repository.RepositorySystem;
import org.codehaus.plexus.PlexusTestCase;

/**
 * @author Igor Fedorenko
 */
public class DefaultMavenMetadataCacheTest
    extends PlexusTestCase
{
    private RepositorySystem repositorySystem;
    private DefaultMavenMetadataCache cache;

    protected void setUp()
        throws Exception
    {
        super.setUp();
        repositorySystem = lookup( RepositorySystem.class );
        cache = new DefaultMavenMetadataCache();
    }

    @Override
    protected void tearDown()
        throws Exception
    {
        repositorySystem = null;
        super.tearDown();
    }

    public void testCacheKey()
        throws Exception
    {
        Artifact a1 = repositorySystem.createArtifact( "testGroup", "testArtifact", "1.2.3", "jar" );
        @SuppressWarnings( "deprecation" )
        ArtifactRepository lr1 = new DelegatingLocalArtifactRepository( repositorySystem.createDefaultLocalRepository() );
        ArtifactRepository rr1 = repositorySystem.createDefaultRemoteRepository();
        a1.setDependencyFilter( new ExcludesArtifactFilter( Arrays.asList( "foo" ) ) );

        Artifact a2 = repositorySystem.createArtifact( "testGroup", "testArtifact", "1.2.3", "jar" );
        @SuppressWarnings( "deprecation" )
        ArtifactRepository lr2 = new DelegatingLocalArtifactRepository( repositorySystem.createDefaultLocalRepository() );
        ArtifactRepository rr2 = repositorySystem.createDefaultRemoteRepository();
        a2.setDependencyFilter( new ExcludesArtifactFilter( Arrays.asList( "foo" ) ) );

        // sanity checks
        assertNotSame( a1, a2 );
        assertNotSame( lr1, lr2 );
        assertNotSame( rr1, rr2 );

        CacheKey k1 = cache.newCacheKey( a1, false, lr1, Collections.singletonList( rr1 ) );
        CacheKey k2 = cache.newCacheKey( a2, false, lr2, Collections.singletonList( rr2 ) );

        assertEquals(k1.hashCode(), k2.hashCode());
    }

    public void testCacheKeyWithPom()
        throws Exception
    {
        Artifact a1 = repositorySystem.createArtifact( "testGroup", "testArtifact", "1.2.3", "pom" );
        a1.setFile( new File( "tmp" ) );
        ArtifactRepository lr1 = new DelegatingLocalArtifactRepository( repositorySystem.createDefaultLocalRepository() );
        ArtifactRepository rr1 = repositorySystem.createDefaultRemoteRepository();
        a1.setDependencyFilter( new ExcludesArtifactFilter( Arrays.asList( "foo" ) ) );

        CacheKey k1 = cache.newCacheKey( a1, true, lr1, Collections.singletonList( rr1 ) );
    }

    public void testCacheKeyEquals()
        throws Exception
    {
        Artifact a1 = repositorySystem.createArtifact( "testGroup", "testArtifact", "1.2.3", "pom" );
        @SuppressWarnings( "deprecation" )
        ArtifactRepository lr1 = new DelegatingLocalArtifactRepository( repositorySystem.createDefaultLocalRepository() );
        ArtifactRepository rr1 = repositorySystem.createDefaultRemoteRepository();
        a1.setDependencyFilter( new ExcludesArtifactFilter( Arrays.asList( "foo" ) ) );
        a1.setScope( "test" );
        a1.setOptional( true );

        Artifact a2 = repositorySystem.createArtifact( "testGroup", "testArtifact", "1.2.3", "pom" );
        @SuppressWarnings( "deprecation" )
        ArtifactRepository lr2 = new DelegatingLocalArtifactRepository( repositorySystem.createDefaultLocalRepository() );
        ArtifactRepository rr2 = repositorySystem.createDefaultRemoteRepository();
        a2.setDependencyFilter( new ExcludesArtifactFilter( Arrays.asList( "foo" ) ) );
        a2.setScope( "test" );
        a2.setOptional( true );

        Artifact a3 = repositorySystem.createArtifact( "testGroup", "testArtifact", "1.2.3", "pom" );
        @SuppressWarnings( "deprecation" )
        ArtifactRepository lr3 = new DelegatingLocalArtifactRepository( repositorySystem.createDefaultLocalRepository() );
        ArtifactRepository rr3 = repositorySystem.createDefaultRemoteRepository();

        CacheKey k1 = cache.newCacheKey( a1, false, lr1, Collections.singletonList( rr1 ) );
        CacheKey k2 = cache.newCacheKey( a2, false, lr2, Collections.singletonList( rr2 ) );
        CacheKey k3 = cache.newCacheKey( a3, false, lr3, Collections.singletonList( rr3 ) );

        // Explicity use the equals() method
        assertTrue( k1.equals( k1 ) );
        assertFalse( k1.equals( new Object() ) );
        assertTrue( k1.equals( k2 ) );
        assertFalse( k1.equals( k3 ) );
    }

    public void testPutInCache()
        throws Exception
    {
        Artifact a1 = repositorySystem.createArtifact( "testGroup", "testArtifact", "1.2.3", "pom" );
        @SuppressWarnings( "deprecation" )
        ArtifactRepository lr1 = new DelegatingLocalArtifactRepository( repositorySystem.createDefaultLocalRepository() );
        ArtifactRepository rr1 = repositorySystem.createDefaultRemoteRepository();
        a1.setDependencyFilter( new ExcludesArtifactFilter( Arrays.asList( "foo" ) ) );
        a1.setScope( "test" );
        a1.setOptional( true );

        ResolutionGroup group = new ResolutionGroup( a1, new HashSet<Artifact>(), new ArrayList<ArtifactRepository>() );

        cache.put( a1, false, lr1, Collections.singletonList( rr1 ), group );

        ResolutionGroup result = cache.get( a1, false, lr1, Collections.singletonList( rr1 ) );

        assertTrue( result.getPomArtifact().equals( group.getPomArtifact() ) );
        assertTrue( result.getArtifacts().equals( group.getArtifacts() ) );
        assertTrue( result.getResolutionRepositories().equals( group.getResolutionRepositories() ) );
    }

    public void testStaleEntryWithAlwaysUpdatingPolicy()
        throws Exception
    {
        Artifact a1 = repositorySystem.createArtifact( "testGroup", "testArtifact", "1.2.3", "pom" );
        a1.setFile( new File( "tmp" ) );
        @SuppressWarnings( "deprecation" )
        ArtifactRepository lr1 = new DelegatingLocalArtifactRepository( repositorySystem.createDefaultLocalRepository() );
        ArtifactRepository rr1 = repositorySystem.createDefaultRemoteRepository();
        a1.setDependencyFilter( new ExcludesArtifactFilter( Arrays.asList( "foo" ) ) );
        a1.setScope( "test" );
        a1.setOptional( true );

        List<ArtifactRepository> remoteRepositories = new ArrayList<ArtifactRepository>();
        ArtifactRepository rr2 = repositorySystem.createDefaultRemoteRepository();
        rr2.setSnapshotUpdatePolicy( new ArtifactRepositoryPolicy( true, ArtifactRepositoryPolicy.UPDATE_POLICY_ALWAYS, ArtifactRepositoryPolicy.CHECKSUM_POLICY_IGNORE ) );
        rr2.setReleaseUpdatePolicy( new ArtifactRepositoryPolicy( true, ArtifactRepositoryPolicy.UPDATE_POLICY_ALWAYS, ArtifactRepositoryPolicy.CHECKSUM_POLICY_IGNORE ) );
        remoteRepositories.add( rr2 );
        ResolutionGroup group = new ResolutionGroup( a1, new HashSet<Artifact>(), remoteRepositories );

        cache.put( a1, false, lr1, Collections.singletonList( rr1 ), group );

        ResolutionGroup result = cache.get( a1, false, lr1, Collections.singletonList( rr1 ) );

        //assertTrue( result == null );
    }
}
