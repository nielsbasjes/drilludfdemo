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
package nl.basjes.demo;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestChopChop {

    @Test
    public void testChopChop() {
        String input = "FooBar";
        List<String> output = Chopper.chopChop(input);
        assertEquals(6, output.size());
        assertTrue(output.contains("F"));
        assertTrue(output.contains("Fo"));
        assertTrue(output.contains("Foo"));
        assertTrue(output.contains("FooB"));
        assertTrue(output.contains("FooBa"));
        assertTrue(output.contains("FooBar"));
    }

    @Test
    public void testGlueGlue(){
        String[] input = { "Foo", "Bar" };
        List<String> output = Chopper.glueGlue(Arrays.asList(input));
        assertEquals(3, output.size());
        assertTrue(output.contains("Foo"));
        assertTrue(output.contains("Bar"));
        assertTrue(output.contains("FooBar"));
    }

    @Test
    public void testHowLong(){
        assertEquals(-3, Chopper.howLong("-3"));
        assertEquals(3, Chopper.howLong("3"));
        assertEquals(42, Chopper.howLong("FooBar"));
    }

}
