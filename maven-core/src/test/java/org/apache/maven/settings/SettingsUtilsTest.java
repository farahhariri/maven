package org.apache.maven.settings;

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

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class SettingsUtilsTest
    extends TestCase
{

    public void testShouldAppendRecessivePluginGroupIds()
    {
        Settings dominant = new Settings();
        dominant.addPluginGroup( "org.apache.maven.plugins" );
        dominant.addPluginGroup( "org.codehaus.modello" );

        Settings recessive = new Settings();
        recessive.addPluginGroup( "org.codehaus.plexus" );

        SettingsUtils.merge( dominant, recessive, Settings.GLOBAL_LEVEL );

        List<String> pluginGroups = dominant.getPluginGroups();

        assertNotNull( pluginGroups );
        assertEquals( 3, pluginGroups.size() );
        assertEquals( "org.apache.maven.plugins", pluginGroups.get( 0 ) );
        assertEquals( "org.codehaus.modello", pluginGroups.get( 1 ) );
        assertEquals( "org.codehaus.plexus", pluginGroups.get( 2 ) );
    }

    public void testConvertToSettingsProfileNullActivation()
    {
        Random entropy = new Random();
        Profile p = new Profile();
        p.setId( "id" + Long.toHexString( entropy.nextLong() ) );
        p.setActivation( null );

        Profile clone = SettingsUtils.convertToSettingsProfile( SettingsUtils.convertFromSettingsProfile( p ) );

        assertEquals( p.getId(), clone.getId() );
        assertEquals( null, clone.getActivation() );
    }

    public void testConvertToSettingsProfileNullProperty()
    {
        Random entropy = new Random();
        Profile p = new Profile();
        p.setId( "id" + Long.toHexString( entropy.nextLong() ) );
        Activation a = new Activation();
        a.setActiveByDefault( entropy.nextBoolean() );
        a.setJdk( "jdk" + Long.toHexString( entropy.nextLong() ) );
        ActivationFile af = new ActivationFile();
        af.setExists( "exists" + Long.toHexString( entropy.nextLong() ) );
        af.setMissing( "missing" + Long.toHexString( entropy.nextLong() ) );
        a.setFile( af );
        a.setProperty( null );
        ActivationOS ao = new ActivationOS();
        ao.setArch( "arch" + Long.toHexString( entropy.nextLong() ) );
        ao.setFamily( "family" + Long.toHexString( entropy.nextLong() ) );
        ao.setName( "name" + Long.toHexString( entropy.nextLong() ) );
        ao.setVersion( "version" + Long.toHexString( entropy.nextLong() ) );
        a.setOs( ao );
        p.setActivation( a );

        Profile clone = SettingsUtils.convertToSettingsProfile( SettingsUtils.convertFromSettingsProfile( p ) );

        assertEquals( p.getId(), clone.getId() );
        assertEquals( p.getActivation().getJdk(), clone.getActivation().getJdk() );
        assertEquals( p.getActivation().getFile().getExists(), clone.getActivation().getFile().getExists() );
        assertEquals( p.getActivation().getFile().getMissing(), clone.getActivation().getFile().getMissing() );
        assertEquals( null, clone.getActivation().getProperty());
        assertEquals( p.getActivation().getOs().getArch(), clone.getActivation().getOs().getArch() );
        assertEquals( p.getActivation().getOs().getFamily(), clone.getActivation().getOs().getFamily() );
        assertEquals( p.getActivation().getOs().getName(), clone.getActivation().getOs().getName() );
        assertEquals( p.getActivation().getOs().getVersion(), clone.getActivation().getOs().getVersion() );
    }

    public void testConvertToSettingsProfileNullOS()
    {
        Random entropy = new Random();
        Profile p = new Profile();
        p.setId( "id" + Long.toHexString( entropy.nextLong() ) );
        Activation a = new Activation();
        a.setActiveByDefault( entropy.nextBoolean() );
        a.setJdk( "jdk" + Long.toHexString( entropy.nextLong() ) );
        ActivationFile af = new ActivationFile();
        af.setExists( "exists" + Long.toHexString( entropy.nextLong() ) );
        af.setMissing( "missing" + Long.toHexString( entropy.nextLong() ) );
        a.setFile( af );
        ActivationProperty ap = new ActivationProperty();
        ap.setName( "name" + Long.toHexString( entropy.nextLong() ) );
        ap.setValue( "value" + Long.toHexString( entropy.nextLong() ) );
        a.setProperty( ap );
        a.setOs( null );
        p.setActivation( a );

        Profile clone = SettingsUtils.convertToSettingsProfile( SettingsUtils.convertFromSettingsProfile( p ) );

        assertEquals( p.getId(), clone.getId() );
        assertEquals( p.getActivation().getJdk(), clone.getActivation().getJdk() );
        assertEquals( p.getActivation().getFile().getExists(), clone.getActivation().getFile().getExists() );
        assertEquals( p.getActivation().getFile().getMissing(), clone.getActivation().getFile().getMissing() );
        assertEquals( p.getActivation().getProperty().getName(), clone.getActivation().getProperty().getName() );
        assertEquals( p.getActivation().getProperty().getValue(), clone.getActivation().getProperty().getValue() );
        assertEquals( null, clone.getActivation().getOs() );
    }

    public void testConvertToSettingsProfileNullFile()
    {
        Random entropy = new Random();
        Profile p = new Profile();
        p.setId( "id" + Long.toHexString( entropy.nextLong() ) );
        Activation a = new Activation();
        a.setActiveByDefault( entropy.nextBoolean() );
        a.setJdk( "jdk" + Long.toHexString( entropy.nextLong() ) );
        a.setFile( null );
        ActivationProperty ap = new ActivationProperty();
        ap.setName( "name" + Long.toHexString( entropy.nextLong() ) );
        ap.setValue( "value" + Long.toHexString( entropy.nextLong() ) );
        a.setProperty( ap );
        ActivationOS ao = new ActivationOS();
        ao.setArch( "arch" + Long.toHexString( entropy.nextLong() ) );
        ao.setFamily( "family" + Long.toHexString( entropy.nextLong() ) );
        ao.setName( "name" + Long.toHexString( entropy.nextLong() ) );
        ao.setVersion( "version" + Long.toHexString( entropy.nextLong() ) );
        a.setOs( ao );
        p.setActivation( a );

        Profile clone = SettingsUtils.convertToSettingsProfile( SettingsUtils.convertFromSettingsProfile( p ) );

        assertEquals( p.getId(), clone.getId() );
        assertEquals( p.getActivation().getJdk(), clone.getActivation().getJdk() );
        assertEquals( null, clone.getActivation().getFile() );
        assertEquals( p.getActivation().getProperty().getName(), clone.getActivation().getProperty().getName() );
        assertEquals( p.getActivation().getProperty().getValue(), clone.getActivation().getProperty().getValue() );
        assertEquals( p.getActivation().getOs().getArch(), clone.getActivation().getOs().getArch() );
        assertEquals( p.getActivation().getOs().getFamily(), clone.getActivation().getOs().getFamily() );
        assertEquals( p.getActivation().getOs().getName(), clone.getActivation().getOs().getName() );
        assertEquals( p.getActivation().getOs().getVersion(), clone.getActivation().getOs().getVersion() );
    }

    public void testRoundTripProfiles()
    {
        Random entropy = new Random();
        Profile p = new Profile();
        p.setId( "id" + Long.toHexString( entropy.nextLong() ) );
        Activation a = new Activation();
        a.setActiveByDefault( entropy.nextBoolean() );
        a.setJdk( "jdk" + Long.toHexString( entropy.nextLong() ) );
        ActivationFile af = new ActivationFile();
        af.setExists( "exists" + Long.toHexString( entropy.nextLong() ) );
        af.setMissing( "missing" + Long.toHexString( entropy.nextLong() ) );
        a.setFile( af );
        ActivationProperty ap = new ActivationProperty();
        ap.setName( "name" + Long.toHexString( entropy.nextLong() ) );
        ap.setValue( "value" + Long.toHexString( entropy.nextLong() ) );
        a.setProperty( ap );
        ActivationOS ao = new ActivationOS();
        ao.setArch( "arch" + Long.toHexString( entropy.nextLong() ) );
        ao.setFamily( "family" + Long.toHexString( entropy.nextLong() ) );
        ao.setName( "name" + Long.toHexString( entropy.nextLong() ) );
        ao.setVersion( "version" + Long.toHexString( entropy.nextLong() ) );
        a.setOs( ao );
        p.setActivation( a );
        Properties props = new Properties();
        int count = entropy.nextInt( 10 );
        for ( int i = 0; i < count; i++ )
        {
            props.setProperty( "name" + Long.toHexString( entropy.nextLong() ),
                               "value" + Long.toHexString( entropy.nextLong() ) );
        }
        p.setProperties( props );
        count = entropy.nextInt( 3 );
        List<Repository> repos = new ArrayList<Repository>();
        for ( int i = 0; i < count; i++ )
        {
            Repository r = new Repository();
            r.setId( "id" + Long.toHexString( entropy.nextLong() ) );
            r.setName( "name" + Long.toHexString( entropy.nextLong() ) );
            r.setUrl( "url" + Long.toHexString( entropy.nextLong() ) );
            repos.add( r );
        }
        p.setRepositories( repos );
        count = entropy.nextInt( 3 );
        repos = new ArrayList<Repository>();
        for ( int i = 0; i < count; i++ )
        {
            Repository r = new Repository();
            r.setId( "id" + Long.toHexString( entropy.nextLong() ) );
            r.setName( "name" + Long.toHexString( entropy.nextLong() ) );
            r.setUrl( "url" + Long.toHexString( entropy.nextLong() ) );
            repos.add( r );
        }
        p.setPluginRepositories( repos );

        Profile clone = SettingsUtils.convertToSettingsProfile( SettingsUtils.convertFromSettingsProfile( p ) );

        assertEquals( p.getId(), clone.getId() );
        assertEquals( p.getActivation().getJdk(), clone.getActivation().getJdk() );
        assertEquals( p.getActivation().getFile().getExists(), clone.getActivation().getFile().getExists() );
        assertEquals( p.getActivation().getFile().getMissing(), clone.getActivation().getFile().getMissing() );
        assertEquals( p.getActivation().getProperty().getName(), clone.getActivation().getProperty().getName() );
        assertEquals( p.getActivation().getProperty().getValue(), clone.getActivation().getProperty().getValue() );
        assertEquals( p.getActivation().getOs().getArch(), clone.getActivation().getOs().getArch() );
        assertEquals( p.getActivation().getOs().getFamily(), clone.getActivation().getOs().getFamily() );
        assertEquals( p.getActivation().getOs().getName(), clone.getActivation().getOs().getName() );
        assertEquals( p.getActivation().getOs().getVersion(), clone.getActivation().getOs().getVersion() );
        assertEquals( p.getProperties(), clone.getProperties() );
        assertEquals( p.getRepositories().size(), clone.getRepositories().size() );
        // TODO deep compare the lists
        assertEquals( p.getPluginRepositories().size(), clone.getPluginRepositories().size() );
        // TODO deep compare the lists
    }

    public void testConvertToSettingsRepositoryNonNullSnapshots()
    {
        Random entropy = new Random();
        Profile p = new Profile();
        List<Repository> repos = new ArrayList<Repository>();
        Repository r = new Repository();
        r.setId( "id" + Long.toHexString( entropy.nextLong() ) );
        r.setName( "name" + Long.toHexString( entropy.nextLong() ) );
        r.setUrl( "url" + Long.toHexString( entropy.nextLong() ) );
        RepositoryPolicy rp = new RepositoryPolicy();
        rp.setEnabled( true );
        rp.setUpdatePolicy( Long.toHexString( entropy.nextLong() ) );
        rp.setChecksumPolicy( Long.toHexString( entropy.nextLong() ) );
        r.setSnapshots( rp );
        repos.add( r );
        p.setRepositories( repos );

        Profile clone = SettingsUtils.convertToSettingsProfile( SettingsUtils.convertFromSettingsProfile( p ) );

        assertEquals( 1, clone.getRepositories().size() );
        assertEquals( p.getRepositories().get(0).getId(), clone.getRepositories().get(0).getId() );
        assertEquals( p.getRepositories().get(0).getName(), clone.getRepositories().get(0).getName() );
        assertEquals( p.getRepositories().get(0).getUrl(), clone.getRepositories().get(0).getUrl() );
        assertEquals( p.getRepositories().get(0).getSnapshots().isEnabled(), clone.getRepositories().get(0).getSnapshots().isEnabled() );
        assertEquals( p.getRepositories().get(0).getSnapshots().getUpdatePolicy(), clone.getRepositories().get(0).getSnapshots().getUpdatePolicy() );
        assertEquals( p.getRepositories().get(0).getSnapshots().getChecksumPolicy(), clone.getRepositories().get(0).getSnapshots().getChecksumPolicy() );
    }

    public void testConvertToSettingsRepositoryNonNullReleases()
    {
        Random entropy = new Random();
        Profile p = new Profile();
        List<Repository> repos = new ArrayList<Repository>();
        Repository r = new Repository();
        r.setId( "id" + Long.toHexString( entropy.nextLong() ) );
        r.setName( "name" + Long.toHexString( entropy.nextLong() ) );
        r.setUrl( "url" + Long.toHexString( entropy.nextLong() ) );
        RepositoryPolicy rp = new RepositoryPolicy();
        rp.setEnabled( true );
        rp.setUpdatePolicy( Long.toHexString( entropy.nextLong() ) );
        rp.setChecksumPolicy( Long.toHexString( entropy.nextLong() ) );
        r.setReleases( rp );
        repos.add( r );
        p.setRepositories( repos );

        Profile clone = SettingsUtils.convertToSettingsProfile( SettingsUtils.convertFromSettingsProfile( p ) );

        assertEquals( 1, clone.getRepositories().size() );
        assertEquals( p.getRepositories().get(0).getId(), clone.getRepositories().get(0).getId() );
        assertEquals( p.getRepositories().get(0).getName(), clone.getRepositories().get(0).getName() );
        assertEquals( p.getRepositories().get(0).getUrl(), clone.getRepositories().get(0).getUrl() );
        assertEquals( p.getRepositories().get(0).getReleases().isEnabled(), clone.getRepositories().get(0).getReleases().isEnabled() );
        assertEquals( p.getRepositories().get(0).getReleases().getUpdatePolicy(), clone.getRepositories().get(0).getReleases().getUpdatePolicy() );
        assertEquals( p.getRepositories().get(0).getReleases().getChecksumPolicy(), clone.getRepositories().get(0).getReleases().getChecksumPolicy() );
    }

    public void testCopySettingsNull()
    {
        assertEquals(null, SettingsUtils.copySettings( null ));
    }

    public void testCopySettingsNonNull()
    {
        Settings s = new Settings();

        Settings clone = SettingsUtils.copySettings( s );

        assertEquals( s.getActiveProfiles(), clone.getActiveProfiles() );
        assertEquals( s.isInteractiveMode(), clone.isInteractiveMode() );
        assertEquals( s.getLocalRepository(), clone.getLocalRepository() );
        assertEquals( s.getMirrors(), clone.getMirrors() );
        assertEquals( s.getModelEncoding(), clone.getModelEncoding() );
        assertEquals( s.isOffline(), clone.isOffline() );
        assertEquals( s.getPluginGroups(), clone.getPluginGroups() );
        assertEquals( s.getProfiles(), clone.getProfiles() );
        assertEquals( s.getProxies(), clone.getProxies() );
        assertEquals( s.getServers(), clone.getServers() );
        assertEquals( s.getSourceLevel(), clone.getSourceLevel() );
        assertEquals( s.isUsePluginRegistry(), clone.isUsePluginRegistry() );
    }

}
