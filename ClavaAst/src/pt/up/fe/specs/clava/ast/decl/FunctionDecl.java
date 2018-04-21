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

package pt.up.fe.specs.clava.ast.decl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import pt.up.fe.specs.clava.ClavaLog;
import pt.up.fe.specs.clava.ClavaNode;
import pt.up.fe.specs.clava.ClavaNodeInfo;
import pt.up.fe.specs.clava.Types;
import pt.up.fe.specs.clava.ast.decl.data.DeclData;
import pt.up.fe.specs.clava.ast.decl.data.FunctionDeclData;
import pt.up.fe.specs.clava.ast.decl.enums.StorageClass;
import pt.up.fe.specs.clava.ast.expr.CallExpr;
import pt.up.fe.specs.clava.ast.extra.App;
import pt.up.fe.specs.clava.ast.extra.TranslationUnit;
import pt.up.fe.specs.clava.ast.extra.VariadicType;
import pt.up.fe.specs.clava.ast.stmt.CXXTryStmt;
import pt.up.fe.specs.clava.ast.stmt.CompoundStmt;
import pt.up.fe.specs.clava.ast.stmt.Stmt;
import pt.up.fe.specs.clava.ast.type.FunctionProtoType;
import pt.up.fe.specs.clava.ast.type.FunctionType;
import pt.up.fe.specs.clava.ast.type.Type;
import pt.up.fe.specs.clava.ast.type.data.FunctionProtoTypeData;
import pt.up.fe.specs.clava.ast.type.enums.ExceptionSpecifier;
import pt.up.fe.specs.util.SpecsCollections;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.exceptions.CaseNotDefinedException;
import pt.up.fe.specs.util.lazy.Lazy;

/**
 * Represents a function declaration or definition.
 * 
 * <p>
 * Structure of children:<br>
 * - Parameters (ParmVarDecl)<br>
 * - Definition (Stmt)
 * 
 * @author JoaoBispo
 *
 */
public class FunctionDecl extends DeclaratorDecl {

    private final FunctionDeclData functionDeclData;

    private Lazy<FunctionDecl> declaration;
    private Lazy<FunctionDecl> definition;
    // CHECK: Directly enconding information relative to the tree structure (e.g., how many parameter nodes)
    // prevents direct transformations in the tree (e.g., if we remove a parameter, the value needs to be updated)
    // Solutions:
    // 1) Treat the nodes as immutable -> Does not really solve the problem in this case, the information of the
    // parameters is information about the children, not the node itself
    // 2) Can only add/remove nodes using node-specific functions -> How do we distinguish nodes where we can use
    // generic insert/remove from these special nodes?
    // 3) Create special nodes -> e.g., FunctionDecl would have a Inputs node, a Definition node, etc. More cumbersome
    // tree
    // 4) Make internal logic for returning/adding nodes more complex -> Removes node information about children, but
    // reverts to 2)
    // private final int numParameters;
    // private final boolean hasDefinition;

    // Just for testing
    // public FunctionDecl(FunctionDeclDataV2 data, List<ClavaNode> children) {
    // super(null, null, null, null, Collections.emptyList());
    // functionDeclData = null;
    // }

    /**
     * Constructor for a function definition.
     */
    public FunctionDecl(String declName, List<ParmVarDecl> inputs, Type functionType, FunctionDeclData functionDeclData,
            DeclData declData, ClavaNodeInfo info, Stmt definition) {

        this(declName, functionType, functionDeclData, declData, info, inputs, definition);
    }

    /**
     * 
     * 
     * @param declName
     * @param inputs
     * @param returnType
     * @param attributes
     * @param info
     * @param definition
     */
    protected FunctionDecl(String declName, Type functionType, FunctionDeclData functionDeclData, DeclData declData,
            ClavaNodeInfo info, List<ParmVarDecl> inputs, Stmt definition) {

        this(declName, functionType, functionDeclData, declData, info, SpecsCollections.concat(inputs, definition));

        checkDefinition(definition);
    }

    protected FunctionDecl(String declName, Type functionType, FunctionDeclData functionDeclData, DeclData declData,
            ClavaNodeInfo info, List<? extends ClavaNode> children) {

        super(declName, functionType, declData, info, children);

        this.functionDeclData = functionDeclData;
        this.declaration = Lazy.newInstance(() -> this.findDeclOrDef(true));
        this.definition = Lazy.newInstance(() -> this.findDeclOrDef(false));

    }

    protected void checkDefinition(Stmt definition) {

        if (definition == null) {
            return;
        }

        // Allowed statements: CompoundStmt, CXXTryStmt
        if (definition instanceof CompoundStmt ||
                definition instanceof CXXTryStmt) {
            return;
        }

        throw new RuntimeException("Stmt now allowed as a definition of FunctionDecl: " + definition.getClass());
    }

