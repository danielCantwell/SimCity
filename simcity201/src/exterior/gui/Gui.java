package exterior.gui;

import java.awt.*;

import SimCity.Base.Person;

public interface Gui {
    public void updatePosition();
    public void draw(Graphics2D g);
    public boolean isPresent();
    public int getX();
    public int getY();
    public int getRotation();
    public boolean SHOW_RECT = false;
    public String getType();
    public int getID();
    public Person getPerson();
}
