package me.dm7.barcodescanner.zbar.sample;

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