    @Override
    protected ClavaNode copyPrivate() {
        return new FunctionDecl(getDeclName(), getFunctionType(), functionDeclData, getDeclData(), getInfo(),
                Collections.emptyList(), null);
    }

    public FunctionType getFunctionType() {
        return Types.getFunctionType(getType());
    }

    public Type getReturnType() {
        return getFunctionType().getReturnType();
    }

    public FunctionDeclData getFunctionDeclData() {
        return functionDeclData;
    }

    public boolean isInline() {
        return functionDeclData.isInline();
    }

    /**
     * Has definition if the last child is a Stmt.
     * 
     * @return
     */
    public boolean hasBody() {
        return SpecsCollections.lastTry(getChildren()).map(child -> child instanceof Stmt).orElse(false);
    }

    public Optional<Stmt> setBody(CompoundStmt body) {
        // If no body, just add the stmt and return
        if (!hasBody()) {
            addChild(body);
            return Optional.empty();
        }

        // Replace last statement
        int lastIndex = getNumChildren() - 1;
        Stmt previousBody = (Stmt) getChild(lastIndex);

        setChild(lastIndex, body);

        return Optional.of(previousBody);
    }

    @Override
    public String getCode() {
        StringBuilder code = new StringBuilder();

        code.append(ln());

        code.append(getDeclarationId(true));
        /*
        if (getFunctionDeclData().isInline()) {
            code.append("inline ");
        }
        
        if (getFunctionDeclData().getStorageClass() != StorageClass.NONE) {
            code.append(getFunctionDeclData().getStorageClass().getString()).append(" ");
        }
        
        String returnType = getFunctionType().getReturnType().getCode();
        
        code.append(returnType).append(" ").append(getTypelessCode()).append(getCodeBody());
        */
        code.append(getCodeBody());
        return code.toString();
    }

    /**
     * 
     * @return the node representing the declaration of this function, if it exists
     */
    public Optional<FunctionDecl> getDeclaration() {
        // If no body, this node already is the declaration
        if (!hasBody()) {
            return Optional.of(this);
        }

        // Get 'cached' value
        return Optional.ofNullable(declaration.get());
    }

    public Optional<FunctionDecl> getFunctionDefinition() {
        // If has body, this node is already the definition
        if (hasBody()) {
            return Optional.of(this);
        }

        // Get 'cached' value
        return Optional.ofNullable(definition.get());
    }

    // private FunctionDecl findDeclaration() {
    //
    // }

    private FunctionDecl findDeclOrDef(boolean findDecl) {
        App app = getAppTry().orElse(null);

        if (app == null) {
            return null;
        }

        // Find function declarations with the same name and the same signature
        List<FunctionDecl> decls = getApp().getDescendantsStream()
                .filter(FunctionDecl.class::isInstance)
                .map(FunctionDecl.class::cast)
                .filter(fdecl -> findDecl ? !fdecl.hasBody() : fdecl.hasBody())
                .filter(fdecl -> fdecl.getDeclName().equals(getDeclName()))
                .filter(fdecl -> fdecl.getDeclarationId(false).equals(getDeclarationId(false)))
                .collect(Collectors.toList());

        if (decls.size() > 1) {
            SpecsLogs.msgInfo("getDeclaration(): Found more than one declaration for function at " + getLocation());
        }

        return !decls.isEmpty() ? decls.get(0) : null;
    }

    public String getDeclarationId(boolean useReturnType) {
        StringBuilder code = new StringBuilder();

        if (getFunctionDeclData().hasOpenCLKernelAttr()) {
            code.append("__kernel ");
        }

        if (getFunctionDeclData().isInline()) {
            code.append("inline ");
        }

        if (getFunctionDeclData().getStorageClass() != StorageClass.NONE) {
            code.append(getFunctionDeclData().getStorageClass().getString()).append(" ");
        }

        if (useReturnType) {
            String returnType = getFunctionType().getReturnType().getCode();
            code.append(returnType);
        }

        code.append(" ").append(getTypelessCode());

        return code.toString().trim();
    }

    @Override
    public String getTypelessCode() {
        StringBuilder code = new StringBuilder();

        code.append(getDeclName());
        String parameters = getParametersCode();

        // if (!getFunctionTypeTry().isPresent()) {
        if (Types.getFunctionType(getType()) == null) {
            return "<no function type>";
        }

        Optional<Type> lastParamType = SpecsCollections.lastTry(getFunctionType().getParamTypes());
        boolean hasVariadicArguments = lastParamType.map(type -> type instanceof VariadicType).orElse(false);
        // lastParamType == null ? false : lastParamType instanceof VariadicType;

        // boolean hasVariadicArguments = getFunctionType().getCode().endsWith(", ...)");
        parameters = hasVariadicArguments ? parameters + ", ..." : parameters;

        code.append("(").append(parameters).append(")");

        code.append(getCodeAfterParams());

        return code.toString();
    }

