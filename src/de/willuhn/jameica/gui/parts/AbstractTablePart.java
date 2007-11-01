/**********************************************************************
 * $Source: /cvsroot/jameica/jameica/src/de/willuhn/jameica/gui/parts/AbstractTablePart.java,v $
 * $Revision: 1.6 $
 * $Date: 2007/11/01 21:07:35 $
 * $Author: willuhn $
 * $Locker:  $
 * $State: Exp $
 *
 * Copyright (c) by willuhn software & services
 * All rights reserved
 *
 **********************************************************************/

package de.willuhn.jameica.gui.parts;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Vector;

import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.formatter.Formatter;
import de.willuhn.jameica.system.Settings;
import de.willuhn.logging.Logger;

/**
 * Abstrakte Basis-Klasse von Tabellen-aehnlichen Parts.
 */
public abstract class AbstractTablePart implements Part
{
  protected ContextMenu menu               = null;
  protected boolean changeable             = false;
  protected boolean rememberColWidth       = false;
  protected boolean rememberOrder          = false;
  protected Vector columns                 = new Vector();
  protected final static Settings settings = new Settings(AbstractTablePart.class);

  /**
   * Fuegt der Tabelle eine neue Spalte hinzu.
   * @param title Name der Spaltenueberschrift.
   * @param field Name des Feldes aus dem dbObject, der angezeigt werden soll.
   */
  public void addColumn(String title, String field)
  {
    addColumn(title,field,null);
  }
  
  /**
   * Fuegt der Tabelle eine neue Spalte hinzu und dazu noch einen Formatierer.
   * @param title Name der Spaltenueberschrift.
   * @param field Name des Feldes aus dem dbObject, der angezeigt werden soll.
   * @param f Formatter, der fuer die Anzeige des Wertes verwendet werden soll.
   */
  public void addColumn(String title, String field, Formatter f)
  {
    addColumn(title,field,f,false);
  }

  /**
   * Fuegt der Tabelle eine neue Spalte hinzu und dazu noch einen Formatierer.
   * @param title Name der Spaltenueberschrift.
   * @param field Name des Feldes aus dem dbObject, der angezeigt werden soll.
   * @param f Formatter, der fuer die Anzeige des Wertes verwendet werden soll.
   * @param changeable legt fest, ob die Werte in dieser Spalte direkt editierbar sein sollen.
   * Wenn der Parameter true ist, dann sollte der Tabelle via <code>addChangeListener</code>
   * ein Listener hinzugefuegt werden, der benachrichtigt wird, wenn der Benutzer einen
   * Wert geaendert hat. Es ist anschliessend Aufgabe des Listeners, den geaenderten
   * Wert im Fachobjekt zu uebernehmen.
   */
  public void addColumn(String title, String field, Formatter f, boolean changeable)
  {
    this.columns.add(new Column(field,title,f,changeable));
    this.changeable |= changeable;
  }
  
  /**
   * Liefert die Sortierreihenfolge der Spalten.
   * @return Int-Array mit der Reihenfolge oder <code>null</code>.
   */
  int[] getColumnOrder()
  {
    try
    {
      // Mal schauen, ob wir eine gespeicherte Sortierung haben
      String order = settings.getString("column.order." + getID(),null);
      if (order == null || order.length() == 0 || order.indexOf(",") == -1)
        return null;
      String[] s = order.split(",");
      if (s.length != this.columns.size())
      {
        Logger.warn("column count missmatch. column order: " + order + ", columns: " + this.columns.size());
        return null;
      }
      int[] cols = new int[s.length];
      for (int i=0;i<s.length;++i)
      {
        cols[i] = Integer.parseInt(s[i]);
      }
      return cols;
    }
    catch (Exception e)
    {
      Logger.warn("unable to determine column order: " + e.toString());
    }
    return null;
  }
  
  /**
   * Speichert die Reihenfolge der Spalten.
   * @param cols die Reihenfolge der Spalten.
   */
  void setColumnOrder(int[] cols)
  {
    try
    {
      String s = "";
      if (cols != null && cols.length > 0)
      {
        for (int i=0;i<cols.length;++i)
        {
          s += Integer.toString(cols[i]);
          if (i+1<cols.length)
            s += ",";
        }
      }
      settings.setAttribute("column.order." + getID(),s.length() == 0 ? null : s);
    }
    catch (Exception e)
    {
      Logger.error("unable to save column order",e);
    }
  }

  /**
   * Fuegt ein KontextMenu hinzu.
   * @param menu das anzuzeigende Menu.
   */
  public void setContextMenu(ContextMenu menu)
  {
    this.menu = menu;
  }
  
  /**
   * Liefert eine eindeutige ID fuer genau diese Tabelle.
   * @return die ID.
   * @throws Exception
   */
  abstract String getID() throws Exception;

  /**
   * Liefert die Fach-Objekte der Tabelle.
   * @return Liste der Fachobjekte.
   * @throws RemoteException
   */
  public abstract List getItems() throws RemoteException;

  /**
   * Legt fest, ob sich die Tabelle die Spaltenbreiten merken soll.
   * @param remember true, wenn sie sich die Spaltenbreiten merken soll.
   */
  public void setRememberColWidths(boolean remember)
  {
    this.rememberColWidth = remember;
  }
  
  /**
   * Legt fest, ob sich die Tabelle die Sortierreihenfolge merken soll.
   * @param remember true, wenn sie sich die Reihenfolge merken soll.
   */
  public void setRememberOrder(boolean remember)
  {
    this.rememberOrder = remember;
  }
  
  protected static class Column
  {
    protected String columnId     = null;
    protected String name         = null;
    protected Formatter formatter = null;
    protected boolean canChange   = false;
    
    protected Column(String id, String name, Formatter f, boolean changeable)
    {
      this.columnId   = id;
      this.name       = name;
      this.formatter  = f;
      this.canChange  = changeable;
    }
  }
}


/*********************************************************************
 * $Log: AbstractTablePart.java,v $
 * Revision 1.6  2007/11/01 21:07:35  willuhn
 * @N Spalten von Tabellen und mehrspaltigen Trees koennen mit mit Drag&Drop umsortiert werden. Die Sortier-Reihenfolge wird automatisch gespeichert und wiederhergestellt
 *
 * Revision 1.5  2007/04/15 21:31:33  willuhn
 * @N "getItems()" in TreePart
 *
 * Revision 1.4  2007/03/28 16:59:04  willuhn
 * @C Eine Settings-Instanz fuer alle TableParts/TreeParts
 *
 * Revision 1.3  2007/03/22 22:36:47  willuhn
 * @N Contextmenu in Trees
 * @C Kategorie-Baum in separates TreePart ausgelagert
 *
 * Revision 1.2  2007/03/21 18:42:16  willuhn
 * @N Formatter fuer TreePart
 * @C mehr gemeinsamer Code in AbstractTablePart
 *
 * Revision 1.1  2007/03/08 18:55:49  willuhn
 * @N Tree mit Unterstuetzung fuer Spalten
 *
 **********************************************************************/