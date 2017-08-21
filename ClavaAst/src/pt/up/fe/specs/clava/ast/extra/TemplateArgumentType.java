/**
 * Copyright 2017 SPeCS.
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

package pt.up.fe.specs.clava.ast.extra;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import pt.up.fe.specs.clava.ClavaNode;
import pt.up.fe.specs.clava.ClavaNodeInfo;

public class TemplateArgumentType extends TemplateArgument {

    private final List<String> type;

    public TemplateArgumentType(List<String> type, ClavaNodeInfo nodeInfo) {
        super(nodeInfo, Collections.emptyList());

        this.type = type;
    }

    @Override
    protected ClavaNode copyPrivate() {
        return new TemplateArgumentType(type, getInfo());
    }

    public List<String> getType() {
        return type;
    }

    @Override
    public String toContentString() {
        return getType().stream().collect(Collectors.joining(":"));
    }

    @Override
    public String getCode() {
        return type.get(0);
    }
}
