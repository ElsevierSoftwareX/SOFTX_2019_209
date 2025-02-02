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

package pt.up.fe.specs.clang.transforms;

import pt.up.fe.specs.clava.ClavaNode;
import pt.up.fe.specs.clava.ast.pragma.Pragma;
import pt.up.fe.specs.clava.ast.stmt.WrapperStmt;
import pt.up.fe.specs.clava.transform.SimplePreClavaRule;
import pt.up.fe.specs.util.stringparser.StringParser;
import pt.up.fe.specs.util.stringparser.StringParsers;
import pt.up.fe.specs.util.treenode.transform.TransformQueue;

/**
 * Removes gcc 'poison' pragmas, since the code will have the macros resolved, the poison can prevent the code from
 * parsing.
 * 
 * 
 * @author JoaoBispo
 *
 */
public class RemovePoison implements SimplePreClavaRule {

    @Override
    public void applySimple(ClavaNode node, TransformQueue<ClavaNode> queue) {
        if (!(node instanceof Pragma)) {
            return;
        }

        Pragma pragma = (Pragma) node;

        if (!pragma.getName().equals("GCC")) {
            return;
        }

        if (!new StringParser(pragma.getContent()).apply(StringParsers::hasWord, "poison")) {
            return;
        }

        // If pragma is inside a WrapperStmt, remove the stmt
        if (pragma.getParent() instanceof WrapperStmt) {
            node = pragma.getParent();
        }

        queue.delete(node);
    }

}
