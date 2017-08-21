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

package pt.up.fe.specs.clava.weaver;

import java.util.Optional;

import com.google.common.base.Preconditions;

import pt.up.fe.specs.clava.ClavaNode;
import pt.up.fe.specs.clava.ClavaNodes;
import pt.up.fe.specs.clava.ast.ClavaNodeFactory;
import pt.up.fe.specs.clava.ast.stmt.CompoundStmt;
import pt.up.fe.specs.clava.ast.stmt.Stmt;
import pt.up.fe.specs.clava.weaver.abstracts.ACxxWeaverJoinPoint;
import pt.up.fe.specs.clava.weaver.abstracts.joinpoints.AJoinPoint;
import pt.up.fe.specs.clava.weaver.abstracts.joinpoints.AStatement;
import pt.up.fe.specs.util.treenode.NodeInsertUtils;

/**
 * Class with utility methods related with weaver actions.
 * 
 * @author JoaoBispo
 *
 */
public class CxxActions {

    /**
     * Insert children from a C/C++ token before/after the reference token, when the token is a statement of is further
     * below in the AST.
     * 
     * <p>
     * Minimum granularity level of insert before/after is at the statement level.
     * 
     * @param target
     * @param position
     * @param from
     */
    public static void insertAsStmt(ClavaNode target, String code, Insert insert, CxxWeaver weaver) {

        switch (insert) {
        case BEFORE:
            NodeInsertUtils.insertBefore(getValidStatement(target), ClavaNodeFactory.literalStmt(code));
            break;

        case AFTER:
            NodeInsertUtils.insertAfter(getValidStatement(target), ClavaNodeFactory.literalStmt(code));
            break;

        case AROUND:
        case REPLACE:
            weaver.clearUserField(target);
            NodeInsertUtils.replace(target, ClavaNodes.toLiteral(code, null, target));
            break;
        default:
            throw new RuntimeException("Case not defined:" + insert);
        }
    }

    public static void insertAsChild(String position, ClavaNode base, ClavaNode node, CxxWeaver weaver) {

        switch (position) {
        case "before":
            // Insert before all statements in body
            base.addChild(0, node);
            break;

        case "after":
            base.addChild(node);
            break;

        case "around":
        case "replace":
            removeChildren(base, weaver);
            // // Clear use fields
            // for (ClavaNode child : base.getChildren()) {
            // weaver.clearUserField(child);
            // }
            // // Remove all children
            // base.removeChildren(0, base.getNumChildren());
            base.addChild(node);
            break;
        default:
            throw new RuntimeException("Case not defined:" + position);
        }
    }

    // public static void replace(ClavaNode newNode, ClavaNode target, String position) {
    //
    // }

    public static void replace(ClavaNode target, ClavaNode newNode, CxxWeaver weaver) {
        weaver.clearUserField(target);
        NodeInsertUtils.replace(target, newNode);
    }

    public static AJoinPoint insertBefore(AJoinPoint baseJp, AJoinPoint newJp) {
        Stmt newStmt = ClavaNodes.toStmt(newJp.getNode());
        Stmt baseStmt = getValidStatement(baseJp.getNode());
        NodeInsertUtils.insertBefore(baseStmt, newStmt);

        // return new CxxStatement(newStmt, null);
        return CxxJoinpoints.create(newStmt, baseJp);
    }

    public static AJoinPoint insertAfter(AJoinPoint baseJp, AJoinPoint newJp) {
        Stmt newStmt = ClavaNodes.toStmt(newJp.getNode());
        Stmt baseStmt = getValidStatement(baseJp.getNode());
        NodeInsertUtils.insertAfter(baseStmt, newStmt);

        return CxxJoinpoints.create(newStmt, baseJp);
        // return new CxxStatement(newStmt, null);
    }

    /**
     * Returns the first valid statement where we can insert another node in the after/before inserts
     * 
     * @param node
     * @return
     */
    public static Stmt getValidStatement(ClavaNode node) {
        Optional<Stmt> stmt = ClavaNodes.getStatement(node);

        if (!stmt.isPresent()) {
            throw new RuntimeException("Node does not have a statement ancestor:\n" + node);
        }

        return stmt.get();
    }

    public static AJoinPoint insertJpAsStatement(AJoinPoint baseJp, AJoinPoint newJp, String position,
            CxxWeaver weaver) {

        ACxxWeaverJoinPoint parent = newJp.getHasParentImpl() ? (ACxxWeaverJoinPoint) newJp.getParentImpl() : null;
        AStatement stmtJp = CxxJoinpoints.create(ClavaNodes.toStmt(newJp.getNode()), parent,
                AStatement.class);
        // CxxStatement stmtJp = new CxxStatement(ClavaNodes.toStmt(newJp.getNode()), parent);

        return insertJp(baseJp, stmtJp, position, weaver);
    }

    /**
     * Generic implementation that just directly inserts/replaced the node in the joinpoint.
     * 
     * @param baseJp
     * @param newJpS
     * @param position
     */
    public static AJoinPoint insertJp(AJoinPoint baseJp, AJoinPoint newJp, String position, CxxWeaver weaver) {

        switch (position) {
        case "before":
            NodeInsertUtils.insertBefore(baseJp.getNode(), newJp.getNode());
            break;

        case "after":
            NodeInsertUtils.insertAfter(baseJp.getNode(), newJp.getNode());
            break;

        case "around":
        case "replace":
            weaver.clearUserField(baseJp.getNode());
            NodeInsertUtils.replace(baseJp.getNode(), newJp.getNode());
            break;

        default:
            throw new RuntimeException("Case not defined:" + position);
        }

        return newJp;
    }

    public static void insertStmt(String position, Stmt body, Stmt stmt, CxxWeaver weaver) {
        Preconditions.checkArgument(body instanceof CompoundStmt);
        switch (position) {
        case "before":
            // Insert before all statements in body
            body.addChild(0, stmt);
            break;

        case "after":
            body.addChild(stmt);
            break;

        case "around":
        case "replace":
            // Remove all children
            removeChildren(body, weaver);
            // Add given statement
            body.addChild(stmt);
            break;
        default:
            throw new RuntimeException("Case not defined:" + position);
        }
    }

    public static void removeChildren(ClavaNode node, CxxWeaver weaver) {
        // Clear use fields
        for (ClavaNode child : node.getChildren()) {
            weaver.clearUserField(child);
        }

        // Remove all children
        node.removeChildren(0, node.getNumChildren());
    }
}
