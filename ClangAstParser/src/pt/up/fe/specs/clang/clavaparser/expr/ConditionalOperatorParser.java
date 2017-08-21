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

import java.util.List;

import pt.up.fe.specs.clang.ast.ClangNode;
import pt.up.fe.specs.clang.clavaparser.AClangNodeParser;
import pt.up.fe.specs.clang.clavaparser.ClangConverterTable;
import pt.up.fe.specs.clang.clavaparser.utils.ClangDataParsers;
import pt.up.fe.specs.clava.ClavaNode;
import pt.up.fe.specs.clava.ast.ClavaNodeFactory;
import pt.up.fe.specs.clava.ast.expr.ConditionalOperator;
import pt.up.fe.specs.clava.ast.expr.Expr;
import pt.up.fe.specs.clava.ast.expr.data.ExprData;
import pt.up.fe.specs.util.stringparser.StringParser;

/**
 * Parser for the '?:' ternary operator.
 * 
 * @author Jo�o Bispo
 *
 */
public class ConditionalOperatorParser extends AClangNodeParser<ConditionalOperator> {

    public ConditionalOperatorParser(ClangConverterTable converter) {
        super(converter);
    }

    @Override
    protected ConditionalOperator parse(ClangNode node, StringParser parser) {

        // Examples:
        //
        // 'int'

        ExprData exprData = parser.apply(ClangDataParsers::parseExpr, node, getTypesMap());

        List<ClavaNode> children = parseChildren(node);

        // CHECK: Always three children?
        checkNumChildren(children, 3);

        List<Expr> exprs = toExpr(children);

        return ClavaNodeFactory.conditionalOperator(exprData, node.getInfo(), exprs.get(0), exprs.get(1), exprs.get(2));

    }

}
