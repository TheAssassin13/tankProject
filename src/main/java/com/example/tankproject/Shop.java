package com.example.tankproject;

public class Shop {
    int lightammunitionprice;
    int mediumammunitionprice;
    int heavyammunitionprice;

    public Shop(){
        this.lightammunitionprice = Constants.AMMO_PRICE[0];
        this.mediumammunitionprice = Constants.AMMO_PRICE[1];
        this.heavyammunitionprice = Constants.AMMO_PRICE[2];
    }
    
    /*
    public void BuyLightAmmo(Player p, int amount){
        int SaveAmmo = p.tank.ammunition.get(0);
        int TotalPrice = this.lightammunitionprice * amount;
        if(p.tank.getCredits() > TotalPrice){
            if(SaveAmmo < Constants.AMMO_QUANTITY[0] && SaveAmmo + amount <= Constants.AMMO_QUANTITY[0]){
                p.tank.ammunition.set(0, SaveAmmo + amount);
                ReduceCredits(p, TotalPrice);
            }
        }
    }

    public void BuyMediumAmmo(Player p, int amount){
        int SaveAmmo = p.tank.ammunition.get(1);
        int TotalPrice = this.mediumammunitionprice * amount;
        if(p.tank.getCredits() > TotalPrice){
            if(SaveAmmo < Constants.AMMO_QUANTITY[1] && SaveAmmo + amount <= Constants.AMMO_QUANTITY[1]){
                p.tank.ammunition.set(1, SaveAmmo + amount);
                ReduceCredits(p, TotalPrice);
            }
        }
    }

    public void BuyHeavyAmmo(Player p, int amount){
        int SaveAmmo = p.tank.ammunition.get(2);
        int TotalPrice = this.heavyammunitionprice * amount;
        if(p.tank.getCredits() > TotalPrice){
            if(SaveAmmo < Constants.AMMO_QUANTITY[2] && SaveAmmo + amount <= Constants.AMMO_QUANTITY[2]){
                p.tank.ammunition.set(2, SaveAmmo + amount);
                ReduceCredits(p, TotalPrice);
            }
        }
    }*/

    public void BuyBullet(Player p, int price, int quantity){
        int SaveAmmo;
        if(p.tank.getCredits() >= price){
            if(price == this.lightammunitionprice){
                SaveAmmo = p.tank.ammunition.get(0);
                p.tank.ammunition.set(0, SaveAmmo + quantity);
                ReduceCredits(p, this.lightammunitionprice * quantity);
        }
            else if(price == this.mediumammunitionprice){
                SaveAmmo = p.tank.ammunition.get(1);
                p.tank.ammunition.set(1,SaveAmmo + quantity);
                ReduceCredits(p, this.mediumammunitionprice * quantity);
        }
            else{
                SaveAmmo = p.tank.ammunition.get(2);
                p.tank.ammunition.set(2,SaveAmmo + quantity);
                ReduceCredits(p, this.heavyammunitionprice * quantity);
        }
    }
}

    public void LoadCredits(Player p, int charge){
        p.tank.setCredits(p.tank.getCredits() + charge);
    }

    public void ReduceCredits(Player p, int TotalPrice){
        p.tank.setCredits(p.tank.getCredits() - TotalPrice);
    }
}
