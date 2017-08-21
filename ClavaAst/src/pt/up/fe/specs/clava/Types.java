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

package pt.up.fe.specs.clava;

import pt.up.fe.specs.clava.ast.type.AttributedType;
import pt.up.fe.specs.clava.ast.type.BuiltinType;
import pt.up.fe.specs.clava.ast.type.FunctionProtoType;
import pt.up.fe.specs.clava.ast.type.FunctionType;
import pt.up.fe.specs.clava.ast.type.NullType;
import pt.up.fe.specs.clava.ast.type.PointerType;
import pt.up.fe.specs.clava.ast.type.Type;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.classmap.FunctionClassMap;

public class Types {

    private static final FunctionClassMap<Type, FunctionType> TO_FUNCTION_TYPE;

    static {
        TO_FUNCTION_TYPE = new FunctionClassMap<>();
        TO_FUNCTION_TYPE.put(FunctionType.class, type -> type);
        TO_FUNCTION_TYPE.put(FunctionProtoType.class, type -> type);
        TO_FUNCTION_TYPE.put(AttributedType.class, type -> TO_FUNCTION_TYPE.apply(type.getModifiedType()));
        TO_FUNCTION_TYPE.put(PointerType.class, type -> TO_FUNCTION_TYPE.apply(type.getPointeeType()));
        // TO_FUNCTION_TYPE.put(QualType.class, type -> TO_FUNCTION_TYPE.apply(type.getQualifiedType()));
        TO_FUNCTION_TYPE.put(NullType.class, type -> null);
    }

    public static boolean isVoid(Type type) {
        if (!(type instanceof BuiltinType)) {
            return false;
        }

        return type.getBareType().equals("void");
    }

    /**
     * Removes stray occurrences of 'class ' and 'struct '.
     * 
     * @param type
     * @return
     */
    public static String cleanElaborated(String type) {
        String currentType = type;

        if (!currentType.startsWith("class ")) {
            currentType = SpecsStrings.remove(currentType, "class ");
        }

        if (!currentType.startsWith("struct ")) {
            currentType = SpecsStrings.remove(currentType, "struct ");
        }

        return currentType;
    }

    /**
     * Tries to convert the type into a FunctionType. If it is not possible, returns null.
     * 
     * @param type
     * @return
     */
    public static FunctionType getFunctionType(Type type) {
        // System.out.println("TYPE:" + type);
        return TO_FUNCTION_TYPE.apply(type);
        /*
        if (type instanceof FunctionType) {
        return (FunctionType) type;
        }
        
        if (type instanceof AttributedType) {
        return getFunctionType(((AttributedType) type).getModifiedType());
        }
        
        if (type instanceof NullType) {
        return null;
        }
        
        throw new RuntimeException("Could not convert type to FunctionType:" + type.getClass().getSimpleName());
        */
    }

    public static boolean isPointer(Type type) {
        if (type instanceof PointerType) {
            return true;
        }

        return false;
    }

    public static Type getPointeeType(Type type) {
        if (type instanceof PointerType) {
            return ((PointerType) type).getPointeeType();
        }

        throw new RuntimeException("Not implemented for type '" + type.getClass().getSimpleName() + "'");
    }

}
