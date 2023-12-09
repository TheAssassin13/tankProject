package com.example.tankproject;

public class Shop {
    int lightAmmunitionPrice;
    int mediumAmmunitionPrice;
    int heavyAmmunitionPrice;

    public Shop(){
        this.lightAmmunitionPrice = Constants.AMMO_PRICE[0];
        this.mediumAmmunitionPrice = Constants.AMMO_PRICE[1];
        this.heavyAmmunitionPrice = Constants.AMMO_PRICE[2];
    }

    public void buyBullet(Player p, int price, int quantity, boolean temporary){
        if(p.tank.getCredits() >= price){
            if(price == this.lightAmmunitionPrice){
                if (!temporary) p.tank.ammunition.set(0, p.tank.ammunition.get(0) + quantity);
                else p.tank.temporaryAmmunition.set(0,p.tank.temporaryAmmunition.get(0) + quantity);
                reduceCredits(p, this.lightAmmunitionPrice * quantity);
        }
            else if(price == this.mediumAmmunitionPrice){
                if (!temporary) p.tank.ammunition.set(1, p.tank.ammunition.get(1) + quantity);
                else p.tank.temporaryAmmunition.set(1,p.tank.temporaryAmmunition.get(1) + quantity);
                reduceCredits(p, this.mediumAmmunitionPrice * quantity);
        }
            else{
                if (!temporary) p.tank.ammunition.set(2, p.tank.ammunition.get(2) + quantity);
                else p.tank.temporaryAmmunition.set(2,p.tank.temporaryAmmunition.get(2) + quantity);
                reduceCredits(p, this.heavyAmmunitionPrice * quantity);
        }
    }
}

    public static void loadCredits(Player p, int charge){
        p.tank.setCredits(p.tank.getCredits() + charge);
    }

    public static void reduceCredits(Player p, int TotalPrice){
        p.tank.setCredits(p.tank.getCredits() - TotalPrice);
    }
}
