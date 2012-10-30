/*
 * tuProlog - Copyright (C) 2001-2002  aliCE team at deis.unibo.it
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package alice.tuprolog;

import alice.util.ReadOnlyLinkedList;
import java.util.*;

/**
 * Customized HashMap for storing clauses in the TheoryManager
 *
 * @author ivar.orstavik@hist.no
 *
 * Reviewed by Paolo Contessi
 */
@SuppressWarnings("serial")
class ClauseDatabase extends HashMap<String,FamilyClausesList> implements Iterable<ClauseInfo> {

	void addFirst(String key, ClauseInfo d) {
		FamilyClausesList family = get(key);
		if (family == null)
			put(key, family = new FamilyClausesList());
		family.addFirst(d);
	}

	void addLast(String key, ClauseInfo d) {
		FamilyClausesList family = get(key);
		if (family == null)
			put(key, family = new FamilyClausesList());
		family.addLast(d);
	}

	FamilyClausesList abolish(String key) 
	{
		return (FamilyClausesList) remove(key);
	}

	/**
	 * Retrieves a list of the predicates which has the same name and arity
	 * as the goal and which has a compatible first-arg for matching.
	 *
	 * @param headt The goal
	 * @return  The list of matching-compatible predicates
	 */
	List<ClauseInfo> getPredicates(Term headt) {
		FamilyClausesList family = (FamilyClausesList) get(((Struct) headt).getPredicateIndicator());
		if (family == null){
			return new ReadOnlyLinkedList<ClauseInfo>();
		}
		return family.get(headt);
	}

	/**
	 * Retrieves the list of clauses of the requested family
	 *
	 * @param key   Goal's Predicate Indicator
	 * @return      The family clauses
	 */
	List<ClauseInfo> getPredicates(String key){
		FamilyClausesList family = (FamilyClausesList) get(key);
		if(family == null){
			return new ReadOnlyLinkedList<ClauseInfo>();
		}
		return new ReadOnlyLinkedList<ClauseInfo>(family);
	}

	public Iterator<ClauseInfo> iterator() {
		return new CompleteIterator(this);
	}

	private static class CompleteIterator implements Iterator<ClauseInfo> {
		Iterator<FamilyClausesList> values;
		Iterator<ClauseInfo> workingList;
		//private boolean busy = false;

		public CompleteIterator(ClauseDatabase clauseDatabase) {
			values = clauseDatabase.values().iterator();
		}

		public boolean hasNext() {
			if (workingList != null && workingList.hasNext())
				return true;
			if (values.hasNext()) {
				workingList = values.next().iterator();
				return hasNext(); //start again on next workingList
			}
			return false;
		}

		public synchronized ClauseInfo next() {
			if (workingList.hasNext())
				return workingList.next();
			else return null;
		}

		public void remove() {
			workingList.remove();
		}
	}

}