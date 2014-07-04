package com.my.payment.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="operations")
public class Operation {
	
	public Operation(){}
	///Regular constructor
	public Operation(User user,User admin,Float amount  ){
		this.amount = amount;
		this.admin = admin;
		this.user = user;
	}
	///Constructor for tests
	public Operation(Integer id,Timestamp created,User user,User admin,Float amount  ){
		this.id = id;
		this.created = created;
		this.amount = amount;
		this.admin = admin;
		this.user = user;
	}

	@Id
    @Column(name = "id")
    @GeneratedValue
    private Integer id;
	
	@Column(name = "created", nullable = false)
	private Date created = new Date();
	
	@Column(name = "amount", nullable = false, columnDefinition="Decimal(7,2)")
	private Float amount;
	///Many operations can have one admin so no need to cascade admin on operation
	@ManyToOne
    @JoinTable(name="admins_operations",  
        joinColumns = {@JoinColumn(name="operation_id", referencedColumnName="id")},  
        inverseJoinColumns = {@JoinColumn(name="admin_id", referencedColumnName="id")}  
    )  
    private User admin;
	///Many operations can have one user so no need to cascade user on operation
	@ManyToOne
    @JoinTable(name="users_operations",  
        joinColumns = {@JoinColumn(name="operation_id", referencedColumnName="id")},  
        inverseJoinColumns = {@JoinColumn(name="user_id", referencedColumnName="id")}  
    )  
    private User user;
	///Hash code and equals methods does not include ID, because it is unique.
	///But entities with all other fields equal are equal
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((admin == null) ? 0 : admin.hashCode());
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Operation other = (Operation) obj;
		if (admin == null) {
			if (other.admin != null)
				return false;
		} else if (!admin.equals(other.admin))
			return false;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public User getAdmin() {
		return admin;
	}

	public void setAdmin(User admin) {
		this.admin = admin;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
    
	
}
