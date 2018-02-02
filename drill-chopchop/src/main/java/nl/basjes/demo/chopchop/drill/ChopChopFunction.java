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

import nl.basjes.demo.Chopper;
import org.apache.drill.exec.expr.DrillSimpleFunc;
import org.apache.drill.exec.expr.annotations.FunctionTemplate;
import org.apache.drill.exec.expr.annotations.Output;
import org.apache.drill.exec.expr.annotations.Param;
import org.apache.drill.exec.expr.annotations.Workspace;
import org.apache.drill.exec.expr.holders.NullableVarCharHolder;
import org.apache.drill.exec.vector.complex.writer.BaseWriter;

import javax.inject.Inject;
import java.util.List;

@FunctionTemplate(
        name="chop_chop",
        scope= FunctionTemplate.FunctionScope.SIMPLE,
        nulls = FunctionTemplate.NullHandling.NULL_IF_NULL
)
public class ChopChopFunction implements DrillSimpleFunc {

    @Param
    NullableVarCharHolder input;

    @Output
    BaseWriter.ComplexWriter outWriter;

    @Inject
    io.netty.buffer.DrillBuf outBuffer;

    public void setup() {
    }

    public void eval() {
        org.apache.drill.exec.vector.complex.writer.BaseWriter.MapWriter queryMapWriter = outWriter.rootAsMap();

        String inputString = org.apache.drill.exec.expr.fn.impl.StringFunctionHelpers.getStringFromVarCharHolder(input);

        if (inputString.isEmpty() || inputString.equals("null")) {
            return;// Nothing to do
        }

        List<String> output = Chopper.chopChop(inputString);

        for (String outputValue: output) {
            org.apache.drill.exec.expr.holders.VarCharHolder rowHolder = new org.apache.drill.exec.expr.holders.VarCharHolder();


            byte[] rowStringBytes = outputValue.getBytes();
            outBuffer.reallocIfNeeded(rowStringBytes.length);
            outBuffer.setBytes(0, rowStringBytes);

            rowHolder.start = 0;
            rowHolder.end = rowStringBytes.length;
            rowHolder.buffer = outBuffer;

            queryMapWriter.varChar(outputValue).write(rowHolder);
        }
    }
}
