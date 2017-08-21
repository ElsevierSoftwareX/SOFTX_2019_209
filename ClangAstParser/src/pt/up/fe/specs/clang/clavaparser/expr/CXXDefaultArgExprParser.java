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

package pt.up.fe.specs.clang.clavaparser.expr;

import com.google.common.base.Preconditions;

import pt.up.fe.specs.clang.ast.ClangNode;
import pt.up.fe.specs.clang.clavaparser.AClangNodeParser;
import pt.up.fe.specs.clang.clavaparser.ClangConverterTable;
import pt.up.fe.specs.clang.clavaparser.utils.ClangGenericParsers;
import pt.up.fe.specs.clava.ast.ClavaNodeFactory;
import pt.up.fe.specs.clava.ast.expr.CXXDefaultArgExpr;
import pt.up.fe.specs.clava.ast.expr.data.ValueKind;
import pt.up.fe.specs.clava.ast.type.Type;
import pt.up.fe.specs.util.stringparser.StringParser;

public class CXXDefaultArgExprParser extends AClangNodeParser<CXXDefaultArgExpr> {

    public CXXDefaultArgExprParser(ClangConverterTable converter) {
        super(converter);
    }

    @Override
    public CXXDefaultArgExpr parse(ClangNode node, StringParser parser) {
        // Examples:
        // 'const class std::allocator<char>':'const class std::allocator<char>' lvalue
        //

        Type type = parser.apply(ClangGenericParsers::parseClangType, node, getTypesMap());
        ValueKind valueKind = parser.apply(ClangGenericParsers::parseValueKind);

        Preconditions.checkArgument(node.getNumChildren() == 0, "Do not expect children");

        return ClavaNodeFactory.cxxDefaultArgExpr(valueKind, type, info(node));
    }

}
