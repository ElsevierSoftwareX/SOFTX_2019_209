package pt.up.fe.specs.clava.weaver.abstracts.joinpoints;

import org.lara.interpreter.weaver.interf.events.Stage;
import java.util.Optional;
import org.lara.interpreter.exception.AttributeException;
import java.util.List;
import org.lara.interpreter.weaver.interf.SelectOp;
import java.util.Map;
import org.lara.interpreter.weaver.interf.JoinPoint;
import java.util.stream.Collectors;
import java.util.Arrays;

/**
 * Auto-Generated class for join point AClass
 * This class is overwritten by the Weaver Generator.
 * 
 * 
 * @author Lara Weaver Generator
 */
public abstract class AClass extends ARecord {

    protected ARecord aRecord;

    /**
     * 
     */
    public AClass(ARecord aRecord){
        super(aRecord);
        this.aRecord = aRecord;
    }
    /**
     * Get value on attribute methods
     * @return the attribute's value
     */
    public abstract AMethod[] getMethodsArrayImpl();

    /**
     * Get value on attribute methods
     * @return the attribute's value
     */
    public Object getMethodsImpl() {
        AMethod[] aMethodArrayImpl0 = getMethodsArrayImpl();
        Object nativeArray0 = getWeaverEngine().getScriptEngine().toNativeArray(aMethodArrayImpl0);
        return nativeArray0;
    }

