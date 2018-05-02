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

package pt.up.fe.specs.clava.ast.expr;

import java.util.Collection;
import java.util.Optional;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.clava.ClavaNode;
import pt.up.fe.specs.clava.ClavaNodeInfo;
import pt.up.fe.specs.clava.ClavaNodes;
import pt.up.fe.specs.clava.ast.expr.data.ExprData;
import pt.up.fe.specs.clava.ast.expr.enums.ExprUse;
import pt.up.fe.specs.clava.ast.expr.enums.ObjectKind;
import pt.up.fe.specs.clava.ast.expr.enums.ValueKind;
import pt.up.fe.specs.clava.ast.type.Type;
import pt.up.fe.specs.clava.utils.Typable;

/**
 * Represents an expression.
 * 
 * @author JoaoBispo
 *
 */
public abstract class Expr extends ClavaNode implements Typable {

    /// DATAKEYS BEGIN

    public final static DataKey<Type> TYPE = KeyFactory.object("type", Type.class);

    public final static DataKey<ValueKind> VALUE_KIND = KeyFactory.enumeration("valueKind", ValueKind.class)
            .setDefault(() -> ValueKind.getDefault());

    public final static DataKey<ObjectKind> OBJECT_KIND = KeyFactory.enumeration("objectKind", ObjectKind.class)
            .setDefault(() -> ObjectKind.ORDINARY);

    public final static DataKey<ImplicitCastExpr> IMPLICIT_CAST = KeyFactory
            .object("implicitCast", ImplicitCastExpr.class);

    /// DATAKEYS END

    private ExprData exprData;
    private ImplicitCastExpr implicitCast;

    public Expr(DataStore data, Collection<? extends ClavaNode> children) {
        super(data, children);
    }

    /**
     * For legacy.
     * 
     * @param exprData
     * @param info
     * @param children
     */
    public Expr(ExprData exprData, ClavaNodeInfo info, Collection<? extends ClavaNode> children) {
        super(info, children);

        this.exprData = exprData;
        this.implicitCast = null;
    }

    @Override
    public Type getType() {
        if (hasDataI()) {
            return getDataI().get(TYPE);
        }

        // System.out.println(getClass().getSimpleName() + ": Legacy");
        return exprData.getType();
    }

    @Override
    public void setType(Type type) {
        if (hasDataI()) {
            getDataI().put(TYPE, type);
            return;
        }

        this.exprData = new ExprData(type, exprData.getValueKind());

    }

    public Optional<Type> getExprTypeTry() {
        return Optional.ofNullable(getType());
    }

    public Type getExprType() {
        return getType();
    }

    public ValueKind getValueKind() {
        if (hasDataI()) {
            return getDataI().get(VALUE_KIND);
        }

        return exprData.getValueKind();
    }

    public ExprData getExprData() {
        if (hasDataI()) {
            throw new RuntimeException("This is a ClavaData node, .getExprData should not be used");
        }
        return exprData;
    }

    @Override
    public String toContentString() {
        if (hasDataI()) {
            return super.toContentString();
        }

        return ClavaNode.toContentString(super.toContentString(), "exprData: [" + exprData + "]");
        // return ClavaNode.toContentString(super.toContentString(), "types:" + getExprType().getCode() + ", valueKind:"
        // + getValueKind() + ", exprData: [" + exprData + "]");
        // return super.toContentString() + "types:" + getExprType().getCode() + ", valueKind:" + getValueKind();
    }

    /**
     * 
     * @return 'read' if the value in the expression is read, 'write' if the value the expression represents is written,
     *         or 'readwrite' if is both read, and written
     */
    public ExprUse use() {
        return ClavaNodes.use(this);
    }

    /**
     * 
     * @return true if the expression is used inside a function call
     */
    public boolean isFunctionArgument() {
        CallExpr functionCall = getAncestorTry(CallExpr.class).orElse(null);

        if (functionCall == null) {
            return false;
        }

        return functionCall.getArgs().stream()
                .flatMap(ClavaNode::getDescendantsAndSelfStream)
                .filter(descendant -> descendant == this)
                .findFirst().isPresent();

    }

    public void setImplicitCast(ImplicitCastExpr implicitCast) {
        if (hasDataI()) {
            getDataI().put(IMPLICIT_CAST, implicitCast);
            return;
        }

        this.implicitCast = implicitCast;
    }

    public Optional<ImplicitCastExpr> getImplicitCast() {
        if (hasDataI()) {
            return Optional.ofNullable(getDataI().get(IMPLICIT_CAST));
        }

        return Optional.ofNullable(implicitCast);
    }

}
