/**
 * Copyright 2018 SPeCS.
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

package pt.up.fe.specs.clava.ast;

import java.util.Arrays;

import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.clava.ClavaNode;
import pt.up.fe.specs.clava.ast.decl.DummyDecl;
import pt.up.fe.specs.clava.ast.decl.legacy.DummyDeclLegacy;
import pt.up.fe.specs.clava.ast.expr.Expr;
import pt.up.fe.specs.clava.ast.stmt.ExprStmt;
import pt.up.fe.specs.clava.ast.stmt.legacy.ExprStmtLegacy;

/**
 * Utility class with methods that need to take into account both nodes that use ClavaData and legacy nodes.
 * 
 * @author JoaoBispo
 *
 */
public class ClavaNodesLegacy {

    // public static CXXFunctionalCastExpr cxxFunctionCastExpr(CastExpr castExpr) {
    // ClavaNodeFactory.cxxFunctionalCastExpr("bool", castExpr.getCastKind(), castExpr.getExprData(),
    // castExpr.getInfo(), subExpr);
    // }

    public static ExprStmt exprStmt(Expr expr) {
        return exprStmt(true, expr);
    }

    public static ExprStmt exprStmt(boolean hasSemicolon, Expr expr) {
        if (expr.hasDataI()) {
            // TODO: Replace with factory?

            DataStore exprStmtData = DataStore.newInstance("ExprStmt")
                    .put(ExprStmt.HAS_SEMICOLON, hasSemicolon);

            return new ExprStmt(exprStmtData, Arrays.asList(expr));
        }

        if (hasSemicolon) {
            return new ExprStmtLegacy(expr.getInfo(), expr);
        } else {
            return new ExprStmtLegacy(false, expr.getInfo(), expr);
        }

    }

    public static DummyDecl dummyDecl(ClavaNode node) {
        if (node.hasDataI()) {
            DataStore dummyData = node.getDataI().copy()
                    .put(DummyNode.DUMMY_CONTENT, node.getClass().getSimpleName());

            return new DummyDecl(dummyData, node.getChildren());
        }

        return new DummyDeclLegacy(node.toContentString(), node.getInfo(), node.getChildren());
    }

}
