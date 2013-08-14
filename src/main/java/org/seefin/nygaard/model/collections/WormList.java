package org.seefin.nygaard.model.collections;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Write-once, read-many List<br/>
 * List implementation that only allows query methods, and ensures that methods
 * such as subList() returns a collection that does not allow modification
 * <p/>
 * Note: this collection cannot implement the java.util.List interface, as this would
 * not be type-safe (mutating operations would be visible, yet have to throw an
 * exception)
 * 	
 * @param <T> Data type for List values
 * 
 * @author phillipsr
 *
 */
public final class WormList<T>
	implements Iterable<T>, Serializable
{
	private final List<T> list;
	
	public WormList ( Collection<T> data)
	{
		if ( data == null || data.isEmpty () == true)
		{
			throw new IllegalArgumentException ("ReadOnlyList requires initial data");
		}
		list = Collections.unmodifiableList (new ArrayList<>(data));
	}
	
	public int size ()
	{
		return list.size();
	}

	public boolean isEmpty ()
	{
		return list.isEmpty ();
	}

	public boolean contains ( T o)
	{
		return list.contains ( o);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws UnsupportedOperationException if caller attempts to modify
	 * 			the underlying list via the returned iterator (e.g., remove()) 
	 */
	@Override
	public Iterator<T> iterator ()
	{
		return list.iterator ();
	}

	public Object[] toArray ()
	{
		return list.toArray ();
	}

	public <E> E[] toArray ( E[] prototype)
	{
		return list.toArray ( prototype);
	}

	/**
	 * Are all the elements in the collection <code>c</code>
	 * present in this list?
	 * @param c collection to be checked for containment in this list  
	 * @return true if this list contains all of the elements of the specified collection 
	 * @throws ClassCastException if the types of one or more elements in the specified
	 * 				collection are incompatible with this list (optional) 
	 * @throws NullPointerException if the specified collection contains one or more null
	 * 				elements and this list does not permit null elements (optional), 
	 * 				or if the specified collection is null
	 */
	public boolean containsAll ( Collection<T> c)
	{
		return list.containsAll ( c);
	}

	public T get ( int index)
	{
		return list.get ( index);
	}

	public int indexOf ( T o)
	{
		return list.indexOf ( o);
	}

	public int lastIndexOf ( T o)
	{
		return list.lastIndexOf ( o);
	}

	public ListIterator<T> listIterator ()
	{
		return list.listIterator ();
	}

	public ListIterator<T> listIterator ( int index)
	{
		return list.listIterator ( index);
	}

	public WormList<T> subList ( int fromIndex, int toIndex)
	{
		return new WormList<> ( list.subList ( fromIndex, toIndex));
	}
	
	/**
	 * @return a regular (e.g., read/write) copy of this list
	 */
	public List<T>
	toList()
	{
		return new ArrayList<>(list);
	}
	
	@Override
	public String
	toString()
	{
		return list.toString ();
	}
	
	@Override
	public int
	hashCode()
	{
		return list.hashCode();
	}

}
