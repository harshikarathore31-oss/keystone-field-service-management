package com.keystone.service;

import com.keystone.entity.Site;

import java.util.List;

public interface SiteService {

    Site addSite(Site site);

    List<Site> getAllSites();

    Site getSiteById(Long id);

    Site updateSite(Long id, Site site);

    void deleteSite(Long id);
}