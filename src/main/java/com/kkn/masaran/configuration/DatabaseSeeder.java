package com.kkn.masaran.configuration;

import com.kkn.masaran.model.Businessman;
import com.kkn.masaran.model.Juragan;
import com.kkn.masaran.model.RawMaterial;
import com.kkn.masaran.model.UserRole;
import com.kkn.masaran.repository.BusinessmanRepository;
import com.kkn.masaran.repository.JuraganRepository;
import com.kkn.masaran.repository.RawMaterialRepository;
import com.kkn.masaran.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
public class DatabaseSeeder {

    @Autowired
    BusinessmanRepository businessmanRepository;
    @Autowired
    JuraganRepository juraganRepository;
    @Autowired
    UserRoleRepository userRoleRepository;
    @Autowired
    RawMaterialRepository rawMaterialRepository;

    @PostConstruct
    private void initTestData(){
        Businessman businessman = new Businessman();
        Juragan juragan = new Juragan();
        UserRole userRole = new UserRole();

        businessman = new Businessman();
        userRole = new UserRole();
        businessman.setBusinessName("UMKM");
        businessman.setUsername("Ardiumkm");
        businessman.setPassword("12345");
        businessman.setAddress("Jalan Enak");
        businessman.setEmail("umkm@gmail.com");
        businessman.setDescription("UMKM");
        businessman.setPhoneNumber("086537236173");
        userRole.setUsername("Ardiumkm");
        userRole.setRole("ROLE_UMKM");
        businessmanRepository.save(businessman);
        userRoleRepository.save(userRole);

        juragan = new Juragan();
        userRole = new UserRole();
        juragan.setJuraganName("admin");
        juragan.setUsername("admin");
        juragan.setPassword("12345");
        juragan.setEmail("juragan@gmail.com");
        juragan.setDescription("Juragan");
        userRole.setUsername("admin");
        userRole.setRole("ROLE_JURAGAN");
        juraganRepository.save(juragan);
        userRoleRepository.save(userRole);

        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setName("Roti tawar");
        rawMaterial.setPhoto("https://upload.wikimedia.org/wikipedia/commons/b/b3/Various_grains.jpg");
        rawMaterial.setDescription("Roti dengan gandum utuh dan organik");
        rawMaterial.setJuragan(juragan);
        rawMaterial.setPrice("1-3000|10-2800|100-2500|1000-2000");
        rawMaterial.setStock(250);
        rawMaterialRepository.save(rawMaterial);
    }
}
