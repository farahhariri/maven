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

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * @author Igor Fedorenko
 */
public class DefaultMavenMetadataCacheTest
    extends PlexusTestCase
{
    private RepositorySystem repositorySystem;
    private DefaultMavenMetadataCache cache;

    private Artifact a1;
    private Artifact a2;

    private ArtifactRepository lr1;
    private ArtifactRepository rr1;
    private ArtifactRepository lr2;
    private ArtifactRepository rr2;

    private File pomFile = new File( "tmp" );

    protected void setUp()
        throws Exception
    {
        super.setUp();
        repositorySystem = lookup( RepositorySystem.class );
        cache = new DefaultMavenMetadataCache();

        // Setup the artifacts we are going to be use between many tests
        a1 = repositorySystem.createArtifact( "testGroup", "testArtifact", "1.2.3", "jar" );
        lr1 = new DelegatingLocalArtifactRepository( repositorySystem.createDefaultLocalRepository() );
        rr1 = repositorySystem.createDefaultRemoteRepository();
        a1.setDependencyFilter( new ExcludesArtifactFilter( Arrays.asList( "foo" ) ) );

        a2 = repositorySystem.createArtifact( "testGroup", "testArtifact", "1.2.3", "jar" );
        lr2 = new DelegatingLocalArtifactRepository( repositorySystem.createDefaultLocalRepository() );
        rr2 = repositorySystem.createDefaultRemoteRepository();
        a2.setDependencyFilter( new ExcludesArtifactFilter( Arrays.asList( "foo" ) ) );
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
        // Change type to pom and add a file
        a1 = repositorySystem.createArtifact( "testGroup", "testArtifact", "1.2.3", "pom" );
        a1.setFile( pomFile );

        CacheKey k1 = cache.newCacheKey( a1, true, lr1, Collections.singletonList( rr1 ) );
        // Only need to assert that we don't fail in having a file and having type pom, also increases coverage
    }

    public void testCacheKeyEquals()
        throws Exception
    {
        // Artifacts a1 and a2 should have different scope/optional, different from a3
        a1.setScope( "test" );
        a1.setOptional( true );
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
        // First check if no cache record exists
        ResolutionGroup result = cache.get( a1, false, lr1, Collections.singletonList( rr1 ) );
        assertTrue( result == null );

        // Actually put in the cache
        ResolutionGroup group = new ResolutionGroup( a1, new HashSet<Artifact>(), new ArrayList<ArtifactRepository>() );
        cache.put( a1, false, lr1, Collections.singletonList( rr1 ), group );
        result = cache.get( a1, false, lr1, Collections.singletonList( rr1 ) );

        assertTrue( result.getPomArtifact().equals( group.getPomArtifact() ) );
        assertTrue( result.getArtifacts().equals( group.getArtifacts() ) );
        assertTrue( result.getResolutionRepositories().equals( group.getResolutionRepositories() ) );
    }

    public void testStaleEntryWithAlwaysUpdatingPolicy()
        throws Exception
    {
        File spyFile = spy( pomFile );
        when( spyFile.canRead() ).thenReturn( false );  // Cannot read, but has repositories with always update policy
        when( spyFile.length() ).thenReturn( 1L );
        when( spyFile.lastModified() ).thenReturn( 1L );
        a1.setFile( spyFile );

        // Add remote repository with always update policy
        List<ArtifactRepository> remoteRepositories = new ArrayList<ArtifactRepository>();
        ArtifactRepository remoteRepo = repositorySystem.createDefaultRemoteRepository();
        remoteRepo.setSnapshotUpdatePolicy( new ArtifactRepositoryPolicy( true, ArtifactRepositoryPolicy.UPDATE_POLICY_ALWAYS, ArtifactRepositoryPolicy.CHECKSUM_POLICY_IGNORE ) );
        remoteRepo.setReleaseUpdatePolicy( new ArtifactRepositoryPolicy( true, ArtifactRepositoryPolicy.UPDATE_POLICY_ALWAYS, ArtifactRepositoryPolicy.CHECKSUM_POLICY_IGNORE ) );
        remoteRepositories.add( remoteRepo );
        ResolutionGroup group = new ResolutionGroup( a1, new HashSet<Artifact>(), remoteRepositories );

        cache.put( a1, false, lr1, Collections.singletonList( rr1 ), group );
        ResolutionGroup result = cache.get( a1, false, lr1, Collections.singletonList( rr1 ) );

        assertTrue( result == null );
    }

    public void testStaleEntryWithModifedPomFile()
        throws Exception
    {
        File spyFile = spy( pomFile );
        when( spyFile.canRead() ).thenReturn( true );   // Set readable file
        when( spyFile.length() ).thenReturn( 1L );
        when( spyFile.lastModified() ).thenReturn( 1L );
        a1.setFile( spyFile );

        ResolutionGroup group = new ResolutionGroup( a1, new HashSet<Artifact>(), new ArrayList<ArtifactRepository>() );
        cache.put( a1, false, lr1, Collections.singletonList( rr1 ), group );

        // "Touch" the file to make it not stale
        when( spyFile.length() ).thenReturn( 2L );
        when( spyFile.lastModified() ).thenReturn( 2L );

        ResolutionGroup result = cache.get( a1, false, lr1, Collections.singletonList( rr1 ) );

        assertTrue( result == null );
    }

    public void testFlush()
        throws Exception
    {
        ResolutionGroup group = new ResolutionGroup( a1, new HashSet<Artifact>(), new ArrayList<ArtifactRepository>() );

        cache.put( a1, false, lr1, Collections.singletonList( rr1 ), group );
        cache.flush();

        ResolutionGroup result = cache.get( a1, false, lr1, Collections.singletonList( rr1 ) );

        assertTrue( result == null );
    }
}