    private String getCodeAfterParams() {
        FunctionType functionType = getFunctionType();

        if (!(functionType instanceof FunctionProtoType)) {
            return "";
        }

        FunctionProtoType functionProtoType = (FunctionProtoType) functionType;
        FunctionProtoTypeData ptData = functionProtoType.getFunctionProtoTypeData();
        StringBuilder code = new StringBuilder();

        // Add const/volatile
        if (ptData.isConst()) {
            code.append(" const");
        }
        if (ptData.isVolatile()) {
            code.append(" volatile");
        }

        code.append(getCodeExcept(ptData));

        return code.toString();
    }

    private String getCodeExcept(FunctionProtoTypeData ptData) {
        // FunctionType functionType = getFunctionType();
        // if (!(functionType instanceof FunctionProtoType)) {
        // return "";
        // }
        //
        // FunctionProtoType functionProtoType = (FunctionProtoType) functionType;
        ExceptionSpecifier specifier = ptData.getSpecifier();
        switch (specifier) {
        case NONE:
            return "";
        case MS_ANY:
            return " throw(...)";
        case DYNAMIC_NONE:
            return " throw()";
        case BASIC_NOEXCEPT:
            return " noexcept";
        case COMPUTED_NOEXCEPT:
            return " noexcept(" + ptData.getNoexceptExpr() + ")";
        case UNEVALUATED:
            // Appears to be used in cases like
            // ~A(), ~A() = delete and ~A() = 0
            // where there is no exception specifier.

            // However, declarations can later have an implicit noexcept
            // that is made explicit by the parser and, by extension, Clava's code output
            // Returning "" would make this incompatible with the later noexcept definition,
            // so we also specify noexcept here
            // There are cases where the definition has throw() instead, but definitions with
            // noexcept appear to be compatible with throw().
            return " noexcept";
        default:
            ClavaLog.info("Code generation not implemented yet for Exception Specifier '" + specifier + "'");
            return "\n#if 0\nNOT IMPLEMENTED: " + specifier + "\n#endif\n";
        /*
        throw new RuntimeException(
                "Code generation not implemented yet for Exception Specifier '" + specifier + "': "
                        + getLocation());*/
        }
    }

    protected String getCodeBody() {
        StringBuilder code = new StringBuilder();

        if (!hasBody()) {
            code.append(";");
            return code.toString();
        }

        // Translate code of the only child
        Stmt body = getDefinition().orElseThrow(() -> new RuntimeException("Expected a body"));
        if (body instanceof CXXTryStmt) {
            code.append(" ");
        }
        code.append(body.getCode());

        return code.toString();
    }

    public List<ParmVarDecl> getParameters() {
        return SpecsCollections.peek(getChildren(), ParmVarDecl.class);
    }

    public int getNumParameters() {
        return getParameters().size();
    }

    /**
     * TODO: Replace uses with .getBody(). This should be function definition
     * 
     * @return
     */
    public Optional<Stmt> getDefinition() {
        if (!hasBody()) {
            return Optional.empty();
        }

        // Last child should be a Stmt
        return Optional.of(getChild(Stmt.class, getNumChildren() - 1));
    }

    public Optional<CompoundStmt> getBody() {
        return getDefinition().map(this::getCompoundStmt);
    }

    private CompoundStmt getCompoundStmt(Stmt body) {
        if (body instanceof CompoundStmt) {
            return (CompoundStmt) body;
        }

        if (body instanceof CXXTryStmt) {
            return ((CXXTryStmt) body).getBody();
        }

        throw new CaseNotDefinedException(body.getClass());
    }

    protected String getParametersCode() {

        return getParameters().stream()
                // .map(param -> parseTypes(param.getType()) + param.getDeclName())
                .map(param -> param.getCode())
                .collect(Collectors.joining(", "));
    }

