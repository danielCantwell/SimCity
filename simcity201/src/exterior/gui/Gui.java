package exterior.gui;

import java.awt.*;

public interface Gui {
    public void updatePosition();
    public void draw(Graphics2D g);
    public boolean isPresent();
    public int getX();
    public int getY();
    public int getRotation();
    public boolean SHOW_RECT = true;
    public String getType();
}
