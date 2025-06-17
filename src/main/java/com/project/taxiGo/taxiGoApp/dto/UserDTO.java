package com.project.taxiGo.taxiGoApp.dto;

import com.project.taxiGo.taxiGoApp.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private String email;
    private String name;
    private String phone;
    private Set<Roles> roles;
}
