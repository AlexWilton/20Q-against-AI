package org.encog.ml.bayesian;

import java.util.ArrayList;
import java.util.List;

import org.encog.ml.bayesian.table.BayesianTable;

public class BayesianEvent {
	
	private final String label;
	private final List<BayesianEvent> parents = new ArrayList<BayesianEvent>();
	private final List<BayesianEvent> children = new ArrayList<BayesianEvent>();
	private final String[] choices;
	private BayesianTable table;
	
	public BayesianEvent(String theLabel, String[] theChoices) {
		this.label = theLabel;
		this.choices = theChoices;		
	}
	
	public BayesianEvent(String theLabel) {
		this(theLabel,BayesianNetwork.CHOICES_TRUE_FALSE);
	}

	/**
	 * @return the parents
	 */
	public List<BayesianEvent> getParents() {
		return parents;
	}
	/**
	 * @return the children
	 */
	public List<BayesianEvent> getChildren() {
		return children;
	}


	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	public void addChild(BayesianEvent e) {
		this.children.add(e);
	}	
	
	public void addParent(BayesianEvent e) {
		this.parents.add(e);
	}
	
	public boolean hasParents() {
		return this.parents.size()>0;
	}
	
	public boolean hasChildren() {
		return this.parents.size()>0;
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder();
		
		result.append("P(");
		result.append(this.getLabel());
		
		if( hasParents() ) {
			result.append("|");
		}
		
		boolean first = true;
		for(BayesianEvent e : this.parents) {
			if( !first )
				result.append(",");
			first = false;
			result.append(e.getLabel());
		}
		
		result.append(")");
		return result.toString();
	}
	
	public int calculateParameterCount() {
		int result = 1;
		
		for(BayesianEvent parent: this.parents) {
			result *= parent.getChoices().length;
		}
		
		return result;
	}

	/**
	 * @return the choices
	 */
	public String[] getChoices() {
		return choices;
	}

	/**
	 * @return the table
	 */
	public BayesianTable getTable() {
		return table;
	}

	public void finalizeStructure() {
		this.table = new BayesianTable(this);		
	}

	public void validate() {
		this.table.validate();
	}

	public boolean isBoolean() {
		return this.choices==BayesianNetwork.CHOICES_TRUE_FALSE;
	}	
}