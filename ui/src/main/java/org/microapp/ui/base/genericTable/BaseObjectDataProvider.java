package org.microapp.ui.base.genericTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;

public class BaseObjectDataProvider<T extends Serializable> implements IDataProvider<T> {


	private static final long serialVersionUID = 1L;

	private IModel<List<T>> itemsModel;

	public BaseObjectDataProvider(List<T> items) {
		this.itemsModel = new ListModel<>(items);
	}
	
	public BaseObjectDataProvider(IModel<List<T>> itemsModel) {
		this.itemsModel = itemsModel;
	}
	
	@Override
	/**
	 * Iterator from, to.
	 */
	public Iterator<? extends T> iterator(long first, long last) {
		return itemsModel.getObject().subList((int)first, (int)last).iterator();
	}

	@Override
	public IModel<T> model(T arg0) {
		return new Model<T>(arg0);
	}

	@Override
	public long size() {
		return itemsModel.getObject() == null? 0 : itemsModel.getObject().size();
	}

	@Override
	public void detach() {
		//not sure what should be done here
		itemsModel.detach();
	}
	
	public void newData(IModel<List<T>> newModel) {
		this.itemsModel = newModel;
	}
	
}

