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

    public void BuyBullet(Player p, int price, int quantity, boolean temporary){
        if(p.tank.getCredits() >= price){
            if(price == this.lightammunitionprice){
                if (!temporary) p.tank.ammunition.set(0, p.tank.ammunition.get(0) + quantity);
                else p.tank.temporaryAmmunition.set(0,p.tank.temporaryAmmunition.get(0) + quantity);
                ReduceCredits(p, this.lightammunitionprice * quantity);
        }
            else if(price == this.mediumammunitionprice){
                if (!temporary) p.tank.ammunition.set(1, p.tank.ammunition.get(1) + quantity);
                else p.tank.temporaryAmmunition.set(1,p.tank.temporaryAmmunition.get(1) + quantity);
                ReduceCredits(p, this.mediumammunitionprice * quantity);
        }
            else{
                if (!temporary) p.tank.ammunition.set(2, p.tank.ammunition.get(2) + quantity);
                else p.tank.temporaryAmmunition.set(2,p.tank.temporaryAmmunition.get(2) + quantity);
                ReduceCredits(p, this.heavyammunitionprice * quantity);
        }
    }
}

    public static void LoadCredits(Player p, int charge){
        p.tank.setCredits(p.tank.getCredits() + charge);
    }

    public static void ReduceCredits(Player p, int TotalPrice){
        p.tank.setCredits(p.tank.getCredits() - TotalPrice);
    }
}
