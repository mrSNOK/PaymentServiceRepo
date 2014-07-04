package com.my.payment.exceptions;

public class RoleNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 7879324325107902804L;
	private final Integer roleId;
	private final String roleName;
    ///Constructor for getting role by ID exception
    public RoleNotFoundException(Integer roleId) {
    	this.roleId = roleId;
    	roleName = null;
    }
    ///Constructor for getting role by name exception
    public RoleNotFoundException(String roleName) {
    	this.roleName = roleName;
    	roleId = null;
    }
    public Integer getRoleId() {
        return roleId;
    }
    public String getRoleName() {
        return roleName;
    }
}
