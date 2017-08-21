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

package pt.up.fe.specs.clava.ast.type;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import pt.up.fe.specs.clava.ClavaNode;
import pt.up.fe.specs.clava.ClavaNodeInfo;
import pt.up.fe.specs.clava.ast.type.data.ArrayTypeData;
import pt.up.fe.specs.clava.ast.type.data.TypeData;

/**
 * Represents a C array with a specified constant size.
 *
 * <p>
 * For multiple dimensions there will be nested {@link ConstantArrayType} nodes. Top-most nodes represent outermost
 * dimensions of the array. E.g., int [3][4] will have dimension [4] as topmost node, them [3], then int.
 *
 * <p>
 * In this regard, the AST is opposite to what Clang does (topmost nodes represent innermost dimensions).
 *
 * @author JoaoBispo
 *
 */
public class ConstantArrayType extends ArrayType {

    private final int constant;

    public ConstantArrayType(int constant, ArrayTypeData arrayTypeData, TypeData typeData, ClavaNodeInfo info,
            Type elementType) {
        this(constant, arrayTypeData, typeData, info, Arrays.asList(elementType));
    }

    private ConstantArrayType(int constant, ArrayTypeData arrayTypeData, TypeData typeData, ClavaNodeInfo info,
            Collection<? extends ClavaNode> children) {
        super(arrayTypeData, typeData, info, children);

        this.constant = constant;
    }

    public int getConstant() {
        return constant;
    }

    @Override
    protected ClavaNode copyPrivate() {
        return new ConstantArrayType(constant, getArrayTypeData(), getTypeData(), getInfo(), Collections.emptyList());
    }

    @Override
    public Type getElementType() {
        return getChild(Type.class, 0);
    }

    // @Override
    // public Type getElementType() {
    // return getChild(Type.class, 0);
    // }

    // @Override
    // public String getCode() {
    // Type elementType = getElementType();
    // String space = elementType instanceof ConstantArrayType ? "" : " ";
    //
    // return elementType.getCode() + space + "[" + constant + "]";
    // }

    /*
    @Override
    public String getCode(String name) {
        String nameCode = name == null ? "" : name;
    
        Type elementType = getElementType();
        // If element type is itself a ConstantArrayType, add constant to the name
        if (elementType instanceof ArrayType) {
            return elementType.getCode(nameCode + "[" + constant + "]");
        }
    
        return getElementType().getCode() + " " + nameCode + "[" + constant + "]";
    }
    */

    @Override
    protected String getArrayCode() {
        return Integer.toString(constant);
    }

}