    public void setName(String name) {
        String functionName = getDeclName();

        // Determine scope of change. If static, only change calls inside the file
        boolean isStatic = getFunctionDeclData().getStorageClass() == StorageClass.STATIC;
        ClavaNode root = isStatic ? getAncestorTry(TranslationUnit.class).orElse(null) : getAppTry().orElse(null);

        Optional<FunctionDecl> decl = getDeclaration();
        Optional<FunctionDecl> def = getFunctionDefinition();

        // Find all calls of this function
        if (root != null) {
            for (CallExpr callExpr : root.getDescendants(CallExpr.class)) {
                testAndSetCallName(callExpr, name);
            }

            /*
            root.getDescendantsStream()
                    .filter(node -> node instanceof CallExpr && !(node instanceof CXXMemberCallExpr)
                            && !(node instanceof CXXOperatorCallExpr))
                    .map(node -> CallExpr.class.cast(node))
                    .filter(node -> functionName.equals(node.getCalleeName()))
                    .forEach(callExpr -> callExpr.setCallName(name));
                    */
        }

        // Change name of itself, both definition and declaration
        // setDeclName(name);
        // if (name.equals("declAndDefNew")) {
        // System.out.println("BEFORE");
        // System.out.println("DECL:" + getDeclaration());
        // System.out.println("DEF:" + getFunctionDefinition());
        // }
        decl.ifPresent(node -> node.setDeclName(name));
        def.ifPresent(node -> node.setDeclName(name));
        // if (name.equals("declAndDefNew")) {
        // System.out.println("AFTER");
        // System.out.println("DECL:" + getDeclaration());
        // System.out.println("DEF:" + getFunctionDefinition());
        // }
        // Change name of declaration
        getDeclaration()
                .filter(functionDecl -> functionDecl != this)
                .ifPresent(functionDecl -> functionDecl.setDeclName(name));

    }

    /**
     * Sets the name of call if the corresponding definition, declaration or both refer to the function represented by
     * this node.
     * 
     * @param name
     * @param original
     * @param tentative
     */
    private void testAndSetCallName(CallExpr call, String newName) {
        if (isCorrespondingCall(call)) {
            call.setCallName(newName);
        }
    }

    private boolean isCorrespondingCall(CallExpr call) {
        // If declaration exists, declaration of call must exist too, if call refers to this function

        Optional<Boolean> result = getDeclaration().map(decl -> match(decl, call.getDeclaration()));
        if (result.isPresent()) {
            return result.get();
        }

        result = getFunctionDefinition().map(def -> match(def, call.getDefinition()));
        if (result.isPresent()) {
            return result.get();
        }

        System.out.println("DECL:" + getDeclaration());
        System.out.println("DEF:" + getFunctionDefinition());

        throw new RuntimeException("Should not arrive here, either function declaration or definition must be defined");
        /*
        return getDeclaration().map(decl -> match(decl, call.getDeclaration()))
                // If no declaration, try definition
                .orElse(getFunctionDefinition().map(def -> match(def, call.getDefinition()))
                        .orElseThrow(() -> new RuntimeException(
                                "Should not arrive here, either function declaration or definition must be defined")));
        */
        // Optional<FunctionDecl> functionDecl = getDeclaration();
        // Optional<FunctionDecl> callDecl = call.getDeclaration();

        /*
        Optional<Boolean> declMatch = match(getDeclaration(), call.getDeclaration());
        if (declMatch.isPresent()) {
            return declMatch.get();
        }
        
        Optional<Boolean> defMatch = match(getFunctionDefinition(), call.getDefinition());
        if (defMatch.isPresent()) {
            return defMatch.get();
        }
        
        throw new RuntimeException("Should not arrive here, either function declaration or definition must be defined");
        */
        /*
        if (functionDecl.isPresent()) {
            FunctionDecl decl = functionDecl.get();
        
            if (!callDecl.isPresent()) {
                return;
            }
        
            boolean isFunction = decl == callDecl.get();
        
            if (isFunction) {
                call.setCallName(newName);
            }
        
            return;
        }
        */
        /*
        boolean isDeclOfCall;
        // If declaration exists, check if declaration of call is the 
        getDeclaration();
        call.getDeclaration()
        */
    }

    /**
     * True if both objects match, false if they don't match, or empty if the first argument (functionDecl) is not
     * present.
     * 
     * @param function
     * @param call
     * @return
     */
    /*
    private static Optional<Boolean> match(Optional<FunctionDecl> functionDecl, Optional<FunctionDecl> callDecl) {
        if (!functionDecl.isPresent()) {
            return Optional.empty();
        }
    
        FunctionDecl decl = functionDecl.get();
    
        if (!callDecl.isPresent()) {
            return Optional.of(false);
        }
    
        return Optional.of(decl == callDecl.get());
    }
    */

    private static boolean match(FunctionDecl decl, Optional<FunctionDecl> callDecl) {

        return callDecl.map(cDecl -> decl == cDecl).orElse(false);
        /*
        if (!callDecl.isPresent()) {
            return false;
        }
        
        return decl == callDecl.get();
        */
    }

}
