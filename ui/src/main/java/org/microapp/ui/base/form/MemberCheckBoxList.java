package org.microapp.ui.base.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.microapp.Diary.model.MemberInfo;

public class MemberCheckBoxList extends Panel {
	private static final long serialVersionUID = 1L;

	private final String CHECK_BOXES_ID = "members";
	
	private List<MemberInfo> values;
	private ArrayList<MemberInfo> selected;
	
	public MemberCheckBoxList(String id, List<MemberInfo> values) {
		super(id);
		selected = new ArrayList<MemberInfo>();
		this.values = values;
		addComponents();
	}
	
	private void addComponents() {
		IChoiceRenderer<MemberInfo> memInfoRenderer = new ChoiceRenderer<MemberInfo>("fullName");
		CheckBoxMultipleChoice<MemberInfo> members = new CheckBoxMultipleChoice<MemberInfo>(CHECK_BOXES_ID, new Model(selected), values, memInfoRenderer);
		add(members);
	}
	
	public List<MemberInfo> getSelected() {
		return selected;
	}

}
