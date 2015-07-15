/*
 Copyright © 2014 by eBusiness Information
 All rights reserved. This source code or any portion thereof
 may not be reproduced or used in any manner whatsoever
 without the express written permission of eBusiness Information.
*/
package com.loicortola.training.archiveaggregator.common.util;

import java.util.List;

/**
 * Created by Loïc Ortola on 13/07/2015.
 */
public abstract class StringUtils {

  /**
   * Checks whether a filename is valid or not: must be alpha numeric, and accept - and _ too.
   *
   * @param fileName the name of the file
   * @return true if the name is valid, false otherwise
   */
  public static boolean isValidFileName(String fileName) {
    String valid = "[\\p{Alnum}\\s_-]+";
    return fileName.matches(valid);
  }

  /**
   * Transform a list of string into a single string.
   *
   * @param items     the list of strings
   * @param start     the starting string
   * @param separator the separator
   * @param end       the ending string
   * @return the concatenated string
   */
  public static String stringListBuilder(List<String> items, String start, String separator, String end) {
    StringBuilder sb = new StringBuilder();
    if (items == null) {
      return sb.append(start).append(end).toString();
    } else if (items.size() == 1) {
      return sb.append(start).append(items.get(0)).append(end).toString();
    } else {
      int size = items.size();
      for (int i = 0; i < size; i++) {
        String item = items.get(i);
        if (i == 0) {
          sb.append(start);
        }
        sb.append(item);
        if (i < size - 1) {
          sb.append(separator);
        } else {
          sb.append(end);
        }
      }
      return sb.toString();
    }
  }

  /**
   * Retrieve file extension without the file name.
   *
   * @param fileName the complete file file name
   * @return the fileName only
   */
  public static String getExtension(String fileName) {
    final String[] fileNameParts = fileName.split("\\.");
    String lastPart = fileNameParts[fileNameParts.length - 1];
    return lastPart;
  }

  /**
   * Retrieve file name without extension from name.
   *
   * @param name the name
   * @return the name only
   */
  public static String getNameWithoutExtension(String name) {
    final String[] fileNameParts = name.split("\\.");
    String firstPart = fileNameParts[0];
    return firstPart;
  }

}
