/***** BEGIN LICENSE BLOCK *****
 * Version: CPL 1.0/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Common Public
 * License Version 1.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.eclipse.org/legal/cpl-v10.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * Copyright (C) 2002 Benoit Cerrina <b.cerrina@wanadoo.fr>
 * Copyright (C) 2002 Jan Arne Petersen <jpetersen@uni-bonn.de>
 * Copyright (C) 2002-2004 Anders Bengtsson <ndrsbngtssn@yahoo.se>
 * Copyright (C) 2004 Thomas E Enebo <enebo@acm.org>
 * Copyright (C) 2004 Stefan Matthias Aust <sma@3plus4.de>
 * 
 * Alternatively, the contents of this file may be used under the terms of
 * either of the GNU General Public License Version 2 or later (the "GPL"),
 * or the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the CPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the CPL, the GPL or the LGPL.
 ***** END LICENSE BLOCK *****/
package org.jruby.ast;

import java.util.List;

import org.jruby.ast.visitor.NodeVisitor;
import org.jruby.evaluator.Instruction;
import org.jruby.lexer.yacc.ISourcePosition;

/**
 * a Range in a boolean expression.
 * named after a FlipFlop component in electronic I believe.
 * 
 * @author  jpetersen
 */
public class FlipNode extends Node {
    static final long serialVersionUID = -4735579451657299802L;

    private final Node beginNode;
    private final Node endNode;
    private final boolean exclusive;
    private final int count;
    
    public FlipNode(ISourcePosition position, Node beginNode, Node endNode, boolean exclusive, int count) {
        super(position);
        this.beginNode = beginNode;
        this.endNode = endNode;
        this.exclusive = exclusive;
        this.count = count;
    }

    /**
     * Accept for the visitor pattern.
     * @param iVisitor the visitor
     **/
    public Instruction accept(NodeVisitor iVisitor) {
        return iVisitor.visitFlipNode(this);
    }

    /**
     * Gets the beginNode.
	 * beginNode will set the FlipFlop the first time it is true
     * @return Returns a Node
     */
    public Node getBeginNode() {
        return beginNode;
    }

    /**
     * Gets the endNode.
	 * endNode will reset the FlipFlop when it is true while the FlipFlop is set.
     * @return Returns a Node
     */
    public Node getEndNode() {
        return endNode;
    }

    /**
     * Gets the exclusive.
	 * if the range is a 2 dot range it is false if it is a three dot it is true
     * @return Returns a boolean
     */
    public boolean isExclusive() {
        return exclusive;
    }

    /**
     * Gets the count.
     * @return Returns a int
     */
    public int getCount() {
        return count;
    }
    
    public List childNodes() {
        return Node.createList(beginNode, endNode);
    }

}