/**
 * 
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.clava.ClavaNode;
import pt.up.fe.specs.clava.ast.type.data.exception.ExceptionSpecification;
import pt.up.fe.specs.clava.language.ReferenceQualifier;

public class FunctionProtoType extends FunctionType {

    /// DATAKEYS BEGIN

    public final static DataKey<Integer> NUM_PARAMETERS = KeyFactory.integer("numParameters");

    public final static DataKey<List<Type>> PARAMETERS_TYPES = KeyFactory.generic("parametersTypes",
            new ArrayList<Type>());

    public final static DataKey<Boolean> HAS_TRAILING_RETURNS = KeyFactory.bool("hasTrailingReturn");

    public final static DataKey<Boolean> IS_VARIADIC = KeyFactory.bool("isVariadic");

    // public final static DataKey<Boolean> IS_CONST = KeyFactory.bool("isConst");
    //
    // public final static DataKey<Boolean> IS_VOLATILE = KeyFactory.bool("isVolatile");
    //
    // public final static DataKey<Boolean> IS_RESTRICT = KeyFactory.bool("isRestrict");

    public final static DataKey<ReferenceQualifier> REFERENCE_QUALIFIER = KeyFactory
            .enumeration("referenceQualifier", ReferenceQualifier.class);

    public final static DataKey<ExceptionSpecification> EXCEPTION_SPECIFICATION = KeyFactory
            .object("exceptionSpecification", ExceptionSpecification.class);

    // public final static DataKey<ExceptionSpecificationType> EXCEPTION_SPECIFICATION_TYPE = KeyFactory
    // .enumeration("exceptionSpecificationType", ExceptionSpecificationType.class);
    //
    // public final static DataKey<Expr> NOEXCEPT_EXPR = KeyFactory.object("noexceptExpr", Expr.class);

    /// DATAKEYS END

    public FunctionProtoType(DataStore data, Collection<? extends ClavaNode> children) {
        super(data, children);
    }

    @Override
    public int getIndexParamStart() {
        return getIndexReturnType() + 1;
    }

    @Override
    public int getIndexParamEnd() {
        return getIndexParamStart() + get(NUM_PARAMETERS);
    }

    public String getCodeAfterParams() {
        StringBuilder code = new StringBuilder();

        // Add const/volatile
        if (get(IS_CONST)) {
            // System.out.println("CONSTTTT");
            code.append(" const");
        }
        if (get(IS_VOLATILE)) {
            code.append(" volatile");
        }

        // String exceptCode = get(EXCEPTION_SPECIFICATION_TYPE).getCode(get(NOEXCEPT_EXPR));
        String exceptCode = get(EXCEPTION_SPECIFICATION).getCode(this);
        code.append(exceptCode);

        return code.toString();
    }

    @Override
    public int getNumParams() {
        return get(NUM_PARAMETERS);
    }

    @Override
    public boolean isVariadic() {
        return get(IS_VARIADIC);
    }

    @Override
    public List<Type> getParamTypes() {
        return get(PARAMETERS_TYPES);
    }

}
