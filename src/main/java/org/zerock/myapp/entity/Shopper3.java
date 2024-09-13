package org.zerock.myapp.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Vector;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.zerock.myapp.listener.CommonEntityLifecyleListener;

import lombok.Data;
import lombok.ToString;


@Data
@EntityListeners(CommonEntityLifecyleListener.class)
@Entity(name = "Shopper3")
@Table(name = "shopper3")
public class Shopper3 implements Serializable{
	@Serial private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "shopper_id")
	private Long id;
	
	@Basic(optional = false)
	private String name;
	
	@OneToMany(mappedBy = "shopper", targetEntity = ShopperProduct.class)
	@ToString.Exclude
	private List<ShopperProduct> ShopperProducts = new Vector<>();
} // end class
