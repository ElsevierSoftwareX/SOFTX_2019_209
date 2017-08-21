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

package pt.up.fe.specs.clang.clavaparser.type;

import java.util.List;

import pt.up.fe.specs.clang.ast.ClangNode;
import pt.up.fe.specs.clang.clavaparser.AClangNodeParser;
import pt.up.fe.specs.clang.clavaparser.ClangConverterTable;
import pt.up.fe.specs.clang.clavaparser.utils.ClangDataParsers;
import pt.up.fe.specs.clava.ClavaNode;
import pt.up.fe.specs.clava.ast.ClavaNodeFactory;
import pt.up.fe.specs.clava.ast.type.ParenType;
import pt.up.fe.specs.clava.ast.type.Type;
import pt.up.fe.specs.clava.ast.type.data.TypeData;
import pt.up.fe.specs.util.stringparser.StringParser;

public class ParenTypeParser extends AClangNodeParser<ParenType> {

    public ParenTypeParser(ClangConverterTable converter) {
        super(converter);
    }

    @Override
    protected ParenType parse(ClangNode node, StringParser parser) {
        // Examples:
        //
        // '__ostream_type &(__ostream_type &)' sugar

        TypeData typeData = parser.apply(ClangDataParsers::parseType);

        List<ClavaNode> children = parseChildren(node);
        checkNumChildren(children, 1);

        Type innerType = toType(children.get(0));

        return ClavaNodeFactory.parenType(typeData, node.getInfo(), innerType);
    }

}
