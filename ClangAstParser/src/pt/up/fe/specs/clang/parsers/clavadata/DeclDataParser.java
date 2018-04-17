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

package pt.up.fe.specs.clang.parsers.clavadata;

import java.util.List;
import java.util.stream.Collectors;

import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.clang.parsers.ClavaDataParser;
import pt.up.fe.specs.clang.parsers.ClavaNodes;
import pt.up.fe.specs.clang.parsers.GeneralParsers;
import pt.up.fe.specs.clava.ast.ClavaData;
import pt.up.fe.specs.clava.ast.attr.Attribute;
import pt.up.fe.specs.clava.ast.decl.data2.CXXMethodDeclDataV2;
import pt.up.fe.specs.clava.ast.decl.data2.DeclDataV2;
import pt.up.fe.specs.clava.ast.decl.data2.FunctionDeclDataV2;
import pt.up.fe.specs.clava.ast.decl.data2.NamedDeclData;
import pt.up.fe.specs.clava.ast.decl.data2.ParmVarDeclData;
import pt.up.fe.specs.clava.ast.decl.data2.VarDeclDataV2;
import pt.up.fe.specs.clava.ast.decl.enums.InitializationStyle;
import pt.up.fe.specs.clava.ast.decl.enums.NameKind;
import pt.up.fe.specs.clava.ast.decl.enums.StorageClass;
import pt.up.fe.specs.clava.ast.decl.enums.TemplateKind;
import pt.up.fe.specs.clava.language.TLSKind;
import pt.up.fe.specs.util.utilities.LineStream;

/**
 * ClavaData parsers for Decl nodes.
 * 
 * @author JoaoBispo
 *
 */
public class DeclDataParser {

    public static DeclDataV2 parseDeclData(LineStream lines, DataStore dataStore) {

        ClavaData clavaData = ClavaDataParser.parseClavaData(lines, dataStore);

        boolean isImplicit = GeneralParsers.parseOneOrZero(lines);
        boolean isUsed = GeneralParsers.parseOneOrZero(lines);
        boolean isReferenced = GeneralParsers.parseOneOrZero(lines);
        boolean isInvalidDecl = GeneralParsers.parseOneOrZero(lines);

        // List<Attribute> attributes = GeneralParsers.parseAttributes(lines);

        // GeneralParsers.parseStringList(lines).stream()
        // .map(attrId -> )
        List<Attribute> attributes = GeneralParsers.parseStringList(lines).stream()
                .map(attrId -> ClavaNodes.getAttr(dataStore, attrId))
                .collect(Collectors.toList());

        // for (String attributeId : attributesIds) {
        // System.out.println("ATTRIBUTE:" + config.get(ClavaDataParser.getParsedNodesKey()).get(attributeId));
        // }

        return new DeclDataV2(isImplicit, isUsed, isReferenced, isInvalidDecl, attributes, clavaData);
    }

    public static NamedDeclData parseNamedDeclData(LineStream lines, DataStore dataStore) {

        // Parse Decl data
        DeclDataV2 declData = parseDeclData(lines, dataStore);

        String qualifiedName = lines.nextLine();
        NameKind nameKind = NameKind.getHelper().valueOf(GeneralParsers.parseInt(lines));
        boolean isHidden = GeneralParsers.parseOneOrZero(lines);

        return new NamedDeclData(qualifiedName, nameKind, isHidden, declData);
    }

    public static FunctionDeclDataV2 parseFunctionDeclData(LineStream lines, DataStore dataStore) {

        // Parse NamedDecl data
        NamedDeclData namedDeclData = parseNamedDeclData(lines, dataStore);

        boolean isConstexpr = GeneralParsers.parseOneOrZero(lines);
        TemplateKind templateKind = TemplateKind.getHelper().valueOf(GeneralParsers.parseInt(lines));

        return new FunctionDeclDataV2(isConstexpr, templateKind, namedDeclData);
    }

    public static CXXMethodDeclDataV2 parseCXXMethodDeclData(LineStream lines, DataStore dataStore) {

        // Parse FunctionDecl data
        FunctionDeclDataV2 functionDeclData = parseFunctionDeclData(lines, dataStore);

        String recordId = lines.nextLine();

        return new CXXMethodDeclDataV2(recordId, functionDeclData);
    }

    public static VarDeclDataV2 parseVarDeclData(LineStream lines, DataStore dataStore) {

        // Parse NamedDecl data
        NamedDeclData namedDeclData = parseNamedDeclData(lines, dataStore);

        StorageClass storageClass = GeneralParsers.enumFromInt(StorageClass.getHelper(), lines);
        TLSKind tlsKind = GeneralParsers.enumFromInt(TLSKind.getHelper(), lines);
        boolean isModulePrivate = GeneralParsers.parseOneOrZero(lines);
        boolean isNRVOVariable = GeneralParsers.parseOneOrZero(lines);
        InitializationStyle initStyle = GeneralParsers.enumFromInt(InitializationStyle.getHelper(), lines);

        boolean isConstexpr = GeneralParsers.parseOneOrZero(lines);
        boolean isStaticDataMember = GeneralParsers.parseOneOrZero(lines);
        boolean isOutOfLine = GeneralParsers.parseOneOrZero(lines);
        boolean hasGlobalStorage = GeneralParsers.parseOneOrZero(lines);

        return new VarDeclDataV2(storageClass, tlsKind, isModulePrivate, isNRVOVariable, initStyle, isConstexpr,
                isStaticDataMember, isOutOfLine, hasGlobalStorage, namedDeclData);
    }

    public static ParmVarDeclData parseParmVarDeclData(LineStream lines, DataStore dataStore) {
        // Parse VarDecl data
        VarDeclDataV2 varDeclData = parseVarDeclData(lines, dataStore);

        boolean hasInheritedDefaultArg = GeneralParsers.parseOneOrZero(lines);

        return new ParmVarDeclData(hasInheritedDefaultArg, varDeclData);
    }

}
