package org.apache.maven.artifact.versioning;

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

import java.util.Locale;

import junit.framework.TestCase;

@SuppressWarnings( "unchecked" )
public class Awshi2Hariri2ComparableVersionTest
    extends TestCase
{

    private String createComparable( String version )
    {
        ComparableVersion ret = new ComparableVersion( version );
        return ret.getCanonical();
    }


    private static final String[] VERSIONS_QUALIFIER =
        { "1-alpha2snapshot", "1-alpha2", "1-alpha-123", "1-beta-2", "1-beta123", "1-m2", "1-m11", "1-rc", "1-cr2",
            "1-rc123", "1-SNAPSHOT", "1", "1-sp", "1-sp2", "1-sp123", "1-abc", "1-def", "1-pom-1", "1-1-snapshot",
            "1-1", "1-2", "1-123" };

    private static final String[] VERSIONS_NUMBER =
        { "2.0", "2-1", "2.0.a", "2.0.0.a", "2.0.2", "2.0.123", "2.1.0", "2.1-a", "2.1b", "2.1-c", "2.1-1", "2.1.0.1",
            "2.2", "2.123", "11.a2", "11.a11", "11.b2", "11.b11", "11.m2", "11.m11", "11", "11.a", "11b", "11c", "11m" };


    public void testEmptyString() {
        String emptyVersion = createComparable("");
        assertEquals( "", emptyVersion );
    }

    public void testabc() {
        String version = createComparable("abc");
        assertEquals( "abc", version );
    }

    public void testGrammar() {
        String version = createComparable(".aZ12-#$.");
        assertEquals( "0.az-12-#$", version );
    }
}
