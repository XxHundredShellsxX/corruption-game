package bin.BattleElements;

/**
 * Created by Sajid on 2016-05-24.
 */
//base item class with common properties like cost, description, hp restoration etc.
public class Item {

    private int hpRestoration;
    private String name;
    private String description;
    private int cost;

    public Item(String name, int cost, int hpRestoration, String description){
        this.name = name;
        this.cost = cost;
        this.hpRestoration = hpRestoration;
        this.description = description;
    }

    public String getName(){
        return name;
    }

    public int getCost(){
        return cost;
    }

    public int healAmount(){
        return hpRestoration;
    }

    public String getDescription(){
        return description;
    }

}
