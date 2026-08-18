package com.keystone.service;

import com.keystone.entity.Site;
import com.keystone.repository.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
public class SiteServiceImpl implements SiteService {

    @Autowired
    private SiteRepository siteRepository;

    @Override
    public Site addSite(Site site) {
        return siteRepository.save(site);
    }

    @Override
    public List<Site> getAllSites() {
        return siteRepository.findAll();
    }

    @Override
    public Site getSiteById(Long id) {
        return siteRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Site not found with id: " + id
                        )
                );
    }
    @Override
    public Site updateSite(Long id, Site site) {

        Site existingSite = siteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Site not found with id: " + id));

        existingSite.setSiteName(site.getSiteName());
        existingSite.setAddress(site.getAddress());
        existingSite.setCity(site.getCity());
        existingSite.setState(site.getState());
        existingSite.setPincode(site.getPincode());
        existingSite.setCustomer(site.getCustomer());

        return siteRepository.save(existingSite);
    }

    @Override
    public void deleteSite(Long id) {
        siteRepository.deleteById(id);
    }
}
