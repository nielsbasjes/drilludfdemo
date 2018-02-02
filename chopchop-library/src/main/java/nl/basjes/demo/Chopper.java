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

import java.util.ArrayList;
import java.util.List;

public class Chopper {

    /**
     * This is a function that chops a string into pieces.
     * Intended behaviour:
     * Input: "FooBar"
     * Output: { "F", "Fo", "Foo", "FooB", "FooBa", "FooBar" }
     * This has only one use: Something to demonstrate.
     */
    public static List<String> chopChop(String input) {
        List<String> result = new ArrayList<>(input.length());
        for (int i = 1 ; i <= input.length() ; i ++ ) {
            result.add(input.substring(0, i));
        }
        return result;
    }

    /**
     * This is a function that add the concatenated string.
     * Intended behaviour:
     * Input: { "Foo", "Bar" }
     * Output: { "Foo", "Bar", "FooBar" }
     * This has only one use: Something to demonstrate.
     */
    public static List<String> glueGlue(List<String> input) {
        List<String> result = new ArrayList<>(input);
        StringBuilder sb = new StringBuilder();
        for (String string: input) {
            sb.append(string);
        }
        result.add(sb.toString());
        return result;
    }

    public static long howLong(String input) {
        try {
            return Long.parseLong(input);
        } catch (NumberFormatException nfe) {
            return 42;
        }
    }

}
