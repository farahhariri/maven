package org.apache.maven.model.profile;

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

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.Profile;
import org.apache.maven.model.building.ModelBuildingRequest;
import org.apache.maven.model.building.ModelProblemCollector;

import org.codehaus.plexus.PlexusTestCase;

public class DefaultProfileInjectorTest
    //extends PlexusTestCase
{

    /*public void testMNG5539()
    {
        DefaultProfileInjector profileInjector = new DefaultProfileInjector();

        Model model = new Model();
        Build modelBuild = new Build();
        List<Plugin> modelPlugins = new ArrayList<Plugin>();
        Plugin p1 = new Plugin();
        p1.setGroupId( "group1" );
        p1.setArtifactId( "artifact1" );
        modelPlugins.add(p1);
        Plugin p2 = new Plugin();
        p2.setGroupId( "group2" );
        p2.setArtifactId( "artifact2" );
        modelPlugins.add(p2);
        modelBuild.setPlugins(modelPlugins);
        model.setBuild( modelBuild );

        Profile profile = new Profile();
        Build profileBuild = new Build();
        List<Plugin> profilePlugins = new ArrayList<Plugin>();
        p2 = new Plugin();
        p2.setGroupId( "group2" );
        p2.setArtifactId( "artifact2" );
        profilePlugins.add(p2);
        p1 = new Plugin();
        p1.setGroupId( "group1" );
        p1.setArtifactId( "artifact1" );
        profilePlugins.add(p1);
        profileBuild.setPlugins(profilePlugins);
        profile.setBuild( profileBuild );

        // These parameters are not used, so they can be null
        ModelBuildingRequest request = null;
        ModelProblemCollector problems = null;

        profileInjector.injectProfile( model, profile, request, problems );

        // Need to ensure the plugins are in the same order as in the profile
        List<Plugin> resultPlugins = model.getBuild().getPlugins();
        assertTrue( resultPlugins.size() == 2 );
        assertTrue( resultPlugins.get( 0 ).getGroupId().equals( "group2" ) );
        assertTrue( resultPlugins.get( 0 ).getArtifactId().equals( "artifact2" ) );
        assertTrue( resultPlugins.get( 1 ).getGroupId().equals( "group1" ) );
        assertTrue( resultPlugins.get( 1 ).getArtifactId().equals( "artifact1" ) );
    }*/
}
