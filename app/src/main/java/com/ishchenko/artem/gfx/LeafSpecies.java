/*
 * Leaves Recognition - a neuronal network based recognition of leaf images
 * Copyright (C) 2001 Jens Langner, LightSpeed Communications GbR
 *
 * LightSpeed Communications GbR
 * Lannerstrasse 1
 * 01219 Dresden
 * Germany
 * http://www.light-speed.de/
 * <Jens.Langner@light-speed.de>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * $Id$
 */

package com.ishchenko.artem.gfx;

import java.util.ArrayList;

import lombok.EqualsAndHashCode;

/**
* class that represents a Species of a leaf Image
*
*/
@EqualsAndHashCode
public class LeafSpecies
{
  private String speciesName;
  private ArrayList<LeafImage> images;
  private double[] ID;

  public LeafSpecies(String name)
  {
    speciesName = name;
    images = new ArrayList<>();
    ID = new double[0];
  }

  public void setName(String name)
  {
    speciesName = name;
  }

  public String getName()
  {
    return speciesName;
  }

  public void addImage(LeafImage limage)
  {
    images.add(limage);
  }

  public ArrayList<LeafImage> getImages()
  {
    return images;
  }

  public LeafImage getImage(int idx)
  {
    return (LeafImage)images.get(idx);
  }

  public int numImages()
  {
    return images.size();
  }

  public void setID(double[] id)
  {
    this.ID = id;
  }

  public double[] getID()
  {
    return ID;
  }

  /**
  * toString() method
  *
  * this method is needed for the JTree to display the correct name
  * of this object
  */
  public String toString()
  {
    return speciesName+" ("+images.size()+")";
  }
}