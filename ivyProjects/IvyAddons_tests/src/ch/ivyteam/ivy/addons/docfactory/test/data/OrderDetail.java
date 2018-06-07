package ch.ivyteam.ivy.addons.docfactory.test.data;

import java.io.Serializable;
import java.util.Date;

public class OrderDetail implements Serializable {
	
	private static final long serialVersionUID = 5607706116687561339L;
	
	private Date date;
	private Order order;
	private String comment;
	
	public OrderDetail(Order order) {
		this.order = order;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	

}
