/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, DefaultTip.java is part of JTStand.
 *
 * JTStand is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JTStand is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JTStand.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jdesktop.swingx.tips;

import org.jdesktop.swingx.tips.TipOfTheDayModel.Tip;

/**
 * Default {@link org.jdesktop.swingx.tips.TipOfTheDayModel.Tip} implementation.<br>
 * 
 * @author <a href="mailto:fred@L2FProd.com">Frederic Lavigne</a>
 */
public class DefaultTip implements Tip {

  private String name;
  private Object tip;

  public DefaultTip() {   
  }
  
  public DefaultTip(String name, Object tip) {
    this.name = name;
    this.tip = tip;
  }

  public Object getTip() {
    return tip;
  }

  public void setTip(Object tip) {
    this.tip = tip;
  }

  public String getTipName() {
    return name;
  }

  public void setTipName(String name) {
    this.name = name;
  }
  
  @Override
  public String toString() {
    return getTipName();
  }
  
}