package me.dm7.barcodescanner.zbar.sample;

import java.util.Comparator;

/**
 * Created by Entazis on 2017. 06. 08..
 */

public class Product {
    private String ProductName;
    private String ProductBrand;
    private String ProductType;
    private String Barcode;
    private String BarcodeFormat;
    private String ProductPrice;
    private String Afa;
    private int Amount;

    public void setProductName(String name){
        ProductName = name;
    }
    public String getProductName(){
        return ProductName;
    }

    public void setProductBrand(String brand){
        ProductBrand = brand;
    }
    public String getProductBrand(){
        return ProductBrand;
    }

    public void setProductType(String type){
        ProductType = type;
    }
    public String getProductType(){
        return ProductType;
    }

    public void setBarcode(String barcode){
        Barcode = barcode;
    }
    public void setBarcode(){
        Barcode = "0";
    }
    public String getBarcode(){
        return Barcode;
    }

    public void setBarcodeFormat(String barcodeformat){
        BarcodeFormat = barcodeformat;
    }
    public String getBarcodeFormat(){
        return BarcodeFormat;
    }

    public void setProductPrice(String price){
        ProductPrice = price;
    }
    public String getProductPrice(){
        return ProductPrice;
    }

    public void setProductAfa(String afa){
        Afa = afa;
    }
    public String getProductAfa(){
        return Afa;
    }

    public void setAmount(int amount){
        Amount = amount;
    }
    public void setAmount(){
        Amount = 1;
    }
    public int getAmount(){
        return Amount;
    }
}

class ProductCompare implements Comparator<Product> {

    @Override
    public int compare(Product o1, Product o2) {
        // write comparison logic here like below , it's just a sample
        if(o1.getProductBrand().equals(o2.getProductBrand())){
            if (o1.getProductName().equals(o2.getProductName())){
                if(o1.getProductType().equals(o2.getProductType())){
                    return o1.getProductType().compareTo(o2.getProductType());
                }
                else return o1.getProductType().compareTo(o2.getProductType());
            }
            else return o1.getProductName().compareTo(o2.getProductName());
        }
        else return o1.getProductBrand().compareTo(o2.getProductBrand());
    }
}
