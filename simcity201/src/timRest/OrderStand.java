package timRest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import SimCity.trace.AlertLog;
import SimCity.trace.AlertTag;

public class OrderStand
{
    public TimCookRole cook;
    
    private List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
    
    public void push(TimAbstractWaiterRole waiter, String order, int tableNumber)
    {
        orders.add(new Order(waiter, order, tableNumber));
        AlertLog.getInstance().logInfo(AlertTag.TimRest, "T_OrderStand", "Order in: " + waiter + ", " + order + ", " + tableNumber + ".");
        cook.msgOrderIn();
    }
    
    public Order pop()
    {
        if (orders.isEmpty())
        {
            AlertLog.getInstance().logWarning(AlertTag.TimRest, "T_OrderStand", "Popping empty list!");
            return null;
        }
        Order o = orders.get(0);
        orders.remove(0);
        return o;
    }
    
    public int size()
    {
        return orders.size();
    }
    
    public class Order
    {
        TimAbstractWaiterRole waiter;
        String order;
        int tableNumber;
        
        public Order(TimAbstractWaiterRole waiter, String order, int tableNumber)
        {
            this.waiter = waiter;
            this.order = order;
            this.tableNumber = tableNumber;
        }
    }
}
