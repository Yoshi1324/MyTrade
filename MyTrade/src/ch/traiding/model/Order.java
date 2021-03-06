package ch.traiding.model;

/**
* @author  Joshua	Blöchliger
* @version 1.1
*/
public class Order {

    private Integer id = 0;

    private Stock product;

    private User seller;

    private double price;

    public Order(){
    	seller = new User();
    	product = new Stock();
    }
    
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Stock getProduct() {
        return product;
    }

    public void setProduct(Stock product) {
        this.product = product;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
