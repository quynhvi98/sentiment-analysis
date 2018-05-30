/*******************************************************************************
 * Copyright (c) 2012 ePi Technologies.
 * 
 * This file is part of VNLP: a Natural Language Processing framework 
 * for Vietnamese.
 * 
 * VNLP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * VNLP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with VNLP.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package vn.com.epi.gate.refdiff;

import gate.Document;
import gate.Gate;
import gate.creole.AbstractVisualResource;
import gate.creole.metadata.CreoleResource;
import gate.creole.metadata.GuiType;
import gate.gui.MainFrame;
import gate.util.GateException;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

@CreoleResource(name = "Reference Annotation Differ", guiType = GuiType.SMALL,
	resourceDisplayed = "gate.Document"/*, 
	helpURL = "http://gate.ac.uk/userguide/sec:eval:corpusqualityassurance"*/)
public class ReferenceAnnotationDiffPlugin extends AbstractVisualResource {
	
	private String referenceSet;
	private Document document;
	private JButton btnDiff = new JButton();
	private ReferenceAnnotationDiffGUI diffGui;

	
	public ReferenceAnnotationDiffPlugin() {
		setLayout(new FlowLayout(FlowLayout.LEFT));
		btnDiff.setText("Diff");
		btnDiff.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (document != null) {
					// use the document displayed in the view to compute the
					// differences
					diffGui = new ReferenceAnnotationDiffGUI("Annotation Diff Tool",
							document.getName(), document.getName(), null, null,
							null, null, referenceSet);
				} else {
					diffGui = new ReferenceAnnotationDiffGUI("Annotation Diff Tool");
				}
				diffGui.pack();
				diffGui.setLocationRelativeTo(MainFrame.getInstance());
				diffGui.setVisible(true);
			}
		});
		add(btnDiff);
	}

	@Override
	public void setTarget(Object target) {
		if (target instanceof Document) {
			document = (Document) target;
		} else {
			document = null;
		}
	}
	
	public String getReferenceSet() {
		return referenceSet;
	}

	public void setReferenceSet(String referenceSet) {
		this.referenceSet = referenceSet;
	}

	public static void main(String[] args) throws GateException {
		gate.Main.main(args);
	}

}
