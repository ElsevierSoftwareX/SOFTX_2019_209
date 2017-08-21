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

package pt.up.fe.specs.clava.weaver.joinpoints;

import java.util.Arrays;
import java.util.List;

import pt.up.fe.specs.clava.ClavaNode;
import pt.up.fe.specs.clava.ast.expr.BinaryOperator;
import pt.up.fe.specs.clava.ast.expr.BinaryOperator.BinaryOperatorKind;
import pt.up.fe.specs.clava.ast.expr.CompoundAssignOperator;
import pt.up.fe.specs.clava.weaver.CxxJoinpoints;
import pt.up.fe.specs.clava.weaver.abstracts.ACxxWeaverJoinPoint;
import pt.up.fe.specs.clava.weaver.abstracts.joinpoints.ABinaryOp;
import pt.up.fe.specs.clava.weaver.abstracts.joinpoints.AExpression;
import pt.up.fe.specs.clava.weaver.abstracts.joinpoints.AJoinPoint;

public class CxxBinaryOp extends ABinaryOp {

    private final BinaryOperator op;
    private final ACxxWeaverJoinPoint parent;

    public CxxBinaryOp(BinaryOperator op, ACxxWeaverJoinPoint parent) {
        super(new CxxExpression(op, parent));

        this.op = op;
        this.parent = parent;
    }

    @Override
    public ACxxWeaverJoinPoint getParentImpl() {
        return parent;
    }

    @Override
    public ClavaNode getNode() {
        return op;
    }

    @Override
    public String getKindImpl() {
        return op.getOp().name().toLowerCase();
    }

    @Override
    public List<? extends AExpression> selectLeft() {
        return Arrays.asList((AExpression) CxxJoinpoints.create(op.getLhs(), this));
    }

    @Override
    public List<? extends AExpression> selectRight() {
        return Arrays.asList((AExpression) CxxJoinpoints.create(op.getRhs(), this));
    }

    @Override
    public AJoinPoint getLeftImpl() {
        List<? extends AExpression> left = selectLeft();
        return left.isEmpty() ? null : left.get(0);
    }

    @Override
    public AJoinPoint getRightImpl() {
        List<? extends AExpression> right = selectRight();
        return right.isEmpty() ? null : right.get(0);
    }

    @Override
    public Boolean getIsAssignmentImpl() {
        return op.getOp() == BinaryOperatorKind.ASSIGN || op instanceof CompoundAssignOperator;
    }
}
