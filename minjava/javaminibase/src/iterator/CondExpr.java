package iterator;

import java.lang.*;
import java.io.*;
import global.*;

/**
 * This class will hold single select condition It is an element of linked list
 * which is logically connected by OR operators.
 */

public class CondExpr {

	/**
	 * Operator like "<"
	 */
	public AttrOperator op;

	/**
	 * Types of operands, Null AttrType means that operand is not a literal but
	 * an attribute name
	 */
	public AttrType type1;
	public AttrType type2;

	/**
	 * the left operand and right operand
	 */
	public Operand operand1;
	public Operand operand2;

	/**
	 * Pointer to the next element in linked list
	 */
	public CondExpr next;

	/**
	 * Distance bw 2 AttrDesc type operands. -1 if other type of operands.
	 */
	public double distance = -1;

	/**
	 * constructor
	 */
	public CondExpr() {

		operand1 = new Operand();
		operand2 = new Operand();

		operand1.integer = 0;
		operand2.integer = 0;
		// now made suitable to handle Descriptor attribute type
		if (type1 != null && type2 != null && type1.attrType == AttrType.attrDesc && type2.attrType == AttrType.attrDesc) {
			distance = 0;
		}
		next = null;
	}
}
