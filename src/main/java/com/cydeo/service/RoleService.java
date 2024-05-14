package com.cydeo.service;

import com.cydeo.dto.RoleDTO;
import java.util.List;

public interface RoleService {

    RoleDTO save(RoleDTO role);
    RoleDTO findById(long id);
    List<RoleDTO> findAll();
    void deleteById(long id);
}
