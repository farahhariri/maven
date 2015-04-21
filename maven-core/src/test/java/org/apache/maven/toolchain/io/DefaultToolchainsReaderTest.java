package org.apache.maven.toolchain.io;

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
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.Before;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashMap;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Map;

public class DefaultToolchainsReaderTest {

    private DefaultToolchainsReader reader;
    private Map<String, Boolean> options;
    private String IS_STRICT = "org.apache.maven.toolchains.io.isStrict";
    @Before
    public void onSetup() throws Exception
    {
	reader = new DefaultToolchainsReader();
	options = new HashMap<String, Boolean>();
	options.put(IS_STRICT, true);
    }
    @Test(expected=IllegalArgumentException.class)
    public void testReadWithNullReaderInput() throws IOException {
	Reader input = null;
	reader.read(input, options);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testReadWithNullInputStreamInput() throws IOException {
	InputStream input = null;
	reader.read(input, options);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testReadWithNullFileInput() throws IOException {
	File input = null;
	reader.read(input, options);
    }

    @Test(expected=ToolchainsParseException.class)
    public void testReadWithEmptyByteArrayInputStreamInput() throws IOException {
	byte[] bytes = new byte[1024];
	InputStream input = new ByteArrayInputStream(bytes);
	reader.read(input, options);
    }

    @Test(expected=ToolchainsParseException.class)
    public void testReadWithEmptyReaderInput() throws IOException {
	Reader input = new StringReader("whatever");
	reader.read(input, options);
    }

    @Test(expected=FileNotFoundException.class)
    public void testReadWithEmptyFileInput() throws IOException {
	File input = new File("whatever");
	reader.read(input, options);
    }

}
