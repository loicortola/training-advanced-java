/*
Copyright © 2014 by eBusiness Information
All rights reserved. This source code or any portion thereof
may not be reproduced or used in any manner whatsoever
without the express written permission of eBusiness Information.
*/
package com.loicortola.training.archiveaggregator.webapp.webservice;

import com.loicortola.training.archiveaggregator.common.dao.ArchiveDAO;
import com.loicortola.training.archiveaggregator.common.model.Archive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Loïc Ortola on 11/07/2015.
 */
@RestController
@RequestMapping("/")
public class InitWebService {

  @Autowired
  ArchiveDAO archiveDAO;

  /**
   * Generate a new archive.
   *
   * @return The archive set with the archive uuid
   */
  @RequestMapping(value = "/init", method = RequestMethod.GET)
  public Archive initGeneration() {
    Archive archive = new Archive();
    archiveDAO.save(archive);
    return archive;
  }

}
