package org.zerock.myapp.entity;

import java.io.Serial;
import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.zerock.myapp.listener.CommonEntityLifecyleListener;

import lombok.Data;


@Data
@EntityListeners(CommonEntityLifecyleListener.class)
@Entity(name = "ShopperProduct")
@Table(name="shopper_product")
@IdClass(ShopperProductId.class)
public class ShopperProduct implements Serializable {
	@Serial private static final long serialVersionUID = 1L;
	
	@Basic(optional = false)
	private Integer amount;

	@Id
	@ManyToOne(targetEntity = Shopper3.class)
	@JoinColumn(name = "shopper_id")
	private Shopper3 shopper;	// FK
	
	@Id
	@ManyToOne(targetEntity = Product3.class)
	@JoinColumn(name = "product_id")
	private Product3 product;	// FK
} // end class
