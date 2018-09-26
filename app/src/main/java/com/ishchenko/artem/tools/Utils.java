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

package com.ishchenko.artem.tools;

import java.io.File;

public class Utils
{
  public final static String jpeg = "jpeg";
  public final static String jpg  = "jpg";
  public final static String gif  = "gif";
  public final static String tiff = "tiff";
  public final static String tif  = "tif";
  public final static String xml  = "xml";

  /*
  * Get the extension of a file.
  */
  public static String getFileExtension(File file) {
    String extension = "";

    try {
      if (file != null && file.exists()) {
        String name = file.getName();
        extension = name.substring(name.lastIndexOf("."));
      }
    } catch (Exception e) {
      extension = "";
    }

    return extension;
  }
}
