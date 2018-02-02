/*
 * Trying to create a custom Drill function
 * Copyright (C)2018 Niels Basjes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.basjes.demo.chopchop.drill;

import io.netty.buffer.DrillBuf;
import org.apache.drill.common.config.DrillConfig;
import org.apache.drill.exec.expr.DrillSimpleFunc;
import org.apache.drill.exec.expr.annotations.FunctionTemplate;
import org.apache.drill.exec.expr.holders.NullableVarCharHolder;
import org.apache.drill.exec.memory.BufferAllocator;
import org.apache.drill.exec.memory.RootAllocatorFactory;
import org.apache.drill.exec.store.TestOutputMutator;
import org.apache.drill.exec.vector.complex.impl.ComplexWriterImpl;
import org.apache.drill.exec.vector.complex.writer.BaseWriter;
import org.apache.drill.test.BaseDirTestWatcher;
import org.apache.drill.test.ClusterFixture;
import org.apache.drill.test.ClusterFixtureBuilder;
import org.apache.drill.test.ClusterTest;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TestChopChopFunction extends ClusterTest {

    private static final Logger LOG = LoggerFactory.getLogger(TestChopChopFunction.class);

    private static final String TEST_INPUT =
        "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.82 Safari/537.36";
    private static final String EXPECTED_DEVICE_CLASS = "Desktop";
    private static final String EXPECTED_AGENT_NAME_VERSION = "Chrome 48.0.2564.82";

    @ClassRule
    public static final BaseDirTestWatcher DIR_TEST_WATCHER = new BaseDirTestWatcher();

    @BeforeClass
    public static void setup() throws Exception {
        ClusterFixtureBuilder builder = ClusterFixture.builder(DIR_TEST_WATCHER)
            .configProperty("drill.classpath.scanning.cache.enabled", false);
        startCluster(builder);
    }

    // -------------------------------------------------------------------------------------------------------

    @Test
    public void testAnnotation() {
        Class<? extends DrillSimpleFunc> fnClass = ChopChopFunction.class;
        FunctionTemplate fnDefn = fnClass.getAnnotation(FunctionTemplate.class);
        assertNotNull(fnDefn);
        assertEquals("chop_chop", fnDefn.name());
        assertEquals(FunctionTemplate.FunctionScope.SIMPLE, fnDefn.scope());
        assertEquals(FunctionTemplate.NullHandling.NULL_IF_NULL, fnDefn.nulls());
    }

//    @Test
//    public void testDirect() {
//        ChopChopFunction chopChopFunction = new ChopChopFunction();
//        chopChopFunction.setup();
//
//        // FIXME: Desperately trying to find the right way to set the test useragent value for the NullableVarCharHolder
//        DrillConfig c = DrillConfig.create();
//        BufferAllocator allocator = RootAllocatorFactory.newRoot(c);
//        TestOutputMutator mutator = new TestOutputMutator(allocator);
//        DrillBuf buffer = mutator.getManagedBuffer();
//        buffer.setBytes(0, ByteBuffer.wrap(TEST_INPUT.getBytes()));
//
//        chopChopFunction.useragent = new NullableVarCharHolder();
//        chopChopFunction.useragent.buffer = buffer;
//
//        chopChopFunction.outWriter = new ComplexWriterImpl("anv", null /* new MapVector() */);
//
//        chopChopFunction.eval();
//
//        assertTrue(chopChopFunction.allFields.contains("DeviceClass"));
//        assertTrue(chopChopFunction.allFields.contains("AgentNameVersion"));
//
//        LOG.info("XXXX"+ chopChopFunction.outWriter.rootAsMap().varChar("DeviceClass"));
//
//        BaseWriter.MapWriter mapWriter = chopChopFunction.outWriter.rootAsMap();
//        assertEquals(EXPECTED_DEVICE_CLASS,       mapWriter.varChar("DeviceClass").toString());
//        assertEquals(EXPECTED_AGENT_NAME_VERSION, mapWriter.varChar("AgentNameVersion").toString());
//    }

    @Test
    public void testChopChop() throws Exception {
        final String query = "" +
            "SELECT chop_chop('FooBar') " +
            "AS chop1 "+//, chop2, chop3, chop4, chop5, chop6 " +
            "FROM (values(1))";
        testBuilder().sqlQuery(query).ordered()
            .baselineColumns("chop1")//, "chop2", "chop3", "chop4", "chop5", "chop6")
            .baselineValues("F")//, "Fo", "Foo", "FooB", "FooBa", "FooBar")
            .go();
    }

    @Test
    public void testHowLong() throws Exception {
        final String query = "select how_long('1234') as howlong from (values(1))";
        testBuilder().sqlQuery(query).ordered().baselineColumns("howlong").baselineValues(Long.parseLong("1234")).go();
    }

}
