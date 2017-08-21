/**
 * Copyright 2016 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.clava.ast.comment;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import pt.up.fe.specs.clava.ClavaNode;
import pt.up.fe.specs.clava.ClavaNodeInfo;

public class MultiLineComment extends Comment {

    private final List<String> lines;

    public MultiLineComment(List<String> lines, ClavaNodeInfo nodeInfo) {
        super(nodeInfo, Collections.emptyList());

        this.lines = lines;
    }

    @Override
    protected ClavaNode copyPrivate() {
        return new MultiLineComment(lines, getInfo());
    }

    @Override
    public String getText() {
        return lines.stream().collect(Collectors.joining(ln()));
    }

    @Override
    public String getCode() {
        // return lines.stream().collect(Collectors.joining("\n", "/*", "*/"));
        return "/*" + getText() + "*/";
    }

    public List<String> getLines() {
        return lines;
    }

}
