package org.apache.maven.configuration;

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

import org.codehaus.plexus.PlexusTestCase;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Benjamin Bentmann
 */
public class BasedirBeanConfigurationPathTranslatorTest
    extends PlexusTestCase
{
    private BasedirBeanConfigurationPathTranslator translator;

    @Override
    protected void setUp()
        throws Exception
    {
        super.setUp();

	File f = new File(".");
	translator = new BasedirBeanConfigurationPathTranslator(f);
    }

    @Override
    protected void tearDown()
        throws Exception
    {
        translator = null;

        super.tearDown();
    }

    public void testTranslatePathWithNullArgument() {
	File f = translator.translatePath(null);

	assertEquals(null, f);
    }

    public void testTranslatePathWithAbsolutePath() {
	File f = mock(File.class);
	when(f.isAbsolute()).thenReturn(true);
	assertEquals(f, translator.translatePath(f));
    }

    public void testTranslatePathWithDriveRelativePath() {
	File f = mock(File.class);
	File absoluteFile = mock(File.class);
	when(f.isAbsolute()).thenReturn(false);
	when(f.getPath()).thenReturn(File.separator);
	when(f.getAbsoluteFile()).thenReturn(absoluteFile);
	assertEquals(absoluteFile, translator.translatePath(f));
    }
}