    /**
     * Get value on attribute methods
     * @return the attribute's value
     */
    public final Object getMethods() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "methods", Optional.empty());
        	}
        	Object result = this.getMethodsImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "methods", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "methods", e);
        }
    }

    /**
     * Default implementation of the method used by the lara interpreter to select methods
     * @return 
     */
    public List<? extends AMethod> selectMethod() {
        return select(pt.up.fe.specs.clava.weaver.abstracts.joinpoints.AMethod.class, SelectOp.DESCENDANTS);
    }

    /**
     * Get value on attribute kind
     * @return the attribute's value
     */
    @Override
    public String getKindImpl() {
        return this.aRecord.getKindImpl();
    }

    /**
     * Get value on attribute fieldsArrayImpl
     * @return the attribute's value
     */
    @Override
    public AJoinPoint[] getFieldsArrayImpl() {
        return this.aRecord.getFieldsArrayImpl();
    }

    /**
     * Method used by the lara interpreter to select fields
     * @return 
     */
    @Override
    public List<? extends AField> selectField() {
        return this.aRecord.selectField();
    }

    /**
     * Get value on attribute name
     * @return the attribute's value
     */
    @Override
    public String getNameImpl() {
        return this.aRecord.getNameImpl();
    }

    /**
     * Get value on attribute isPublic
     * @return the attribute's value
     */
    @Override
    public Boolean getIsPublicImpl() {
        return this.aRecord.getIsPublicImpl();
    }

    /**
     * Get value on attribute qualifiedPrefix
     * @return the attribute's value
     */
    @Override
    public String getQualifiedPrefixImpl() {
        return this.aRecord.getQualifiedPrefixImpl();
    }

    /**
     * Get value on attribute qualifiedName
     * @return the attribute's value
     */
    @Override
    public String getQualifiedNameImpl() {
        return this.aRecord.getQualifiedNameImpl();
    }

    /**
     * 
     */
    public void defNameImpl(String value) {
        this.aRecord.defNameImpl(value);
    }

    /**
     * 
     */
    public void defQualifiedPrefixImpl(String value) {
        this.aRecord.defQualifiedPrefixImpl(value);
    }

    /**
     * 
     */
    public void defQualifiedNameImpl(String value) {
        this.aRecord.defQualifiedNameImpl(value);
    }

    /**
     * Replaces this join point with the given join
     * @param node 
     */
    @Override
    public AJoinPoint replaceWithImpl(AJoinPoint node) {
        return this.aRecord.replaceWithImpl(node);
    }

    /**
     * Overload which accepts a string
     * @param node 
     */
    @Override
    public AJoinPoint replaceWithImpl(String node) {
        return this.aRecord.replaceWithImpl(node);
    }

    /**
     * Inserts the given join point before this join point
     * @param node 
     */
    @Override
    public AJoinPoint insertBeforeImpl(AJoinPoint node) {
        return this.aRecord.insertBeforeImpl(node);
    }

    /**
     * Overload which accepts a string
     * @param node 
     */
    @Override
    public AJoinPoint insertBeforeImpl(String node) {
        return this.aRecord.insertBeforeImpl(node);
    }

    /**
     * Inserts the given join point after this join point
     * @param node 
     */
    @Override
    public AJoinPoint insertAfterImpl(AJoinPoint node) {
        return this.aRecord.insertAfterImpl(node);
    }

    /**
     * Overload which accepts a string
     * @param code 
     */
    @Override
    public AJoinPoint insertAfterImpl(String code) {
        return this.aRecord.insertAfterImpl(code);
    }

    /**
     * Removes the node associated to this joinpoint from the AST
     */
    @Override
    public void detachImpl() {
        this.aRecord.detachImpl();
    }

    /**
     * Sets the type of a node, if it has a type
     * @param type 
     */
    @Override
    public void setTypeImpl(AJoinPoint type) {
        this.aRecord.setTypeImpl(type);
    }

    /**
     * Performs a copy of the node and its children, but not of the nodes in its fields
     */
    @Override
    public AJoinPoint copyImpl() {
        return this.aRecord.copyImpl();
    }

    /**
     * Performs a copy of the node and its children, including the nodes in their fields (only the first level of field nodes, this function is not recursive)
     */
    @Override
    public AJoinPoint deepCopyImpl() {
        return this.aRecord.deepCopyImpl();
    }

    /**
     * Associates arbitrary values to nodes of the AST
     * @param fieldName 
     * @param value 
     */
    @Override
    public Object setUserFieldImpl(String fieldName, Object value) {
        return this.aRecord.setUserFieldImpl(fieldName, value);
    }

    /**
     * Overload which accepts a map
     * @param fieldNameAndValue 
     */
    @Override
    public Object setUserFieldImpl(Map<?, ?> fieldNameAndValue) {
        return this.aRecord.setUserFieldImpl(fieldNameAndValue);
    }

    /**
     * Sets the value associated with the given property key
     * @param key 
     * @param value 
     */
    @Override
    public AJoinPoint setValueImpl(String key, Object value) {
        return this.aRecord.setValueImpl(key, value);
    }

    /**
     * Adds a message that will be printed to the user after weaving finishes. Identical messages are removed
     * @param message 
     */
    @Override
    public void messageToUserImpl(String message) {
        this.aRecord.messageToUserImpl(message);
    }

    /**
     * Removes the children of this node
     */
    @Override
    public void removeChildrenImpl() {
        this.aRecord.removeChildrenImpl();
    }

    /**
     * Replaces the first child, or inserts the join point if no child is present
     * @param node 
     */
    @Override
    public void setFirstChildImpl(AJoinPoint node) {
        this.aRecord.setFirstChildImpl(node);
    }

    /**
     * Replaces the last child, or inserts the join point if no child is present
     * @param node 
     */
    @Override
    public void setLastChildImpl(AJoinPoint node) {
        this.aRecord.setLastChildImpl(node);
    }

    /**
     * 
     * @param position 
     * @param code 
     */
    @Override
    public AJoinPoint[] insertImpl(String position, String code) {
        return this.aRecord.insertImpl(position, code);
    }

    /**
     * 
     */
    @Override
    public String toString() {
        return this.aRecord.toString();
    }

    /**
     * 
     */
    @Override
    public Optional<? extends ARecord> getSuper() {
        return Optional.of(this.aRecord);
    }

    /**
     * 
     */
    @Override
    public final List<? extends JoinPoint> select(String selectName) {
        List<? extends JoinPoint> joinPointList;
        switch(selectName) {
        	case "method": 
        		joinPointList = selectMethod();
        		break;
        	case "field": 
        		joinPointList = selectField();
        		break;
        	default:
        		joinPointList = this.aRecord.select(selectName);
        		break;
        }
        return joinPointList;
    }

    /**
     * 
     */
    @Override
    public final void defImpl(String attribute, Object value) {
        switch(attribute){
        case "type": {
        	if(value instanceof AJoinPoint){
        		this.defTypeImpl((AJoinPoint)value);
        		return;
        	}
        	this.unsupportedTypeForDef(attribute, value);
        }
        case "firstChild": {
        	if(value instanceof AJoinPoint){
        		this.defFirstChildImpl((AJoinPoint)value);
        		return;
        	}
        	this.unsupportedTypeForDef(attribute, value);
        }
        case "lastChild": {
        	if(value instanceof AJoinPoint){
        		this.defLastChildImpl((AJoinPoint)value);
        		return;
        	}
        	this.unsupportedTypeForDef(attribute, value);
        }
        case "name": {
        	if(value instanceof String){
        		this.defNameImpl((String)value);
        		return;
        	}
        	this.unsupportedTypeForDef(attribute, value);
        }
        case "qualifiedPrefix": {
        	if(value instanceof String){
        		this.defQualifiedPrefixImpl((String)value);
        		return;
        	}
        	this.unsupportedTypeForDef(attribute, value);
        }
        case "qualifiedName": {
        	if(value instanceof String){
        		this.defQualifiedNameImpl((String)value);
        		return;
        	}
        	this.unsupportedTypeForDef(attribute, value);
        }
        default: throw new UnsupportedOperationException("Join point "+get_class()+": attribute '"+attribute+"' cannot be defined");
        }
    }

    /**
     * 
     */
    @Override
    protected final void fillWithAttributes(List<String> attributes) {
        this.aRecord.fillWithAttributes(attributes);
        attributes.add("methods");
    }

    /**
     * 
     */
    @Override
    protected final void fillWithSelects(List<String> selects) {
        this.aRecord.fillWithSelects(selects);
        selects.add("method");
    }

    /**
     * 
     */
    @Override
    protected final void fillWithActions(List<String> actions) {
        this.aRecord.fillWithActions(actions);
    }

    /**
     * Returns the join point type of this class
     * @return The join point type
     */
    @Override
    public final String get_class() {
        return "class";
    }

    /**
     * Defines if this joinpoint is an instanceof a given joinpoint class
     * @return True if this join point is an instanceof the given class
     */
    @Override
    public final boolean instanceOf(String joinpointClass) {
        boolean isInstance = get_class().equals(joinpointClass);
        if(isInstance) {
        	return true;
        }
        return this.aRecord.instanceOf(joinpointClass);
    }
    /**
     * 
     */
    protected enum ClassAttributes {
        METHODS("methods"),
        KIND("kind"),
        FIELDS("fields"),
        NAME("name"),
        ISPUBLIC("isPublic"),
        QUALIFIEDPREFIX("qualifiedPrefix"),
        QUALIFIEDNAME("qualifiedName"),
        PARENT("parent"),
        ASTANCESTOR("astAncestor"),
        AST("ast"),
        DATA("data"),
        HASCHILDREN("hasChildren"),
        DESCENDANTSANDSELF("descendantsAndSelf"),
        TYPE("type"),
        CHILDREN("children"),
        FIRSTCHILD("firstChild"),
        NUMCHILDREN("numChildren"),
        ANCESTOR("ancestor"),
        ASTCHILD("astChild"),
        ASTNAME("astName"),
        ASTID("astId"),
        CONTAINS("contains"),
        ASTISINSTANCE("astIsInstance"),
        FILENAME("filename"),
        JAVAFIELDS("javaFields"),
        ASTPARENT("astParent"),
        USERFIELD("userField"),
        HASNODE("hasNode"),
        CHILD("child"),
        ENDLINE("endLine"),
        ENDCOLUMN("endColumn"),
        CODE("code"),
        ISINSIDELOOPHEADER("isInsideLoopHeader"),
        LINE("line"),
        KEYS("keys"),
        ISINSIDEHEADER("isInsideHeader"),
        ASTNUMCHILDREN("astNumChildren"),
        DESCENDANTS("descendants"),
        ASTCHILDREN("astChildren"),
        ISMACRO("isMacro"),
        LASTCHILD("lastChild"),
        ROOT("root"),
        JAVAVALUE("javaValue"),
        KEYTYPE("keyType"),
        CHAINANCESTOR("chainAncestor"),
        CHAIN("chain"),
        JOINPOINTTYPE("joinpointType"),
        CURRENTREGION("currentRegion"),
        HASASTPARENT("hasAstParent"),
        COLUMN("column"),
        PARENTREGION("parentRegion"),
        GETVALUE("getValue"),
        FIRSTJP("firstJp"),
        JAVAFIELDTYPE("javaFieldType"),
        LOCATION("location"),
        GETUSERFIELD("getUserField"),
        PRAGMAS("pragmas"),
        HASPARENT("hasParent");
        private String name;

        /**
         * 
         */
        private ClassAttributes(String name){
            this.name = name;
        }
        /**
         * Return an attribute enumeration item from a given attribute name
         */
        public static Optional<ClassAttributes> fromString(String name) {
            return Arrays.asList(values()).stream().filter(attr -> attr.name.equals(name)).findAny();
        }

        /**
         * Return a list of attributes in String format
         */
        public static List<String> getNames() {
            return Arrays.asList(values()).stream().map(ClassAttributes::name).collect(Collectors.toList());
        }

        /**
         * True if the enum contains the given attribute name, false otherwise.
         */
        public static boolean contains(String name) {
            return fromString(name).isPresent();
        }
    }
}
