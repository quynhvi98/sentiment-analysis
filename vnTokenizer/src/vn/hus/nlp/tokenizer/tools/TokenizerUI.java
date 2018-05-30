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
package vn.hus.nlp.tokenizer.tools;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import vn.hus.nlp.tokenizer.DefaultTokenizerFactory;
import vn.hus.nlp.tokenizer.TokenizerFactory;
import vn.hus.nlp.tokenizer.TokenizerUtils;

public class TokenizerUI extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String resourceDir;
	private TokenizerFactory tokenizerFactory;
	private JPanel pnlButton = new JPanel();
	private JButton btnTokenize = new JButton();
	private JButton btnUntokenize = new JButton();
	private JTextArea txtDocument = new JTextArea();
	
	public TokenizerUI(String resourceDir) {
		this.resourceDir = resourceDir;
		initComponents();
		initTokenizerFactory();
		initEvents();
	}

	private void initComponents() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		pnlButton.setLayout(new FlowLayout());
		
		btnTokenize.setText("Tách từ");
		btnTokenize.setMnemonic('t');
		pnlButton.add(btnTokenize);
		
		btnUntokenize.setText("Bỏ tách từ");
		btnUntokenize.setMnemonic('b');
		pnlButton.add(btnUntokenize);
		
		getContentPane().add(pnlButton, BorderLayout.NORTH);
		
		txtDocument.setColumns(60);
		txtDocument.setRows(35);
		getContentPane().add(new JScrollPane(txtDocument), BorderLayout.CENTER);
		
		pack();
	}
	
	private void initTokenizerFactory() {
		try {
			tokenizerFactory = new DefaultTokenizerFactory(resourceDir);
		} catch (IOException e) {
			System.out.println(e);
			JOptionPane.showMessageDialog(this, 
					"Error while initializing tokenizer factory: " + e.getMessage() + 
					"\nThe program is shuting down.");
			System.exit(-1);
		}		
	}

	private void initEvents() {
		btnTokenize.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				tokenize();
			}
		});
		btnUntokenize.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				untokenize();
			}
		});
	}

	protected void tokenize() {
		txtDocument.setText(TokenizerUtils.toString(tokenizerFactory.getTokenizer(txtDocument.getText())));
	}

	protected void untokenize() {
		txtDocument.setText(txtDocument.getText().replace('_', ' '));
	}

	public static void main(final String[] args) throws InterruptedException,
			InvocationTargetException {
		SwingUtilities.invokeAndWait(new Runnable() {
		
			@Override
			public void run() {
				String resourceDir = args.length >= 1 ? args[0]
						: "vnTokenizer/resources";
				new TokenizerUI(resourceDir).setVisible(true);
			}
		});
	}
	
}
