/**********************************************************************
 * $Source: /cvsroot/jameica/jameica/src/de/willuhn/jameica/gui/parts/Attic/FontInput.java,v $
 * $Revision: 1.1 $
 * $Date: 2004/04/01 00:23:24 $
 * $Author: willuhn $
 * $Locker:  $
 * $State: Exp $
 *
 * Copyright (c) by willuhn.webdesign
 * All rights reserved
 *
 **********************************************************************/
package de.willuhn.jameica.gui.parts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FontDialog;

import de.willuhn.jameica.Application;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.Style;

/**
 * Eingabefeld, zur Auswahl einer Schrift.
 * @author willuhn
 */
public class FontInput extends AbstractInput
{

  private Composite comp;
  private CLabel label;
  private Button button;
  private boolean enabled = true;

	private Font font;

  /**
   * Erzeugt ein neues ContInput mit der angegebenen Schriftart.
   * @param font initial anzuzeigende Schriftart.
   */
  public FontInput(Font font)
  {
    this.font = font;
  }

  /**
   * @see de.willuhn.jameica.gui.parts.AbstractInput#getControl()
   */
  public Control getControl()
  {

		comp = new Composite(getParent(),SWT.NONE);
		comp.setBackground(Style.COLOR_BG);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight=0;
		layout.marginWidth=0;
		comp.setLayout(layout);
  
		Composite around = new Composite(comp,SWT.NONE);
		around.setBackground(Style.COLOR_BORDER);
		around.setLayout(new FormLayout());
		around.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		FormData comboFD = new FormData();
		comboFD.left = new FormAttachment(0, 1);
		comboFD.top = new FormAttachment(0, 1);
		comboFD.right = new FormAttachment(100, -1);
		comboFD.bottom = new FormAttachment(100, -1);
    
		Composite around2 = new Composite(around,SWT.NONE);
		around2.setBackground(Style.COLOR_WHITE);
		around2.setLayout(new FormLayout());
		around2.setLayoutData(comboFD);

		FormData comboFD2 = new FormData();
		comboFD2.left = new FormAttachment(0, 2);
		comboFD2.top = new FormAttachment(0, 2);
		comboFD2.right = new FormAttachment(100, -2);
		comboFD2.bottom = new FormAttachment(100, -2);
  
    label = new CLabel(around2, SWT.NONE);
		label.setLayoutData(comboFD2);
		label.setText("ABCDEFabcdef");
		label.setFont(font);

    button = new Button(comp,SWT.NONE);
    button.setText("...");
    button.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
    button.setAlignment(SWT.RIGHT);
		button.setEnabled(enabled);
    button.addMouseListener(new MouseAdapter()
    {
      public void mouseUp(MouseEvent e)
      {
        Application.getLog().debug("starting font choose dialog");
				FontDialog fd = new FontDialog(GUI.getShell());
				FontData f = fd.open();
				if (f == null)
					return;
				font = new Font(GUI.getDisplay(),f);
				label.setFont(font);
				label.redraw();
				label.forceFocus(); // das muessen wir machen, damit der CommentListener ausgeloest wird
      }
    });
 
    return comp;
  }

  /**
   * Liefert die aktuell ausgewaehlte Schriftart.
   * Rueckgabewert vom Typ <code>Font</code>.
   * @see de.willuhn.jameica.gui.parts.AbstractInput#getValue()
   */
  public Object getValue()
  {
    return font;
  }

  /**
   * @see de.willuhn.jameica.gui.parts.AbstractInput#setValue(java.lang.String)
   */
  public void setValue(Object value)
  {
    if (value == null)
      return;
		if (value instanceof Font)
		{
			this.font = (Font) value;
			label.setFont(font);
			label.redraw();
		}
  }

  /**
   * @see de.willuhn.jameica.gui.parts.AbstractInput#focus()
   */
  public void focus()
  {
    label.setFocus();
  }

  /**
   * @see de.willuhn.jameica.gui.parts.AbstractInput#disable()
   */
  public void disable()
  {
  	enabled = false;
  	if (button != null && !button.isDisposed())
	    button.setEnabled(false);
  }

  /**
   * @see de.willuhn.jameica.gui.parts.AbstractInput#enable()
   */
  public void enable()
  {
		enabled = true;
		if (button != null && !button.isDisposed())
	    button.setEnabled(true);
  }

}

/*********************************************************************
 * $Log: FontInput.java,v $
 * Revision 1.1  2004/04/01 00:23:24  willuhn
 * @N FontInput
 * @N ColorInput
 * @C improved ClassLoader
 * @N Tabs in Settings
 *
 **********************************************************************/