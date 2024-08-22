package com.rbac.common.entity.request;

import com.rbac.common.entity.po.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author Lzzh
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MakeRouterRequest implements Serializable {
    private List<Permission> permissionList;
    private Long pid;
}
